package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.model.Administrateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AdministrateurDAO {
  private Connection connection;

  public AdministrateurDAO() {
    try {
      connection = DatabaseConnection.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Administrateur authentifier(String email,String code) {
    Administrateur admin = null;
    String sql = "SELECT * FROM admin WHERE Email = ? AND Pass = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, email);
      statement.setString(2, code);

      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          String emailResult = resultSet.getString("Email");
          String passResult = resultSet.getString("Pass");
          admin = new Administrateur(emailResult, passResult);
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException("Erreur lors de l'authentification de l'administrateur.", e);
    }
    return admin;
  }
}
