package ru.practicum.category.mapper;

import org.mapstruct.Mapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(uses = CategoryMapper.class, componentModel = SPRING)
public interface CategoryListMapper {
    List<CategoryDto> toCategoryDtoList(List<Category> categories);
}
