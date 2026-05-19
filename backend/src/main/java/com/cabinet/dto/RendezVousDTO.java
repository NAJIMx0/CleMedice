package com.cabinet.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RendezVousDTO {
    private Long id;
    private Long patientId;
    private String patientNom;
    private String patientPrenom;
    private Long medecinId;
    private String medecinNom;
    private LocalDate date;
    private LocalTime heure;
    private String topic;
    private String statut;
}
