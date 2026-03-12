package com.s4m.poo.service;

import com.s4m.poo.database.DBManager;
import com.s4m.poo.model.Theme;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ThemeService {

    private static final String SELECT_ALL_SQL = "SELECT id_theme, nom_theme FROM theme ORDER BY id_theme";

    /**
     * Récupère tous les thèmes de la base de données.
     * @return Une liste d'objets Theme.
     */
    static public List<Theme> getAllThemes() {
        List<Theme> themes = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBManager.getConnection();
            stmt = conn.prepareStatement(SELECT_ALL_SQL);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_theme");
                String nom = rs.getString("nom_theme");

                Theme t = new Theme(id, nom);
                themes.add(t);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des thèmes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermeture des ressources
            DBManager.closeConnection(conn);
            if (stmt != null) { try { stmt.close(); } catch (SQLException e) {} }
            if (rs != null) { try { rs.close(); } catch (SQLException e) {} }
        }

        return themes;
    }
}