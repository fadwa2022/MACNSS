package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.Ennum.Etat;
import org.cnss.model.AgentCNSS;
import org.cnss.model.DossierRembouresement;
import org.cnss.model.Patient;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;

public class DossierRembouresementDAO {
    private Connection connection;

    public DossierRembouresementDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public DossierRembouresement ajouterDossierRemboursement(AgentCNSS agentCNSS, Patient patient) {
        DossierRembouresement dossierRemboursement = null;
        try {
            String insertQuery = "INSERT INTO dossier (agentCNSS, patient) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, agentCNSS.getEmail());
            preparedStatement.setInt(2, patient.getMatricule());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int dossierId = generatedKeys.getInt(1);


                    String selectQuery = "SELECT * FROM dossier WHERE code = ?";
                    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                    selectStatement.setInt(1, dossierId);
                    ResultSet resultSet = selectStatement.executeQuery();

                    if (resultSet.next()) {
                        String etatString = resultSet.getString("etat");
                        Etat etat = Etat.valueOf(etatString);
                        int montant = resultSet.getInt("montant");

                        dossierRemboursement = new DossierRembouresement(dossierId,etat,montant,agentCNSS,patient);
                    }

                    resultSet.close();
                    selectStatement.close();
                }
                generatedKeys.close();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dossierRemboursement;
    }
    public boolean updateDossierRemboursement(DossierRembouresement dossier) {
        try {
            String updateQuery = "UPDATE dossier SET etat = ?, montant = ? WHERE code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, dossier.getEtat().toString());
            preparedStatement.setInt(2, dossier.getMontant());
            preparedStatement.setInt(3, dossier.getCode());

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public boolean deleteDossierRemboursement(int dossiercode) {
        try {
            String deleteQuery = "DELETE FROM dossier WHERE code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, dossiercode);

            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    public ArrayList<DossierRembouresement> getAllDossier() {
        ArrayList<DossierRembouresement> dossiers = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM dossier";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int dossiercode = resultSet.getInt("code");
                String etatString = resultSet.getString("etat");
                Etat etat = Etat.valueOf(etatString);
                int montant = resultSet.getInt("montant");
                String agentCNSS = resultSet.getString("agentCNSS");
                int patientMatricule = resultSet.getInt("patient");

                AgentCNSSDAO agentCNSSDAO = new AgentCNSSDAO();
                AgentCNSS agent = agentCNSSDAO.getAgentParEmail(agentCNSS);

                PatientDAO patientDAO = new PatientDAO();
                Patient patient = patientDAO.getPatientParMatricule(patientMatricule);

                DossierRembouresement dossier = new DossierRembouresement(dossiercode, etat, montant, agent, patient);
                dossiers.add(dossier);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dossiers;
    }
    public DossierRembouresement getDossierParCode(int dossiercode) {
        try {
            String selectQuery = "SELECT * FROM dossier WHERE code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, dossiercode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String etatString = resultSet.getString("etat");
                Etat etat = Etat.valueOf(etatString);
                int montant = resultSet.getInt("montant");
                String agentCNSS = resultSet.getString("agentCNSS");
                int patientMatricule = resultSet.getInt("patient");

                AgentCNSSDAO agentCNSSDAO = new AgentCNSSDAO();
                AgentCNSS agent = agentCNSSDAO.getAgentParEmail(agentCNSS);

                PatientDAO patientDAO = new PatientDAO();
                Patient patient = patientDAO.getPatientParMatricule(patientMatricule);

                return new DossierRembouresement(dossiercode, etat, montant, agent, patient);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<DossierRembouresement> getDossierParPatient(Patient patient) {
        List<DossierRembouresement> dossiers = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM dossier WHERE patient = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, patient.getMatricule());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int dossiercode = resultSet.getInt("code");
                String etatString = resultSet.getString("etat");
                Etat etat = Etat.valueOf(etatString);
                int montant = resultSet.getInt("montant");
                String agentCNSS = resultSet.getString("agentCNSS");


                AgentCNSSDAO agentCNSSDAO = new AgentCNSSDAO();
                AgentCNSS agent = agentCNSSDAO.getAgentParEmail(agentCNSS);

                DossierRembouresement dossier = new DossierRembouresement(dossiercode, etat, montant, agent, patient);
                dossiers.add(dossier);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dossiers;
    }
    public void updateEtatDossier(DossierRembouresement dossier) {
        try {
            String updateQuery = "UPDATE dossier SET etat = ? WHERE code = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, dossier.getEtat().toString());
            preparedStatement.setInt(2, dossier.getCode());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("Aucun dossier mis à jour. Vérifiez le code du dossier.");
            } else {
                System.out.println("Dossier mis à jour avec succès !");
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
