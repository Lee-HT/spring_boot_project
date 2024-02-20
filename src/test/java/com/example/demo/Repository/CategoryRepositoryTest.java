package com.example.demo.Repository;

import com.example.demo.Entity.CategoryEntity;
import io.jsonwebtoken.lang.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CategoryRepositoryTest {

    private final CategoryRepository categoryRepository;
    private final List<CategoryEntity> categoryEntities = new ArrayList<>();
    private final List<Long> pk = new ArrayList<>();

    @Autowired
    public CategoryRepositoryTest(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @BeforeEach
    void setCategory() {
        for (int i = 1; i < 3; i++) {
            categoryEntities.add(CategoryEntity.builder().parent("parent").name("category" + i).build());
        }
        categoryRepository.saveAll(categoryEntities);
        for (CategoryEntity ett : categoryRepository.findAll()) {
            this.pk.add(ett.getId());
        }
    }

    @Test
    void getCategory() {
        List<CategoryEntity> result = categoryRepository.findAll();

        Assertions.assertThat(result).isEqualTo(categoryEntities);
    }

    @Test
    void getCategoryById() {
        Optional<CategoryEntity> result = categoryRepository.findById(this.pk.get(0));

        Assert.isTrue(result.isPresent());
        Assertions.assertThat(result.get()).usingRecursiveComparison().isEqualTo(categoryEntities.get(0));
    }

    @Test
    void saveCategory() {
        CategoryEntity categoryEntity = CategoryEntity.builder().parent("parent3").name("category3").build();

        CategoryEntity result = categoryRepository.save(categoryEntity);
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(categoryEntity);
    }
}
