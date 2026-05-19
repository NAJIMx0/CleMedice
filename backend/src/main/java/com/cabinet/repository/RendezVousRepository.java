package com.cabinet.repository;

import com.cabinet.model.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    List<RendezVous> findByDateOrderByHeure(LocalDate date);
    List<RendezVous> findByPatientId(Long patientId);
    List<RendezVous> findByMedecinId(Long medecinId);
    List<RendezVous> findByDateBetweenOrderByDateAscHeureAsc(LocalDate start, LocalDate end);
    List<RendezVous> findByStatut(String statut);
    long countByDate(LocalDate date);
}
