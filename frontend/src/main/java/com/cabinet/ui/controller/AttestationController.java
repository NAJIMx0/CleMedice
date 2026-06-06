package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.PatientDTO;
import com.cabinet.ui.service.ApiService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class AttestationController {

    @FXML private ComboBox<PatientDTO> patientCombo;
    @FXML private TextArea contenuField;

    @FXML
    public void initialize() {
        try {
            List<PatientDTO> patients = ApiService.getPatients();
            patientCombo.setItems(FXCollections.observableArrayList(patients));
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les patients");
        }
    }

    @FXML
    private void handleGenerate(ActionEvent event) {
        PatientDTO p = patientCombo.getValue();
        if (p == null) {
            showAlert("Attention", "Selectionnez un patient");
            return;
        }
        String contenu = contenuField.getText().trim();
        if (contenu.isEmpty()) {
            showAlert("Attention", "Saisissez le contenu de l'attestation");
            return;
        }
        try {
            byte[] pdf = ApiService.generateAttestation(
                    p.getNom(), p.getPrenom(), contenu, LocalDate.now().toString()
            );
            File f = File.createTempFile("attestation_", ".pdf");
            f.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(f)) { fos.write(pdf); }
            if (java.awt.Desktop.isDesktopSupported() &&
                java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)) {
                java.awt.Desktop.getDesktop().open(f);
            } else {
                showAlert("Info", "PDF sauvegarde: " + f.getAbsolutePath());
            }
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de generer l'attestation: " + e.getMessage());
        }
    }

    @FXML
    private void handlePrint(ActionEvent event) {
        handleGenerate(event);
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        MainApp.showDashboardView();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
