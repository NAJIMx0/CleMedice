package com.cabinet.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceSummaryDTO {
    private Double totalMensuel;
    private Double totalAnnuel;
    private Map<Integer, Double> mensuelParMois;

    public Double getTotalMensuel() { return totalMensuel; }
    public void setTotalMensuel(Double totalMensuel) { this.totalMensuel = totalMensuel; }
    public Double getTotalAnnuel() { return totalAnnuel; }
    public void setTotalAnnuel(Double totalAnnuel) { this.totalAnnuel = totalAnnuel; }
    public Map<Integer, Double> getMensuelParMois() { return mensuelParMois; }
    public void setMensuelParMois(Map<Integer, Double> mensuelParMois) { this.mensuelParMois = mensuelParMois; }
}
