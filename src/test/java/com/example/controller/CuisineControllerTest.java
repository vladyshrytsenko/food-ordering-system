package com.example.controller;

import com.example.StartupApplication;
import com.example.model.dto.CuisineDto;
import com.example.model.entity.Cuisine;
import com.example.model.request.CuisineRequest;
import com.example.repository.CuisineRepository;
import com.example.service.CuisineService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = StartupApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CuisineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private CuisineService cuisineService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.cuisineRepository.deleteAll();
    }

    @Test
    public void testCreateCuisine() throws Exception {
        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
        String cuisineRequestAsString = objectMapper.writeValueAsString(cuisineRequest);

        this.mockMvc.perform(post("/api/cuisines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cuisineRequestAsString))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Mexican"));

        List<Cuisine> cuisines = this.cuisineRepository.findAll();
        assertEquals(1, cuisines.size());
        assertEquals("Mexican", cuisines.getFirst().getName());
    }

    @Test
    public void testFindAllCuisines() throws Exception {
        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
        this.cuisineService.create(cuisineRequest);

        this.mockMvc.perform(get("/api/cuisines"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Mexican"));
    }

    @Test
    public void testGetCuisineById() throws Exception {
        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
        CuisineDto cuisineDto = this.cuisineService.create(cuisineRequest);

        this.mockMvc.perform(get("/api/cuisines/{id}", cuisineDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Mexican"));
    }

    @Test
    public void testUpdateCuisine() throws Exception {
        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
        CuisineDto cuisineDto = this.cuisineService.create(cuisineRequest);

        CuisineRequest updateRequest = new CuisineRequest("Polish");
        String updatedRequestAsString = objectMapper.writeValueAsString(updateRequest);

        this.mockMvc.perform(put("/api/cuisines/{id}", cuisineDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRequestAsString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Polish"));

        Cuisine updatedCuisine = this.cuisineRepository.findById(cuisineDto.getId()).orElse(null);
        assertEquals("Polish", updatedCuisine.getName());
    }

    @Test
    public void testDeleteCuisine() throws Exception {
        CuisineRequest cuisineRequest = new CuisineRequest("Mexican");
        CuisineDto cuisineDto = this.cuisineService.create(cuisineRequest);

        this.mockMvc.perform(delete("/api/cuisines/{id}", cuisineDto.getId()))
            .andExpect(status().isNoContent());

        Optional<Cuisine> byId = this.cuisineRepository.findById(cuisineDto.getId());
        assertFalse(byId.isPresent());
    }
}
