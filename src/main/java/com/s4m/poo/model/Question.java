package com.s4m.poo.model;

import javafx.beans.property.*;

/**
 * Représente une question dans l'application Quiz Master.
 */
public class Question {

    private IntegerProperty idQuestion = new SimpleIntegerProperty();
    private StringProperty libelle = new SimpleStringProperty();
    private StringProperty choix1 = new SimpleStringProperty();
    private StringProperty choix2 = new SimpleStringProperty();
    private StringProperty choix3 = new SimpleStringProperty();
    private StringProperty choix4 = new SimpleStringProperty();
    private IntegerProperty reponse = new SimpleIntegerProperty();
    private ObjectProperty<Quiz> quiz = new SimpleObjectProperty<>();
    private ObjectProperty<Difficulte> difficulte = new SimpleObjectProperty<>();

    // Constructeurs
    public Question() {
    }

    public Question(int idQuestion, String libelle, String choix1, String choix2,
                    String choix3, String choix4, int reponse, Quiz quiz, Difficulte difficulte) {
        this.idQuestion.set(idQuestion);
        this.libelle.set(libelle);
        this.choix1.set(choix1);
        this.choix2.set(choix2);
        this.choix3.set(choix3);
        this.choix4.set(choix4);
        this.reponse.set(reponse);
        this.quiz.set(quiz);
        this.difficulte.set(difficulte);
    }

    // --- Property Getters (pour JavaFX TableView) ---
    public IntegerProperty idQuestionProperty() { return idQuestion; }
    public StringProperty libelleProperty() { return libelle; }
    public StringProperty choix1Property() { return choix1; }
    public StringProperty choix2Property() { return choix2; }
    public StringProperty choix3Property() { return choix3; }
    public StringProperty choix4Property() { return choix4; }
    public IntegerProperty reponseProperty() { return reponse; }
    public ObjectProperty<Quiz> quizProperty() { return quiz; }
    public ObjectProperty<Difficulte> difficulteProperty() { return difficulte; }

    // --- Getters Standards ---
    public int getIdQuestion() { return idQuestion.get(); }
    public String getLibelle() { return libelle.get(); }
    public String getChoix1() { return choix1.get(); }
    public String getChoix2() { return choix2.get(); }
    public String getChoix3() { return choix3.get(); }
    public String getChoix4() { return choix4.get(); }
    public int getReponse() { return reponse.get(); }
    public Quiz getQuiz() { return quiz.get(); }
    public Difficulte getDifficulte() { return difficulte.get(); }

    // --- Setters Standards ---
    public void setIdQuestion(int idQuestion) { this.idQuestion.set(idQuestion); }
    public void setLibelle(String libelle) { this.libelle.set(libelle); }
    public void setChoix1(String choix1) { this.choix1.set(choix1); }
    public void setChoix2(String choix2) { this.choix2.set(choix2); }
    public void setChoix3(String choix3) { this.choix3.set(choix3); }
    public void setChoix4(String choix4) { this.choix4.set(choix4); }
    public void setReponse(int reponse) { this.reponse.set(reponse); }
    public void setQuiz(Quiz quiz) { this.quiz.set(quiz); }
    public void setDifficulte(Difficulte difficulte) { this.difficulte.set(difficulte); }

    // --- Méthodes utilitaires pour l'affichage TableView ---
    public String getNomTheme() {
        return (quiz.get() != null && quiz.get().getTheme() != null)
                ? quiz.get().getTheme().getNomTheme() : "N/A";
    }

    public String getNomDifficulte() {
        return (difficulte.get() != null)
                ? difficulte.get().getNiveau() : "N/A";
    }

    public String getTitreQuiz() {
        return (quiz.get() != null)
                ? quiz.get().getTitre() : "N/A";
    }

    // --- Property pour l'affichage TableView ---
    public StringProperty themeNameProperty() {
        return new SimpleStringProperty(getNomTheme());
    }

    public StringProperty difficultyLevelProperty() {
        return new SimpleStringProperty(getNomDifficulte());
    }

    public StringProperty quizTitleProperty() {
        return new SimpleStringProperty(getTitreQuiz());
    }
}