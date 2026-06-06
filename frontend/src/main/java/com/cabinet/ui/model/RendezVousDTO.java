package com.cabinet.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonProperty("patient")
    private PatientInfo patient;
    @JsonProperty("medecin")
    private MedecinInfo medecin;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPatientId() {
        if (patient != null) return patient.id;
        return patientId;
    }
    public void setPatientId(Long patientId) { this.patientId = patientId; }

    public String getPatientNom() {
        if (patient != null) {
            String name = patient.nom;
            if (patient.prenom != null && !patient.prenom.isEmpty()) name += " " + patient.prenom;
            return name;
        }
        return patientNom;
    }
    public void setPatientNom(String patientNom) { this.patientNom = patientNom; }

    public String getPatientPrenom() {
        if (patient != null) return patient.prenom;
        return patientPrenom;
    }
    public void setPatientPrenom(String patientPrenom) { this.patientPrenom = patientPrenom; }

    public Long getMedecinId() {
        if (medecin != null) return medecin.id;
        return medecinId;
    }
    public void setMedecinId(Long medecinId) { this.medecinId = medecinId; }

    public String getMedecinNom() {
        if (medecin != null) return medecin.nom;
        return medecinNom;
    }
    public void setMedecinNom(String medecinNom) { this.medecinNom = medecinNom; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getHeure() { return heure; }
    public void setHeure(LocalTime heure) { this.heure = heure; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public static class PatientInfo {
        public Long id;
        public String nom;
        public String prenom;
    }

    public static class MedecinInfo {
        public Long id;
        public String nom;
    }
}
