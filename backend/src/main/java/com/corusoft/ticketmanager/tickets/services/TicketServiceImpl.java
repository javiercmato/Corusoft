package com.corusoft.ticketmanager.tickets.services;

import com.corusoft.ticketmanager.common.exceptions.*;
import com.corusoft.ticketmanager.tickets.controllers.dtos.CreateTicketParamsDTO;
import com.corusoft.ticketmanager.tickets.entities.*;
import com.corusoft.ticketmanager.tickets.repositories.*;
import com.corusoft.ticketmanager.tickets.services.utils.TicketUtils;
import com.corusoft.ticketmanager.users.entities.User;
import com.corusoft.ticketmanager.users.services.utils.UserUtils;
import com.mindee.DocumentToParse;
import com.mindee.MindeeClient;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.field.TaxField;
import com.mindee.parsing.receipt.ReceiptV4DocumentPrediction;
import com.mindee.parsing.receipt.ReceiptV4Inference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
public class TicketServiceImpl implements TicketService {

    /* ******************** DEPENDENCIAS ******************** */
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private CustomizedCategoryRepository customCategoryRepo;
    @Autowired
    private ParsedTicketDataRepository parsedTicketDataRepo;
    @Autowired
    private TicketRepository ticketRepo;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private TicketUtils ticketUtils;
    @Autowired
    private MindeeClient mindeeClient;


    /* ******************** FUNCIONALIDADES CATEGORÍAS ******************** */
    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public CustomizedCategory createCustomCategory(Long userID, String customCategoryName, Float wasteLimit)
            throws EntityNotFoundException {
        // Comprobar si existe el usuario
        User user = userUtils.fetchUserByID(userID);
        // Comprobar si existe una categoría con nombre recibido
        Category category = ticketUtils.fetchCategoryByName(customCategoryName);

        // Crear CustomCategory con el límite de gasto asignado
        CustomizedCategory customCategory = new CustomizedCategory();
        customCategory.setMaxWasteLimit(wasteLimit);
        customCategory.setCategory(category);
        user.assignCustomizedCategory(customCategory);

        return customCategoryRepo.save(customCategory);
    }

    @Override
    public CustomizedCategory updateCustomCategory(Long userID, Long customCategoryID, Float newWasteLimit)
            throws EntityNotFoundException {
        // Comprobar si existe el usuario
        userUtils.fetchUserByID(userID);

        // Comprobar si existe la customCategory y que ésta pertenezca al usuario
        CustomizedCategory customizedCategory = ticketUtils.
                fetchCustomizedCategoryById(new CustomizedCategoryID(userID, customCategoryID));
        customizedCategory.setMaxWasteLimit(newWasteLimit);

        return customCategoryRepo.save(customizedCategory);
    }

    @Override
    public List<CustomizedCategory> getCustomCategoriesByUser(Long userID) throws EntityNotFoundException {
        // Comprobar si existe el usuario
        User user = userUtils.fetchUserByID(userID);

        return user.getCustomizedCategories().stream().toList();
    }


    /* ******************** FUNCIONALIDADES TICKETS ******************** */
    @Override
    public ParsedTicketData parseTicketContent(String ticketContentAsB64) throws UnableToParseImageException {
        // Si no hay imagen se lanza excepción
        if (ticketContentAsB64 == null)
            throw new UnableToParseImageException();

        // Parsear ticket en la API
        ReceiptV4DocumentPrediction ticketPrediction;
        try {
            File imageToFile = ticketUtils.parseB64ImageToFile(ticketContentAsB64);
            DocumentToParse documentToParse = mindeeClient.loadDocument(imageToFile);
            Document<ReceiptV4Inference> parsedDocument = mindeeClient.parse(ReceiptV4Inference.class, documentToParse);
            ticketPrediction = parsedDocument.getInference().getDocumentPrediction();
        } catch (IOException e) {
            throw new UnableToParseImageException();
        }

        // Extraer información del ticket
        ParsedTicketData parsedTicket = new ParsedTicketData();
        parsedTicket.setSupplier(ticketPrediction.getSupplierName().toString());
        parsedTicket.setCategory(ticketPrediction.getCategory().getValue());
        parsedTicket.setSubcategory(ticketPrediction.getSubCategory().toString());
        parsedTicket.setEmitted_at_date(ticketPrediction.getDate().getValue());
        parsedTicket.setEmitted_at_time(ticketPrediction.getTime().getValue());
        parsedTicket.setCountry(ticketPrediction.getLocaleField().getCountry());
        parsedTicket.setLanguage(ticketPrediction.getLocaleField().getLanguage());
        parsedTicket.setCurrency(ticketPrediction.getLocaleField().getCurrency());
        parsedTicket.setTotalAmount(ticketPrediction.getTotalAmount().getValue().floatValue());
        // Calcular tasas y coste total del ticket
        Float totalTaxes = 0f;
        for (TaxField tax: ticketPrediction.getTaxes()) {
            totalTaxes += tax.getValue().floatValue();
        }
        parsedTicket.setTotalTax(totalTaxes);

        parsedTicket.setRegistered_at(LocalDateTime.now());
        return parsedTicket;
    }

