package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.ConsultationDTO;
import com.cabinet.ui.model.RendezVousDTO;
import com.cabinet.ui.service.ApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class ConsultationController {

    @FXML private Label patientLabel;
    @FXML private Label dateLabel;
    @FXML private DatePicker datePicker;
    @FXML private TextArea descriptionField;
    @FXML private TextArea observationsField;
    @FXML private TextField casPatientField;
    @FXML private Button ordonnanceButton;

    private RendezVousDTO currentRdv;
    private Long savedConsultationId;

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
    }

    public void setRendezVous(RendezVousDTO rdv) {
        this.currentRdv = rdv;
        this.savedConsultationId = null;
        if (rdv != null) {
            patientLabel.setText(rdv.getPatientNom() + " " + (rdv.getPatientPrenom() != null ? rdv.getPatientPrenom() : ""));
            datePicker.setValue(rdv.getDate());
            dateLabel.setText("Date RDV: " + (rdv.getDate() != null ? rdv.getDate().toString() : ""));
        } else {
            patientLabel.setText("Aucun rendez-vous selectionne");
            dateLabel.setText("");
        }
        ordonnanceButton.setDisable(true);
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
            ConsultationDTO result = ApiService.createConsultation(dto);
            savedConsultationId = result.getId();
            ordonnanceButton.setDisable(false);
            showAlert("Succes", "Consultation enregistree");
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'enregistrer: " + e.getMessage());
        }
    }

    @FXML
    private void handleOrdonnance(ActionEvent event) throws Exception {
        if (savedConsultationId == null) {
            showAlert("Attention", "Enregistrez d'abord la consultation");
            return;
        }
        MainApp.showOrdonnanceView(savedConsultationId, currentRdv);
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
