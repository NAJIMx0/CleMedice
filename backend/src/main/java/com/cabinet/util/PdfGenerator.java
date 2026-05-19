package com.cabinet.util;

import com.cabinet.model.Ordonnance;
import com.cabinet.model.Medicament;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class PdfGenerator {

    public byte[] generateOrdonnance(Ordonnance ordonnance) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("ORDONNANCE MEDICALE")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            document.add(new Paragraph("Cabinet Medical CleMedice")
                    .setFontSize(14)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Date: " + ordonnance.getDate()));
            document.add(new Paragraph("Cas: " + (ordonnance.getCasPatient() != null ? ordonnance.getCasPatient() : "")));
            document.add(new Paragraph("Patient: " + ordonnance.getConsultation().getRendezVous().getPatient().getNom()
                    + " " + ordonnance.getConsultation().getRendezVous().getPatient().getPrenom()));
            document.add(new Paragraph(" "));

            if (ordonnance.getMedicaments() != null && !ordonnance.getMedicaments().isEmpty()) {
                Table table = new Table(UnitValue.createPercentArray(new float[]{40, 20, 20, 20}));
                table.addHeaderCell(new Cell().add(new Paragraph("Medicament").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Dosage").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Duree").setBold()));
                table.addHeaderCell(new Cell().add(new Paragraph("Instructions").setBold()));

                for (Medicament med : ordonnance.getMedicaments()) {
                    table.addCell(new Cell().add(new Paragraph(med.getNom())));
                    table.addCell(new Cell().add(new Paragraph(med.getDosage() != null ? med.getDosage() : "")));
                    table.addCell(new Cell().add(new Paragraph(med.getDuree() != null ? med.getDuree() : "")));
                    table.addCell(new Cell().add(new Paragraph(med.getInstructions() != null ? med.getInstructions() : "")));
                }
                document.add(table);
            }

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Signature du medecin: ___________________"));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    public byte[] generateAttestation(String patientNom, String patientPrenom, String contenu, String date) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("ATTESTATION MEDICALE")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            document.add(new Paragraph("Cabinet Medical CleMedice")
                    .setFontSize(14)
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Date: " + date));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Je soussigne, medecin au Cabinet Medical CleMedice,"));
            document.add(new Paragraph("certifie que le patient " + patientNom + " " + patientPrenom));
            document.add(new Paragraph(contenu));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Fait pour valoir ce que de droit."));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Signature du medecin: ___________________"));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate attestation PDF", e);
        }
    }
}
