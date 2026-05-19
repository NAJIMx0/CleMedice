package com.cabinet.util;

import com.cabinet.model.Paiement;
import com.cabinet.model.Patient;
import com.cabinet.model.RendezVous;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class ExcelExporter {

    public byte[] exportPatients(List<Patient> patients) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Patients");
            createHeaderRow(sheet, "ID", "Nom", "Prenom", "CIN", "Telephone", "Date Naissance", "Adresse");

            int rowNum = 1;
            for (Patient p : patients) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getNom());
                row.createCell(2).setCellValue(p.getPrenom());
                row.createCell(3).setCellValue(p.getCin() != null ? p.getCin() : "");
                row.createCell(4).setCellValue(p.getTelephone() != null ? p.getTelephone() : "");
                row.createCell(5).setCellValue(p.getDateNaissance() != null ? p.getDateNaissance().toString() : "");
                row.createCell(6).setCellValue(p.getAdresse() != null ? p.getAdresse() : "");
            }

            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export patients", e);
        }
    }

    public byte[] exportRendezVous(List<RendezVous> list) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("RendezVous");
            createHeaderRow(sheet, "ID", "Patient", "Medecin", "Date", "Heure", "Topic", "Statut");

            int rowNum = 1;
            for (RendezVous r : list) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(r.getId());
                row.createCell(1).setCellValue(r.getPatient().getNom() + " " + r.getPatient().getPrenom());
                row.createCell(2).setCellValue(r.getMedecin().getNom());
                row.createCell(3).setCellValue(r.getDate().toString());
                row.createCell(4).setCellValue(r.getHeure().toString());
                row.createCell(5).setCellValue(r.getTopic() != null ? r.getTopic() : "");
                row.createCell(6).setCellValue(r.getStatut().name());
            }

            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export rendez-vous", e);
        }
    }

    public byte[] exportFinance(List<Paiement> paiements) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Finance");
            createHeaderRow(sheet, "ID", "Patient", "Montant", "Date", "Mode Paiement", "Statut", "Notes");

            int rowNum = 1;
            double total = 0;
            for (Paiement p : paiements) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getRendezVous().getPatient().getNom() + " " + p.getRendezVous().getPatient().getPrenom());
                row.createCell(2).setCellValue(p.getMontant());
                row.createCell(3).setCellValue(p.getDate().toString());
                row.createCell(4).setCellValue(p.getModePaiement().name());
                row.createCell(5).setCellValue(p.getStatut());
                row.createCell(6).setCellValue(p.getNotes() != null ? p.getNotes() : "");
                total += p.getMontant();
            }

            Row totalRow = sheet.createRow(rowNum + 1);
            totalRow.createCell(1).setCellValue("TOTAL");
            totalRow.getCell(1).setCellStyle(getBoldStyle(workbook));
            totalRow.createCell(2).setCellValue(total);

            for (int i = 0; i < 7; i++) sheet.autoSizeColumn(i);
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to export finances", e);
        }
    }

    private void createHeaderRow(Sheet sheet, String... headers) {
        Row headerRow = sheet.createRow(0);
        CellStyle style = getBoldStyle(sheet.getWorkbook());
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }
    }

    private CellStyle getBoldStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
