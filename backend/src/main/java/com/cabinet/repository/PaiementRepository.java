package com.cabinet.repository;

import com.cabinet.model.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Optional<Paiement> findByRendezVousId(Long rendezVousId);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE p.date BETWEEN :start AND :end")
    Double sumByDateBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    List<Paiement> findByDateBetweenOrderByDateAsc(LocalDate start, LocalDate end);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE p.date BETWEEN :start AND :end AND p.rendezVous.medecin.id = :medecinId")
    Double sumByDateBetweenAndMedecin(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("medecinId") Long medecinId);

    @Query("SELECT FUNCTION('MONTH', p.date) as mois, COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE FUNCTION('YEAR', p.date) = :annee GROUP BY FUNCTION('MONTH', p.date) ORDER BY FUNCTION('MONTH', p.date)")
    List<Object[]> monthlyTotalsForYear(@Param("annee") int annee);

    @Query("SELECT FUNCTION('YEAR', p.date) as annee, COALESCE(SUM(p.montant), 0) FROM Paiement p GROUP BY FUNCTION('YEAR', p.date) ORDER BY FUNCTION('YEAR', p.date)")
    List<Object[]> yearlyTotals();
}
