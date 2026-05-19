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
        showAlert("Info", "Ajout utilisateur (integration a finaliser)");
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        showAlert("Info", "Modification utilisateur (integration a finaliser)");
    }

    @FXML
    private void handleResetPassword(ActionEvent event) {
        showAlert("Info", "Reset mot de passe (integration a finaliser)");
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        showAlert("Info", "Suppression utilisateur (integration a finaliser)");
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
