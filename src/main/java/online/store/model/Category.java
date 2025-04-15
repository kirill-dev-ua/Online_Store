package online.store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "category")
@NoArgsConstructor
public class Category extends BaseEntity {
    private String name;

    public static Category of(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

    public Category(Long id, String name) {
        super();
        this.setId(id);
        this.name = name;
    }
}
