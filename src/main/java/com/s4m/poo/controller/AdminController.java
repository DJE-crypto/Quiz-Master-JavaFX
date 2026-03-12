package com.s4m.poo.controller;

import com.s4m.poo.app.Main;
import com.s4m.poo.model.Question;
import com.s4m.poo.model.Difficulte;
import com.s4m.poo.model.Quiz;
import com.s4m.poo.model.Theme;
import com.s4m.poo.service.QuestionService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminController {

    // Injection des éléments FXML
    @FXML private TableView<Question> questionsTable;
    @FXML private TableColumn<Question, Integer> idColumn;
    @FXML private TableColumn<Question, String> questionColumn;
    @FXML private TableColumn<Question, String> themeColumn;
    @FXML private TableColumn<Question, String> difficultyColumn;
    @FXML private TableColumn<Question, String> quizColumn;
    @FXML private TableColumn<Question, Void> actionsColumn;

    @FXML private Label totalQuestionsLabel;
    @FXML private Label easyQuestionsLabel;
    @FXML private Label mediumQuestionsLabel;
    @FXML private Label hardQuestionsLabel;
    @FXML private Label lastUpdateLabel;
    @FXML private Label statusLabel;
    @FXML private Label formTitle;

    @FXML private VBox formContainer;
    @FXML private TextArea questionTextArea;
    @FXML private TextField choice1Field, choice2Field, choice3Field, choice4Field;
    @FXML private RadioButton choice1Radio, choice2Radio, choice3Radio, choice4Radio;
    @FXML private ToggleGroup answerToggleGroup;
    @FXML private ComboBox<String> themeCombo;
    @FXML private ComboBox<String> difficultyCombo;
    @FXML private ComboBox<String> quizCombo;
    @FXML private TextField pointsField;
    @FXML private TextArea explanationTextArea;

    // Champs pour la recherche et pagination
    @FXML private TextField searchField;
    @FXML private Label resultsCountLabel;
    @FXML private Label pageLabel;
    @FXML private ComboBox<String> filterCombo;

    private final QuestionService questionService = new QuestionService();

    private boolean isEditMode = false;
    private Question currentQuestion = null;

    @FXML
    public void initialize() {
        System.out.println("AdminController initialisé");
        setupTableColumns();
        loadQuestions();
        updateStatistics();
        initializeFormControls();

        // Initialisation des contrôles
        resultsCountLabel.setText("0");
        pageLabel.setText("Page 1/1");
        updateStatus("Prêt");

        // Configuration du style des champs de formulaire
        setupFormStyles();

        // Configuration du groupe de boutons radio
        setupRadioButtons();
    }

    private void setupTableColumns() {
        // Configuration des colonnes avec PropertyValueFactory
        idColumn.setCellValueFactory(cellData ->
                cellData.getValue().idQuestionProperty().asObject());

        questionColumn.setCellValueFactory(cellData ->
                cellData.getValue().libelleProperty());

        // Utilisation des propriétés calculées pour les colonnes liées
        themeColumn.setCellValueFactory(cellData ->
                cellData.getValue().themeNameProperty());

        difficultyColumn.setCellValueFactory(cellData ->
                cellData.getValue().difficultyLevelProperty());

        quizColumn.setCellValueFactory(cellData ->
                cellData.getValue().quizTitleProperty());

        // Configuration de la colonne d'actions
        actionsColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Question, Void> call(final TableColumn<Question, Void> param) {
                return new TableCell<>() {
                    private final Button editBtn = new Button("✏️");
                    private final Button deleteBtn = new Button("🗑️");
                    private final HBox pane = new HBox(editBtn, deleteBtn);

                    {
                        editBtn.getStyleClass().add("edit-btn");
                        deleteBtn.getStyleClass().add("delete-btn");
                        pane.setSpacing(5);

                        editBtn.setOnAction(event -> {
                            Question question = getTableView().getItems().get(getIndex());
                            editQuestion(question);
                        });

                        deleteBtn.setOnAction(event -> {
                            Question question = getTableView().getItems().get(getIndex());
                            deleteQuestion(question);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        });

        // Personnalisation de l'affichage des lignes
        questionsTable.setRowFactory(tv -> new TableRow<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    // Alternance de couleurs pour les lignes
                    if (getIndex() % 2 == 0) {
                        setStyle("-fx-background-color: #f8f9fa;");
                    } else {
                        setStyle("-fx-background-color: white;");
                    }

                    // Survol de ligne
                    setOnMouseEntered(event -> {
                        if (!isEmpty()) {
                            setStyle("-fx-background-color: #e8f4fc; -fx-border-color: #3498db; -fx-border-width: 0 0 0 3;");
                        }
                    });

                    setOnMouseExited(event -> {
                        if (!isEmpty()) {
                            if (getIndex() % 2 == 0) {
                                setStyle("-fx-background-color: #f8f9fa; -fx-border-color: transparent;");
                            } else {
                                setStyle("-fx-background-color: white; -fx-border-color: transparent;");
                            }
                        }
                    });
                }
            }
        });
    }

    private void initializeFormControls() {
        // Initialisation des ComboBox avec des données de test
        themeCombo.getItems().addAll("Histoire", "Géographie", "Science", "Sport", "Cinéma",
                "Manga", "Musique", "Littérature", "Informatique");
        difficultyCombo.getItems().addAll("Facile", "Moyen", "Difficile");
        quizCombo.getItems().addAll("Quiz Histoire", "Quiz Géographie", "Quiz Science",
                "Quiz Sport", "Quiz Cinéma", "Quiz Manga",
                "Quiz Musique", "Quiz Littérature", "Quiz Informatique");

        // Initialisation du filtre
        filterCombo.getItems().addAll("Tous", "Histoire", "Géographie", "Science", "Sport",
                "Cinéma", "Manga", "Musique", "Littérature", "Informatique");
        filterCombo.getSelectionModel().selectFirst();
        filterCombo.setOnAction(e -> filterQuestions());
    }

    private void setupRadioButtons() {
        // Créer un groupe de boutons radio pour les réponses
        answerToggleGroup = new ToggleGroup();
        choice1Radio.setToggleGroup(answerToggleGroup);
        choice2Radio.setToggleGroup(answerToggleGroup);
        choice3Radio.setToggleGroup(answerToggleGroup);
        choice4Radio.setToggleGroup(answerToggleGroup);
    }

    private void setupFormStyles() {
        // Appliquer des styles par défaut aux champs
        String fieldStyle = "-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5; " +
                "-fx-padding: 8; -fx-font-size: 14px; -fx-text-fill: #333;";

        String textAreaStyle = "-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5; " +
                "-fx-padding: 8; -fx-font-size: 14px; -fx-text-fill: #333; " +
                "-fx-font-family: 'Segoe UI', Arial, sans-serif;";

        String comboBoxStyle = "-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5; " +
                "-fx-padding: 8; -fx-font-size: 14px; -fx-text-fill: #333;";

        // Appliquer les styles
        questionTextArea.setStyle(textAreaStyle);
        explanationTextArea.setStyle(textAreaStyle);

        choice1Field.setStyle(fieldStyle);
        choice2Field.setStyle(fieldStyle);
        choice3Field.setStyle(fieldStyle);
        choice4Field.setStyle(fieldStyle);
        pointsField.setStyle(fieldStyle);

        themeCombo.setStyle(comboBoxStyle);
        difficultyCombo.setStyle(comboBoxStyle);
        quizCombo.setStyle(comboBoxStyle);
    }

    private void loadQuestions() {
        try {
            List<Question> questions = questionService.getAllQuestions();
            questionsTable.setItems(FXCollections.observableArrayList(questions));
            resultsCountLabel.setText(String.valueOf(questions.size()));

            updateStatus(questions.size() + " questions chargées");

        } catch (Exception e) {
            showAlert("Erreur Critique",
                    "Impossible de charger les questions : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateStatistics() {
        List<Question> allQuestions = questionService.getAllQuestions();
        int total = allQuestions.size();
        int easy = 0, medium = 0, hard = 0;

        for (Question q : allQuestions) {
            String difficulty = q.getNomDifficulte();
            if (difficulty.equalsIgnoreCase("facile")) easy++;
            else if (difficulty.equalsIgnoreCase("moyen")) medium++;
            else if (difficulty.equalsIgnoreCase("difficile")) hard++;
        }

        totalQuestionsLabel.setText(String.valueOf(total));
        easyQuestionsLabel.setText(String.valueOf(easy));
        mediumQuestionsLabel.setText(String.valueOf(medium));
        hardQuestionsLabel.setText(String.valueOf(hard));
        lastUpdateLabel.setText(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }

    // ==============================
    // MÉTHODES POUR LES BOUTONS D'ACTION
    // ==============================

    @FXML
    public void addNewQuestion() {
        formContainer.setVisible(true);
        isEditMode = false;
        currentQuestion = null;
        formTitle.setText("NOUVELLE QUESTION");
        clearForm();
        updateStatus("Nouvelle question - Formulaire ouvert");
    }

    @FXML
    public void searchQuestions() {
        String searchText = searchField.getText();
        if (searchText == null || searchText.trim().isEmpty()) {
            loadQuestions();
        } else {
            List<Question> allQuestions = questionService.getAllQuestions();
            List<Question> filtered = allQuestions.stream()
                    .filter(q -> q.getLibelle().toLowerCase().contains(searchText.toLowerCase()))
                    .collect(Collectors.toList());
            questionsTable.setItems(FXCollections.observableArrayList(filtered));
            resultsCountLabel.setText(String.valueOf(filtered.size()));
            updateStatus("Recherche: " + filtered.size() + " résultats pour \"" + searchText + "\"");
        }
    }

    @FXML
    public void refreshQuestions() {
        loadQuestions();
        updateStatistics();
        updateStatus("Liste actualisée avec succès");
    }

    @FXML
    public void exportToCSV() {
        try {
            List<Question> questions = questionService.getAllQuestions();
            String fileName = "questions_export_" + java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";

            FileWriter writer = new FileWriter(fileName);

            // En-tête CSV
            writer.append("ID,Question,Choix 1,Choix 2,Choix 3,Choix 4,Réponse,Thème,Difficulté,Quiz\n");

            // Données
            for (Question q : questions) {
                writer.append(String.valueOf(q.getIdQuestion())).append(",");
                writer.append("\"").append(q.getLibelle().replace("\"", "\"\"")).append("\",");
                writer.append("\"").append(q.getChoix1().replace("\"", "\"\"")).append("\",");
                writer.append("\"").append(q.getChoix2().replace("\"", "\"\"")).append("\",");
                writer.append("\"").append(q.getChoix3().replace("\"", "\"\"")).append("\",");
                writer.append("\"").append(q.getChoix4().replace("\"", "\"\"")).append("\",");
                writer.append(String.valueOf(q.getReponse())).append(",");
                writer.append("\"").append(q.getNomTheme()).append("\",");
                writer.append("\"").append(q.getNomDifficulte()).append("\",");
                writer.append("\"").append(q.getTitreQuiz()).append("\"\n");
            }

            writer.flush();
            writer.close();

            showAlert("Export Réussi", "Les questions ont été exportées dans le fichier:\n" + fileName);
            updateStatus("Export CSV réussi: " + fileName);

        } catch (IOException e) {
            showAlert("Erreur d'Export", "Erreur lors de l'export CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void previousPage() {
        // Implémentation basique de pagination
        showAlert("Information", "Pagination - À implémenter complètement");
    }

    @FXML
    public void nextPage() {
        // Implémentation basique de pagination
        showAlert("Information", "Pagination - À implémenter complètement");
    }

    @FXML
    public void saveQuestion() {
        // Validation des champs
        if (questionTextArea.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez saisir une question.");
            questionTextArea.requestFocus();
            return;
        }

        if (choice1Field.getText().isEmpty() || choice2Field.getText().isEmpty() ||
                choice3Field.getText().isEmpty() || choice4Field.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les choix de réponse.");
            return;
        }

        // Vérifier qu'une réponse est sélectionnée
        int correctAnswer = getSelectedAnswer();
        if (correctAnswer == 0) {
            showAlert("Erreur", "Veuillez sélectionner la réponse correcte.");
            return;
        }

        // Vérifier les ComboBox
        if (themeCombo.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un thème.");
            themeCombo.requestFocus();
            return;
        }

        if (difficultyCombo.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner une difficulté.");
            difficultyCombo.requestFocus();
            return;
        }

        if (quizCombo.getValue() == null) {
            showAlert("Erreur", "Veuillez sélectionner un quiz.");
            quizCombo.requestFocus();
            return;
        }

        try {
            // Récupérer les valeurs des ComboBox
            String selectedTheme = themeCombo.getValue();
            String selectedDifficulty = difficultyCombo.getValue();
            String selectedQuiz = quizCombo.getValue();

            // Créer ou mettre à jour la question
            if (isEditMode && currentQuestion != null) {
                // Mise à jour d'une question existante
                // On met à jour les champs de base
                currentQuestion.setLibelle(questionTextArea.getText().trim());
                currentQuestion.setChoix1(choice1Field.getText().trim());
                currentQuestion.setChoix2(choice2Field.getText().trim());
                currentQuestion.setChoix3(choice3Field.getText().trim());
                currentQuestion.setChoix4(choice4Field.getText().trim());
                currentQuestion.setReponse(correctAnswer);

                // Pour l'édition, on suppose que les objets Quiz et Difficulte existent déjà
                // On met à jour juste leurs noms
                currentQuestion.getQuiz().setTitre(selectedQuiz);
                currentQuestion.getQuiz().getTheme().setNomTheme(selectedTheme);
                currentQuestion.getDifficulte().setNiveau(selectedDifficulty);

                // Appeler le service de mise à jour
                boolean success = questionService.saveQuestion(currentQuestion);
                if (success) {
                    updateStatus("Question modifiée avec succès");
                    showAlert("Succès", "Question modifiée avec succès!");
                } else {
                    showAlert("Erreur", "Échec de la modification de la question.");
                    return;
                }
            } else {
                // Création d'une nouvelle question
                Question newQuestion = new Question();

                // Remplir les données de base
                newQuestion.setLibelle(questionTextArea.getText().trim());
                newQuestion.setChoix1(choice1Field.getText().trim());
                newQuestion.setChoix2(choice2Field.getText().trim());
                newQuestion.setChoix3(choice3Field.getText().trim());
                newQuestion.setChoix4(choice4Field.getText().trim());
                newQuestion.setReponse(correctAnswer);

                // IMPORTANT: Créer les objets Quiz et Difficulte
                // Ce sont des objets simplifiés pour l'exemple
                // En réalité, il faudrait récupérer les vrais objets depuis la base

                // Créer un objet Theme
                Theme theme = new Theme();
                theme.setNomTheme(selectedTheme);
                theme.setIdTheme(getThemeIdFromName(selectedTheme)); // Méthode à implémenter

                // Créer un objet Quiz
                Quiz quiz = new Quiz();
                quiz.setTitre(selectedQuiz);
                quiz.setIdQuiz(getQuizIdFromName(selectedQuiz)); // Méthode à implémenter
                quiz.setTheme(theme);

                // Créer un objet Difficulte
                Difficulte difficulte = new Difficulte();
                difficulte.setNiveau(selectedDifficulty);
                difficulte.setIdDifficulte(getDifficultyIdFromName(selectedDifficulty)); // Méthode à implémenter

                // Assigner les objets à la question
                newQuestion.setQuiz(quiz);
                newQuestion.setDifficulte(difficulte);

                // Appeler le service d'insertion
                boolean success = questionService.saveQuestion(newQuestion);
                if (success) {
                    updateStatus("Nouvelle question sauvegardée");
                    showAlert("Succès", "Nouvelle question sauvegardée avec succès!");
                } else {
                    showAlert("Erreur", "Échec de la sauvegarde de la question.");
                    return;
                }
            }

            formContainer.setVisible(false);
            clearForm();
            loadQuestions();
            updateStatistics();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la sauvegarde: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthodes auxiliaires pour récupérer les IDs (à implémenter selon votre base)
    private int getThemeIdFromName(String themeName) {
        // Table de mapping simplifiée
        switch(themeName) {
            case "Histoire": return 1;
            case "Géographie": return 2;
            case "Science": return 3;
            case "Sport": return 4;
            case "Cinéma": return 5;
            case "Manga": return 6;
            case "Musique": return 7;
            case "Littérature": return 8;
            case "Informatique": return 9;
            default: return 1;
        }
    }

    private int getQuizIdFromName(String quizName) {
        // Table de mapping simplifiée
        switch(quizName) {
            case "Quiz Histoire": return 1;
            case "Quiz Géographie": return 2;
            case "Quiz Science": return 3;
            case "Quiz Sport": return 4;
            case "Quiz Cinéma": return 5;
            case "Quiz Manga": return 6;
            case "Quiz Musique": return 7;
            case "Quiz Littérature": return 8;
            case "Quiz Informatique": return 9;
            default: return 1;
        }
    }

    private int getDifficultyIdFromName(String difficultyName) {
        // Table de mapping simplifiée
        switch(difficultyName) {
            case "Facile": return 1;
            case "Moyen": return 2;
            case "Difficile": return 3;
            default: return 1;
        }
    }

    @FXML
    public void saveAndNew() {
        saveQuestion();
        if (!formContainer.isVisible()) {
            addNewQuestion();
        }
    }

    @FXML
    public void cancelForm() {
        formContainer.setVisible(false);
        clearForm();
        updateStatus("Formulaire annulé");
    }

    // ==============================
    // MÉTHODES POUR LES ACTIONS DE LA TABLE
    // ==============================

    private void editQuestion(Question question) {
        formContainer.setVisible(true);
        isEditMode = true;
        currentQuestion = question;
        formTitle.setText("MODIFIER LA QUESTION");

        // Remplir le formulaire avec les données de la question
        questionTextArea.setText(question.getLibelle());
        choice1Field.setText(question.getChoix1());
        choice2Field.setText(question.getChoix2());
        choice3Field.setText(question.getChoix3());
        choice4Field.setText(question.getChoix4());

        // Sélectionner la réponse correcte
        int correctAnswer = question.getReponse();
        switch (correctAnswer) {
            case 1 -> choice1Radio.setSelected(true);
            case 2 -> choice2Radio.setSelected(true);
            case 3 -> choice3Radio.setSelected(true);
            case 4 -> choice4Radio.setSelected(true);
        }

        // Sélectionner les ComboBox
        themeCombo.getSelectionModel().select(question.getNomTheme());
        difficultyCombo.getSelectionModel().select(question.getNomDifficulte());
        quizCombo.getSelectionModel().select(question.getTitreQuiz());

        updateStatus("Édition de la question ID: " + question.getIdQuestion());
    }

    private void deleteQuestion(Question question) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation de suppression");
        confirmAlert.setHeaderText("Supprimer la question ?");
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer la question :\n\"" +
                question.getLibelle() + "\" ?\nCette action est irréversible.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean success = questionService.deleteQuestion(question.getIdQuestion());
                if (success) {
                    showAlert("Succès", "Question supprimée avec succès!");
                    updateStatus("Question ID " + question.getIdQuestion() + " supprimée");

                    // Recharger la liste
                    loadQuestions();
                    updateStatistics();
                } else {
                    showAlert("Erreur", "Échec de la suppression de la question.");
                }
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la suppression: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ==============================
    // MÉTHODES POUR LES AUTRES ONGLETS
    // ==============================

    @FXML
    public void showQuestionsManagement() {
        updateStatus("Onglet Questions - Déjà actif");
    }

    @FXML
    public void showQuizzesManagement() {
        showAlert("À venir", "Gestion des quiz - En développement");
        updateStatus("Onglet Quiz - En développement");
    }

    @FXML
    public void showThemesManagement() {
        showAlert("À venir", "Gestion des thèmes - En développement");
        updateStatus("Onglet Thèmes - En développement");
    }

    @FXML
    public void showUsersManagement() {
        showAlert("À venir", "Gestion des utilisateurs - En développement");
        updateStatus("Onglet Utilisateurs - En développement");
    }

    @FXML
    public void showStats() {
        showAlert("À venir", "Statistiques - En développement");
        updateStatus("Onglet Statistiques - En développement");
    }

    @FXML
    public void logout() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Déconnexion");
        confirmAlert.setHeaderText("Voulez-vous vraiment vous déconnecter ?");
        confirmAlert.setContentText("Vous allez être redirigé vers la page de connexion.");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Navigation vers la page de login
                Main.changeScene("/fxml/login-view.fxml", "Quiz Master - Connexion");
                updateStatus("Déconnexion réussie");
            } catch (IOException e) {
                showAlert("Erreur", "Impossible de se déconnecter: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // ==============================
    // MÉTHODES UTILITAIRES PRIVÉES
    // ==============================

    private void clearForm() {
        questionTextArea.clear();
        choice1Field.clear();
        choice2Field.clear();
        choice3Field.clear();
        choice4Field.clear();
        pointsField.setText("1");
        explanationTextArea.clear();

        // Désélectionner tous les boutons radio
        if (answerToggleGroup != null) {
            answerToggleGroup.selectToggle(null);
        }

        // Réinitialiser les ComboBox
        if (themeCombo != null) {
            themeCombo.getSelectionModel().clearSelection();
        }
        if (difficultyCombo != null) {
            difficultyCombo.getSelectionModel().clearSelection();
        }
        if (quizCombo != null) {
            quizCombo.getSelectionModel().clearSelection();
        }

        // Réappliquer les styles
        setupFormStyles();
    }

    private int getSelectedAnswer() {
        if (choice1Radio != null && choice1Radio.isSelected()) return 1;
        if (choice2Radio != null && choice2Radio.isSelected()) return 2;
        if (choice3Radio != null && choice3Radio.isSelected()) return 3;
        if (choice4Radio != null && choice4Radio.isSelected()) return 4;
        return 0; // Aucune sélection
    }

    private void filterQuestions() {
        String selectedFilter = filterCombo.getValue();
        if (selectedFilter == null || selectedFilter.equals("Tous")) {
            loadQuestions();
        } else {
            List<Question> allQuestions = questionService.getAllQuestions();
            List<Question> filtered = allQuestions.stream()
                    .filter(q -> q.getNomTheme().equals(selectedFilter))
                    .collect(Collectors.toList());
            questionsTable.setItems(FXCollections.observableArrayList(filtered));
            resultsCountLabel.setText(String.valueOf(filtered.size()));
            updateStatus("Filtré par thème: " + selectedFilter + " - " + filtered.size() + " questions");
        }
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
        System.out.println("STATUS: " + message);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}