package org.cnss.Dao;

import org.cnss.DataBase.DatabaseConnection;
import org.cnss.Ennum.Etat;
import org.cnss.Ennum.StatutDeTravail;
import org.cnss.model.Employé;
import org.cnss.model.Retraités;
import org.cnss.model.Salaires;
import org.cnss.model.Société;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
public class SalairesDAO {
    private static Connection connection;
    EmployéDAO employéDAO =new  EmployéDAO();
    SociétéDAO sociétéDAO =new SociétéDAO();
    RetraitésDAO retraitésDAO =new RetraitésDAO();

    public SalairesDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void ajouterEmployerSalairesPremierfois(Employé employé, Date dateajouteemploye) {
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        try {
            String insertQuery = "INSERT INTO `salaires`(`matriculeEmployer`, `salaire`, `societe`,`dateSalaire`) VALUES ( ?,?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);


            preparedStatement.setString(1, employé.getMatricule());
            preparedStatement.setInt(2, employé.getSalaireActuel());
            preparedStatement.setInt(3, employé.gatsocieteActuel().getNuméroSociété());
            preparedStatement.setDate(4, (java.sql.Date) dateajouteemploye);
            int rowsAffected = preparedStatement.executeUpdate();


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

    }
    public Salaires ajouterLesjoursCotises(Employé employé, Date dateDeJoursDabsant, int joursAbsents) {
        Salaires salaires = null;

        try {

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateDeJoursDabsant);
            int annee = calendar.get(Calendar.YEAR);
            int mois = calendar.get(Calendar.MONTH) + 1;

            String sql = "UPDATE salaires SET joursCotises = joursCotises + ? " +
                    "WHERE matriculeEmployer = ? " +
                    "AND YEAR(dateSalaire) = ? " +
                    "AND MONTH(dateSalaire) = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, joursAbsents);
            preparedStatement.setString(2, employé.getMatricule());
            preparedStatement.setInt(3, annee);
            preparedStatement.setInt(4, mois);


            int rowsAffected = preparedStatement.executeUpdate();



            if (rowsAffected > 0) {
                String selectSql = "SELECT * FROM salaires " +
                        "WHERE matriculeEmployer = ? " +
                        "AND YEAR(dateSalaire) = ? " +
                        "AND MONTH(dateSalaire) = ?";

                PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                selectStatement.setString(1, employé.getMatricule());
                selectStatement.setInt(2, annee);
                selectStatement.setInt(3, mois);

                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String matriculeEmployer = resultSet.getString("matriculeEmployer");
                    Employé employé1=employéDAO.getEmployéByMatricule(matriculeEmployer);
                    int salaire = resultSet.getInt("salaire");
                    int societe = resultSet.getInt("societe");
                    Société société=sociétéDAO.getSociétéById(societe);
                    int joursCotises = resultSet.getInt("joursCotises");
                    Date dateSalaire = resultSet.getDate("dateSalaire");

                    salaires = new Salaires(id, employé1, salaire, société, joursCotises, dateSalaire);
                }

                resultSet.close();
                selectStatement.close();
            } else {
                String insertSql = "INSERT INTO salaires (matriculeEmployer, salaire, societe, joursCotises, dateSalaire) " +
                        "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setString(1, employé.getMatricule());
                insertStatement.setInt(2, employé.getSalaireActuel());
                insertStatement.setInt(3, employé.gatsocieteActuel().getNuméroSociété());
                insertStatement.setInt(4, joursAbsents);
                insertStatement.setDate(5, new java.sql.Date(dateDeJoursDabsant.getTime()));

                int rowsInserted = insertStatement.executeUpdate();
                insertStatement.close();

                if (rowsInserted > 0) {

                    salaires= new Salaires(1,employé,employé.getSalaireActuel(),employé.gatsocieteActuel(),joursAbsents,dateDeJoursDabsant);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return salaires;
    }
    public List<Salaires> GetListesalaires(Employé employé) throws SQLException {
        // Créez une liste pour stocker les salaires de l'employé.
        List<Salaires> salaireEmployer = new ArrayList<>();
        PreparedStatement preparedStatement = null;

        try {
            String selectQuery = "SELECT * FROM salaires WHERE matriculeEmployer = ? ";

            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, employé.getMatricule());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int salaire = resultSet.getInt("salaire");
                int societeId = resultSet.getInt("societe"); // Utilisez un nom plus clair pour la variable.
                int joursCotises = resultSet.getInt("joursCotises");
                Date dateSalaire = resultSet.getDate("dateSalaire");

                Société société = sociétéDAO.getSociétéById(societeId);

                Salaires salaires = new Salaires(id, employé, salaire, société, joursCotises, dateSalaire);
                salaireEmployer.add(salaires);
            }

            resultSet.close();
        } catch (SQLException e) {
            // Gérez les exceptions ici (par exemple, en les lançant ou en les enregistrant).
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

        return salaireEmployer;
    }
    public List<Salaires> GetListesalairesexceptionsmoischomageetcotisation(Employé employé) throws SQLException {
        // Créez une liste pour stocker les salaires de l'employé.
        List<Salaires> salaireEmployer = new ArrayList<>();
        PreparedStatement preparedStatement = null;

        try {
            String selectQuery = "SELECT * FROM salaires WHERE matriculeEmployer = ? AND salaire != 0 AND joursCotises = 0";

            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, employé.getMatricule());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int salaire = resultSet.getInt("salaire");
                int societeId = resultSet.getInt("societe"); // Utilisez un nom plus clair pour la variable.
                int joursCotises = resultSet.getInt("joursCotises");
                Date dateSalaire = resultSet.getDate("dateSalaire");

                Société société = sociétéDAO.getSociétéById(societeId);

                Salaires salaires = new Salaires(id, employé, salaire, société, joursCotises, dateSalaire);
                salaireEmployer.add(salaires);
            }

            resultSet.close();
        } catch (SQLException e) {
            // Gérez les exceptions ici (par exemple, en les lançant ou en les enregistrant).
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }

