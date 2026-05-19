package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.PatientDTO;
import com.cabinet.ui.service.ApiService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        PatientDTO patient = patientCombo.getValue();
        if (patient == null) {
            showAlert("Attention", "Selectionnez un patient");
            return;
        }
        showAlert("Succes", "Attestation PDF generee (integration backend a finaliser)");
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
