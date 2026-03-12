package com.s4m.poo.model;

import java.util.Date;

public class Score {
    private int id_score;
    private int valeur;
    private Date date_score;
    private int utilisateur_id; // FK
    private int quiz_id; // FK

    public Score(int id_score, int valeur, Date date_score, int utilisateur_id, int quiz_id) {
        this.id_score = id_score;
        this.valeur = valeur;
        this.date_score = date_score;
        this.utilisateur_id = utilisateur_id;
        this.quiz_id = quiz_id;
    }

    // Getters
    public int getValeur() { return valeur; }
    // ...
}