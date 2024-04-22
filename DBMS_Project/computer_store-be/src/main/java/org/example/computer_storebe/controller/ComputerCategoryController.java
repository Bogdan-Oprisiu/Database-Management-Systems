package org.example.computer_storebe.controller;

import org.example.computer_storebe.entity.computer.ComputerCategory;
import org.example.computer_storebe.service.ComputerCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class ComputerCategoryController {

    private final ComputerCategoryService categoryService;

    @Autowired
    public ComputerCategoryController(ComputerCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<ComputerCategory> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ComputerCategory getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ComputerCategory addCategory(@RequestBody ComputerCategory category) {
        return categoryService.saveCategory(category);
    }

    @PutMapping("/{id}")
    public ComputerCategory updateCategory(@PathVariable Long id, @RequestBody ComputerCategory category) {
        category.setId(id);
        return categoryService.saveCategory(category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
