package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.Ennum.Etat;
import org.cnss.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentsDAO {
    private  Connection connection;

    public DocumentsDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Document> getDocumentPardossier(DossierRembouresement dossier) {
        ArrayList<Document> documents = new ArrayList<>();

        try {
            String selectQuery = "SELECT * FROM document WHERE dossier = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, dossier.getCode());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String documentCode = resultSet.getString("Code");
                int montant = resultSet.getInt("Montant");
                int categoryCode = resultSet.getInt("Category");
                int taux = resultSet.getInt("taux");

                Categories categorie = new CategorieDAO().getCategoriesParcode(categoryCode);

                Document document = new Document(documentCode, montant, categorie, dossier, taux);
                documents.add(document);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return documents;
    }
    public boolean ajouterDocument(Document document) {
        PreparedStatement preparedStatement = null;
        try {
            int codeDossier = document.getDossierRembouresement().getCode();
            String selectDossierQuery = "SELECT Etat FROM dossier WHERE Code = ?";
            PreparedStatement selectDossierStatement = connection.prepareStatement(selectDossierQuery);
            selectDossierStatement.setInt(1, codeDossier);
            ResultSet dossierResultSet = selectDossierStatement.executeQuery();

            if (dossierResultSet.next()) {
                String etatDossier = dossierResultSet.getString("Etat");

                if ("VALIDE".equals(etatDossier)) {
                    // L'état du dossier est VALIDE, vous pouvez ajouter le document
                    String insertQuery = "INSERT INTO document (Code, Montant, Category, dossier, taux) VALUES (?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, document.getCode());
                    preparedStatement.setFloat(2, document.getMontant());
                    preparedStatement.setInt(3, document.getCategory().getCode_Categories());
                    preparedStatement.setInt(4, codeDossier);
                    preparedStatement.setInt(5, document.getTaux());

                    int rowsAffected = preparedStatement.executeUpdate();

                    return rowsAffected > 0;
                } else {
                    // Mettre à jour l'état du dossier en VALIDE et ajouter le document
                    String updateDossierQuery = "UPDATE dossier SET Etat = 'VALIDE' WHERE Code = ?";
                    PreparedStatement updateDossierStatement = connection.prepareStatement(updateDossierQuery);
                    updateDossierStatement.setInt(1, codeDossier);
                    updateDossierStatement.executeUpdate();

                    // Ajouter le document
                    String insertQuery = "INSERT INTO document (Code, Montant, Category, dossier, taux) VALUES (?, ?, ?, ?, ?)";
                    preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, document.getCode());
                    preparedStatement.setFloat(2, document.getMontant());
                    preparedStatement.setInt(3, document.getCategory().getCode_Categories());
                    preparedStatement.setInt(4, codeDossier);
                    preparedStatement.setInt(5, document.getTaux());

                    int rowsAffected = preparedStatement.executeUpdate();

                    return rowsAffected > 0;
                }
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

        return false;
    }

}

