package com.cabinet.controller;

import com.cabinet.dto.OrdonnanceDTO;
import com.cabinet.model.Ordonnance;
import com.cabinet.service.OrdonnanceService;
import com.cabinet.util.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordonnances")
@RequiredArgsConstructor
public class OrdonnanceController {

    private final OrdonnanceService ordonnanceService;
    private final PdfGenerator pdfGenerator;

    @GetMapping
    public ResponseEntity<List<Ordonnance>> getAll() {
        return ResponseEntity.ok(ordonnanceService.getAllOrdonnances());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ordonnance> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ordonnanceService.getOrdonnanceById(id));
    }

    @PostMapping
    public ResponseEntity<Ordonnance> create(@RequestBody OrdonnanceDTO dto) {
        return ResponseEntity.ok(ordonnanceService.createOrdonnance(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ordonnanceService.deleteOrdonnance(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long id) {
        Ordonnance ordonnance = ordonnanceService.getOrdonnanceById(id);
        byte[] pdf = pdfGenerator.generateOrdonnance(ordonnance);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ordonnance_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
