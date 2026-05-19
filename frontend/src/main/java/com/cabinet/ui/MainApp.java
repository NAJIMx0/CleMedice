package com.cabinet.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLoginView();
        stage.setTitle("CleMedice - Gestion Cabinet Medical");
        stage.setMinWidth(1024);
        stage.setMinHeight(768);
        stage.show();
    }

    public static void showLoginView() throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/com/cabinet/ui/view/LoginView.fxml"));
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.centerOnScreen();
    }

    public static void showDashboardView() throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/com/cabinet/ui/view/DashboardView.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.centerOnScreen();
    }

    public static void showPatientsView() throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/com/cabinet/ui/view/PatientsView.fxml"));
        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(MainApp.class.getResource("/com/cabinet/ui/view/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    public static void showRendezVousView() throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/com/cabinet/ui/view/RendezVousView.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.centerOnScreen();
    }

    public static void showConsultationView() throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/com/cabinet/ui/view/ConsultationView.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.centerOnScreen();
    }

    public static void showOrdonnanceView() throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/com/cabinet/ui/view/OrdonnanceView.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.centerOnScreen();
    }

    public static void showAttestationView() throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/com/cabinet/ui/view/AttestationView.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.centerOnScreen();
    }

    public static void showFinanceView() throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/com/cabinet/ui/view/FinanceView.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.centerOnScreen();
    }

    public static void showUsersView() throws Exception {
        Parent root = FXMLLoader.load(MainApp.class.getResource("/com/cabinet/ui/view/UsersView.fxml"));
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
