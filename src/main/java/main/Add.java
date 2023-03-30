package main;

import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Add extends JFrame {

    private JLabel numeroLabel = new JLabel("Numéro de la chambre:");
    private JTextField numeroField = new JTextField();

    private JLabel personneLabel = new JLabel("Nombre de personne:");
    private JTextField personneField = new JTextField();

    private JLabel tailleLabel = new JLabel("Taille:");
    private JTextField tailleField = new JTextField();

    private JLabel litLabel = new JLabel("Nombre de lit:");
    private JTextField litField = new JTextField();

    private JLabel descriptionLabel = new JLabel("Description:");
    private JTextArea descriptionArea = new JTextArea(5, 20);

    private JButton enregistrerButton = new JButton("Enregistrer");

    public Add() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Ajouter une chambre");
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(6, 2, 10, 10));

        this.add(numeroLabel);
        this.add(numeroField);

        this.add(personneLabel);
        this.add(personneField);

        this.add(tailleLabel);
        this.add(tailleField);

        this.add(litLabel);
        this.add(litField);

        this.add(descriptionLabel);
        this.add(descriptionArea);

        this.add(new JLabel()); // case vide pour faire de la place
        this.add(enregistrerButton);

        enregistrerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String numero = numeroField.getText();
                String personne = personneField.getText();
                String taille = tailleField.getText();
                String lit = litField.getText();
                String description = descriptionArea.getText();
                saveToDatabase(numero, personne, taille, lit, description);
            }
        });

        this.setVisible(true);
    }

    private void saveToDatabase(String numero, String personne, String taille, String lit, String description) {
        try {
            // Connexion à la base de données
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost/alotel", "root", "");

            // Préparation de la requête SQL
            String query = "INSERT INTO chambre (numero, personne, taille, lit, description) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, numero);
            stmt.setString(2, personne);
            stmt.setString(3, taille);
            stmt.setString(4, lit);
            stmt.setString(5, description);

            // Exécution de la requête
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "La chambre a été ajoutée avec succès.");
                this.dispose();
            }
            else {
                JOptionPane.showMessageDialog(this, "Erreur : la chambre n'a pas pu être ajoutée.");
            }

            // Fermeture de la connexion
            stmt.close();
            con.close();
        }
        catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : le pilote MySQL n'a pas été trouvé.");
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Add();
    }

}
