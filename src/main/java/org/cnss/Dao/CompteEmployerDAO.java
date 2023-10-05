package org.cnss.Dao;

import org.cnss.model.CompteEmployer;
import org.cnss.model.Employé;
import org.cnss.model.Société;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompteEmployerDAO {
    private Connection connection;
    EmployéDAO employéDAO =new EmployéDAO();
    SociétéDAO sociétéDAO =new SociétéDAO();

    public CompteEmployer ajouterCompteEmployer(Employé employé, Société société) {
        CompteEmployer compteEmployer = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            String insertQuery = "INSERT INTO compteemploye (employé, société) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, employé.getMatricule());
            preparedStatement.setInt(2, société.getNuméroSociété());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                compteEmployer = new CompteEmployer(employé, société);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception appropriée ici
        } finally {
            // Fermez les ressources PreparedStatement et ResultSet
            // ...

            return compteEmployer;
        }
    }

    public void updateCompteEmployer(CompteEmployer compteEmployer) {
        PreparedStatement preparedStatement = null;

        try {
            String updateQuery = "UPDATE compteemploye SET  société = ? WHERE employé = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, compteEmployer.getSociété().getNuméroSociété());

            preparedStatement.setString(2, compteEmployer.getEmployé().getMatricule());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                // Le compte employé avec l'ID spécifié n'existe pas
                // Vous pouvez gérer cela de manière appropriée (exception, message d'erreur, etc.)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception appropriée ici
        } finally {
            // Fermez la ressource PreparedStatement
            // ...
        }
    }

    public void deleteCompteEmployer(Employé employé) {
        PreparedStatement preparedStatement = null;

        try {
            String deleteQuery = "DELETE FROM compteemploye WHERE employé = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, employé.getMatricule());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                // Le compte employé avec l'ID spécifié n'existe pas
                // Vous pouvez gérer cela de manière appropriée (exception, message d'erreur, etc.)
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception appropriée ici
        } finally {
            // Fermez la ressource PreparedStatement
            // ...
        }
    }

    public List<CompteEmployer> getAllCompteEmployer() {
        List<CompteEmployer> compteEmployers = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            String selectQuery = "SELECT * FROM compteemploye";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String matricule = resultSet.getString("employé");
                int idSociété = resultSet.getInt("société");

                Employé employé = employéDAO.getEmployéByMatricule(matricule);
                Société société = sociétéDAO.getSociétéById(idSociété);

                CompteEmployer compteEmployer = new CompteEmployer(employé, société);
                compteEmployers.add(compteEmployer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception appropriée ici
        } finally {
            // Fermez les ressources Statement et ResultSet
            // ...
        }

        return compteEmployers;
    }

    public CompteEmployer getCompteEmployerParmatricule(String matricule) {
        CompteEmployer compteEmployer = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String selectQuery = "SELECT * FROM compteemploye WHERE employé = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, matricule);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String matriculeemployer = resultSet.getString("employé");
                int idSociété = resultSet.getInt("société");

                Employé employé = employéDAO.getEmployéByMatricule(matriculeemployer);
                Société société = sociétéDAO.getSociétéById(idSociété);



                compteEmployer = new CompteEmployer(employé, société);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception appropriée ici
        } finally {
            // Fermez les ressources PreparedStatement et ResultSet
            // ...
        }

        return compteEmployer;
    }

    public List<CompteEmployer> getAllCompteEmployerParSociété(int idSociété) {
        List<CompteEmployer> compteEmployers = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String selectQuery = "SELECT * FROM compteemploye WHERE société = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idSociété);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String Employer = resultSet.getNString("employé");
                int idsociété = resultSet.getInt("société");

                Employé employé = employéDAO.getEmployéByMatricule(Employer);
                Société société = sociétéDAO.getSociétéById(idSociété);



                CompteEmployer compteEmployer = new CompteEmployer(employé, société);
                compteEmployers.add(compteEmployer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception appropriée ici
        } finally {
            // Fermez les ressources PreparedStatement et ResultSet
            // ...
        }

        return compteEmployers;
    }
}

