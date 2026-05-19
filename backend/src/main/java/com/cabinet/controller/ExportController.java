package com.cabinet.controller;

import com.cabinet.model.Paiement;
import com.cabinet.model.Patient;
import com.cabinet.model.RendezVous;
import com.cabinet.service.PaiementService;
import com.cabinet.service.PatientService;
import com.cabinet.service.RendezVousService;
import com.cabinet.util.ExcelExporter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
public class ExportController {

    private final PatientService patientService;
    private final RendezVousService rendezVousService;
    private final PaiementService paiementService;
    private final ExcelExporter excelExporter;

    @GetMapping("/patients")
    public ResponseEntity<byte[]> exportPatients() {
        List<Patient> patients = patientService.getAllPatients();
        byte[] data = excelExporter.exportPatients(patients);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=patients.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @GetMapping("/rendezvous")
    public ResponseEntity<byte[]> exportRendezVous(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<RendezVous> list;
        if (start != null && end != null) {
            list = rendezVousService.getRendezVousBetweenDates(start, end);
        } else {
            list = rendezVousService.getAllRendezVous();
        }
        byte[] data = excelExporter.exportRendezVous(list);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=rendezvous.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }

    @GetMapping("/finance")
    public ResponseEntity<byte[]> exportFinance(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Paiement> paiements = paiementService.getPaiementsBetweenDates(start, end);
        byte[] data = excelExporter.exportFinance(paiements);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=finance.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(data);
    }
}
