package com.example.controller;

import com.example.StartupApplication;
import com.example.model.dto.DessertDto;
import com.example.model.entity.Dessert;
import com.example.model.request.CuisineRequest;
import com.example.model.request.DessertRequest;
import com.example.repository.CuisineRepository;
import com.example.repository.DessertRepository;
import com.example.service.CuisineService;
import com.example.service.DessertService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = StartupApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DessertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DessertRepository dessertRepository;

    @Autowired
    private DessertService dessertService;

    @Autowired
    private CuisineService cuisineService;

    @Autowired
    private CuisineRepository cuisineRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.dessertRepository.deleteAll();
        this.cuisineRepository.deleteAll();

        CuisineRequest cuisineRequest = new CuisineRequest();
        cuisineRequest.setName("Italian");
        this.cuisineService.create(cuisineRequest);
    }

    @Test
    public void testCreateDessert() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setCuisineName("Italian");
            setPortionWeight(200);
            setPrice(4f);
        }};

        String dessertRequestAsString = objectMapper.writeValueAsString(dessertRequest);
        this.mockMvc.perform(post("/api/desserts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dessertRequestAsString))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Chocolate Cake"));

        List<Dessert> desserts = this.dessertRepository.findAll();
        assertEquals(1, desserts.size());
        assertEquals("Chocolate Cake", desserts.getFirst().getName());
    }

    @Test
    public void testFindAllDesserts() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setCuisineName("Italian");
            setPortionWeight(200);
            setPrice(4f);
        }};
        this.dessertService.create(dessertRequest);

        this.mockMvc.perform(get("/api/desserts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name").value("Chocolate Cake"));
    }

    @Test
    public void testGetDessertById() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setCuisineName("Italian");
            setPortionWeight(200);
            setPrice(4f);
        }};
        DessertDto dessertDto = this.dessertService.create(dessertRequest);

        this.mockMvc.perform(get("/api/desserts/{id}", dessertDto.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Chocolate Cake"));
    }

    @Test
    public void testUpdateDessert() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setCuisineName("Italian");
            setPortionWeight(200);
            setPrice(4f);
        }};
        DessertDto dessertDto = this.dessertService.create(dessertRequest);

        DessertRequest updateRequest = new DessertRequest();
        updateRequest.setName("Vanilla Cake");
        String updatedRequestAsString = objectMapper.writeValueAsString(updateRequest);

        this.mockMvc.perform(put("/api/desserts/{id}", dessertDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedRequestAsString))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Vanilla Cake"));

        Dessert updatedDessert = this.dessertRepository.findById(dessertDto.getId()).orElse(null);
        assertEquals("Vanilla Cake", updatedDessert.getName());
    }

    @Test
    public void testDeleteDessert() throws Exception {
        DessertRequest dessertRequest = new DessertRequest() {{
            setName("Chocolate Cake");
            setCuisineName("Italian");
            setPortionWeight(200);
            setPrice(4f);
        }};
        DessertDto dessertDto = this.dessertService.create(dessertRequest);

        this.mockMvc.perform(delete("/api/desserts/{id}", dessertDto.getId()))
            .andExpect(status().isNoContent());

        Optional<Dessert> byId = this.dessertRepository.findById(dessertDto.getId());
        assertFalse(byId.isPresent());
    }
}