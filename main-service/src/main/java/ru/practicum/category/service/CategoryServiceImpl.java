package ru.practicum.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.IllegalActionException;
import ru.practicum.exception.ObjectNotFoundException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    @Override
    public Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Category with id=%d not found", catId))
        );
    }

    @Override
    public Category saveCategory(Category category) {
        if (isNameExist(category.getName())) {
            throw new IllegalActionException("Name is already used");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, Long catId) {
        Category categoryToSave = findCategoryById(catId);
        if (isNameExist(category.getName()) && !category.getName().equals(categoryToSave.getName())) {
            throw new IllegalActionException("Name is already used");
        }
        categoryToSave.setName(category.getName());
        return categoryRepository.save(categoryToSave);
    }

    @Override
    public void deleteCategory(Long catId) {
        existCategoryById(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<Category> getCategories(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();
    }

    @Override
    public void existCategoryById(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new IllegalActionException(String.format("Category with id=%d not found", catId));
        }
    }

    private boolean isNameExist(String name) {
        return categoryRepository.existsByName(name);
    }
}
