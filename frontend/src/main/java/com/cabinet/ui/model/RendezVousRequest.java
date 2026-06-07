package com.cabinet.ui.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class RendezVousRequest {

    private final Long    patientId;
    private final Long    medecinId;
    private final LocalDate date;
    private final LocalTime heure;
    private final String  topic;
    private final String  statut;

    private RendezVousRequest(Builder builder) {
        this.patientId = builder.patientId;
        this.medecinId = builder.medecinId;
        this.date      = builder.date;
        this.heure     = builder.heure;
        this.topic     = builder.topic;
        this.statut    = builder.statut;
    }

    public Long      getPatientId() { return patientId; }
    public Long      getMedecinId() { return medecinId; }
    public LocalDate getDate()      { return date; }
    public LocalTime getHeure()     { return heure; }
    public String    getTopic()     { return topic; }
    public String    getStatut()    { return statut; }

    @Override
    public String toString() {
        return "RendezVousRequest{patientId=" + patientId +
               ", medecinId=" + medecinId +
               ", date=" + date +
               ", heure=" + heure +
               ", topic='" + topic + "'" +
               ", statut='" + statut + "'}";
    }

    public static class Builder {
        private Long      patientId;
        private Long      medecinId;
        private LocalDate date;
        private LocalTime heure;
        private String    topic  = "";
        private String    statut = "PLANIFIE";

        public Builder patientId(Long patientId) {
            this.patientId = patientId;
            return this;
        }
        public Builder medecinId(Long medecinId) {
            this.medecinId = medecinId;
            return this;
        }
        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }
        public Builder heure(LocalTime heure) {
            this.heure = heure;
            return this;
        }
        public Builder topic(String topic) {
            this.topic = topic;
            return this;
        }
        public Builder statut(String statut) {
            this.statut = statut;
            return this;
        }
        public RendezVousRequest build() {
            if (patientId == null) throw new IllegalStateException("patientId is required");
            if (medecinId == null) throw new IllegalStateException("medecinId is required");
            if (date == null)      throw new IllegalStateException("date is required");
            if (heure == null)     throw new IllegalStateException("heure is required");
            return new RendezVousRequest(this);
        }
    }
}
