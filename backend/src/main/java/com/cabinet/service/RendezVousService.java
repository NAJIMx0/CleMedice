package com.cabinet.service;

import com.cabinet.dto.RendezVousDTO;
import com.cabinet.model.*;
import com.cabinet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public List<RendezVous> getAllRendezVous() {
        return rendezVousRepository.findAll();
    }

    public RendezVous getRendezVousById(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RendezVous not found with id: " + id));
    }

    public RendezVous createRendezVous(RendezVousDTO dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        User medecin = userRepository.findById(dto.getMedecinId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        RendezVous rdv = RendezVous.builder()
                .patient(patient)
                .medecin(medecin)
                .date(dto.getDate())
                .heure(dto.getHeure())
                .topic(dto.getTopic())
                .statut(StatutRDV.PLANIFIE)
                .build();
        return rendezVousRepository.save(rdv);
    }

    public RendezVous updateStatut(Long id, String statut) {
        RendezVous rdv = getRendezVousById(id);
        rdv.setStatut(StatutRDV.valueOf(statut));
        return rendezVousRepository.save(rdv);
    }

    public List<RendezVous> getRendezVousByDate(LocalDate date) {
        return rendezVousRepository.findByDateOrderByHeure(date);
    }

    public List<RendezVous> getRendezVousBetweenDates(LocalDate start, LocalDate end) {
        return rendezVousRepository.findByDateBetweenOrderByDateAscHeureAsc(start, end);
    }

    public void deleteRendezVous(Long id) {
        rendezVousRepository.deleteById(id);
    }
}
