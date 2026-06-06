package com.cabinet.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrdonnanceDTO {
    private Long consultationId;
    private String casPatient;
    private LocalDate date;
    private List<MedicamentDTO> medicaments;

    public Long getConsultationId() { return consultationId; }
    public void setConsultationId(Long consultationId) { this.consultationId = consultationId; }
    public String getCasPatient() { return casPatient; }
    public void setCasPatient(String casPatient) { this.casPatient = casPatient; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public List<MedicamentDTO> getMedicaments() { return medicaments; }
    public void setMedicaments(List<MedicamentDTO> medicaments) { this.medicaments = medicaments; }
}
