package com.cabinet.repository;

import com.cabinet.model.ConsultationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConsultationDetailRepository extends JpaRepository<ConsultationDetail, Long> {
    Optional<ConsultationDetail> findByRendezVousId(Long rendezVousId);
    List<ConsultationDetail> findByCasPatientContainingIgnoreCase(String casPatient);
}
