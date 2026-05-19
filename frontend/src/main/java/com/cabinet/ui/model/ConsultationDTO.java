package com.cabinet.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsultationDTO {
    private Long id;
    private Long rendezVousId;
    private String description;
    private String observations;
    private String casPatient;
    private LocalDate date;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRendezVousId() { return rendezVousId; }
    public void setRendezVousId(Long rendezVousId) { this.rendezVousId = rendezVousId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }
    public String getCasPatient() { return casPatient; }
    public void setCasPatient(String casPatient) { this.casPatient = casPatient; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
