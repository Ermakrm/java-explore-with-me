package ru.practicum.category.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.mapper.CategoryListMapper;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PublicCategoriesController {
    CategoryService categoryService;
    CategoryMapper categoryMapper;
    CategoryListMapper categoryListMapper;

    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("getCategories from={};size={}", from, size);
        return categoryListMapper.toCategoryDtoList(
                categoryService.getCategories(from, size)
        );
    }

    @GetMapping("/{catId}")
    public CategoryDto findCategoryById(@PathVariable Long catId) {
        log.info("findCategoryById catId={}", catId);
        return categoryMapper.toCategoryDto(
                categoryService.findCategoryById(catId)
        );
    }
}
