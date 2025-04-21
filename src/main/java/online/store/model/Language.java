package online.store.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "language")
@NoArgsConstructor
public class Language extends BaseEntity {
    private String name;

    public static Language of(String name) {
        Language language = new Language();
        language.setName(name);
        return language;
    }

    public Language(Long id, String name) {
        super();
        this.setId(id);
        this.name = name;
    }
}
