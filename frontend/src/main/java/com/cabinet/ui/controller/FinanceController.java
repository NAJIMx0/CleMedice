package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.FinanceSummaryDTO;
import com.cabinet.ui.model.PaiementDTO;
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

public class FinanceController {

    @FXML private Label totalMensuelLabel;
    @FXML private Label totalAnnuelLabel;
    @FXML private TextField anneeField;
    @FXML private ComboBox<String> moisCombo;
    @FXML private TableView<PaiementDTO> paiementTable;
    @FXML private TableColumn<PaiementDTO, String> patientCol;
    @FXML private TableColumn<PaiementDTO, Double> montantCol;
    @FXML private TableColumn<PaiementDTO, LocalDate> dateCol;
    @FXML private TableColumn<PaiementDTO, String> modeCol;
    @FXML private TableColumn<PaiementDTO, String> statutCol;
    @FXML private TableColumn<PaiementDTO, String> actionsCol;

    private final ObservableList<PaiementDTO> paiements = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientNom"));
        montantCol.setCellValueFactory(new PropertyValueFactory<>("montant"));
        montantCol.setCellFactory(column -> new TableCell<PaiementDTO, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(String.format("%.2f MAD", item));
            }
        });
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        modeCol.setCellValueFactory(new PropertyValueFactory<>("modePaiement"));
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));
        actionsCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        moisCombo.setItems(FXCollections.observableArrayList(
                "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"
        ));
        anneeField.setText(String.valueOf(LocalDate.now().getYear()));
        moisCombo.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        paiementTable.setItems(paiements);
        loadData();
    }

    private void loadData() {
        try {
            int annee = Integer.parseInt(anneeField.getText());
            int mois = moisCombo.getSelectionModel().getSelectedIndex() + 1;
            FinanceSummaryDTO summary = ApiService.getFinanceSummary(annee, mois);
            totalMensuelLabel.setText(String.format("%.2f MAD", summary.getTotalMensuel() != null ? summary.getTotalMensuel() : 0));
            totalAnnuelLabel.setText(String.format("%.2f MAD", summary.getTotalAnnuel() != null ? summary.getTotalAnnuel() : 0));
        } catch (Exception e) {
            totalMensuelLabel.setText("0.00 MAD");
            totalAnnuelLabel.setText("0.00 MAD");
        }
        try {
            List<PaiementDTO> list = ApiService.getPaiements();
            paiements.setAll(list);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les paiements: " + e.getMessage());
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) { loadData(); }

    @FXML
    private void handleAdd(ActionEvent event) {
        try {
            List<RendezVousDTO> rdvs = ApiService.getRendezVous();
            if (rdvs.isEmpty()) {
                showAlert("Attention", "Aucun rendez-vous disponible");
                return;
            }
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les rendez-vous: " + e.getMessage());
            return;
        }

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Ajouter Paiement");

        ButtonType saveBtn = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        ComboBox<com.cabinet.ui.model.RendezVousDTO> rdvCombo = new ComboBox<>();
        rdvCombo.setPrefWidth(350);
        try {
            List<com.cabinet.ui.model.RendezVousDTO> rdvs = ApiService.getRendezVous();
            rdvCombo.setItems(FXCollections.observableArrayList(rdvs));
        } catch (Exception e) { showAlert("Erreur", e.getMessage()); }
        rdvCombo.setConverter(new javafx.util.StringConverter<>() {
            public String toString(com.cabinet.ui.model.RendezVousDTO r) {
                return r != null ? r.getPatientNom() + " - " + (r.getDate() != null ? r.getDate().toString() : "") : "";
            }
            public com.cabinet.ui.model.RendezVousDTO fromString(String s) { return null; }
        });

        TextField montantField = new TextField();
        montantField.setPromptText("Montant en MAD");
        ComboBox<String> modeCombo = new ComboBox<>();
        modeCombo.setItems(FXCollections.observableArrayList("ESPECES", "CHEQUE", "VIREMENT", "CARTE_BANCAIRE"));
        modeCombo.getSelectionModel().selectFirst();
        TextField notesField = new TextField();
        notesField.setPromptText("Notes (optionnel)");

        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Rendez-vous:"), 0, 0); grid.add(rdvCombo, 1, 0);
        grid.add(new Label("Montant:"), 0, 1); grid.add(montantField, 1, 1);
        grid.add(new Label("Mode:"), 0, 2); grid.add(modeCombo, 1, 2);
        grid.add(new Label("Notes:"), 0, 3); grid.add(notesField, 1, 3);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                com.cabinet.ui.model.RendezVousDTO r = rdvCombo.getValue();
                if (r == null) {
                    showAlert("Attention", "Selectionnez un rendez-vous");
                    return null;
                }
                try {
                    double montant = Double.parseDouble(montantField.getText().trim());
                    ApiService.createPaiement(r.getId(), montant, modeCombo.getValue(), notesField.getText());
                    showAlert("Succes", "Paiement enregistre");
                    loadData();
                } catch (NumberFormatException e) {
                    showAlert("Erreur", "Montant invalide");
                } catch (Exception e) {
                    showAlert("Erreur", "Impossible d'enregistrer: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleExport(ActionEvent event) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Exporter Finance");

        ButtonType exportBtn = new ButtonType("Exporter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(exportBtn, ButtonType.CANCEL);

        DatePicker startPicker = new DatePicker(LocalDate.now().withDayOfMonth(1));
        DatePicker endPicker = new DatePicker(LocalDate.now());

        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Date debut:"), 0, 0); grid.add(startPicker, 1, 0);
        grid.add(new Label("Date fin:"), 0, 1); grid.add(endPicker, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == exportBtn) {
                try {
                    byte[] bytes = ApiService.exportFinance(startPicker.getValue().toString(), endPicker.getValue().toString());
                    File f = File.createTempFile("finance_export_", ".xlsx");
                    f.deleteOnExit();
                    try (FileOutputStream fos = new FileOutputStream(f)) { fos.write(bytes); }
                    if (java.awt.Desktop.isDesktopSupported() &&
                        java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.OPEN)) {
                        java.awt.Desktop.getDesktop().open(f);
                    } else {
                        showAlert("Info", "Fichier sauvegarde: " + f.getAbsolutePath());
                    }
                } catch (Exception e) {
                    showAlert("Erreur", "Impossible d'exporter: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
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
