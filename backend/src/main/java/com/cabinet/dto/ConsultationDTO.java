package com.cabinet.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ConsultationDTO {
    private Long id;
    private Long rendezVousId;
    private String description;
    private String observations;
    private String casPatient;
    private LocalDate date;
}
