package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.model.Société;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SociétéDAO {
    private Connection connection;

    public SociétéDAO(){
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Société authentifierSociété(String email, int password) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Société sociétéAuthentifiée = null;

        try {
            String selectQuery = "SELECT * FROM société WHERE email = ? AND password = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, email);
            preparedStatement.setInt(2, password);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int numéroSociété = resultSet.getInt("numéroSociété");
                String nomSociété = resultSet.getString("nomSociété");
                String emailSociété = resultSet.getString("email");
                int passwordSociété = resultSet.getInt("password");
                sociétéAuthentifiée = new Société(numéroSociété, nomSociété, emailSociété, passwordSociété);
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

        return sociétéAuthentifiée;
    }

    public Société ajouterSociété(Société société) {
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            String insertQuery = "INSERT INTO société (nomSociété, email, password) VALUES (?, ?, ?)";

            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, société.getNomSociété());
            preparedStatement.setString(2, société.getEmail());
            preparedStatement.setInt(3, société.getPassword());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 1) {
                generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int sociétéId = generatedKeys.getInt(1);
                    société = new Société(sociétéId, société.getNomSociété(),société.getEmail(),société.getPassword());
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

        return société;
    }
    public void updateSociété(int numéroSociété, String nouveauNomSociété) {
        PreparedStatement preparedStatement = null;

        try {
            String updateQuery = "UPDATE société SET nomSociété = ? WHERE numéroSociété = ?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, nouveauNomSociété);
            preparedStatement.setInt(2, numéroSociété);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
             System.out.println("aucune société trouver");
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
    public void deleteSociété(int idSociété) {
        PreparedStatement preparedStatement = null;

        try {
            String deleteQuery = "DELETE FROM société WHERE numéroSociété = ?";
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, idSociété);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("aucune société trouver");
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
    public List<Société> afficherToutesSociétés() {
        List<Société> sociétés = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM société";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int numéroSociété = resultSet.getInt("numéroSociété");
                String nomSociété = resultSet.getString("nomSociété");
                String email = resultSet.getString("email");
                int password = resultSet.getInt("password");

                Société société = new Société(numéroSociété, nomSociété,email,password);
                sociétés.add(société);
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sociétés;
    }
    public Société getSociétéById(int idSociété) {
        Société société = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String selectQuery = "SELECT * FROM société WHERE numéroSociété  = ?";
            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, idSociété);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nomSociété = resultSet.getString("nomSociété");
                String email = resultSet.getString("email");
                int password = resultSet.getInt("password");
                société = new Société(idSociété, nomSociété,email,password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérez l'exception appropriée ici
        } finally {
            // Fermez les ressources ResultSet et PreparedStatement
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

        return société;
    }

}
