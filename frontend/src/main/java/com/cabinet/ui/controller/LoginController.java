package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.model.LoginResponse;
import com.cabinet.ui.service.ApiService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        loginButton.setDisable(true);
        errorLabel.setVisible(false);

        try {
            LoginResponse response = ApiService.login(email, password);
            ApiService.setToken(response.getToken());
            MainApp.showDashboardView();
        } catch (Exception e) {
            e.printStackTrace(); // add this
            showError("Erreur: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        } finally {
            loginButton.setDisable(false);
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }
}
