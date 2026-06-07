package com.cabinet.controller;

import com.cabinet.dto.PatientDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("PatientController — Integration Tests")
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule());

    private String getAdminToken() throws Exception {
        String loginBody = """
            {"email":"admin@clemedice.com","password":"admin123"}
            """;
        String response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        int start = response.indexOf("\"token\":\"") + 9;
        int end   = response.indexOf("\"", start);
        return response.substring(start, end);
    }

    @Test
    @DisplayName("GET /api/patients returns 200 with valid token")
    void testGetAllPatientsAuthenticated() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get("/api/patients")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/patients returns 403 without token")
    void testGetAllPatientsUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/patients"))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/patients creates a new patient")
    void testCreatePatient() throws Exception {
        String token = getAdminToken();

        PatientDTO dto = new PatientDTO();
        dto.setNom("TestNom");
        dto.setPrenom("TestPrenom");
        dto.setCin("TEST00001");
        dto.setTelephone("0600000001");
        dto.setDateNaissance(LocalDate.of(1990, 1, 1));
        dto.setAdresse("Rue Test 1, Tanger");

        mockMvc.perform(post("/api/patients")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nom").value("TestNom"))
            .andExpect(jsonPath("$.prenom").value("TestPrenom"))
            .andExpect(jsonPath("$.cin").value("TEST00001"));
    }

    @Test
    @DisplayName("GET /api/patients/search returns matching patients")
    void testSearchPatients() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get("/api/patients/search?keyword=Najim")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /api/auth/login with wrong password returns 401")
    void testLoginWrongPassword() throws Exception {
        String body = """
            {"email":"admin@clemedice.com","password":"wrongpassword"}
            """;
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isUnauthorized());
    }
}
