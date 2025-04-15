package online.store.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import online.store.dto.CategoryDto;
import online.store.repo.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CategoryServiceITTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    public CategoryDto category;
    String categoryName = "test2";

    @BeforeEach
    void setCategory() {
        categoryRepository.deleteAll();

        CategoryDto dto = new CategoryDto();
        dto.setName(categoryName);
        category = categoryService.create(dto);
    }

    @Test
    void create() {
        var categoryFromDb = categoryService.findById(category.getId());

        assertNotNull(categoryFromDb);
        assertEquals(categoryName, categoryFromDb.getName());
    }

    @Test
    void create_duplicateNane_throwsEntityExistsException() {
        CategoryDto newDto = new CategoryDto();
        newDto.setName("test2");

        assertThrows(EntityExistsException.class,
                () -> categoryService.create(newDto));
    }

    @Test
    void update() {
        CategoryDto newDto = new CategoryDto();
        newDto.setName("test3");

        CategoryDto updDto = categoryService.update(category.getId(), newDto);

        assertEquals("test3", updDto.getName());
    }

    @Test
    void update_notFound_throwsEntityNotFoundException() {
        CategoryDto newDto = new CategoryDto();
        newDto.setName("test3");

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(2L, newDto));
    }

    @Test
    void update_duplicateNane_throwsEntityExistsException() {
        CategoryDto newDto = new CategoryDto();
        newDto.setName("test2");

        assertThrows(EntityExistsException.class,
                () -> categoryService.update(category.getId(), newDto));
    }

    @Test
    void findById() {
        assertEquals("test2",
                categoryService.findById(category.getId()).getName());
    }

    @Test
    void findById_notFound_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(2L));
    }

    @Test
    void findAll() {
        var page = categoryService.findAll(PageRequest.of(0, 10));
        assertEquals(1, page.records().size());
        assertEquals(category.getName(), page.records().getFirst().getName());
    }

    @Test
    void delete() {
        categoryService.delete(category.getId());
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(category.getId()));
    }

    @Test
    void delete_notFound_throwsEntityNotFoundException() {
        assertThrows(EntityNotFoundException.class,
                () -> categoryService.delete(2L));
    }
}