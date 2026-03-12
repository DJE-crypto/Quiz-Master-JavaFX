package com.s4m.poo.model;

public class Utilisateur {
    private int id_user;
    private String nom;
    private String email;
    private String password;
    private int role_id; // Clé étrangère

    // NOUVEAU CONSTRUCTEUR POUR L'INSCRIPTION
    // Il ne prend pas d'id_user (généré par la DB) et le role_id sera défini par le service
    public Utilisateur(String nom, String email, String password) {
        this.nom = nom;
        this.email = email;
        this.password = password;
        // Le rôle sera défini à "player" (role_id=2, par exemple) dans le service.
        // On initialise ici par sécurité, en supposant 2 pour "Joueur"
        this.role_id = 2;
    }

    // Constructeur complet (pour la récupération depuis la DB)
    public Utilisateur(int id_user, String nom, String email, String password, int role_id) {
        this.id_user = id_user;
        this.nom = nom;
        this.email = email;
        this.password = password;
        this.role_id = role_id;
    }

    // Getters pour l'authentification
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public int getRoleId() { return role_id; }

    // Getters pour le dashboard
    public String getNom() { return nom; }
    public int getIdUser() { return id_user; }

    // Utile pour le contrôleur de jeu/admin
    public boolean isAdmin() {
        // En supposant que le rôle Admin a l'ID 1
        return this.role_id == 1;
    }
}