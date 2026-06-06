package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.MedicamentDTO;
import com.cabinet.ui.model.OrdonnanceDTO;
import com.cabinet.ui.model.OrdonnanceResultDTO;
import com.cabinet.ui.model.RendezVousDTO;
import com.cabinet.ui.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.List;

public class OrdonnanceController {

    @FXML private Label patientLabel;
    @FXML private TextField casPatientField;
    @FXML private TableView<MedicamentDTO> medicamentTable;
    @FXML private TableColumn<MedicamentDTO, String> nomCol;
    @FXML private TableColumn<MedicamentDTO, String> dosageCol;
    @FXML private TableColumn<MedicamentDTO, String> dureeCol;
    @FXML private TableColumn<MedicamentDTO, String> instrCol;
    @FXML private Button printButton;

    private final ObservableList<MedicamentDTO> medicaments = FXCollections.observableArrayList();
    private Long consultationId;
    private RendezVousDTO currentRdv;
    private Long savedOrdonnanceId;

    public void setContext(Long consultationId, RendezVousDTO rdv) {
        this.consultationId = consultationId;
        this.currentRdv = rdv;
        if (rdv != null) {
            patientLabel.setText("Patient: " + rdv.getPatientNom());
        }
        printButton.setDisable(true);
    }

    @FXML
    public void initialize() {
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        dosageCol.setCellValueFactory(new PropertyValueFactory<>("dosage"));
        dureeCol.setCellValueFactory(new PropertyValueFactory<>("duree"));
        instrCol.setCellValueFactory(new PropertyValueFactory<>("instructions"));
        medicamentTable.setItems(medicaments);
    }

    @FXML
    private void handleAddMedicament(ActionEvent event) {
        Dialog<MedicamentDTO> dialog = new Dialog<>();
        dialog.setTitle("Ajouter Medicament");
        ButtonType saveBtn = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField nomField = new TextField();
        TextField dosageField = new TextField();
        TextField dureeField = new TextField();
        TextField instrField = new TextField();

        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Nom:"), 0, 0); grid.add(nomField, 1, 0);
        grid.add(new Label("Dosage:"), 0, 1); grid.add(dosageField, 1, 1);
        grid.add(new Label("Duree:"), 0, 2); grid.add(dureeField, 1, 2);
        grid.add(new Label("Instructions:"), 0, 3); grid.add(instrField, 1, 3);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                MedicamentDTO med = new MedicamentDTO();
                med.setNom(nomField.getText());
                med.setDosage(dosageField.getText());
                med.setDuree(dureeField.getText());
                med.setInstructions(instrField.getText());
                return med;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(med -> medicaments.add(med));
    }

    @FXML
    private void handleRemoveMedicament(ActionEvent event) {
        MedicamentDTO selected = medicamentTable.getSelectionModel().getSelectedItem();
        if (selected != null) medicaments.remove(selected);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (consultationId == null) {
            showAlert("Erreur", "Aucune consultation liee");
            return;
        }
        if (medicaments.isEmpty()) {
            showAlert("Attention", "Ajoutez au moins un medicament");
            return;
        }
        try {
            OrdonnanceDTO dto = new OrdonnanceDTO();
            dto.setConsultationId(consultationId);
            dto.setCasPatient(casPatientField.getText());
            dto.setDate(LocalDate.now());
            dto.setMedicaments(List.copyOf(medicaments));
            OrdonnanceResultDTO result = ApiService.createOrdonnance(dto);
            savedOrdonnanceId = result.getId();
            printButton.setDisable(false);
            showAlert("Succes", "Ordonnance enregistree (ID: " + savedOrdonnanceId + ")");
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'enregistrer: " + e.getMessage());
        }
    }

    @FXML
    private void handlePrint(ActionEvent event) {
        if (savedOrdonnanceId == null) {
            showAlert("Attention", "Enregistrez d'abord l'ordonnance");
            return;
        }
        try {
            byte[] pdfBytes = ApiService.getOrdonnancePdf(savedOrdonnanceId);
            File tempFile = File.createTempFile("ordonnance_" + savedOrdonnanceId + "_", ".pdf");
            tempFile.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(pdfBytes);
            }
            if (java.awt.Desktop.isDesktopSupported() &&
                java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)) {
                java.awt.Desktop.getDesktop().open(tempFile);
            } else {
                showAlert("Info", "PDF sauvegarde: " + tempFile.getAbsolutePath());
            }
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir le PDF: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        MainApp.showConsultationView(currentRdv);
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
