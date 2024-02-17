package com.example.demo.Converter;

import com.example.demo.DTO.CategoryDto;
import com.example.demo.Entity.CategoryEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryEntity toEntity(CategoryDto categoryDto) {
        return CategoryEntity.builder().parent(categoryDto.getParent()).name(categoryDto.getName()).build();
    }

    public List<CategoryEntity> toEntity(List<CategoryDto> categoryDto) {
        List<CategoryEntity> categoryEntity = new ArrayList<>();
        for (CategoryDto dto: categoryDto){
            categoryEntity.add(toEntity(dto));
        }
        return categoryEntity;
    }

    public CategoryDto toDto(CategoryEntity categoryEntity) {
        return CategoryDto.builder().id(categoryEntity.getId()).parent(categoryEntity.getParent())
                .name(categoryEntity.getName()).build();
    }

    public List<CategoryDto> toDto(List<CategoryEntity> categoryEntity) {
        List<CategoryDto> categoryDto = new ArrayList<>();
        for (CategoryEntity ett: categoryEntity){
            categoryDto.add(toDto(ett));
        }
        return categoryDto;
    }
}
