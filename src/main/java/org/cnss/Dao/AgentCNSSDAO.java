package org.cnss.Dao;

import java.util.HashMap;

import org.cnss.Exceptions.CustomDAOException;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.sun.mail.util.MailSSLSocketFactory;
import org.cnss.DataBase.DatabaseConnection;
import org.cnss.model.AgentCNSS;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class AgentCNSSDAO {
    private Connection connection;
    private int loginAttempts = 0;
    private boolean isBlocked = false;

    public AgentCNSSDAO() {
        try {
            connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private HashMap<String, AgentCNSS> agentMap = new HashMap<>();
    public AgentCNSS Authentified(String enteredEmail, String enteredPassword, int Code_Verification) throws GeneralSecurityException, CustomDAOException {
        try {  AgentCNSS agent = getAgentByEmail(enteredEmail);
        Scanner scanner = new Scanner(System.in);
        if (isBlocked(enteredEmail)) {
            System.out.println("Votre compte est bloqué. Entrer votre message à l'administrateur pour vous débloquer.");
            String enteredObject = scanner.nextLine();
            sendMail(enteredObject, "Blocked", "fadwafoufou0@gmail.com");
            throw new CustomDAOException("Le compte est bloqué.");

        }
        if (agent != null && BCrypt.checkpw(enteredPassword, agent.getMotDePasse()) && agent.getCodeVerification() == Code_Verification) {
            if (isActive(agent.getEmail())) {
                loginAttempts = 0;

                if (isVerificationCodeValid(agent)) {
                    System.out.println("Authentification réussie.");
                    return agent;
                } else {
                    System.out.println("Votre code de vérification a expiré. Un nouveau code a été généré.");
                    generateAndSendVerificationCode(agent.getEmail());
                    return null;
                }
            } else {
                System.out.println("Votre compte est désactivé. Veuillez contacter l'administrateur.");
                return null;
            }
        } else {
            loginAttempts++;
            System.out.println("Authentification échouée.");

            if (loginAttempts >= 3) {
                isBlocked = true;
                System.out.println("Votre compte est maintenant bloqué après 3 échecs.");
                deactivateAgent(agent.getEmail());
            }
            return null;
        }

        } catch (GeneralSecurityException e) {
            throw new CustomDAOException("Erreur lors de l'authentification.", e);
        }
    }

    private boolean isVerificationCodeValid(AgentCNSS agent) {
        Timestamp expirationTime = agent.getCodeExpiration();
        if (expirationTime != null) {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            return !currentTime.after(expirationTime); // Check if current time is not after expiration time
        }
        return false;
    }

    private void generateAndSendVerificationCode(String email) throws GeneralSecurityException {
        String newVerificationCode = generateVerificationCode();
        updateVerificationCode(email, newVerificationCode);
        sendVerificationCodeByEmail(newVerificationCode);
    }

    private void updateVerificationCode(String email, String verificationCode) {
        try {
            // Calculate the expiration time (e.g., 5 minutes from now)
            long currentTimeMillis = System.currentTimeMillis();
            long expirationTimeMillis = currentTimeMillis + (5 * 60 * 1000); // 5 minutes in milliseconds
            Timestamp expirationTime = new Timestamp(expirationTimeMillis);

            String updateQuery = "UPDATE agences SET Code_Verification = ?, Code_Expiration = ? WHERE Email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, verificationCode);
            preparedStatement.setTimestamp(2, expirationTime);
            preparedStatement.setString(3, email);

            int rowsUpdated = preparedStatement.executeUpdate();

            preparedStatement.close();

            if (rowsUpdated > 0) {
                System.out.println("Verification code updated successfully.");
            } else {
                System.out.println("Failed to update verification code.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendVerificationCodeByEmail( String newVerificationCode) throws GeneralSecurityException {

        String emailSubject = "New Verification Code";
        sendMail(newVerificationCode, emailSubject, "fadwafoufou0@gmail.com");
    }
    private AgentCNSS getAgentByEmail(String email) {
        if (agentMap.containsKey(email)) {
            return agentMap.get(email);
        }

        String sql = "SELECT * FROM agences WHERE Email = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        AgentCNSS agent = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String motDePasse = resultSet.getString("Pass");
                int codeVerification = resultSet.getInt("Code_Verification");
                Timestamp CodeExpiration = resultSet.getTimestamp("Code_Expiration");
                int active = resultSet.getInt("Active");
                agent = new AgentCNSS(nom, motDePasse, email, codeVerification, CodeExpiration, active);

                agentMap.put(email, agent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return agent;
    }

    private void deactivateAgent(String email) {
        String sql = "UPDATE agences SET Active = 0 WHERE Email = ?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void activateAgent(String email) {
        String sql = "UPDATE agences SET Active = 1 WHERE Email = ?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isBlocked(String email) {
        String sql = "SELECT Active FROM agences WHERE Email = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int activeStatus = resultSet.getInt("Active");

                if (activeStatus == 0) {
                    // Agent is blocked, send email to admin
                    sendMail("hol","bonjour","fadwafoufou0@gmail.com");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }



    public static Boolean sendMail(String body, String subject, String email) throws GeneralSecurityException {
        final String username = "roumamimohammed2@gmail.com";
        final String password = "ukon cvmx cmxd jpnn";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
        properties.put("mail.smtp.starttls.enable", "true");

        // Add this line to trust all certificates (not recommended for production)
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isActive(String email) {
        String sql = "SELECT Active FROM agences WHERE Email = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isActive = false;

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int activeStatus = resultSet.getInt("Active");
                isActive = activeStatus == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return isActive;
    }


// ...

    public String ajoutAgentCnss(String Nom, String email, String password) {
        String Nomagent = null;
        try {
            AgentCNSS existingAgent = getAgentParEmail(email);

            if (existingAgent != null) {
                System.out.println("Cette adresse e-mail est déjà associée à une agence existante");
                return null;
            } else {
                String verificationCode = generateVerificationCode();
                Timestamp expirationTime = new Timestamp(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));

                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                String insertQuery = "INSERT INTO agences (Email, Pass, Nom, Active, Code_Verification, Code_Expiration) VALUES (?, ?, ?, 1, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, Nom);
                preparedStatement.setString(4, verificationCode);
                preparedStatement.setTimestamp(5, expirationTime);

                preparedStatement.executeUpdate();

                Nomagent = Nom;

                preparedStatement.close();

                String emailSubject = "Verification Code";
                String emailBody = "Your verification code is: " + verificationCode;
                sendMail(emailBody, emailSubject, "fadwafoufou0@gmail.com");
            }
        } catch (SQLException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return Nomagent;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int code = random.nextInt((max - min) + 1) + min;
        return String.valueOf(code);
    }

     public boolean updateAgentCnss( String email , String nouveauNom, String nouveauEmail) {
        try {
            AgentCNSS existingAgent = getAgentParEmail(email);

            if (existingAgent == null) {
                System.out.println("Cette Agent n'existe pas");
                return false;
            }
            String checkEmailQuery = "SELECT Email FROM agences WHERE Email=?";
            PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailQuery);
            checkEmailStatement.setString(1, email);

            ResultSet emailResultSet = checkEmailStatement.executeQuery();

            if (!emailResultSet.next()) {
                return false;
            } else {
                existingAgent = getAgentParEmail(nouveauEmail);

                if (existingAgent != null) {
                    System.out.println("Cette adresse e-mail est déjà associée à une agence existante");
                    return false;
                }else {


                    String updateQuery = "UPDATE agences SET Nom=?, Email=? WHERE  Email=? ";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, nouveauNom);
                    preparedStatement.setString(2, nouveauEmail);
                    preparedStatement.setString(3, email);

                    int rowsUpdated = preparedStatement.executeUpdate();

                    preparedStatement.close();

                    return rowsUpdated > 0;}
            } } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
    public boolean deleteAgentCnss(String email) {
        try {
            String deleteQuery = "DELETE FROM agences WHERE  Email=?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, email);

            int rowsDeleted = preparedStatement.executeUpdate();

            preparedStatement.close();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<AgentCNSS> afficherTousLesAgentsdesactiver() {
        ArrayList<AgentCNSS> agents = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM agences WHERE Active=0";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String email = resultSet.getString("Email");
                String code = resultSet.getString("Pass");
                int Code_Verification = resultSet.getInt("Code_Verification");
                Timestamp CodeExpiration=resultSet.getTimestamp("Code_Expiration");
                AgentCNSS agent = new AgentCNSS(nom,code,email,Code_Verification,CodeExpiration,1);
                agents.add(agent);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return agents;
    }
    public ArrayList<AgentCNSS> afficherTousLesAgents() {
        ArrayList<AgentCNSS> agents = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM agences";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String email = resultSet.getString("Email");
                String code = resultSet.getString("Pass");
                int Code_Verification = resultSet.getInt("Code_Verification");
                Timestamp CodeExpiration=resultSet.getTimestamp("Code_Expiration");

                AgentCNSS agent = new AgentCNSS(nom,code,email,Code_Verification,CodeExpiration,1);
                agents.add(agent);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return agents;
    }
    public AgentCNSS getAgentParEmail(String email) {
        AgentCNSS agent = null;
        try {
            String selectQuery = "SELECT * FROM agences WHERE Email=?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String code = resultSet.getString("Pass");
                Timestamp CodeExpiration=resultSet.getTimestamp("Code_Expiration");
                int codeVerification = resultSet.getInt("Code_Verification");
                agent = new AgentCNSS(nom, code, email, codeVerification,CodeExpiration,1);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return agent;
    }

}
