package com.example.demo.Service;

import com.example.demo.DTO.CategoryDto;
import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<CategoryDto> getCategory();
    Map<String, Map<String,?>> getCategoryGroup();
    List<CategoryDto> getCategoryByParent(String parent);
    CategoryDto saveCategory(CategoryDto categoryDto);
    Long deleteCategory(Long id);

}
