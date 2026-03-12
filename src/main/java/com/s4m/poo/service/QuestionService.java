package com.s4m.poo.service;

import com.s4m.poo.database.DBManager;
import com.s4m.poo.model.Question;
import com.s4m.poo.model.Difficulte;
import com.s4m.poo.model.Quiz;
import com.s4m.poo.model.Theme;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionService {

    // Requêtes SQL
    private static final String INSERT_SQL =
            "INSERT INTO question (libelle, choix1, choix2, choix3, choix4, reponse, quiz_id, difficulte_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE question SET libelle = ?, choix1 = ?, choix2 = ?, choix3 = ?, choix4 = ?, " +
                    "reponse = ?, quiz_id = ?, difficulte_id = ? WHERE id_question = ?";

    private static final String DELETE_SQL =
            "DELETE FROM question WHERE id_question = ?";

    // Requête de sélection complète avec toutes les jointures
    private static final String SELECT_ALL_SQL =
            "SELECT " +
                    "q.id_question, q.libelle, q.choix1, q.choix2, q.choix3, q.choix4, q.reponse, " +
                    "q.quiz_id, q.difficulte_id, " +
                    "d.id_difficulte, d.niveau, " +
                    "qz.id_quiz, qz.titre, qz.date_creation, qz.utilisateur_id, qz.theme_id, " +
                    "t.id_theme, t.nom_theme " +
                    "FROM question q " +
                    "JOIN difficulte d ON q.difficulte_id = d.id_difficulte " +
                    "JOIN quiz qz ON q.quiz_id = qz.id_quiz " +
                    "JOIN theme t ON qz.theme_id = t.id_theme " +
                    "ORDER BY q.id_question DESC";

    /**
     * Sauvegarde une question (INSERT ou UPDATE)
     */
    public boolean saveQuestion(Question question) {
        // Si la question a un ID > 0, c'est un UPDATE, sinon INSERT
        if (question.getIdQuestion() > 0) {
            return updateQuestion(question);
        } else {
            return insertQuestion(question);
        }
    }

    /**
     * Insère une nouvelle question
     */
    private boolean insertQuestion(Question question) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, question.getLibelle());
            pstmt.setString(2, question.getChoix1());
            pstmt.setString(3, question.getChoix2());
            pstmt.setString(4, question.getChoix3());
            pstmt.setString(5, question.getChoix4());
            pstmt.setInt(6, question.getReponse());
            pstmt.setInt(7, question.getQuiz().getIdQuiz());
            pstmt.setInt(8, question.getDifficulte().getIdDifficulte());

            int affectedRows = pstmt.executeUpdate();

            // Récupérer l'ID généré
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        question.setIdQuestion(generatedKeys.getInt(1));
                    }
                }
            }
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de la question : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour une question existante
     */
    public boolean updateQuestion(Question question) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            pstmt.setString(1, question.getLibelle());
            pstmt.setString(2, question.getChoix1());
            pstmt.setString(3, question.getChoix2());
            pstmt.setString(4, question.getChoix3());
            pstmt.setString(5, question.getChoix4());
            pstmt.setInt(6, question.getReponse());
            pstmt.setInt(7, question.getQuiz().getIdQuiz());
            pstmt.setInt(8, question.getDifficulte().getIdDifficulte());
            pstmt.setInt(9, question.getIdQuestion());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la question : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime une question par son ID
     */
    public boolean deleteQuestion(int id) {
        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la question : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Récupère toutes les questions
     */
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                // 1. Créer l'objet Theme
                Theme theme = new Theme();
                theme.setIdTheme(rs.getInt("id_theme"));
                theme.setNomTheme(rs.getString("nom_theme"));

                // 2. Créer l'objet Difficulte
                Difficulte difficulte = new Difficulte();
                difficulte.setIdDifficulte(rs.getInt("id_difficulte"));
                difficulte.setNiveau(rs.getString("niveau"));

                // 3. Créer l'objet Quiz avec la date correctement convertie
                Quiz quiz = new Quiz();
                quiz.setIdQuiz(rs.getInt("id_quiz"));
                quiz.setTitre(rs.getString("titre"));
                Date sqlDate = rs.getDate("date_creation");
                if (sqlDate != null) {
                    quiz.setDateCreation(sqlDate.toLocalDate());
                }
                quiz.setUtilisateurId(rs.getInt("utilisateur_id"));
                quiz.setTheme(theme);

                // 4. Créer l'objet Question
                Question question = new Question();
                question.setIdQuestion(rs.getInt("id_question"));
                question.setLibelle(rs.getString("libelle"));
                question.setChoix1(rs.getString("choix1"));
                question.setChoix2(rs.getString("choix2"));
                question.setChoix3(rs.getString("choix3"));
                question.setChoix4(rs.getString("choix4"));
                question.setReponse(rs.getInt("reponse"));
                question.setQuiz(quiz);
                question.setDifficulte(difficulte);

                questions.add(question);
            }

            System.out.println("DEBUG - " + questions.size() + " questions chargées avec succès");

        } catch (SQLException e) {
            System.err.println("ERREUR lors du chargement des questions : " + e.getMessage());
            e.printStackTrace();
        }
        return questions;
    }
}