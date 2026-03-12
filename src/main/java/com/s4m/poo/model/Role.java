package com.s4m.poo.model;

public class Role {
    private int id;
    private String nom;

    public Role(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
}