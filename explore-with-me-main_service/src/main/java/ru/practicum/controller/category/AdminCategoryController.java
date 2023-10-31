package ru.practicum.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.NewCategoryDto;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto postCategory(@Valid @RequestBody NewCategoryDto categoryDto) {
        log.info("Invoked method postCategory of class AdminCategoriesController " +
                "with parameters: categoryDto = {}", categoryDto);
        return categoryMapper.toCategoryDto(
                categoryService.addCategory(
                        categoryMapper.toCategory(categoryDto)));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Invoked method deleteCategory of class AdminCategoriesController " +
                "with parameters: catId = {}", catId);
        categoryService.removeCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@Valid @RequestBody NewCategoryDto categoryDto,
                                     @PathVariable Long catId) {
        log.info("Invoked method patchCategory of class AdminCategoriesController " +
                "with parameters: catId = {}", catId);
        return categoryMapper.toCategoryDto(
                categoryService.updateCategory(catId, categoryMapper.toCategory(categoryDto)));
    }

}
