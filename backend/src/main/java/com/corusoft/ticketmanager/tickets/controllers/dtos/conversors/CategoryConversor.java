package com.corusoft.ticketmanager.tickets.controllers.dtos.conversors;

import com.corusoft.ticketmanager.tickets.controllers.dtos.CustomizedCategoryDTO;
import com.corusoft.ticketmanager.tickets.controllers.dtos.filters.CategoryDto;
import com.corusoft.ticketmanager.tickets.entities.Category;
import com.corusoft.ticketmanager.tickets.entities.CustomizedCategory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryConversor {

    /* ******************** Convertir a category DTO ******************** */
    public static List<CategoryDto> toCategoryDTOList(List<Category> entityList) {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        for(Category entity: entityList) {
            categoryDtoList.add(new CategoryDto(entity.getId(), entity.getName()));
        }
        return categoryDtoList;
    }

    /* ******************** Convertir a DTO ******************** */
    public static CustomizedCategoryDTO toCustomizedCategoryDTO(CustomizedCategory entity) {
        CustomizedCategoryDTO dto = new CustomizedCategoryDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getCategory().getName());
        dto.setMaxWasteLimit(entity.getMaxWasteLimit());

        return dto;
    }

    /* ******************** Convertir a conjunto de DTOs ******************** */
    public static List<CustomizedCategoryDTO> toCustomizedCategoryDTOList(List<CustomizedCategory> entityList) {
        List<CustomizedCategoryDTO> list = new ArrayList<>(entityList.size());
        for (CustomizedCategory entity: entityList) {
            list.add(toCustomizedCategoryDTO(entity));
        }

        return list;
    }
}
