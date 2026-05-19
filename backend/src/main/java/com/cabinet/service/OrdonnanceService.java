package com.cabinet.service;

import com.cabinet.dto.MedicamentDTO;
import com.cabinet.dto.OrdonnanceDTO;
import com.cabinet.model.*;
import com.cabinet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdonnanceService {

    private final OrdonnanceRepository ordonnanceRepository;
    private final ConsultationDetailRepository consultationRepository;
    private final MedicamentRepository medicamentRepository;

    public List<Ordonnance> getAllOrdonnances() {
        return ordonnanceRepository.findAll();
    }

    public Ordonnance getOrdonnanceById(Long id) {
        return ordonnanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordonnance not found with id: " + id));
    }

    public Ordonnance createOrdonnance(OrdonnanceDTO dto) {
        ConsultationDetail consultation = consultationRepository.findById(dto.getConsultationId())
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        Ordonnance ordonnance = Ordonnance.builder()
                .consultation(consultation)
                .casPatient(dto.getCasPatient())
                .date(dto.getDate())
                .medicaments(new ArrayList<>())
                .build();

        if (dto.getMedicaments() != null) {
            for (MedicamentDTO medDto : dto.getMedicaments()) {
                Medicament med = Medicament.builder()
                        .nom(medDto.getNom())
                        .dosage(medDto.getDosage())
                        .duree(medDto.getDuree())
                        .instructions(medDto.getInstructions())
                        .ordonnance(ordonnance)
                        .build();
                ordonnance.getMedicaments().add(med);
            }
        }

        return ordonnanceRepository.save(ordonnance);
    }

    public void deleteOrdonnance(Long id) {
        ordonnanceRepository.deleteById(id);
    }
}
