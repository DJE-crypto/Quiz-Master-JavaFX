package com.s4m.poo.service;

import com.s4m.poo.database.DBManager;
import com.s4m.poo.model.Quiz;
import com.s4m.poo.model.Theme;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service pour gérer les opérations liées à l'entité Quiz.
 */
public class QuizService {

    /**
     * Récupère la liste de tous les Quizzes existants pour les ComboBox.
     * @return Une liste d'objets Quiz.
     */
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();

        // Requête avec jointure pour récupérer le nom du thème en même temps
        String sql = "SELECT q.id_quiz, q.titre, q.date_creation, q.utilisateur_id, t.id_theme, t.nom_theme " +
                "FROM quiz q JOIN theme t ON q.theme_id = t.id_theme " +
                "ORDER BY q.titre";

        // Utilisation du try-with-resources pour la fermeture automatique des ressources
        try (Connection conn = DBManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // 1. Création de l'objet Theme
                Theme theme = new Theme(
                        rs.getInt("id_theme"),
                        rs.getString("nom_theme")
                );

                // 2. Création de l'objet Quiz (avec l'objet Theme)
                Quiz quiz = new Quiz(
                        rs.getInt("id_quiz"),
                        rs.getString("titre"),
                        rs.getDate("date_creation").toLocalDate(),
                        rs.getInt("utilisateur_id"),
                        theme // Passage de l'objet Theme complet
                );
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des quizzes: " + e.getMessage());
            e.printStackTrace();
        }
        return quizzes;
    }
}