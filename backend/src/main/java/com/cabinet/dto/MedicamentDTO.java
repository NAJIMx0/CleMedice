package com.cabinet.dto;

import lombok.Data;

@Data
public class MedicamentDTO {
    private Long id;
    private String nom;
    private String dosage;
    private String duree;
    private String instructions;
}
