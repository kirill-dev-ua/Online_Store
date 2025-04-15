package online.store.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import online.store.dto.CategoryDto;
import online.store.repo.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryDto categoryDto;
    private final String categoryName = "TestCategory";

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        categoryDto = new CategoryDto();
        categoryDto.setName(categoryName);
    }

    @Test
    void testCreateCategory() throws Exception {
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(categoryName));
    }

    @Test
    void testGetById() throws Exception {
        String json = mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andReturn().getResponse().getContentAsString();

        CategoryDto saved = objectMapper.readValue(json, CategoryDto.class);

        mockMvc.perform(get("/api/v1/categories/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("TestCategory"));
    }
}
