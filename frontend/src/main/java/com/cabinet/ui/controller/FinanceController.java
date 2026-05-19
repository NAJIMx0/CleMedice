package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.FinanceSummaryDTO;
import com.cabinet.ui.service.ApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class FinanceController {

    @FXML private Label totalMensuelLabel;
    @FXML private Label totalAnnuelLabel;
    @FXML private TextField anneeField;
    @FXML private ComboBox<String> moisCombo;
    @FXML private TableView<?> paiementTable;

    @FXML
    public void initialize() {
        moisCombo.setItems(javafx.collections.FXCollections.observableArrayList(
                "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Decembre"
        ));
        anneeField.setText(String.valueOf(LocalDate.now().getYear()));
        moisCombo.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        loadSummary();
    }

    private void loadSummary() {
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
    }

    @FXML
    private void handleRefresh(ActionEvent event) { loadSummary(); }

    @FXML
    private void handleExport(ActionEvent event) {
        showAlert("Info", "Export Excel declenche (integration backend a finaliser)");
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
