package com.s4m.poo.controller;

import com.s4m.poo.model.Utilisateur;
import com.s4m.poo.service.UtilisateurService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class SignUpController {

    // ⚠️ Assurez-vous que ces fx:id correspondent à ceux de votre FXML d'inscription
    @FXML
    private TextField nomField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;

    private UtilisateurService utilisateurService = new UtilisateurService();

    /**
     * Gère l'action du bouton "S'inscrire".
     */
    @FXML
    public void handleSignUpAction() {
        String nom = nomField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 1. Validation des données
        if (nom.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de formulaire", "Veuillez remplir tous les champs.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Erreur de mot de passe", "Les mots de passe ne correspondent pas.");
            return;
        }

        // 2. Création de l'objet Utilisateur
        // ⚠️ NÉCESSITE LE CONSTRUCTEUR Utilisateur(nom, email, password) DANS VOTRE MODÈLE
        Utilisateur nouvelUtilisateur = new Utilisateur(nom, email, password);

        // 3. Appel du service pour enregistrer
        // ⚠️ Si l'erreur "cannot find symbol" persiste, la seule solution est de faire un Rebuild du projet.
        if (utilisateurService.registerUtilisateur(nouvelUtilisateur)) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Inscription réussie ! Vous pouvez maintenant vous connecter.");

            // Redirection vers la page de connexion
            handleBackToLogin();

        } else {
            showAlert(Alert.AlertType.ERROR, "Échec de l'inscription", "L'inscription a échoué. (Nom ou Email peut-être déjà utilisé, ou problème de DB.)");
        }
    }

    /**
     * Gère l'action du bouton "Retour" et redirige vers l'écran de connexion.
     */
    @FXML
    public void handleBackToLogin() {
        try {
            // Utilise n'importe quel champ FXML pour récupérer la fenêtre actuelle
            Stage stage = (Stage) nomField.getScene().getWindow();

            // ⬇️ CHEMIN CORRIGÉ ⬇️
            // Basé sur votre structure: /resources/fxml/login-view.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/login-view.fxml"));
            Scene scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.setTitle("Quiz Master - Connexion");
            stage.show();

        } catch (IOException e) {
            System.err.println("Erreur de chargement de la vue de connexion : " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Erreur interne", "Impossible de charger la page de connexion. Vérifiez le chemin FXML.");
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}