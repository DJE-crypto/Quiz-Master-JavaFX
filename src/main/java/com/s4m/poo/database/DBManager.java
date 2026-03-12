package com.s4m.poo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    // Connexion configurée pour WampServer/MySQL avec la BDD 'quiz_ece'
    private static final String URL = "jdbc:mysql://localhost:3306/quiz_ece?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Ouvre une connexion à la base de données.
     * @return L'objet Connection.
     * @throws SQLException Si la connexion échoue (WampServer éteint, BDD incorrecte, etc.).
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Note: Le port 3307 est utilisé par défaut par WampServer avec MariaDB (comme indiqué dans ton SQL Dump).
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("--- ERREUR DE CONNEXION À LA BASE DE DONNÉES ---");
            System.err.println("1. Vérifiez que WampServer est allumé.");
            System.err.println("2. Assurez-vous que le service MySQL/MariaDB fonctionne sur le port 3307.");
            System.err.println("Détails de l'erreur: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Ferme une connexion.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
}