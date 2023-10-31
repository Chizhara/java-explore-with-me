package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.service.CategoryService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public Collection<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("Invoked method getCategories of class PublicCategoriesController " +
                "with parameters: from = {}, size = {};", from, size);
        return categoryMapper.toCategoryDto(categoryService.getCategories(from, size));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        log.info("Invoked method getCategory of class PublicCategoriesController " +
                "with parameters: catId = {};", catId);
        return categoryMapper.toCategoryDto(categoryService.getCategory(catId));
    }
}
