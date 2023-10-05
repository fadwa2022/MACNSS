package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.model.Categories;
import org.cnss.model.Medicament;

import java.sql.*;
import java.util.ArrayList;

public class MedicamentDAO {
    private static Connection connection;

    public MedicamentDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Medicament> afficherTousLesMedicaments() {
        ArrayList<Medicament> medicaments = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM medicament";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int Code_barre = resultSet.getInt("Code_barre");
                String Nom = resultSet.getString("Nom");
                int prix = resultSet.getInt("prix");
                int Taux_remboursement = resultSet.getInt("Taux_remboursement");
                Medicament medicament = new Medicament(Code_barre,Nom,prix,Taux_remboursement);
                medicaments.add(medicament);

            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicaments;
    }
    public static ArrayList<Medicament> rechercherMedicamentParNom(String nom) {
        ArrayList<Medicament> medicaments = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM medicament WHERE Nom LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, "%" + nom + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int Code_barre = resultSet.getInt("Code_barre");
                String Nom = resultSet.getString("Nom");
                int prix = resultSet.getInt("prix");
                int Taux_remboursement = resultSet.getInt("Taux_remboursement");
                Medicament medicament = new Medicament(Code_barre, Nom, prix, Taux_remboursement);
                medicaments.add(medicament);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicaments;
    }
    public Medicament rechercherMedicamentParCode(int code) {
        Medicament medicament = null;
        try {
            String selectQuery = "SELECT * FROM medicament WHERE Code_barre = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String Nom = resultSet.getString("Nom");
                int prix = resultSet.getInt("prix");
                int Taux_remboursement = resultSet.getInt("Taux_remboursement");
                medicament = new Medicament(code, Nom, prix, Taux_remboursement);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medicament;
    }



}
