package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.ConsultationDTO;
import com.cabinet.ui.model.RendezVousDTO;
import com.cabinet.ui.service.ApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class ConsultationController {

    @FXML private Label patientLabel;
    @FXML private DatePicker datePicker;
    @FXML private TextArea descriptionField;
    @FXML private TextArea observationsField;
    @FXML private TextField casPatientField;

    private RendezVousDTO currentRdv;

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
    }

    public void setRendezVous(RendezVousDTO rdv) {
        this.currentRdv = rdv;
        if (rdv != null) {
            patientLabel.setText(rdv.getPatientNom() + " " + rdv.getPatientPrenom());
            datePicker.setValue(rdv.getDate());
        } else {
            patientLabel.setText("Selectionnez un rendez-vous depuis l'ecran RDV");
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (currentRdv == null) {
            showAlert("Attention", "Aucun rendez-vous selectionne");
            return;
        }
        try {
            ConsultationDTO dto = new ConsultationDTO();
            dto.setRendezVousId(currentRdv.getId());
            dto.setDescription(descriptionField.getText());
            dto.setObservations(observationsField.getText());
            dto.setCasPatient(casPatientField.getText());
            dto.setDate(datePicker.getValue());
            ApiService.createConsultation(dto);
            showAlert("Succes", "Consultation enregistree");
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'enregistrer: " + e.getMessage());
        }
    }

    @FXML
    private void handleOrdonnance(ActionEvent event) throws Exception {
        if (currentRdv == null) {
            showAlert("Attention", "Enregistrez d'abord la consultation");
            return;
        }
        MainApp.showOrdonnanceView();
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        MainApp.showRendezVousView();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
