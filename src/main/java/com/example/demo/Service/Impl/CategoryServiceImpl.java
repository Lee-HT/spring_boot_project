package com.example.demo.Service.Impl;

import com.example.demo.Converter.CategoryConverter;
import com.example.demo.DTO.CategoryDto;
import com.example.demo.DTO.CategoryGroupDto;
import com.example.demo.Entity.CategoryEntity;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Service.CategoryService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryConverter categoryConverter) {
        this.categoryRepository = categoryRepository;
        this.categoryConverter = categoryConverter;
    }

    @Override
    public List<CategoryGroupDto> getCategory() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        Map<String, List<String>> groups = getParentGrouping(categories);
        return group2List(groups);
    }

    @Override
    public List<CategoryDto> getCategoryByParent(String parent) {
        List<CategoryEntity> categoryEntities = categoryRepository.findByParent(parent);
        return categoryConverter.toDto(categoryEntities);
    }

    @Override
    public CategoryDto saveCategory(CategoryDto categoryDto) {
        return categoryConverter.toDto(categoryRepository.save(categoryConverter.toEntity(categoryDto)));
    }

    @Override
    public Long deleteCategory(Long id) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        if (categoryEntity.isPresent()) {
            categoryRepository.delete(categoryEntity.get());
            return id;
        }
        return 0L;
    }

    private Map<String, List<String>> getParentGrouping(List<CategoryEntity> categories) {
        Map<String, List<String>> response = new HashMap<>();
        for (CategoryEntity ett : categories) {
            List<String> cur;
            if (response.containsKey(ett.getParent())) {
                cur = response.get(ett.getParent());
            } else {
                cur = new ArrayList<>();
            }
            cur.add(ett.getName());
            response.put(ett.getParent(), cur);
        }
        return response;
    }

    private List<CategoryGroupDto> group2List(Map<String, List<String>> categories) {
        List<CategoryGroupDto> response = new ArrayList<>();
        for (String parent : categories.keySet()) {
            response.add(CategoryGroupDto.builder().parent(parent).category(categories.get(parent)).build());
        }
        return response;
    }
}
