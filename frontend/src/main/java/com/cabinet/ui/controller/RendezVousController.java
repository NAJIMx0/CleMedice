package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.PatientDTO;
import com.cabinet.ui.model.RendezVousDTO;
import com.cabinet.ui.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RendezVousController {

    @FXML private TableView<RendezVousDTO> rdvTable;
    @FXML private DatePicker datePicker;
    @FXML private TableColumn<RendezVousDTO, Long> idCol;
    @FXML private TableColumn<RendezVousDTO, String> patientCol;
    @FXML private TableColumn<RendezVousDTO, String> medecinCol;
    @FXML private TableColumn<RendezVousDTO, LocalDate> dateCol;
    @FXML private TableColumn<RendezVousDTO, LocalTime> heureCol;
    @FXML private TableColumn<RendezVousDTO, String> topicCol;
    @FXML private TableColumn<RendezVousDTO, String> statutCol;

    private final ObservableList<RendezVousDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientNom"));
        medecinCol.setCellValueFactory(new PropertyValueFactory<>("medecinNom"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureCol.setCellValueFactory(new PropertyValueFactory<>("heure"));
        topicCol.setCellValueFactory(new PropertyValueFactory<>("topic"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));

        rdvTable.setItems(data);
        datePicker.setValue(LocalDate.now());
        loadRendezVous();
    }

    private void loadRendezVous() {
        try {
            List<RendezVousDTO> list = ApiService.getRendezVous();
            LocalDate filter = datePicker.getValue();
            if (filter != null) {
                list = list.stream().filter(r -> r.getDate() != null && r.getDate().equals(filter)).toList();
            }
            data.setAll(list);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger: " + e.getMessage());
        }
    }

    @FXML
    private void handleDateFilter(ActionEvent event) { loadRendezVous(); }

    @FXML
    private void handleToday(ActionEvent event) {
        datePicker.setValue(LocalDate.now());
        loadRendezVous();
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        Dialog<RendezVousDTO> dialog = new Dialog<>();
        dialog.setTitle("Nouveau Rendez-vous");

        ButtonType saveBtn = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        ComboBox<PatientDTO> patientCombo = new ComboBox<>();
        try {
            List<PatientDTO> patients = ApiService.getPatients();
            patientCombo.setItems(FXCollections.observableArrayList(patients));
        } catch (Exception e) { showAlert("Erreur", e.getMessage()); }

        DatePicker datePick = new DatePicker(LocalDate.now());
        TextField heureField = new TextField(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        TextField topicField = new TextField();

        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Patient:"), 0, 0); grid.add(patientCombo, 1, 0);
        grid.add(new Label("Date:"), 0, 1); grid.add(datePick, 1, 1);
        grid.add(new Label("Heure (HH:mm):"), 0, 2); grid.add(heureField, 1, 2);
        grid.add(new Label("Topic:"), 0, 3); grid.add(topicField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                PatientDTO p = patientCombo.getValue();
                if (p == null) return null;
                RendezVousDTO dto = new RendezVousDTO();
                dto.setPatientId(p.getId());
                dto.setPatientNom(p.getNom() + " " + p.getPrenom());
                dto.setDate(datePick.getValue());
                try { dto.setHeure(LocalTime.parse(heureField.getText(), DateTimeFormatter.ofPattern("HH:mm"))); }
                catch (Exception e) { dto.setHeure(LocalTime.now()); }
                dto.setTopic(topicField.getText());
                dto.setMedecinId(1L);
                dto.setStatut("PLANIFIE");
                return dto;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(dto -> {
            try {
                ApiService.createRendezVous(dto);
                loadRendezVous();
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de creer: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleConsultation(ActionEvent event) {
        showAlert("Info", "Selectionnez le RDV dans l'ecran Consultation");
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        RendezVousDTO selected = rdvTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Selectionnez un rendez-vous");
            return;
        }
        selected.setStatut("ANNULE");
        loadRendezVous();
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
