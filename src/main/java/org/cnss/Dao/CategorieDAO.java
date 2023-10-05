package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.model.AgentCNSS;
import org.cnss.model.Categories;
import org.cnss.model.Document;
import org.cnss.model.DossierRembouresement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieDAO {

    private  Connection connection;

    public CategorieDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Categories> afficherTousLesCategories() {
        ArrayList<Categories> categories = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM categories";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                int Code_Categorie = resultSet.getInt("Code_Cat");

                String Nom = resultSet.getString("Nom");

                int taux_de_rembourssement = resultSet.getInt("Taux");

                Categories categorie = new Categories(Code_Categorie,Nom,taux_de_rembourssement);
                categories.add(categorie);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
    public Categories getCategoriesParcode(int Code_Cat) {
        Categories categories = null;
        try {
            String selectQuery = "SELECT * FROM categories WHERE Code_Cat=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, Code_Cat);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                int Taux = resultSet.getInt("Taux");
                categories = new Categories(Code_Cat,nom,Taux);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

}
