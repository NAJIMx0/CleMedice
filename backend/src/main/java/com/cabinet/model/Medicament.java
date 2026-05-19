package com.cabinet.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medicaments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordonnance_id", nullable = false)
    private Ordonnance ordonnance;

    @Column(nullable = false)
    private String nom;

    private String dosage;

    private String duree;

    private String instructions;
}
