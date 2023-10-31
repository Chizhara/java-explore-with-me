package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.NewCategoryDto;

import java.util.Collection;

@Mapper
public interface CategoryMapper {

    CategoryDto toCategoryDto(Category category);

    Collection<CategoryDto> toCategoryDto(Collection<Category> category);

    @Mapping(target = "id", ignore = true)
    Category toCategory(NewCategoryDto category);

}
