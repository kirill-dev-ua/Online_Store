package online.store.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import online.store.dto.CategoryDto;
import online.store.model.Category;
import online.store.model.PageResponse;
import online.store.repo.CategoryRepository;
import online.store.utils.PageResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDto create(CategoryDto dto) {
        Category category = Category.of(dto.getName());
        if(categoryRepository.existsCategoryByName(category.getName())){
            throw new EntityExistsException("Category with name '"
                    + category.getName() + "' already exists");
        }
        return CategoryDto.of(categoryRepository.save(category));
    }

    public CategoryDto update(Long id, CategoryDto dtoUpdate) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category with id " + id + " not found"));
        if(categoryRepository.existsCategoryByName(dtoUpdate.getName())){
            throw new EntityExistsException("Category with name '"
                    + dtoUpdate.getName() + "' already exists");
        }
        existing.setName(dtoUpdate.getName());
        existing.setLastUpdateOn(dtoUpdate.getLastUpdateOn());

        return CategoryDto.of(categoryRepository.save(existing));
    }

    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category with id " + id + " not found"));
        return CategoryDto.of(category);
    }

    public PageResponse<CategoryDto> findAll(Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(pageable);
        return PageResponseUtil.toPageResponse(page, CategoryDto::of);
    }

    public void delete(Long id) {
        if(!categoryRepository.existsById(id)){
            throw new EntityNotFoundException("Category with id " + id + " not found");
        }
        categoryRepository.deleteById(id);
    }
}
