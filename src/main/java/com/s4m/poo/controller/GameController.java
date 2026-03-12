package com.s4m.poo.controller; // 🚨 CORRECTION 1: Changement du package

import com.s4m.poo.app.Main; // 🚨 CORRECTION 2: Importation de la classe Main pour la navigation
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.io.IOException; // Nécessaire pour Main.changeScene

public class GameController {

    // --- Constantes de Couleurs ---
    private static final Color COLOR_GREEN = Color.web("#2d9c49");
    private static final Color COLOR_RED = Color.web("#d92c2c");
    private static final Color COLOR_GOLD = Color.web("#FFD700");
    private static final Color DEFAULT_STROKE_COLOR = Color.BLACK;
    private static final double DEFAULT_STROKE_WIDTH = 1.0;
    private static final double TEMPS_TOTAL = 30.0;
    private static final double MAX_BAR_WIDTH = 700.0;

    // --- Déclarations FXML : Chronomètre et Quiz ---
    @FXML private Rectangle progressBarRectangle;
    @FXML private Label timerLabel;
    @FXML private Label questionLabel;
    @FXML private Label labelA, labelB, labelC, labelD;
    @FXML private StackPane stackPaneA, stackPaneB, stackPaneC, stackPaneD;
    @FXML private Rectangle rectA, rectB, rectC, rectD;

    // --- Déclarations FXML : Navigation et Joker ---
    @FXML private Label exitLabel;
    @FXML private Label jokerLabel;

    // --- Déclarations FXML : Joker Pop-up (Boîte de dialogue) ---
    @FXML private StackPane jokerMessagePane; // Le conteneur à afficher/masquer
    @FXML private Label correctAnswerLabel;  // Le label pour le texte de la réponse


    // --- Variables d'état ---
    private Timeline timeline;
    private int timeSeconds = 30;
    private Rectangle selectedRectangle = null;

    // Déclaration des variables de la bonne réponse. Elles seront initialisées DANS initialize().
    // NOTE: Ces variables devront être mises à jour dynamiquement quand le modèle sera prêt.
    private Label CORRECT_ANSWER_LABEL_NODE;
    private Rectangle CORRECT_ANSWER_RECT_NODE;


    /**
     * Méthode d'initialisation (appelée après le chargement du FXML).
     */
    @FXML
    public void initialize() {
        // Initialiser les noeuds de la bonne réponse ici (Exemple : C est la bonne réponse)
        CORRECT_ANSWER_LABEL_NODE = labelC;
        CORRECT_ANSWER_RECT_NODE = rectC;

        // Initialisation des données (Exemple)
        questionLabel.setText("Quelle est la capitale de la France ?");
        labelA.setText("Berlin");
        labelB.setText("Madrid");
        labelC.setText("Paris");
        labelD.setText("Rome");

        // Masquer la boîte de dialogue au démarrage
        jokerMessagePane.setVisible(false);

        startTimer();
    }

    // --------------------------------------------------------------------------
    // --- LOGIQUE DU CHRONOMÈTRE ---
    // --------------------------------------------------------------------------

