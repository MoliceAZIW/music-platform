package com.example.music.service;

import com.example.music.dto.CategoryRequest;
import com.example.music.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> listAll();
    Category getById(Long id);
    Category create(CategoryRequest request);
    Category update(Long id, CategoryRequest request);
    void delete(Long id);
}