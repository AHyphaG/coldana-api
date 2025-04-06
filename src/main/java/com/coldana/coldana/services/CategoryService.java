package com.coldana.coldana.services;

import com.coldana.coldana.models.Category;
import com.coldana.coldana.repositories.CategoryRepository;

import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService() {
        this.categoryRepository = new CategoryRepository();
    }

    public List<Category> getAllCategoriesByUser(String userId) {
        return categoryRepository.findByUserId(userId);
    }

    public List<Category> getActiveCategoriesByUser(String userId) {
        return categoryRepository.findActiveByUserId(userId);
    }

    public Category getCategoryById(String userId, String categoryId) {
        return categoryRepository.findById(userId, categoryId);
    }

    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

//    public void updateCategory(Category category) {
//        categoryRepository.update(category);
//    }
//
//    public void deleteCategory(String categoryId) {
//        categoryRepository.delete(categoryId);
//    }
//
//    public void deactivateCategory(String categoryId) {
//        categoryRepository.setActive(categoryId, false);
//    }
//
//    public void activateCategory(String categoryId) {
//        categoryRepository.setActive(categoryId, true);
//    }
}

