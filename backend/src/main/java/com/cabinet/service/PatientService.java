package com.cabinet.service;

import com.cabinet.dto.PatientDTO;
import com.cabinet.model.Patient;
import com.cabinet.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));
    }

    public Patient createPatient(PatientDTO dto) {
        Patient patient = Patient.builder()
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .cin(dto.getCin())
                .telephone(dto.getTelephone())
                .dateNaissance(dto.getDateNaissance())
                .adresse(dto.getAdresse())
                .build();
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, PatientDTO dto) {
        Patient patient = getPatientById(id);
        patient.setNom(dto.getNom());
        patient.setPrenom(dto.getPrenom());
        patient.setCin(dto.getCin());
        patient.setTelephone(dto.getTelephone());
        patient.setDateNaissance(dto.getDateNaissance());
        patient.setAdresse(dto.getAdresse());
        return patientRepository.save(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }

    public List<Patient> searchPatients(String keyword) {
        return patientRepository.search(keyword);
    }
}
