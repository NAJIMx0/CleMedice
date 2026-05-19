package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.PatientDTO;
import com.cabinet.ui.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PatientsController {

    @FXML private TableView<PatientDTO> patientTable;
    @FXML private TextField searchField;
    @FXML private TableColumn<PatientDTO, Long> idCol;
    @FXML private TableColumn<PatientDTO, String> nomCol;
    @FXML private TableColumn<PatientDTO, String> prenomCol;
    @FXML private TableColumn<PatientDTO, String> cinCol;
    @FXML private TableColumn<PatientDTO, String> telCol;
    @FXML private TableColumn<PatientDTO, LocalDate> dateCol;
    @FXML private TableColumn<PatientDTO, String> adresseCol;

    private final ObservableList<PatientDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        cinCol.setCellValueFactory(new PropertyValueFactory<>("cin"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override public String toString(LocalDate d) { return d != null ? d.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : ""; }
            @Override public LocalDate fromString(String s) { return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/yyyy")); }
        }));
        adresseCol.setCellValueFactory(new PropertyValueFactory<>("adresse"));

        patientTable.setItems(data);
        loadPatients();
    }

    private void loadPatients() {
        try {
            List<PatientDTO> patients = ApiService.getPatients();
            data.setAll(patients);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les patients: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        loadPatients();
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        Dialog<PatientDTO> dialog = createPatientDialog(null);
        dialog.showAndWait().ifPresent(dto -> {
            try {
                ApiService.createPatient(dto);
                loadPatients();
            } catch (Exception e) {
                showAlert("Erreur", "Impossible d'ajouter le patient: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        PatientDTO selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez selectionner un patient");
            return;
        }
        Dialog<PatientDTO> dialog = createPatientDialog(selected);
        dialog.showAndWait().ifPresent(dto -> {
            try {
                ApiService.updatePatient(selected.getId(), dto);
                loadPatients();
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de modifier: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        PatientDTO selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Veuillez selectionner un patient");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer " + selected.getNom() + " ?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                ApiService.deletePatient(selected.getId());
                loadPatients();
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de supprimer: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {
        MainApp.showDashboardView();
    }

    private Dialog<PatientDTO> createPatientDialog(PatientDTO existing) {
        Dialog<PatientDTO> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Ajouter Patient" : "Modifier Patient");

        ButtonType saveButton = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        TextField nom = new TextField(existing != null ? existing.getNom() : "");
        TextField prenom = new TextField(existing != null ? existing.getPrenom() : "");
        TextField cin = new TextField(existing != null ? existing.getCin() : "");
        TextField tel = new TextField(existing != null ? existing.getTelephone() : "");
        DatePicker dateNaiss = new DatePicker(existing != null ? existing.getDateNaissance() : null);
        TextField adresse = new TextField(existing != null ? existing.getAdresse() : "");

        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Nom:"), 0, 0); grid.add(nom, 1, 0);
        grid.add(new Label("Prenom:"), 0, 1); grid.add(prenom, 1, 1);
        grid.add(new Label("CIN:"), 0, 2); grid.add(cin, 1, 2);
        grid.add(new Label("Telephone:"), 0, 3); grid.add(tel, 1, 3);
        grid.add(new Label("Date Naissance:"), 0, 4); grid.add(dateNaiss, 1, 4);
        grid.add(new Label("Adresse:"), 0, 5); grid.add(adresse, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn == saveButton) {
                PatientDTO dto = new PatientDTO();
                dto.setNom(nom.getText());
                dto.setPrenom(prenom.getText());
                dto.setCin(cin.getText());
                dto.setTelephone(tel.getText());
                dto.setDateNaissance(dateNaiss.getValue());
                dto.setAdresse(adresse.getText());
                return dto;
            }
            return null;
        });
        return dialog;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.setTitle(title);
        alert.showAndWait();
    }
}
