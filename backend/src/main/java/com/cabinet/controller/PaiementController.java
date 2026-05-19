package com.cabinet.controller;

import com.cabinet.dto.FinanceSummaryDTO;
import com.cabinet.dto.PaiementDTO;
import com.cabinet.model.Paiement;
import com.cabinet.service.PaiementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/finance")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService paiementService;

    @GetMapping
    public ResponseEntity<List<Paiement>> getAll() {
        return ResponseEntity.ok(paiementService.getAllPaiements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paiement> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paiementService.getPaiementById(id));
    }

    @PostMapping
    public ResponseEntity<Paiement> create(@RequestBody PaiementDTO dto) {
        return ResponseEntity.ok(paiementService.createPaiement(dto));
    }

    @GetMapping("/summary")
    public ResponseEntity<FinanceSummaryDTO> getSummary(
            @RequestParam int annee,
            @RequestParam int mois) {
        return ResponseEntity.ok(paiementService.getFinanceSummary(annee, mois));
    }

    @GetMapping("/period")
    public ResponseEntity<List<Paiement>> getByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(paiementService.getPaiementsBetweenDates(start, end));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        paiementService.deletePaiement(id);
        return ResponseEntity.ok().build();
    }
}
