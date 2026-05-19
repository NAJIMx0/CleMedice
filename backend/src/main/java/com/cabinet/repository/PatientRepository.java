package com.cabinet.repository;

import com.cabinet.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByCin(String cin);
    List<Patient> findByNomContainingIgnoreCase(String nom);
    List<Patient> findByPrenomContainingIgnoreCase(String prenom);

    @Query("SELECT p FROM Patient p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.prenom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR p.cin LIKE CONCAT('%', :keyword, '%')")
    List<Patient> search(String keyword);
}