    /**
     * Configure et démarre le compte à rebours de la barre de chargement.
     */
    private void startTimer() {
        timeSeconds = (int)TEMPS_TOTAL;

        // Initialisation de l'affichage
        timerLabel.setText(String.valueOf(timeSeconds) + "s");
        timerLabel.setTextFill(COLOR_GOLD);
        progressBarRectangle.setWidth(MAX_BAR_WIDTH);
        progressBarRectangle.setFill(COLOR_GREEN);

        timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(1),
                        event -> {
                            timeSeconds--;

                            // 1. Mise à jour du Label et de la progression
                            timerLabel.setText(String.valueOf(timeSeconds) + "s");
                            double progression = timeSeconds / TEMPS_TOTAL;
                            progressBarRectangle.setWidth(progression * MAX_BAR_WIDTH);

                            // 2. Alerte visuelle (changement de couleur)
                            if (timeSeconds <= 10 && timeSeconds > 0) {
                                progressBarRectangle.setFill(COLOR_RED);
                                timerLabel.setTextFill(COLOR_RED);
                            } else if (timeSeconds > 10) {
                                progressBarRectangle.setFill(COLOR_GREEN);
                                timerLabel.setTextFill(COLOR_GOLD);
                            }

                            if (timeSeconds <= 0) {
                                timeline.stop();
                                timerLabel.setText("FIN!");
                                timerLabel.setTextFill(Color.WHITE);
                                progressBarRectangle.setWidth(0.0);
                                // TODO: Gérer la fin de question (réponse auto-fausse et passage à la suivante)
                            }
                        }
                )
        );

        timeline.setCycleCount((int)TEMPS_TOTAL);
        timeline.play();
    }

    // --------------------------------------------------------------------------
    // --- LOGIQUE DE SÉLECTION DES RÉPONSES ---
    // --------------------------------------------------------------------------

    private void applySelectedStyle(Rectangle rectangle) {
        // Applique le style de sélection (contour doré/effet)
        rectangle.setStroke(Color.GOLD);
        rectangle.setStrokeWidth(5.0);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GOLD);
        shadow.setRadius(30.0);
        shadow.setSpread(0.5);
        rectangle.setEffect(shadow);
    }

    private void resetStyle(Rectangle rectangle) {
        // Réinitialise le style par défaut
        rectangle.setStroke(DEFAULT_STROKE_COLOR);
        rectangle.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        rectangle.setEffect(null);
    }

    /**
     * Gère l'événement de clic sur l'une des options de réponse.
     */
    @FXML
    private void handleRectangleClick(MouseEvent event) {
        StackPane clickedPane = (StackPane) event.getSource();
        Rectangle currentRect = null;

        // 1. Identifier le rectangle cliqué
        if (clickedPane == stackPaneA) {
            currentRect = rectA;
        } else if (clickedPane == stackPaneB) {
            currentRect = rectB;
        } else if (clickedPane == stackPaneC) {
            currentRect = rectC;
        } else if (clickedPane == stackPaneD) {
            currentRect = rectD;
        }

        if (currentRect == null || selectedRectangle == currentRect) return;

        // 2. Désélectionner l'ancien Rectangle
        if (selectedRectangle != null) {
            resetStyle(selectedRectangle);
        }

        // 3. Sélectionner le nouveau Rectangle
        applySelectedStyle(currentRect);
        selectedRectangle = currentRect;

        // 4. Validation immédiate: Arrêter le chronomètre
        if (timeline != null) {
            timeline.stop();
        }

        // 5. Vérifier la réponse (Coloration finale : Vert si correct, Rouge si faux)
        if (currentRect == CORRECT_ANSWER_RECT_NODE) {
            currentRect.setStroke(Color.LIMEGREEN); // Correct
            // TODO: Ajouter des points et passer à la question suivante après un délai
        } else {
            currentRect.setStroke(Color.RED); // Incorrect
            // TODO: Passer à la question suivante après un délai
        }

        // S'assurer que le texte est au-dessus de l'effet de sélection
        for (javafx.scene.Node node : clickedPane.getChildren()) {
            if (node instanceof HBox) {
                node.toFront();
                break;
            }
        }
    }

    // --------------------------------------------------------------------------
    // --- LOGIQUE DU JOKER (Ouverture/Fermeture de la boîte de dialogue) ---
    // --------------------------------------------------------------------------

    /**
     * Gère le clic sur l'icône Joker : Ouvre la boîte de dialogue.
     */
    @FXML
    private void useJoker(MouseEvent event) {
        if (jokerLabel.isDisabled()) {
            return;
        }

        // 1. Définir le texte de la bonne réponse
        correctAnswerLabel.setText(CORRECT_ANSWER_LABEL_NODE.getText());

        // 2. OUVRE la boîte de dialogue (pop-up)
        jokerMessagePane.setVisible(true);
        jokerMessagePane.toFront(); // S'assurer que le pop-up est au-dessus de tout

        // 3. Mettre la bonne réponse en évidence sur la grille
        if (selectedRectangle != null) {
            resetStyle(selectedRectangle);
        }
        applySelectedStyle(CORRECT_ANSWER_RECT_NODE);

        // 4. Arrêter le chronomètre (fenêtre modale)
        if (timeline != null) {
            timeline.stop();
        }

        // 5. Désactiver l'icône joker (usage unique)
        jokerLabel.setDisable(true);
    }

    /**
     * Cache le message du joker lorsque le joueur clique sur le bouton de fermeture.
     */
    @FXML
    private void closeJokerMessage(MouseEvent event) {
        // FERME la boîte de dialogue
        jokerMessagePane.setVisible(false);
    }

    // --------------------------------------------------------------------------
    // --- LOGIQUE DE NAVIGATION ---
    // --------------------------------------------------------------------------

    /**
     * Gère le clic sur l'icône de sortie pour revenir au tableau de bord.
     */
    @FXML
    private void handleExitToDashboard(MouseEvent event) throws IOException { // 🚨 throws IOException ajouté
        if (timeline != null) {
            timeline.stop(); // Arrêter le chronomètre avant de quitter
        }

        System.out.println("Quitter la partie. Retour au Dashboard...");

        // 🚨 CORRECTION 3: Implémentation de la navigation
        Main.changeScene("/fxml/dashboard-view.fxml", "Quiz Master - Dashboard");
    }
}