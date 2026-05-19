package com.cabinet.controller;

import com.cabinet.dto.ConsultationDTO;
import com.cabinet.model.ConsultationDetail;
import com.cabinet.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @GetMapping
    public ResponseEntity<List<ConsultationDetail>> getAll() {
        return ResponseEntity.ok(consultationService.getAllConsultations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationDetail> getById(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getConsultationById(id));
    }

    @GetMapping("/rendezvous/{rdvId}")
    public ResponseEntity<ConsultationDetail> getByRendezVous(@PathVariable Long rdvId) {
        return ResponseEntity.ok(consultationService.getConsultationByRendezVous(rdvId));
    }

    @PostMapping
    public ResponseEntity<ConsultationDetail> create(@RequestBody ConsultationDTO dto) {
        return ResponseEntity.ok(consultationService.createConsultation(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationDetail> update(@PathVariable Long id, @RequestBody ConsultationDTO dto) {
        return ResponseEntity.ok(consultationService.updateConsultation(id, dto));
    }
}
