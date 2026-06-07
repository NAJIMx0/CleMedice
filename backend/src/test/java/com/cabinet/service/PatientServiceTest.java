package com.cabinet.service;

import com.cabinet.dto.PatientDTO;
import com.cabinet.model.Patient;
import com.cabinet.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PatientService — Unit Tests")
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient samplePatient;
    private PatientDTO sampleDTO;

    @BeforeEach
    void setUp() {
        samplePatient = Patient.builder()
            .id(1L)
            .nom("Badr Eddin")
            .prenom("Najim")
            .cin("BK247891")
            .telephone("0661234567")
            .dateNaissance(LocalDate.of(1995, 3, 14))
            .adresse("Rue Ibn Battouta 12, Tanger")
            .build();

        sampleDTO = new PatientDTO();
        sampleDTO.setNom("Badr Eddin");
        sampleDTO.setPrenom("Najim");
        sampleDTO.setCin("BK247891");
        sampleDTO.setTelephone("0661234567");
        sampleDTO.setDateNaissance(LocalDate.of(1995, 3, 14));
        sampleDTO.setAdresse("Rue Ibn Battouta 12, Tanger");
    }

    @Test
    @DisplayName("getAllPatients returns all patients from repository")
    void testGetAllPatients() {
        when(patientRepository.findAll()).thenReturn(List.of(samplePatient));

        List<Patient> result = patientService.getAllPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Badr Eddin", result.get(0).getNom());
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("getPatientById returns correct patient")
    void testGetPatientById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(samplePatient));

        Patient result = patientService.getPatientById(1L);

        assertNotNull(result);
        assertEquals("Najim", result.getPrenom());
        assertEquals("BK247891", result.getCin());
    }

    @Test
    @DisplayName("getPatientById throws RuntimeException when not found")
    void testGetPatientByIdNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> patientService.getPatientById(99L));
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("createPatient saves and returns patient")
    void testCreatePatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(samplePatient);

        Patient result = patientService.createPatient(sampleDTO);

        assertNotNull(result);
        assertEquals("Badr Eddin", result.getNom());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("deletePatient calls repository deleteById")
    void testDeletePatient() {
        doNothing().when(patientRepository).deleteById(1L);

        assertDoesNotThrow(() -> patientService.deletePatient(1L));
        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("searchPatients delegates to repository search")
    void testSearchPatients() {
        when(patientRepository.search("Najim")).thenReturn(List.of(samplePatient));

        List<Patient> result = patientService.searchPatients("Najim");

        assertEquals(1, result.size());
        assertEquals("Najim", result.get(0).getPrenom());
        verify(patientRepository, times(1)).search("Najim");
    }
}
