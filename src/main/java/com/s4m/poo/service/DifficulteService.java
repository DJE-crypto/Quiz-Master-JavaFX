package com.s4m.poo.service;

import com.s4m.poo.database.DBManager;
import com.s4m.poo.model.Difficulte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DifficulteService {

    private static final String SELECT_ALL_SQL = "SELECT id_difficulte, niveau FROM difficulte ORDER BY id_difficulte";

    /**
     * Récupère tous les niveaux de difficulté de la base de données.
     * @return Une liste d'objets Difficulte.
     */
    public List<Difficulte> getAllDifficultes() {
        List<Difficulte> difficultes = new ArrayList<>();

        try (Connection conn = DBManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Difficulte difficulte = new Difficulte(
                        rs.getInt("id_difficulte"),
                        rs.getString("niveau")
                );
                difficultes.add(difficulte);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des difficultés : " + e.getMessage());
            e.printStackTrace();
        }
        return difficultes;
    }
}