package online.store.dto;

import lombok.Getter;
import lombok.Setter;
import online.store.model.Language;

@Getter
@Setter
public class LanguageDto extends BaseResponseDto {
    private String name;

    public static LanguageDto of(Language language) {
        LanguageDto languageDto = new LanguageDto();
        languageDto.setId(language.getId());
        languageDto.setName(language.getName());
        languageDto.setCreatedOn(language.getCreatedOn());
        languageDto.setLastUpdateOn(language.getLastUpdateOn());
        return languageDto;
    }
}
