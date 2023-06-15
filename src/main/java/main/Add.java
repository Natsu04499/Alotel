package main;

import java.sql.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Add extends JFrame {

    private JPanel panel;
    private JLabel numeroLabel;
    private JTextField numeroField;
    private JLabel personneLabel;
    private JTextField personneField;
    private JLabel tailleLabel;
    private JTextField tailleField;
    private JLabel litLabel;
    private JTextField litField;
    private JLabel descriptionLabel;
    private JTextArea descriptionArea;
    private JButton enregistrerButton;

    public Add() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle("Ajouter une chambre");
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);

        // Style moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        panel = new JPanel(new GridLayout(6, 2));
        panel.setBackground(Color.DARK_GRAY); // Couleur de fond
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marge interne

        numeroLabel = new JLabel("Numéro de la chambre:");
        numeroLabel.setForeground(Color.WHITE); // Couleur du texte
        panel.add(numeroLabel);

        numeroField = new JTextField();
        panel.add(numeroField);

        personneLabel = new JLabel("Nombre de personne:");
        personneLabel.setForeground(Color.WHITE); // Couleur du texte
        panel.add(personneLabel);

        personneField = new JTextField();
        panel.add(personneField);

        tailleLabel = new JLabel("Taille:");
        tailleLabel.setForeground(Color.WHITE); // Couleur du texte
        panel.add(tailleLabel);

        tailleField = new JTextField();
        panel.add(tailleField);

        litLabel = new JLabel("Nombre de lit:");
        litLabel.setForeground(Color.WHITE); // Couleur du texte
        panel.add(litLabel);

        litField = new JTextField();
        panel.add(litField);

        descriptionLabel = new JLabel("Description:");
        descriptionLabel.setForeground(Color.WHITE); // Couleur du texte
        panel.add(descriptionLabel);

        descriptionArea = new JTextArea(5, 20);
        panel.add(descriptionArea);

        // Case vide pour faire de la place
        panel.add(new JLabel());

        enregistrerButton = new JButton("Enregistrer");
        enregistrerButton.setForeground(Color.BLACK); // Couleur du texte
        enregistrerButton.setBackground(Color.BLUE); // Couleur de fond
        enregistrerButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Police et taille du texte
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
        panel.add(enregistrerButton);

        this.add(panel);
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
            } else {
                JOptionPane.showMessageDialog(this, "Erreur : la chambre n'a pas pu être ajoutée.");
            }

            // Fermeture de la connexion
            stmt.close();
            con.close();
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : le pilote MySQL n'a pas été trouvé.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Add();
    }
}
