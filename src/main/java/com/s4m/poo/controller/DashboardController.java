package com.s4m.poo.controller;

import com.s4m.poo.app.Main;
import com.s4m.poo.model.Theme;
import com.s4m.poo.model.Utilisateur;
import com.s4m.poo.service.ThemeService;
import com.s4m.poo.service.UtilisateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

public class DashboardController {

    private Utilisateur utilisateur;
    private String selectedTheme = null;
    private VBox currentSelectedThemeCard = null;
    private ObservableList<Theme> listeThemes = FXCollections.observableArrayList();
    private UtilisateurService utilisateurService = new UtilisateurService();

    // FXML Elements
    @FXML private Label userNameLabel;
    @FXML private Label userLevelLabel;
    @FXML private Label userScoreLabel;
    @FXML private Label progressLabel;
    @FXML private Label selectedThemeLabel;
    @FXML private Button continueButton;
    @FXML private TableView<QuizResult> resultsTable;
    @FXML private TableColumn<QuizResult, String> quizColumn;
    @FXML private TableColumn<QuizResult, String> scoreColumn;
    @FXML private TableColumn<QuizResult, String> dateColumn;
    @FXML private TableColumn<QuizResult, String> timeColumn;

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        if (this.utilisateur != null) {
            userNameLabel.setText(this.utilisateur.getNom());

            // Récupérer le score total et le niveau depuis la base de données
            int scoreTotal = utilisateurService.getScoreTotalByUtilisateurId(this.utilisateur.getIdUser());
            int niveau = utilisateurService.getNiveauByUtilisateurId(this.utilisateur.getIdUser());

            userLevelLabel.setText("Niv. " + niveau);
            userScoreLabel.setText(scoreTotal + " pts");
        }
    }

    @FXML
    public void initialize() {
        System.out.println("DashboardController initialisé!");

        progressLabel.setText("5/9");
        continueButton.setDisable(true);
        selectedThemeLabel.setText("Sélectionnez un thème ci-dessus");

        List<Theme> themesBDD = ThemeService.getAllThemes();
        listeThemes.addAll(themesBDD);

        setupResultsTable();
    }

    private void setupResultsTable() {
        quizColumn.setCellValueFactory(new PropertyValueFactory<>("quizName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Données d'exemple
        ObservableList<QuizResult> results = FXCollections.observableArrayList(
                new QuizResult("Culture Générale", "92%", "15/03/2024", "12:45"),
                new QuizResult("Java OOP", "88%", "14/03/2024", "18:30"),
                new QuizResult("Histoire", "76%", "13/03/2024", "15:20"),
                new QuizResult("Géographie", "95%", "12/03/2024", "10:15"),
                new QuizResult("Mathématiques", "81%", "11/03/2024", "22:10")
        );

        resultsTable.setItems(results);

        // Personnalisation des lignes (coloration)
        resultsTable.setRowFactory(tv -> new TableRow<QuizResult>() {
            @Override
            protected void updateItem(QuizResult item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    String score = item.getScore().replace("%", "");
                    int scoreValue = Integer.parseInt(score);
                    if (scoreValue >= 90) {
                        setStyle("-fx-background-color: #c8e6c9; -fx-text-fill: #2e7d32;"); // Vert
                    } else if (scoreValue >= 70) {
                        setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00;"); // Orange
                    } else {
                        setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828;"); // Rouge
                    }
                }
            }
        });
    }

    @FXML
    private void selectTheme(MouseEvent event) {
        VBox themeCard = (VBox) event.getSource();

        if (themeCard.getChildren().size() <= 1 || !(themeCard.getChildren().get(1) instanceof Label)) {
            System.err.println("Erreur: Le titre du thème n'est pas trouvé à l'index 1 du VBox.");
            return;
        }

        Label themeTitle = (Label) themeCard.getChildren().get(1);
        String themeName = themeTitle.getText().trim();

        if (currentSelectedThemeCard != null) {
            currentSelectedThemeCard.getStyleClass().remove("theme-card-selected");
        }

        themeCard.getStyleClass().add("theme-card-selected");

        selectedTheme = themeName;
        currentSelectedThemeCard = themeCard;

        selectedThemeLabel.setText("Thème sélectionné: " + themeName);
        continueButton.setDisable(false);
        System.out.println("Thème sélectionné: " + themeName);
    }

    @FXML
    private void startQuiz() throws IOException {
        if (selectedTheme != null) {
            System.out.println("Démarrage du quiz pour le thème: " + selectedTheme);
            Main.changeScene("/fxml/game-view.fxml", "Quiz en cours: " + selectedTheme);
        }
    }

    @FXML private void showProfile() { /* ... */ }
    @FXML private void startRecommendedQuiz() throws IOException { /* ... */ Main.changeScene("/fxml/game-view.fxml", "Quiz Recommandé"); }

    @FXML
    private void handleLogout() throws IOException {
        System.out.println("Déconnexion...");
        Main.changeScene("/fxml/login-view.fxml", "Quiz Game - Connexion");
    }

    public static class QuizResult {
        private final String quizName;
        private final String score;
        private final String date;
        private final String time;

        public QuizResult(String quizName, String score, String date, String time) {
            this.quizName = quizName;
            this.score = score;
            this.date = date;
            this.time = time;
        }

        public String getQuizName() { return quizName; }
        public String getScore() { return score; }
        public String getDate() { return date; }
        public String getTime() { return time; }
    }
}