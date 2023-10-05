package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.model.AgentCNSS;
import org.cnss.model.Patient;

import java.sql.*;
import java.util.ArrayList;

public class PatientDAO {
    private Connection connection;
    Patient patient ;
    public PatientDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Patient ajouterPatient(String nom, String prenom) {
        Patient patient1 = null;
        try {
            String insertQuery = "INSERT INTO patient (Nom, Prenom) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int matricule = generatedKeys.getInt(1);

                    patient1 = new Patient(nom, prenom, matricule);
                }
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patient1;
    }
    public ArrayList<Patient> afficherTousLespatients() {
        ArrayList<Patient> patients = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM patient";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                int matricule = resultSet.getInt("matricule");

                Patient patient1 = new Patient(nom,prenom,matricule);
                patients.add(patient1);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }
    public Patient getPatientParMatricule(int matricule) {
        Patient patient = null;
        try {
            String selectQuery = "SELECT * FROM patient WHERE Matricule=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, matricule);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String prenom = resultSet.getString("Prenom");
                int Matricule = resultSet.getInt("Matricule");

                patient = new Patient(nom,prenom,Matricule);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patient;
    }
    public boolean updatePatient(int matricule, String nouveauNom, String nouveauPrenom) {
        try {
            String updateQuery = "UPDATE patient SET Nom = ?, Prenom = ? WHERE Matricule = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, nouveauNom);
            preparedStatement.setString(2, nouveauPrenom);
            preparedStatement.setInt(3, matricule);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean deletePatient(int matricule) {
        try {
            String deleteQuery = "DELETE FROM patient WHERE Matricule = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, matricule);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
