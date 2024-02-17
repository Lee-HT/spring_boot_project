package com.example.demo.Service;

import com.example.demo.DTO.CategoryDto;
import com.example.demo.DTO.CategoryGroupDto;
import java.util.List;

public interface CategoryService {
    List<CategoryGroupDto> getCategory();
    List<CategoryDto> getCategoryByParent(String parent);
    CategoryDto saveCategory(CategoryDto categoryDto);
    Long deleteCategory(Long id);

}
