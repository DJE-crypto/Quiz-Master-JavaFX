package com.s4m.poo.model;

public class Difficulte {

    // Correspond à id_difficulte dans la BDD
    private int idDifficulte;

    // Correspond à niveau dans la BDD
    private String niveau;

    // 1. Constructeur par défaut (très utile)
    public Difficulte() {
    }

    // Constructeur avec tous les champs (pour la récupération de la BDD)
    public Difficulte(int idDifficulte, String niveau) {
        this.idDifficulte = idDifficulte;
        this.niveau = niveau;
    }

    // Getters
    public int getIdDifficulte() {
        return idDifficulte;
    }

    public String getNiveau() {
        return niveau;
    }

    // 2. Setters (ajoutés pour la complétude)
    public void setIdDifficulte(int idDifficulte) {
        this.idDifficulte = idDifficulte;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    // 3. Méthode toString() (ESSENTIEL pour les ComboBox de JavaFX)
    @Override
    public String toString() {
        return niveau;
    }
}