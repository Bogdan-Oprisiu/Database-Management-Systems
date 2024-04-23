package org.example.computer_storebe.service;

import org.example.computer_storebe.entity.computer.ComputerCategory;
import org.example.computer_storebe.repository.computer.ComputerCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComputerCategoryService {

    private final ComputerCategoryRepository categoryRepository;

    @Autowired
    public ComputerCategoryService(ComputerCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<ComputerCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public ComputerCategory getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public ComputerCategory saveCategory(ComputerCategory category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public ComputerCategory updateCategory(Long id, ComputerCategory updatedCategory) {
        Optional<ComputerCategory> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            ComputerCategory existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(updatedCategory.getCategoryName());
            existingCategory.setCategoryDescription(updatedCategory.getCategoryDescription());
            return categoryRepository.save(existingCategory);
        }
        return null; // Return null if the category with the given id doesn't exist
    }
}
