package com.cabinet.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "rendez_vous")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medecin_id", nullable = false)
    private User medecin;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime heure;

    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRDV statut;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (statut == null) statut = StatutRDV.PLANIFIE;
    }
}