    @Override
    public Ticket createTicket(CreateTicketParamsDTO params) throws EntityNotFoundException, UnableToParseImageException {
        // Comprobar si existe el usuario
        User user = userUtils.fetchUserByID(params.getUserID());

        // Comprobar si existe la categoría personalizada
        CustomizedCategoryID customCategoryID = new CustomizedCategoryID(user.getId(), params.getCategoryID());
        CustomizedCategory customCategory = ticketUtils.fetchCustomizedCategoryById(customCategoryID);

        // Parsear ticket
        ParsedTicketData parsedTicketData = this.parseTicketContent(params.getTicketData());
        parsedTicketDataRepo.save(parsedTicketData);
        String emmitedAtString = String.format("%sT%s", params.getEmmitedAtDate().toString(), params.getEmmitedAtTime());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime emmitedAt = LocalDateTime.parse(emmitedAtString, dateTimeFormatter);
        byte[] ticketImageAsBytes = ticketUtils.parseImage64StringToBytes(params.getTicketData());

        // Crear ticket
        Ticket ticket = Ticket.builder()
                .name(params.getName())
                .registeredAt(LocalDateTime.now())
                .emittedAt(emmitedAt)
                .amount(params.getTotalAmount())
                .currency(params.getCurrency())
                .picture(ticketImageAsBytes)
                .customizedCategory(customCategory)
                .store(params.getSupplier())
                .parsedTicketData(parsedTicketData)
                .build();
        user.assignTicket(ticket);

        return ticketRepo.save(ticket);
    }

    @Override
    public Ticket shareTicket(Long userID, Long ticketId, Long receiverID) throws EntityNotFoundException,
            TicketAlreadySharedException, PermissionException {
        // Comprobar si existen el autor, el receptor y el ticket
        User owner = userUtils.fetchUserByID(userID);
        User receiver = userUtils.fetchUserByID(receiverID);
        Ticket ticket = ticketUtils.fetchTicketById(ticketId);

        // Comprobar que el usuario sea el dueño del ticket.
        if (!ticket.getCreator().equals(owner)) {
            throw new PermissionException();
        }

        // Comprobar que el receptor del ticket no sea el propietario del ticket
        if (ticket.getCreator().equals(receiver)) {
            throw new TicketAlreadySharedException(Ticket.class.getSimpleName(), receiver.getNickname());
        }

        // Comprobar que el receptor no haya recibido el ticket actual previamente
        if (receiver.getSharedTickets().contains(ticket)) {
            throw new TicketAlreadySharedException(Ticket.class.getSimpleName(), receiver.getNickname());
        }

        // Compartir el ticket con el receptor
        ticket.shareWithUser(receiver);

        return ticketRepo.save(ticket);
    }

    @Override
    public void deleteTicket(Long userID, Long ticketID) throws EntityNotFoundException, TicketNotInPropertyException {
        // Comprobar si existe el usuario y el ticket
        User owner = userUtils.fetchUserByID(userID);
        Ticket ticket = ticketUtils.fetchTicketById(ticketID);

        // Comprobar que el ticket a borrar no haya sido compartido
        // Aunque el usuario sea propietario del ticket, no puede borrar un ticket que ya ha compartido con otro usuario
        if (owner.hasSharedTicket(ticket)) {
            throw new TicketNotInPropertyException();
        }

        ticketRepo.delete(ticket);
    }
}
