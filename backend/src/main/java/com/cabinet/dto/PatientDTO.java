package com.cabinet.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String cin;
    private String telephone;
    private LocalDate dateNaissance;
    private String adresse;
}
