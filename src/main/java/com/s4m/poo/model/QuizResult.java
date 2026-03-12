package com.s4m.poo.model;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class QuizResult {
    private String quizName;
    private int score;
    private Timestamp date;

    // Constructeurs
    public QuizResult() {}

    public QuizResult(String quizName, int score, String formattedDate, String formattedTime) {
        this.quizName = quizName;
        this.score = score;
        // Pour la compatibilité avec les données d'exemple
    }

    // Getters et Setters
    public String getQuizName() { return quizName; }
    public void setQuizName(String quizName) { this.quizName = quizName; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public Timestamp getDate() { return date; }
    public void setDate(Timestamp date) { this.date = date; }

    // Méthodes formatées pour la TableView
    public String getScorePercentage() {
        return score + "%";
    }

    public String getFormattedDate() {
        if (date != null) {
            return date.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        return "";
    }

    public String getFormattedTime() {
        if (date != null) {
            return date.toLocalDateTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return "";
    }
}