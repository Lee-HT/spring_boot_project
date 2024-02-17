package com.example.demo.Controller;

import com.example.demo.DTO.CategoryDto;
import com.example.demo.DTO.CategoryGroupDto;
import com.example.demo.Service.CategoryService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getCategory(){
        HttpStatus status = HttpStatus.OK;
        List<CategoryGroupDto> response = categoryService.getCategory();
        return new ResponseEntity<>(response,status);
    }

    @GetMapping("/parent/{parent}")
    public ResponseEntity<List<CategoryDto>> getCategoryByParent(@PathVariable String parent) {
        HttpStatus status = HttpStatus.OK;
        List<CategoryDto> response = categoryService.getCategoryByParent(parent);
        return new ResponseEntity<>(response, status);
    }

    @PostMapping()
    public ResponseEntity<Long> saveCategory(@RequestBody CategoryDto categoryDto) {
        HttpStatus status = HttpStatus.CREATED;
        CategoryDto response = categoryService.saveCategory(categoryDto);
        if (response.getId() == null) {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteCategory(@PathVariable Long id) {
        HttpStatus status = HttpStatus.NO_CONTENT;
        Long response = categoryService.deleteCategory(id);
        if (response == 0){
            status = HttpStatus.NOT_FOUND;
        }
        return new ResponseEntity<>(status);
    }
}
