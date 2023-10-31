package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.PageableGenerator;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.CategoryService;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategory(long catId) {
        log.debug("Invoked method getCategory of class CategoryServiceImp " +
                "with parameters: catId = {};", catId);
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category", catId));
    }

    @Override
    public Collection<Category> getCategories(int from, int size) {
        log.debug("Invoked method getCategories of class CategoryServiceImp " +
                "with parameters: from = {}, size = {};", from, size);
        return categoryRepository.findAll(PageableGenerator.getPageable(from, size)).toList();
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        log.debug("Invoked method addCategory of class CategoryServiceImp " +
                "with parameters: category = {};", category);
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public Category updateCategory(long catId, Category category) {
        log.debug("Invoked method updateCategory of class CategoryServiceImp " +
                "with parameters: catId = {}, category = {};", catId, category);
        Category updatedCategory = getCategory(catId);

        if (category.getName() != null) {
            updatedCategory.setName(category.getName());
        }
        return categoryRepository.save(updatedCategory);
    }

    @Override
    @Transactional
    public void removeCategory(long catId) {
        log.debug("Invoked method removeCategory of class CategoryServiceImp " +
                "with parameters: catId = {};", catId);
        validateCategoryExistsById(catId);
        categoryRepository.deleteById(catId);
    }

    private void validateCategoryExistsById(long catId) {
        log.debug("Invoked method validateCategoryExistsById of class CategoryServiceImp " +
                "with parameters: catId = {};", catId);
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundException("Category", catId);
        }
    }

}
