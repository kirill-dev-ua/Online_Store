package online.store.controller;

import lombok.RequiredArgsConstructor;
import online.store.dto.LanguageDto;
import online.store.exceptions.BigSizeException;
import online.store.model.PageResponse;
import online.store.service.LanguageService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/languages")
@RequiredArgsConstructor
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping("/{id}")
    public ResponseEntity<LanguageDto> getById(@PathVariable Long id) {
        LanguageDto languageDto = languageService.findById(id);
        return ResponseEntity.ok(languageDto);
    }

    @GetMapping
    public ResponseEntity<PageResponse<LanguageDto>> getAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") String sort) {

        if (size > 100) {
            throw new BigSizeException("You can get maximum 100 positions at one time");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        PageResponse<LanguageDto> response = languageService.findAll(pageable);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LanguageDto> update(@PathVariable Long id, @RequestBody LanguageDto dto) {
        LanguageDto updateLanguage = languageService.update(id, dto);
        return ResponseEntity.ok(updateLanguage);
    }

    @PostMapping
    public ResponseEntity<LanguageDto> create(@RequestBody LanguageDto dto) {
        LanguageDto result = languageService.create(dto);
        URI location = URI.create("/api/v1/languages/" + result.getId());
        return ResponseEntity.created(location).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        languageService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
