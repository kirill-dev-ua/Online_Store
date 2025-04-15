package online.store.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import online.store.dto.CategoryDto;
import online.store.model.Category;
import online.store.model.PageResponse;
import online.store.repo.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    CategoryDto category = new CategoryDto();
    String categoryName = "category_test";
    String notFoundCategoryById;

    @BeforeEach
    void setCategoryDTO() {
        category.setId(1L);
        category.setName(categoryName);
        notFoundCategoryById = "Category with id " + category.getId() + " not found";
    }

    @Test
    void create() {
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(new Category(category.getId(), categoryName));
        var result = categoryService.create(category);

        assertNotNull(result);
        assertEquals(categoryName, result.getName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void create_whenCategoryExists_thenThrowsException() {
        String expectedMessage = "Category with name '" + category.getName() + "' already exists";

        when(categoryRepository.existsCategoryByName(categoryName)).thenReturn(true);

        Exception exception = assertThrows(EntityExistsException.class,
                () -> categoryService.create(category));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void update() {
        CategoryDto updateCategory = new CategoryDto();
        String newCategoryName = "update_category_test";
        updateCategory.setName(newCategoryName);

        Category existingCategory = new Category(1L, categoryName);

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(new Category(category.getId(), newCategoryName));

        var result = categoryService.update(category.getId(), updateCategory);

        assertNotNull(result);
        assertEquals(newCategoryName, result.getName());
        assertEquals(existingCategory.getId(), result.getId());

        verify(categoryRepository).findById(category.getId());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void update_whenCategoryNotFoundById_thenThrowEntityNotFoundException() {

        CategoryDto updateCategory = new CategoryDto();
        String newCategoryName = "update_category_test";
        updateCategory.setName(newCategoryName);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                categoryService.update(category.getId(), updateCategory));

        assertEquals(notFoundCategoryById, exception.getMessage());
    }

    @Test
    void update_whenCategoryExists_thenThrowEntityExistsException() {
        String expectedMessage = "Category with name '" + categoryName + "' already exists";

        Category existingCategory = new Category(category.getId(), categoryName);

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsCategoryByName(categoryName))
                .thenReturn(true);

        Exception exception = assertThrows(EntityExistsException.class, () ->
                categoryService.update(category.getId(), category));

        assertEquals(expectedMessage, exception.getMessage());
        verify(categoryRepository).findById(category.getId());
    }

    @Test
    void findById() {
        Category existingCategory = new Category(category.getId(), categoryName);

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(existingCategory));

        var result = categoryService.findById(existingCategory.getId());

        assertNotNull(result);
        assertEquals(categoryName, result.getName());

        verify(categoryRepository).findById(category.getId());
    }

    @Test
    void findById_whenCategoryNotFoundById_thenThrowEntityNotFoundException(){
        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(category.getId()));

        assertEquals(notFoundCategoryById, exception.getMessage());

        verify(categoryRepository).findById(category.getId());

    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(
                new Category(1L, "category_test1"),
                new Category(2L, "category_test2")
        );
        Page<Category> page = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.findAll(pageable)).thenReturn(page);

        PageResponse<CategoryDto> result = categoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.records().size());
        assertEquals("category_test1", result.records().get(0).getName());
        assertEquals("category_test2", result.records().get(1).getName());
    }

    @Test
    void delete() {
        when(categoryRepository.existsById(category.getId())).thenReturn(true);

        categoryService.delete(category.getId());

        verify(categoryRepository).existsById(category.getId());
        verify(categoryRepository).deleteById(category.getId());
    }

    @Test
    void delete_whenCategoryDoesNotExist_thenThrowEntityNotFoundException(){

        when(categoryRepository.existsById(category.getId())).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                categoryService.delete(category.getId()));

        assertEquals(notFoundCategoryById, exception.getMessage());

        verify(categoryRepository).existsById(category.getId());
        verify(categoryRepository, never()).deleteById(any());
    }
}