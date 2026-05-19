package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.service.ApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class DashboardController {

    @FXML private Text welcomeText;
    @FXML private Label roleLabel;
    @FXML private Button financeButton;
    @FXML private Button usersButton;

    @FXML
    public void initialize() {
        String role = ApiService.getToken() != null ? extractRole() : "";
        welcomeText.setText("Bienvenue au Cabinet CleMedice");
        roleLabel.setText("Role: " + role);

        boolean isMedecinPrincipal = "MEDECIN_PRINCIPAL".equals(role);
        financeButton.setVisible(isMedecinPrincipal);
        usersButton.setVisible(isMedecinPrincipal);
    }

    private String extractRole() {
        try {
            String token = ApiService.getToken();
            String[] chunks = token.split("\\.");
            byte[] payload = java.util.Base64.getUrlDecoder().decode(chunks[1]);
            String json = new String(payload);
            int idx = json.indexOf("role\":\"");
            if (idx > 0) {
                int start = idx + 7;
                int end = json.indexOf("\"", start);
                return json.substring(start, end);
            }
        } catch (Exception e) {
            return "INCONNU";
        }
        return "INCONNU";
    }

    @FXML
    private void handlePatients(ActionEvent event) throws Exception {
        MainApp.showPatientsView();
    }

    @FXML
    private void handleRendezVous(ActionEvent event) throws Exception {
        MainApp.showRendezVousView();
    }

    @FXML
    private void handleConsultations(ActionEvent event) throws Exception {
        MainApp.showConsultationView();
    }

    @FXML
    private void handleOrdonnances(ActionEvent event) throws Exception {
        MainApp.showOrdonnanceView();
    }

    @FXML
    private void handleAttestations(ActionEvent event) throws Exception {
        MainApp.showAttestationView();
    }

    @FXML
    private void handleFinance(ActionEvent event) throws Exception {
        MainApp.showFinanceView();
    }

    @FXML
    private void handleUsers(ActionEvent event) throws Exception {
        MainApp.showUsersView();
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {
        ApiService.setToken(null);
        MainApp.showLoginView();
    }
}
