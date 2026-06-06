package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.service.ApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class DashboardController {

    @FXML private Text welcomeText;
    @FXML private Label roleLabel;
    @FXML private VBox usersCard;

    @FXML
    public void initialize() {
        String role = ApiService.getToken() != null ? extractRole() : "";
        welcomeText.setText("Bienvenue au Cabinet CleMedice");
        roleLabel.setText(role);

        boolean isMedecinPrincipal = "MEDECIN_PRINCIPAL".equals(role);
        usersCard.setVisible(isMedecinPrincipal);
        usersCard.setManaged(isMedecinPrincipal);
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
    private void handlePatients(MouseEvent event) throws Exception {
        MainApp.showPatientsView();
    }

    @FXML
    private void handleRendezVous(MouseEvent event) throws Exception {
        MainApp.showRendezVousView();
    }

    @FXML
    private void handleAttestations(MouseEvent event) throws Exception {
        MainApp.showAttestationView();
    }

    @FXML
    private void handleFinance(MouseEvent event) throws Exception {
        MainApp.showFinanceView();
    }

    @FXML
    private void handleUsers(MouseEvent event) throws Exception {
        MainApp.showUsersView();
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {
        ApiService.setToken(null);
        MainApp.showLoginView();
    }
}
