package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.Map;

public class OrdonnanceController {

    @FXML private TextField casPatientField;
    @FXML private TableView<Map<String, String>> medicamentTable;
    @FXML private TableColumn<Map<String, String>, String> nomCol;
    @FXML private TableColumn<Map<String, String>, String> dosageCol;
    @FXML private TableColumn<Map<String, String>, String> dureeCol;
    @FXML private TableColumn<Map<String, String>, String> instrCol;

    private final ObservableList<Map<String, String>> medicaments = FXCollections.observableArrayList();

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
        Dialog<Map<String, String>> dialog = new Dialog<>();
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
                Map<String, String> med = new HashMap<>();
                med.put("nom", nomField.getText());
                med.put("dosage", dosageField.getText());
                med.put("duree", dureeField.getText());
                med.put("instructions", instrField.getText());
                return med;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(med -> medicaments.add(med));
    }

    @FXML
    private void handleRemoveMedicament(ActionEvent event) {
        Map<String, String> selected = medicamentTable.getSelectionModel().getSelectedItem();
        if (selected != null) medicaments.remove(selected);
    }

    @FXML
    private void handleSave(ActionEvent event) {
        showAlert("Info", "Ordonnance enregistree (integration backend a finaliser)");
    }

    @FXML
    private void handlePrint(ActionEvent event) {
        showAlert("Info", "PDF genere (integration backend a finaliser)");
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        MainApp.showConsultationView();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
