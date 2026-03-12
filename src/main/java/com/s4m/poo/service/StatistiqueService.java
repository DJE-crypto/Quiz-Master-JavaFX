package com.s4m.poo.service;

import com.s4m.poo.database.DBManager;
import java.sql.*;

public class StatistiqueService {
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
}