        return salaireEmployer;
    }

    public List<Date> PremierEtDerniereDateSalaire(Employé employé) throws SQLException {
        List<Salaires> Moisdetravail = GetListesalairesexceptionsmoischomageetcotisation(employé);
        List<Date> premierEtDerniereDate = new ArrayList<>();

        if (!Moisdetravail.isEmpty()) {
            // Triez la liste par ordre décroissant de date de salaire.
            Collections.sort(Moisdetravail, new Comparator<Salaires>() {
                @Override
                public int compare(Salaires s1, Salaires s2) {
                    return s2.getDateSalaire().compareTo(s1.getDateSalaire());
                }
            });

            // La première date (la date la plus récente) est l'élément en tête de liste.
            premierEtDerniereDate.add((Date) Moisdetravail.get(0).getDateSalaire());

            // La dernière date (la date la plus ancienne) est l'élément en queue de liste.
            premierEtDerniereDate.add((Date) Moisdetravail.get(Moisdetravail.size() - 1).getDateSalaire());
        }

        return premierEtDerniereDate;
    }
    public List<Salaires> moisdechomage(Employé employé) throws SQLException {
        List<Salaires> salaireEmployer = GetListesalaires(employé);
        List<Salaires> Salaireschomage = new ArrayList<>();

        // Parcourez tous les salaires de l'employé
        for (Salaires salaire : salaireEmployer) {
            int salaireMensuel = salaire.getSalaire();


            if (salaireMensuel == 0) {
                Salaireschomage.add(salaire);
            }
        }



        return Salaireschomage;
    }
    public int nbrjourscotisation (Employé employé) throws SQLException {
        List<Salaires> salaireEmployer = GetListesalaires(employé);
        int nbrjourscotisation = 0;

        // Parcourez tous les salaires de l'employé
        for (Salaires salaire : salaireEmployer) {
            int jourscotisation = salaire.getJoursCotises();


            if (jourscotisation != 0) {
                nbrjourscotisation+=jourscotisation;
            }
        }



        return nbrjourscotisation;
    }
    public int calculernombrejoursdetravaille(Employé employé) throws SQLException {
        List<Salaires> salaireEmployer = GetListesalaires(employé);
        List<Salaires> Moisdechomage = moisdechomage(employé);
        List<Salaires> MoisdeTravail =GetListesalairesexceptionsmoischomageetcotisation(employé);
        List<Date> premierEtDerniereDate = PremierEtDerniereDateSalaire(employé);
        StatutDeTravail situationTravail = employé.getStatutTravail();

        int nombredemoisdechomage=Moisdechomage.size();
        int nombredejoursdecotisation=nbrjourscotisation(employé);
        int nbrjourdetravail=0;
        int nbrmoisdetravail=0;


                // Si en chômage, calculez la moyenne des mois entre la première et la dernière date de déclaration.
                if (premierEtDerniereDate.size() == 2) {

                    Calendar cal1 = new GregorianCalendar();
                    cal1.setTime(premierEtDerniereDate.get(0));
                    int dernierdatedeclaration = cal1.get(Calendar.YEAR);
                    if (StatutDeTravail.EMPLOYE.equals(situationTravail)){
                        Calendar cal5 = new GregorianCalendar();
                        cal5.setTime(new java.util.Date());
                        dernierdatedeclaration = cal5.get(Calendar.YEAR);
                    }

                    Calendar cal2 = new GregorianCalendar();
                      cal2.setTime(premierEtDerniereDate.get(1));
                    int premiererdatedeclaration = cal2.get(Calendar.YEAR);
                    int nbrmoisdedureetravail=(dernierdatedeclaration-premiererdatedeclaration)*12;

                    for (Salaires moischomage : Moisdechomage) {
                        Calendar calchomage = new GregorianCalendar();
                        calchomage.setTime(moischomage.getDateSalaire());

                        if (calchomage.get(Calendar.YEAR) > dernierdatedeclaration) {
                            nombredemoisdechomage--;
                        }
                    }
                     nbrjourdetravail= ((nbrmoisdedureetravail-nombredemoisdechomage)*26)-nombredejoursdecotisation;

                }

        return nbrjourdetravail; // Par défaut, retournez 0 si aucune condition n'est satisfaite.
    }


    public int calculerSalaireDerniers96MoisTravail(Employé employé) throws SQLException {
        List<Salaires> salaireEmployer = GetListesalaires(employé);
        List<Salaires> moisDeChomage = moisdechomage(employé);
        List<Salaires> salairesnetlistesanschomagertcotisation =GetListesalairesexceptionsmoischomageetcotisation(employé);
        int salaireTotal = 0;
        int moisTravail = 0;
        int NBRMoisRest = 96;
        int moisIgnorés = 0;
        int moisDiff=0;
        java.util.Date currentDate = Calendar.getInstance().getTime();  // Date actuelle

        // Triez la liste par ordre décroissant de date de salaire.
        Collections.sort(salairesnetlistesanschomagertcotisation, new Comparator<Salaires>() {
            @Override
            public int compare(Salaires s1, Salaires s2) {
                return s2.getDateSalaire().compareTo(s1.getDateSalaire());
            }
        });

        if (StatutDeTravail.EMPLOYE.equals(employé.getStatutTravail())){
     Calendar cal1 = new GregorianCalendar();
     cal1.setTime(currentDate);
     Calendar cal2 = new GregorianCalendar();
     cal2.setTime(salairesnetlistesanschomagertcotisation.get(0).getDateSalaire());
      moisDiff = (( cal1.get(Calendar.YEAR)-cal2.get(Calendar.YEAR) ) * 12) +
             ( cal1.get(Calendar.MONTH)-cal2.get(Calendar.MONTH) );
      if (moisDiff > 96){
          moisDiff =96;
          NBRMoisRest =0 ;
      }else {
          NBRMoisRest -= moisDiff ;
      }



     // Ajouter le salaire mensuel estimé pour les mois de travail
     salaireTotal += (salairesnetlistesanschomagertcotisation.get(0).getSalaire() * moisDiff);
 }
        while ( NBRMoisRest >0){
          for (int i = 0; i < salairesnetlistesanschomagertcotisation.size(); i++) {


              Salaires salaire = salairesnetlistesanschomagertcotisation.get(i);
              Salaires auciensalaire = salairesnetlistesanschomagertcotisation.get(i+1);

              int salaireMensuel = auciensalaire.getSalaire();


              if (i + 1 < salairesnetlistesanschomagertcotisation.size()) {

                  // Calculer la durée en mois entre deux déclarations de salaire
                  Calendar cal1 = new GregorianCalendar();
                  cal1.setTime(salaire.getDateSalaire());

                  Calendar cal2 = new GregorianCalendar();
                  cal2.setTime(salairesnetlistesanschomagertcotisation.get(i + 1).getDateSalaire());

                  moisDiff = (( cal1.get(Calendar.YEAR)-cal2.get(Calendar.YEAR) ) * 12) +
                          ( cal1.get(Calendar.MONTH)-cal2.get(Calendar.MONTH) );

                  // Estimer les mois de travail entre les déclarations
                  NBRMoisRest -= moisDiff ;  // Soustraire le mois actuel



                  // Ajouter le salaire mensuel estimé pour les mois de travail
                  salaireTotal += (salaireMensuel * moisDiff);


              }
          }
        }


        return salaireTotal;
    }
    public Retraités calculderetraite(Employé employé) throws SQLException {
        int salairdes96mois =calculerSalaireDerniers96MoisTravail(employé);
        int nbrdesjoursdetravail = calculernombrejoursdetravaille(employé);
        int pourcentageajouter =0;
        float moyennedesalaire =salairdes96mois/96;
        if (nbrdesjoursdetravail>3240){
            pourcentageajouter = (nbrdesjoursdetravail-3240)/216;
            if (pourcentageajouter > 20){
                pourcentageajouter=20;
            }
        }
        float salaireretraite = moyennedesalaire *(50+pourcentageajouter)/100;
        if (salaireretraite >6000){
            salaireretraite =6000;
        }
        Retraités retraités =new Retraités(employé,salaireretraite);
        retraitésDAO.ajouterRetraite(retraités);
    return retraités;
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

}
