package com.cabinet.service;

import com.cabinet.dto.FinanceSummaryDTO;
import com.cabinet.dto.PaiementDTO;
import com.cabinet.model.*;
import com.cabinet.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaiementService {

    private final PaiementRepository paiementRepository;
    private final RendezVousRepository rendezVousRepository;

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Paiement getPaiementById(Long id) {
        return paiementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paiement not found with id: " + id));
    }

    public Paiement createPaiement(PaiementDTO dto) {
        RendezVous rdv = rendezVousRepository.findById(dto.getRendezVousId())
                .orElseThrow(() -> new RuntimeException("RendezVous not found"));

        Paiement paiement = Paiement.builder()
                .rendezVous(rdv)
                .montant(dto.getMontant())
                .date(dto.getDate())
                .modePaiement(ModePaiement.valueOf(dto.getModePaiement()))
                .statut("PAYE")
                .notes(dto.getNotes())
                .build();
        return paiementRepository.save(paiement);
    }

    public FinanceSummaryDTO getFinanceSummary(int annee, int mois) {
        LocalDate startMonth = LocalDate.of(annee, mois, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        LocalDate startYear = LocalDate.of(annee, 1, 1);
        LocalDate endYear = LocalDate.of(annee, 12, 31);

        Double totalMensuel = paiementRepository.sumByDateBetween(startMonth, endMonth);
        Double totalAnnuel = paiementRepository.sumByDateBetween(startYear, endYear);

        List<Object[]> monthlyData = paiementRepository.monthlyTotalsForYear(annee);
        Map<Integer, Double> mensuelParMois = new LinkedHashMap<>();
        for (Object[] row : monthlyData) {
            mensuelParMois.put((Integer) row[0], (Double) row[1]);
        }

        return new FinanceSummaryDTO(totalMensuel, totalAnnuel, mensuelParMois);
    }

    public List<Paiement> getPaiementsBetweenDates(LocalDate start, LocalDate end) {
        return paiementRepository.findByDateBetweenOrderByDateAsc(start, end);
    }

    public void deletePaiement(Long id) {
        paiementRepository.deleteById(id);
    }
}
