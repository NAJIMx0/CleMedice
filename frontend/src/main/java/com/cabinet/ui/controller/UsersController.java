package com.cabinet.ui.controller;

import com.cabinet.ui.MainApp;
import com.cabinet.ui.service.ApiService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class UsersController {

    @FXML private TableView<ApiService.UserDTO> userTable;
    @FXML private TableColumn<ApiService.UserDTO, Long> idCol;
    @FXML private TableColumn<ApiService.UserDTO, String> nomCol;
    @FXML private TableColumn<ApiService.UserDTO, String> emailCol;
    @FXML private TableColumn<ApiService.UserDTO, String> roleCol;
    @FXML private TableColumn<ApiService.UserDTO, Boolean> actifCol;

    private final ObservableList<ApiService.UserDTO> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        actifCol.setCellValueFactory(new PropertyValueFactory<>("enabled"));

        userTable.setItems(data);
        loadUsers();
    }

    private void loadUsers() {
        try {
            List<ApiService.UserDTO> users = ApiService.getUsers();
            data.setAll(users);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger: " + e.getMessage());
        }
    }

    @FXML
    private void handleAdd(ActionEvent event) {
        Dialog<ApiService.UserDTO> dialog = new Dialog<>();
        dialog.setTitle("Ajouter Utilisateur");

        ButtonType saveBtn = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField nomField = new TextField();
        TextField emailField = new TextField();
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.setItems(FXCollections.observableArrayList(
                "MEDECIN_PRINCIPAL", "FERMLIYAT", "ASSISTANTE", "AUTRE_MEDECIN"
        ));
        roleCombo.getSelectionModel().selectFirst();
        PasswordField passwordField = new PasswordField();

        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Nom:"), 0, 0); grid.add(nomField, 1, 0);
        grid.add(new Label("Email:"), 0, 1); grid.add(emailField, 1, 1);
        grid.add(new Label("Role:"), 0, 2); grid.add(roleCombo, 1, 2);
        grid.add(new Label("Mot de passe:"), 0, 3); grid.add(passwordField, 1, 3);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                try {
                    ApiService.createUser(nomField.getText(), emailField.getText(), roleCombo.getValue(), passwordField.getText());
                    showAlert("Succes", "Utilisateur ajoute");
                } catch (Exception e) {
                    showAlert("Erreur", "Impossible d'ajouter: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
        loadUsers();
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        ApiService.UserDTO selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Selectionnez un utilisateur");
            return;
        }

        Dialog<ApiService.UserDTO> dialog = new Dialog<>();
        dialog.setTitle("Modifier Utilisateur");

        ButtonType saveBtn = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        TextField nomField = new TextField(selected.getNom());
        TextField emailField = new TextField(selected.getEmail());
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.setItems(FXCollections.observableArrayList(
                "MEDECIN_PRINCIPAL", "FERMLIYAT", "ASSISTANTE", "AUTRE_MEDECIN"
        ));
        roleCombo.setValue(selected.getRole());

        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Nom:"), 0, 0); grid.add(nomField, 1, 0);
        grid.add(new Label("Email:"), 0, 1); grid.add(emailField, 1, 1);
        grid.add(new Label("Role:"), 0, 2); grid.add(roleCombo, 1, 2);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                try {
                    ApiService.updateUser(selected.getId(), nomField.getText(), emailField.getText(), roleCombo.getValue(), selected.isEnabled());
                    showAlert("Succes", "Utilisateur modifie");
                } catch (Exception e) {
                    showAlert("Erreur", "Impossible de modifier: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
        loadUsers();
    }

    @FXML
    private void handleResetPassword(ActionEvent event) {
        ApiService.UserDTO selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Selectionnez un utilisateur");
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Reset Mot de Passe");

        ButtonType saveBtn = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        PasswordField passwordField = new PasswordField();
        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Nouveau mot de passe:"), 0, 0); grid.add(passwordField, 1, 0);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) return passwordField.getText();
            return null;
        });

        dialog.showAndWait().ifPresent(newPassword -> {
            if (newPassword.isEmpty()) {
                showAlert("Erreur", "Mot de passe vide");
                return;
            }
            try {
                ApiService.resetUserPassword(selected.getId(), newPassword);
                showAlert("Succes", "Mot de passe reinitialise");
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de reinitialiser: " + e.getMessage());
            }
        });
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        ApiService.UserDTO selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Attention", "Selectionnez un utilisateur");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer " + selected.getNom() + " ?");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                ApiService.deleteUser(selected.getId());
                showAlert("Succes", "Utilisateur supprime");
                loadUsers();
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de supprimer: " + e.getMessage());
            }
        }
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
