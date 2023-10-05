package org.cnss.Dao;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.Ennum.Etat;
import org.cnss.Ennum.StatutDeTravail;
import org.cnss.model.Employé;
import org.cnss.model.Société;

public class EmployéDAO {
    private Connection connection;
    SociétéDAO sociétéDAO=new SociétéDAO();
    public EmployéDAO(){
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Employé> ajouterEmployés(List<Employé> employés) {
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        List<Employé> employésAjoutés = new ArrayList<>();

        try {
            String insertQuery = "INSERT INTO employé (matricule, salaireActuel, societeActuel, statutTravail, dateNaissance, nom, prenom) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);

            for (Employé employé : employés) {
                preparedStatement.setString(1, employé.getMatricule());
                preparedStatement.setInt(2, employé.getSalaireActuel());
                preparedStatement.setInt(3, employé.gatsocieteActuel().getNuméroSociété());
                preparedStatement.setString(4, employé.getStatutTravail().toString());
                preparedStatement.setDate(5, new java.sql.Date(employé.getDateNaissance().getTime()));
                preparedStatement.setString(6, employé.getNom());
                preparedStatement.setString(7, employé.getPrenom());

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected == 1) {
                    generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        // Récupérez l'ID généré si nécessaire
                        // employé.setId(generatedKeys.getInt(1));
                        employésAjoutés.add(employé);
                    }
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
        }

        return employésAjoutés;
    }

    public void updateEmployé(Employé employé) {
        PreparedStatement preparedStatement = null;

        try {
            String updateQuery = "UPDATE employé SET salaireActuel = ?, SocieteActuel = ?, statutTravail = ?, dateNaissance = ?, nom = ?, prenom = ? " +
                    "WHERE matricule = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, employé.getSalaireActuel());
            if (employé.gatsocieteActuel() == null) {
                preparedStatement.setNull(2, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setInt(2, employé.gatsocieteActuel().getNuméroSociété());
            }

            preparedStatement.setString(3, employé.getStatutTravail().toString());
            preparedStatement.setDate(4, new java.sql.Date(employé.getDateNaissance().getTime()));
            preparedStatement.setString(5, employé.getNom());
            preparedStatement.setString(6, employé.getPrenom());
            preparedStatement.setString(7, employé.getMatricule());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                // L'employé avec le matricule spécifié n'existe pas
                // Vous pouvez gérer cela de manière appropriée (exception, message d'erreur, etc.)
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void deleteEmployé(String matricule) {
        PreparedStatement preparedStatement = null;

        try {
            String deleteQuery = "DELETE FROM employé WHERE matricule = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, matricule);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                // L'employé avec le matricule spécifié n'existe pas
                // Vous pouvez gérer cela de manière appropriée (exception, message d'erreur, etc.)
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public List<Employé> getAllEmployés(Société société) {
        List<Employé> employés = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String selectQuery = "SELECT * FROM employé WHERE SocieteActuel = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, société.getNuméroSociété());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String matricule = resultSet.getString("matricule");
                int salaireActuel = resultSet.getInt("salaireActuel");
                String statutTravail = resultSet.getString("statutTravail");
                StatutDeTravail statut = StatutDeTravail.valueOf(statutTravail);
                Date dateNaissance = resultSet.getDate("dateNaissance");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");

                Employé employé = new Employé(matricule, salaireActuel, société, statut, dateNaissance, nom, prenom);
                employés.add(employé);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
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
        }

        return employés;
    }
    public Employé getEmployéByMatricule(String matricule) {
        Employé employé = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String selectQuery = "SELECT * FROM employé WHERE matricule = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, matricule);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int salaireActuel = resultSet.getInt("salaireActuel");
                int idsocieteActuel = resultSet.getInt("SocieteActuel");
                String statutTravail = resultSet.getString("statutTravail");
                StatutDeTravail statut = StatutDeTravail.valueOf(statutTravail);
                Date dateNaissance = resultSet.getDate("dateNaissance");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                Société sociétéActuel = sociétéDAO.getSociétéById(idsocieteActuel);

                employé = new Employé(matricule, salaireActuel, sociétéActuel, statut, dateNaissance, nom, prenom);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
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
        }

        return employé;
    }

}
