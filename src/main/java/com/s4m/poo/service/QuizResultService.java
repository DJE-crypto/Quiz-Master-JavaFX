package com.s4m.poo.service;

import com.s4m.poo.model.QuizResult;
import com.s4m.poo.database.DBManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizResultService {

    public List<QuizResult> getRecentResultsByUser(int userId, int limit) {
        List<QuizResult> results = new ArrayList<>();
        String query = "SELECT q.titre, r.score, r.date_completion " +
                "FROM resultat r " +
                "JOIN quiz q ON r.quiz_id = q.id_quiz " +
                "WHERE r.utilisateur_id = ? " +
                "ORDER BY r.date_completion DESC " +
                "LIMIT ?";

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    QuizResult result = new QuizResult();
                    result.setQuizName(rs.getString("titre"));
                    result.setScore(rs.getInt("score"));

                    // Formater la date
                    Timestamp timestamp = rs.getTimestamp("date_completion");
                    if (timestamp != null) {
                        result.setDate(timestamp);
                    }

                    results.add(result);
                }
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des résultats: " + e.getMessage());
            e.printStackTrace();
        }
        return results;
    }

    public int getUserQuizzesCount(int userId) {
        String query = "SELECT COUNT(*) as count FROM resultat WHERE utilisateur_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return 0;
    }

    public double getUserAverageScore(int userId) {
        String query = "SELECT AVG(score) as average FROM resultat WHERE utilisateur_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("average");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return 0.0;
    }

    public int getUserCompletedThemesCount(int userId) {
        String query = "SELECT COUNT(DISTINCT t.id_theme) as count " +
                "FROM resultat r " +
                "JOIN quiz q ON r.quiz_id = q.id_quiz " +
                "JOIN theme t ON q.theme_id = t.id_theme " +
                "WHERE r.utilisateur_id = ?";
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur: " + e.getMessage());
        }
        return 0;
    }
}