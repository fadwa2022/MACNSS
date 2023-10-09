package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;

import org.cnss.model.Retraités;

import java.sql.*;


public class RetraitésDAO {
    private static Connection connection;

    public RetraitésDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Retraités ajouterRetraite(Retraités retraités) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            String insertQuery = "INSERT INTO `retraités`(`salaireDeRetraite`, `matricule`) VALUES (?,?)";

            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setFloat(1, retraités.getSalaireDeRetraite());
            preparedStatement.setString(2, retraités.getEmployé().getMatricule());

            int rowsAffected = preparedStatement.executeUpdate();

            // Récupérez ici les clés générées si nécessaire.

            if (rowsAffected == 1) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    String retraiteid = generatedKeys.getString(2);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


        return retraités;
    }

    // Vous pouvez ajouter une méthode closeConnection() pour gérer la fermeture de la connexion si nécessaire.
}
