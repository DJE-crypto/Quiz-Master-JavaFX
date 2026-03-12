package com.s4m.poo.service;

import com.s4m.poo.model.Utilisateur;
import com.s4m.poo.database.DBManager; // Le chemin correct selon votre structure

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UtilisateurService {

    private static final int ROLE_JOUEUR_ID = 2;

    /**
     * Tente d'enregistrer un nouvel utilisateur (joueur) dans la base de données.
     */
    // ⬇️ C'est cette méthode qui doit être présente
    public boolean registerUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (nom, email, password, role_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getNom());
            pstmt.setString(2, utilisateur.getEmail());
            pstmt.setString(3, utilisateur.getPassword());
            pstmt.setInt(4, ROLE_JOUEUR_ID);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'inscription de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    // La méthode login que nous avons ajoutée précédemment :
    public Optional<Utilisateur> login(String email, String password) {
        String sql = "SELECT id_user, nom, email, password, role_id FROM utilisateur WHERE email = ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String hashedPasswordFromDB = rs.getString("password");

                if (password.equals(hashedPasswordFromDB)) { // Comparaison simple

                    Utilisateur user = new Utilisateur(
                            rs.getInt("id_user"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            hashedPasswordFromDB,
                            rs.getInt("role_id")
                    );
                    return Optional.of(user);
                }
            }
            return Optional.empty();

        } catch (SQLException e) {
            System.err.println("Erreur lors de la tentative de connexion : " + e.getMessage());
            return Optional.empty();
        }
    }
    public int getScoreTotalByUtilisateurId(int utilisateurId) {
        int scoreTotal = 0;
        String query = "SELECT SUM(score) as total FROM resultat WHERE utilisateur_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, utilisateurId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                scoreTotal = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scoreTotal;
    }

    public int getNiveauByUtilisateurId(int utilisateurId) {
        int scoreTotal = getScoreTotalByUtilisateurId(utilisateurId);
        // Exemple : 1000 points par niveau
        return scoreTotal / 1000 + 1;
    }

    // Dans votre UtilisateurService.java, ajoutez:

    public int getUserTotalScore(int userId) {
        String query = "SELECT SUM(score) as total FROM resultat WHERE utilisateur_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return 0;
    }

    public int getUserRank(int userId) {
        // Cette méthode calcule le classement de l'utilisateur
        String query = "SELECT COUNT(*) + 1 as rank " +
                "FROM utilisateur u " +
                "LEFT JOIN (SELECT utilisateur_id, SUM(score) as total_score " +
                "           FROM resultat GROUP BY utilisateur_id) r " +
                "ON u.id_utilisateur = r.utilisateur_id " +
                "WHERE COALESCE(r.total_score, 0) > " +
                "(SELECT COALESCE(SUM(score), 0) FROM resultat WHERE utilisateur_id = ?)";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("rank");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return 999; // Classement par défaut
    }
}