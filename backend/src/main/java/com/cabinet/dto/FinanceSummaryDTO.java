package com.cabinet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class FinanceSummaryDTO {
    private Double totalMensuel;
    private Double totalAnnuel;
    private Map<Integer, Double> mensuelParMois;
}
