package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class ReservationPage extends JFrame {
    private JPanel panel;
    private JLabel nomLabel;
    private JLabel prenomLabel;
    private JLabel mailLabel;
    private JLabel telephoneLabel;
    private JLabel debutLabel;
    private JLabel finLabel;
    private JLabel prixLabel;
    private JTextField nomField;
    private JTextField prenomField;
    private JTextField mailField;
    private JTextField telephoneField;
    private JTextField debutField;
    private JTextField finField;
    private JTextField prixField;
    private JButton reserverButton;

    private int chambreId;

    public ReservationPage(int chambreId) {
        this.chambreId = chambreId;

        this.setSize(400, 300);
        this.setTitle("Réservation");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        panel = new JPanel(new GridLayout(8, 2));

        nomLabel = new JLabel("Nom : ");
        panel.add(nomLabel);
        nomField = new JTextField();
        panel.add(nomField);

        prenomLabel = new JLabel("Prénom : ");
        panel.add(prenomLabel);
        prenomField = new JTextField();
        panel.add(prenomField);

        mailLabel = new JLabel("Mail : ");
        panel.add(mailLabel);
        mailField = new JTextField();
        panel.add(mailField);

        telephoneLabel = new JLabel("Téléphone : ");
        panel.add(telephoneLabel);
        telephoneField = new JTextField();
        panel.add(telephoneField);

        debutLabel = new JLabel("Date de début : ");
        panel.add(debutLabel);
        debutField = new JTextField();
        panel.add(debutField);

        finLabel = new JLabel("Date de fin : ");
        panel.add(finLabel);
        finField = new JTextField();
        panel.add(finField);

        prixLabel = new JLabel("Prix : ");
        panel.add(prixLabel);
        prixField = new JTextField();
        panel.add(prixField);

        reserverButton = new JButton("Réserver");
        reserverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reserver();
            }
        });
        panel.add(reserverButton);

        this.add(panel);
    }

    private void reserver() {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String mail = mailField.getText();
        String telephone = telephoneField.getText();
        Date debut = null;
        Date fin = null;
        int prix = 0;

        // Vérification et conversion des champs de dates
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            debut = df.parse(debutField.getText());
            fin = df.parse(finField.getText());
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez saisir une date valide (yyyy-MM-dd).");
            return;
        }

        // Vérification et conversion du champ de prix
        try {
            prix = Integer.parseInt(prixField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Veuillez saisir un prix valide.");
            return;
        }


        // Envoi du message avec l'API Twilio
        String accountSid = "AC41387735704c0228a69ee90e7bf3170b";
        String authToken = "fc14ef5d6afdc81229257c23a5d592d3";
        String twilioNumber = "+15856394150";
        String destinationNumber = telephoneField.getText();
        String debutStr = df.format(debut);
        String finStr = df.format(fin);
        String messageBody = "Bonjour, " +nom+ " " +prenom+ " votre reservation pour la chambre N°" +chambreId+ " a été effectuer avec succès le " + debutStr + " et elle prendra fin le " + finStr + " à 11h pour une somme de " +prix+ "€ par nuit";

        Twilio.init(accountSid, authToken);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber(destinationNumber),
                new com.twilio.type.PhoneNumber(twilioNumber),
                messageBody)
            .create();

        System.out.println(message.getSid());

        // Connexion à la base de données et insertion du client
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/alotel", "root", "");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO client(nom, prenom, mail, telephone, date_debut, date_fin, prix, numero_chambre) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            // Récupération des valeurs des champs
            String newnom = nomField.getText();
            String newprenom = prenomField.getText();
            String newmail = mailField.getText();
            String newtelephone = telephoneField.getText();
            String dateDebut = debutField.getText();
            String dateFin = finField.getText();
            int idChambre = chambreId;

            // Remplissage des paramètres de la requête
            stmt.setString(1, newnom);
            stmt.setString(2, newprenom);
            stmt.setString(3, newmail);
            stmt.setString(4, newtelephone);
            stmt.setString(5, dateDebut);
            stmt.setString(6, dateFin);
            stmt.setInt(7, prix);
            stmt.setInt(8, idChambre);

            // Exécution de la requête
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(null, "La réservation a été enregistrée avec succès !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de l'enregistrement de la réservation.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Une erreur est survenue lors de l'enregistrement de la réservation.");
        }
    }
}
