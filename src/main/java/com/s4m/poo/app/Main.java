package com.s4m.poo.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage; // Référence à la fenêtre principale

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Commence par charger la vue de connexion
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // On cherche à lier le CSS "login-style.css" ou "style.css" si vous n'avez pas encore séparé
        if (getClass().getResource("/css/style.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        }

        stage.setTitle("Quiz Game - Connexion");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Méthode utilitaire pour changer de scène *sans* retourner le contrôleur (pour les scènes simples).
     */
    public static void changeScene(String fxmlFile, String title) throws IOException {
        changeSceneAndGetLoader(fxmlFile, title);
    }

    /**
     * Méthode utilitaire essentielle pour changer de scène et récupérer le FXMLLoader.
     * Ceci est crucial pour accéder au contrôleur et lui transmettre des données (ex: l'utilisateur connecté).
     * @param fxmlFile Le chemin vers le fichier FXML.
     * @param title Le nouveau titre de la fenêtre.
     * @return Le FXMLLoader utilisé, dont on peut appeler getController().
     */
    public static FXMLLoader changeSceneAndGetLoader(String fxmlFile, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
        Scene scene = new Scene(fxmlLoader.load());

        // Réapplique le CSS global (ajustez le chemin si vous avez séparé les fichiers)
        if (Main.class.getResource("/css/style.css") != null) {
            scene.getStylesheets().add(Main.class.getResource("/css/style.css").toExternalForm());
        }

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();

        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }
}