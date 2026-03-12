package com.s4m.poo.model;

public class Theme {
    private int idTheme;
    private String nomTheme;
    private String description;

    // Constructeurs
    public Theme() {}

    public Theme(int idTheme, String nomTheme) {
        this.idTheme = idTheme;
        this.nomTheme = nomTheme;
    }

    // Getters et Setters
    public int getIdTheme() { return idTheme; }
    public void setIdTheme(int idTheme) { this.idTheme = idTheme; }

    public String getNomTheme() { return nomTheme; }
    public void setNomTheme(String nomTheme) { this.nomTheme = nomTheme; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return nomTheme;
    }
}