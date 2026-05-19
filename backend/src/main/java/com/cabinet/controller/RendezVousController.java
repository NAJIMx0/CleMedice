package com.cabinet.controller;

import com.cabinet.dto.RendezVousDTO;
import com.cabinet.model.RendezVous;
import com.cabinet.service.RendezVousService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rendezvous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;

    @GetMapping
    public ResponseEntity<List<RendezVous>> getAll() {
        return ResponseEntity.ok(rendezVousService.getAllRendezVous());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVous> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rendezVousService.getRendezVousById(id));
    }

    @GetMapping("/date")
    public ResponseEntity<List<RendezVous>> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(rendezVousService.getRendezVousByDate(date));
    }

    @GetMapping("/period")
    public ResponseEntity<List<RendezVous>> getByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(rendezVousService.getRendezVousBetweenDates(start, end));
    }

    @PostMapping
    public ResponseEntity<RendezVous> create(@RequestBody RendezVousDTO dto) {
        return ResponseEntity.ok(rendezVousService.createRendezVous(dto));
    }

    @PutMapping("/{id}/statut")
    public ResponseEntity<RendezVous> updateStatut(@PathVariable Long id, @RequestParam String statut) {
        return ResponseEntity.ok(rendezVousService.updateStatut(id, statut));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.ok().build();
    }
}
