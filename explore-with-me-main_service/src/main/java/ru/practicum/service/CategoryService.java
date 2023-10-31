package ru.practicum.service;

import ru.practicum.model.category.Category;

import java.util.Collection;

public interface CategoryService {

    Category getCategory(long catId);

    Collection<Category> getCategories(int from, int size);

    Category addCategory(Category category);

    void removeCategory(long catId);

    Category updateCategory(long catId, Category category);

}
