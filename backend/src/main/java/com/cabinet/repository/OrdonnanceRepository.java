package com.cabinet.repository;

import com.cabinet.model.Ordonnance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Long> {
    List<Ordonnance> findByConsultationId(Long consultationId);
    List<Ordonnance> findByCasPatientContainingIgnoreCase(String casPatient);
}
