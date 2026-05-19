package com.cabinet.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrdonnanceDTO {
    private Long id;
    private Long consultationId;
    private String casPatient;
    private LocalDate date;
    private List<MedicamentDTO> medicaments;
}
