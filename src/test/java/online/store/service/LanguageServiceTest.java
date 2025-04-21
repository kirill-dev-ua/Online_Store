package online.store.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import online.store.dto.LanguageDto;
import online.store.model.Language;
import online.store.model.PageResponse;
import online.store.repo.LanguageRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class LanguageServiceTest {

    private static final Long LANGUAGE_ID = 1L;
    private static final String LANGUAGE_NAME = "English";
    private static final String UPDATED_NAME = "Ukrainian";
    private static final String NOT_FOUND_MESSAGE = "Language with id " + LANGUAGE_ID + " not found";
    private static final String DUPLICATE_NAME_MESSAGE = "Language with name '" + LANGUAGE_NAME + "' already exists";

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageService languageService;

    private LanguageDto languageDto;

    @BeforeEach
    void setUp() {
        languageDto = createLanguageDto(LANGUAGE_ID, LANGUAGE_NAME);
    }

    @Test
    void shouldCreateLanguage_whenValidDataProvided() {
        when(languageRepository.save(any(Language.class)))
                .thenReturn(createLanguage(LANGUAGE_ID, LANGUAGE_NAME));

        var result = languageService.create(languageDto);

        assertNotNull(result);
        assertEquals(LANGUAGE_NAME, result.getName());
        verify(languageRepository).save(any(Language.class));
    }

    @Test
    void shouldThrowException_whenLanguageAlreadyExists() {
        when(languageRepository.existsLanguageByName(LANGUAGE_NAME))
                .thenReturn(true);

        var exception = assertThrows(EntityExistsException.class,
                () -> languageService.create(languageDto));

        assertEquals(DUPLICATE_NAME_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldUpdateLanguage_whenExists() {
        var updateDto = createLanguageDto(null, UPDATED_NAME);

        when(languageRepository.findById(LANGUAGE_ID))
                .thenReturn(Optional.of(createLanguage(LANGUAGE_ID, LANGUAGE_NAME)));
        when(languageRepository.save(any(Language.class)))
                .thenReturn(createLanguage(LANGUAGE_ID, UPDATED_NAME));

        var result = languageService.update(LANGUAGE_ID, updateDto);

        assertNotNull(result);
        assertEquals(UPDATED_NAME, result.getName());
        assertEquals(LANGUAGE_ID, result.getId());
    }

    @Test
    void shouldThrowNotFoundException_whenLanguageIdNotFoundOnUpdate() {
        when(languageRepository.findById(LANGUAGE_ID)).thenReturn(Optional.empty());

        var dto = createLanguageDto(null, UPDATED_NAME);

        var exception = assertThrows(EntityNotFoundException.class,
                () -> languageService.update(LANGUAGE_ID, dto));

        assertEquals(NOT_FOUND_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldThrowExistsException_whenLanguageWithSameNameExistsOnUpdate() {
        when(languageRepository.findById(LANGUAGE_ID))
                .thenReturn(Optional.of(createLanguage(LANGUAGE_ID, LANGUAGE_NAME)));
        when(languageRepository.existsLanguageByName(LANGUAGE_NAME))
                .thenReturn(true);

        var exception = assertThrows(EntityExistsException.class,
                () -> languageService.update(LANGUAGE_ID, languageDto));

        assertEquals(DUPLICATE_NAME_MESSAGE, exception.getMessage());
    }

    @Test
    void shouldReturnLanguage_whenFoundById() {
        when(languageRepository.findById(LANGUAGE_ID))
                .thenReturn(Optional.of(createLanguage(LANGUAGE_ID, LANGUAGE_NAME)));

        var result = languageService.findById(LANGUAGE_ID);

        assertNotNull(result);
        assertEquals(LANGUAGE_ID, result.getId());
        assertEquals(LANGUAGE_NAME, result.getName());
    }

    @Test
    void shouldThrowNotFoundException_whenLanguageNotFoundById() {
        when(languageRepository.findById(LANGUAGE_ID))
                .thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class,
                () -> languageService.findById(LANGUAGE_ID));

        assertEquals(NOT_FOUND_MESSAGE, exception.getMessage());
        verify(languageRepository).findById(LANGUAGE_ID);
    }

    @Test
    void shouldReturnAllLanguages_whenRequested() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Language> languages = List.of(
                createLanguage(1L, "English"),
                createLanguage(2L, "French")
        );
        Page<Language> page = new PageImpl<>(languages, pageable, languages.size());

        when(languageRepository.findAll(pageable)).thenReturn(page);

        PageResponse<LanguageDto> result = languageService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.records().size());
        assertEquals("English", result.records().get(0).getName());
        assertEquals("French", result.records().get(1).getName());
    }

    @Test
    void shouldDeleteLanguage_whenExists() {
        when(languageRepository.existsById(LANGUAGE_ID)).thenReturn(true);

        languageService.delete(LANGUAGE_ID);

        verify(languageRepository).existsById(LANGUAGE_ID);
        verify(languageRepository).deleteById(LANGUAGE_ID);
    }

    @Test
    void shouldThrowNotFoundException_whenDeletingNonExistentLanguage() {
        when(languageRepository.existsById(LANGUAGE_ID)).thenReturn(false);

        var exception = assertThrows(EntityNotFoundException.class,
                () -> languageService.delete(LANGUAGE_ID));

        assertEquals(NOT_FOUND_MESSAGE, exception.getMessage());
        verify(languageRepository, never()).deleteById(any());
    }

    private LanguageDto createLanguageDto(Long id, String name) {
        LanguageDto dto = new LanguageDto();
        dto.setId(id);
        dto.setName(name);
        return dto;
    }

    private Language createLanguage(Long id, String name) {
        return new Language(id, name);
    }
}