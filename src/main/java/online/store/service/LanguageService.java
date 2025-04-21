package online.store.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import online.store.dto.LanguageDto;
import online.store.model.Language;
import online.store.model.PageResponse;
import online.store.repo.LanguageRepository;
import online.store.utils.PageResponseUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;

    public LanguageDto create(LanguageDto dto) {
        Language language = Language.of(dto.getName());
        if (languageRepository.existsLanguageByName(language.getName())) {
            throw new EntityExistsException("Language with name '"
                    + language.getName() + "' already exists");
        }
        return LanguageDto.of(languageRepository.save(language));
    }

    public LanguageDto update(Long id, LanguageDto dtoUpdate) {
        Language update = languageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Language with id " + id + " not found"));
        if (languageRepository.existsLanguageByName(dtoUpdate.getName())) {
            throw new EntityExistsException("Language with name '"
                    + dtoUpdate.getName() + "' already exists");
        }
        update.setName(dtoUpdate.getName());
        update.setLastUpdateOn(LocalDateTime.now());

        return LanguageDto.of(languageRepository.save(update));
    }

    public LanguageDto findById(Long id) {
        Language language = languageRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Language with id " + id + " not found"));
        return LanguageDto.of(language);
    }

    public PageResponse<LanguageDto> findAll(Pageable pageable) {
        Page<Language> page = languageRepository.findAll(pageable);
        return PageResponseUtil.toPageResponse(page, LanguageDto::of);
    }

    public void delete(Long id) {
        if (!languageRepository.existsById(id)) {
            throw new EntityNotFoundException("Language with id " + id + " not found");
        }
        languageRepository.deleteById(id);
    }
}
