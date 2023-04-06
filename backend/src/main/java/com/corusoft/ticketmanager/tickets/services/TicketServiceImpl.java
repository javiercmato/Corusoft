package com.corusoft.ticketmanager.tickets.services;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.common.exceptions.UnableToParseImageException;
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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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
        parsedTicket.setTotal_amount(ticketPrediction.getTotalAmount().getValue().floatValue());
        // Calcular tasas y coste total del ticket
        Float totalTaxes = 0f;
        for (TaxField tax: ticketPrediction.getTaxes()) {
            totalTaxes += tax.getValue().floatValue();
        }
        parsedTicket.setTotal_tax(totalTaxes);

        parsedTicket.setRegistered_at(LocalDateTime.now());
        return parsedTicketDataRepo.save(parsedTicket);
    }
}
