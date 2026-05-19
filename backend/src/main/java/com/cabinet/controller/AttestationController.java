package com.cabinet.controller;

import com.cabinet.util.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/attestations")
@RequiredArgsConstructor
public class AttestationController {

    private final PdfGenerator pdfGenerator;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateAttestation(@RequestBody Map<String, String> request) {
        String patientNom = request.get("patientNom");
        String patientPrenom = request.get("patientPrenom");
        String contenu = request.get("contenu");
        String date = request.get("date");

        byte[] pdf = pdfGenerator.generateAttestation(patientNom, patientPrenom, contenu, date);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attestation.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
