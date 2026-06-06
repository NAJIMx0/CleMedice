package com.cabinet.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonProperty("rendezVous")
    private RendezVousInfo rendezVous;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRendezVousId() { return rendezVousId; }
    public void setRendezVousId(Long rendezVousId) { this.rendezVousId = rendezVousId; }

    public String getPatientNom() {
        if (rendezVous != null && rendezVous.patient != null) {
            String name = rendezVous.patient.nom;
            if (rendezVous.patient.prenom != null) name += " " + rendezVous.patient.prenom;
            return name;
        }
        return patientNom;
    }
    public void setPatientNom(String patientNom) { this.patientNom = patientNom; }

    public String getPatientPrenom() {
        if (rendezVous != null && rendezVous.patient != null) return rendezVous.patient.prenom;
        return patientPrenom;
    }
    public void setPatientPrenom(String patientPrenom) { this.patientPrenom = patientPrenom; }

    public Double getMontant() { return montant; }
    public void setMontant(Double montant) { this.montant = montant; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public static class RendezVousInfo {
        public PatientInfo patient;
    }

    public static class PatientInfo {
        public String nom;
        public String prenom;
    }
}
