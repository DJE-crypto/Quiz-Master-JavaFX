package com.s4m.poo.controller;

import com.s4m.poo.app.Main; // Nouvelle dépendance
import com.s4m.poo.model.Utilisateur;
import com.s4m.poo.service.UtilisateurService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.Optional;

public class LoginController {

    // --- CHAMPS DE LA VUE LOGIN / SIGN UP (laissez ces champs intacts) ---
    @FXML private VBox loginBox;
    @FXML private TextField usernameField; // EMAIL du LOGIN
    @FXML private PasswordField passwordField;
    @FXML private Label loginErrorLabel;
    @FXML private VBox signUpBox;
    @FXML private TextField newUsernameField;
    @FXML private TextField emailField; // EMAIL du SIGN UP
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label registerErrorLabel;

    private UtilisateurService utilisateurService = new UtilisateurService();

    // --- ACTIONS DE COMMUTATION DE VUE ---
    @FXML
    public void showSignUpView() {
        // Masquer la boîte de connexion, afficher la boîte d'inscription
        loginBox.setVisible(false);
        loginBox.setManaged(false);
        signUpBox.setVisible(true);
        signUpBox.setManaged(true);
        // Effacer les erreurs éventuelles
        loginErrorLabel.setText("");
        registerErrorLabel.setText("");
    }

    @FXML
    public void showLoginView() {
        // Masquer la boîte d'inscription, afficher la boîte de connexion
        signUpBox.setVisible(false);
        signUpBox.setManaged(false);
        loginBox.setVisible(true);
        loginBox.setManaged(true);
        // Effacer les erreurs éventuelles
        registerErrorLabel.setText("");
        loginErrorLabel.setText("");
    }

    // --- ACTIONS DE L'INSCRIPTION ---
    @FXML
    public void handleRegisterButton() {
        String username = newUsernameField.getText();
        String email = emailField.getText();
        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        registerErrorLabel.setText("");

        // Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            registerErrorLabel.setText("Tous les champs sont obligatoires.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            registerErrorLabel.setText("Les mots de passe ne correspondent pas.");
            return;
        }

        // Créer un nouvel utilisateur (vous devrez peut-être ajuster le constructeur)
        Utilisateur nouvelUtilisateur = new Utilisateur(username, email, password);
        // Par défaut, le rôle est joueur (roleId = 2) par exemple, selon votre modèle
        // Si votre modèle nécessite un rôle, vous pouvez le définir ici.

        // Appeler le service d'inscription
        boolean success = utilisateurService.registerUtilisateur(nouvelUtilisateur);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
            showLoginView(); // Revenir à la vue de connexion
        } else {
            registerErrorLabel.setText("L'inscription a échoué. Le nom d'utilisateur ou l'email est peut-être déjà utilisé.");
        }
    }

    // --- ACTIONS DE LA CONNEXION (méthode handleLoginButton modifiée pour appeler navigateToDashboard) ---
    @FXML
    public void handleLoginButton() {
        String email = usernameField.getText();
        String password = passwordField.getText();
        loginErrorLabel.setText("");

        if (email.isEmpty() || password.isEmpty()) {
            loginErrorLabel.setText("Veuillez entrer l'email et le mot de passe.");
            return;
        }

        Optional<Utilisateur> optionalUser = utilisateurService.login(email, password);

        if (optionalUser.isPresent()) {
            // Connexion réussie
            navigateToDashboard(optionalUser.get()); // Appelle la méthode de navigation
        } else {
            // Échec
            loginErrorLabel.setText("Email ou mot de passe incorrect.");
        }
    }


    // --- GESTION DE LA NAVIGATION (Méthode cruciale modifiée) ---
    private void navigateToDashboard(Utilisateur user) {
        try {
            String fxmlPath;
            String title;
            FXMLLoader loader;

            // Correction du chemin FXML de /resources/fxml/... à /fxml/...
            if (user.getRoleId() == 1) { // Admin
                fxmlPath = "/fxml/admin-dashboard.fxml";
                title = "Quiz Master - Administration";

                // 1. Changer de scène et récupérer le loader
                loader = Main.changeSceneAndGetLoader(fxmlPath, title);

                // 2. Accéder au contrôleur et transmettre l'utilisateur
                // NOTE: Assurez-vous que AdminController.java existe et a une méthode setUtilisateur(Utilisateur user)
                AdminController adminController = loader.getController();
                // adminController.setUtilisateur(user);

            } else { // Joueur
                fxmlPath = "/fxml/dashboard-view.fxml";
                title = "Quiz Master - Tableau de Bord";

                // 1. Changer de scène et récupérer le loader
                loader = Main.changeSceneAndGetLoader(fxmlPath, title);

                // 2. Accéder au contrôleur et transmettre l'utilisateur
                DashboardController dashboardController = loader.getController();
                dashboardController.setUtilisateur(user); // C'est ici que l'objet est transmis !
            }

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de navigation", "Impossible de charger le tableau de bord.");
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