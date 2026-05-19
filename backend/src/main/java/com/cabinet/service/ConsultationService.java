package com.cabinet.service;

import com.cabinet.dto.ConsultationDTO;
import com.cabinet.model.*;
import com.cabinet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationDetailRepository consultationRepository;
    private final RendezVousRepository rendezVousRepository;

    public List<ConsultationDetail> getAllConsultations() {
        return consultationRepository.findAll();
    }

    public ConsultationDetail getConsultationById(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultation not found with id: " + id));
    }

    public ConsultationDetail getConsultationByRendezVous(Long rendezVousId) {
        return consultationRepository.findByRendezVousId(rendezVousId)
                .orElseThrow(() -> new RuntimeException("Consultation not found for this rendez-vous"));
    }

    public ConsultationDetail createConsultation(ConsultationDTO dto) {
        RendezVous rdv = rendezVousRepository.findById(dto.getRendezVousId())
                .orElseThrow(() -> new RuntimeException("RendezVous not found"));

        ConsultationDetail consultation = ConsultationDetail.builder()
                .rendezVous(rdv)
                .description(dto.getDescription())
                .observations(dto.getObservations())
                .casPatient(dto.getCasPatient())
                .date(dto.getDate())
                .build();
        consultation = consultationRepository.save(consultation);

        rdv.setStatut(StatutRDV.EFFECTUE);
        rendezVousRepository.save(rdv);

        return consultation;
    }

    public ConsultationDetail updateConsultation(Long id, ConsultationDTO dto) {
        ConsultationDetail consultation = getConsultationById(id);
        consultation.setDescription(dto.getDescription());
        consultation.setObservations(dto.getObservations());
        consultation.setCasPatient(dto.getCasPatient());
        consultation.setDate(dto.getDate());
        return consultationRepository.save(consultation);
    }
}
