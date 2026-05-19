package com.cabinet.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PaiementDTO {
    private Long id;
    private Long rendezVousId;
    private String patientNom;
    private String patientPrenom;
    private Double montant;
    private LocalDate date;
    private String modePaiement;
    private String statut;
    private String notes;
}
