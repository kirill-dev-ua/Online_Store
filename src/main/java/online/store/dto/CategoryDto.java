package online.store.dto;

import lombok.*;
import online.store.model.Category;

@Getter
@Setter
public class CategoryDto extends BaseResponseDto {
    private String name;

    public static CategoryDto of(Category category) {
        CategoryDto view = new CategoryDto();
        view.setId(category.getId());
        view.setName(category.getName());
        view.setCreatedOn(category.getCreatedOn());
        view.setLastUpdateOn(category.getLastUpdateOn());
        return view;
    }
}
