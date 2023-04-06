package com.corusoft.ticketmanager.tickets.services.utils;

import com.corusoft.ticketmanager.common.exceptions.EntityNotFoundException;
import com.corusoft.ticketmanager.common.exceptions.UnableToParseImageException;
import com.corusoft.ticketmanager.tickets.entities.*;
import com.corusoft.ticketmanager.tickets.repositories.CategoryRepository;
import com.corusoft.ticketmanager.tickets.repositories.CustomizedCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Path;
import java.util.Base64;

import static com.corusoft.ticketmanager.TicketManagerApplication.TEMP_PATH;

@Component
@Transactional(readOnly = true)
public class TicketUtils {

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private CustomizedCategoryRepository customizedCategoryRepo;

    /**
     * Busca una categoría por su nombre en la base de datos.
     *
     * @param name Nombre de la categoría a buscar
     * @return Categoría encontrada
     * @throws EntityNotFoundException No se encuentra al usuario
     */
    public Category fetchCategoryByName(String name) throws EntityNotFoundException {
        return categoryRepo.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException(Category.class.getSimpleName(), name));
    }

    /**
     * Busca una categoría customizada en la base de datos.
     *
     * @param customizedCategoryID de la categoría customizada a buscar.
     * @return Categoría Customizada encontrada
     * @throws EntityNotFoundException No se encuentra la categoría customizada asociada al usuario.
     */
    public CustomizedCategory fetchCustomizedCategoryById(CustomizedCategoryID customizedCategoryID)
            throws EntityNotFoundException {
        return customizedCategoryRepo.findById(customizedCategoryID).orElseThrow(() ->
                new EntityNotFoundException(CustomizedCategory.class.getSimpleName(), customizedCategoryID));

    }

    /**
     * Crea un <c>File</c> a partir de la imágen recibida, codificada como un String en Base64
     * @param imageAsB64String - Imágen codificada en Base64
     * @return File que representa la imágen
     */
    public File parseB64ImageToFile(String imageAsB64String) throws UnableToParseImageException {
        String imageB64DataString = "";
        String imageFileExtension = ".jpg";

        // Separa la cabecera "data:image/{tipo_imagen}" de la imagen en Base 64 si la tiene
        if (imageAsB64String.contains(",")) {
            String[] splittedB64ImageHeaders = imageAsB64String.split(",");
            // Obtiene extensión de la imágen: jpg, jpeg, png, ...
            String extension = splittedB64ImageHeaders[0]
                    .split("/")[1]
                    .split(";")[0];
            if (!extension.equals("*")) {
                imageFileExtension = "." + extension;
            }

            // Datos de la imágen
            imageB64DataString = splittedB64ImageHeaders[1];
        }

        // Decodificar imagen recibida
        byte[] pictureBytes = Base64.getDecoder().decode(imageB64DataString);

        // Crear fichero con los datos de la imágen
        File imageFile;
        try {
            Path tempDirectoryPath = TEMP_PATH;
            imageFile = File.createTempFile("image-", imageFileExtension, new File(TEMP_PATH.toString()));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new UnableToParseImageException();
        }

        // Escribir datos de imagen a fichero
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            fos.write(pictureBytes);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            throw new UnableToParseImageException();
        }

        return imageFile;
    }
}
