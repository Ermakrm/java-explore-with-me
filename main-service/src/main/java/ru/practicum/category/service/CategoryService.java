package ru.practicum.category.service;

import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryService {
    Category findCategoryById(Long catId);

    Category saveCategory(Category category);

    Category updateCategory(Category category, Long catId);

    void deleteCategory(Long catId);

    List<Category> getCategories(int from, int size);

    void existCategoryById(Long catId);
}
