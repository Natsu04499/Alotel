package main;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;

public class ModifierPage extends JFrame {
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
    private JTextField descriptionField;

    public ModifierPage(int chambreId) {
        this.setSize(400, 300);
        this.setTitle("Modifier la chambre");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        panel = new JPanel(new GridLayout(6, 2));

        numeroLabel = new JLabel("Numéro : ");
        panel.add(numeroLabel);

        numeroField = new JTextField();
        panel.add(numeroField);

        personneLabel = new JLabel("Personne : ");
        panel.add(personneLabel);

        personneField = new JTextField();
        panel.add(personneField);

        tailleLabel = new JLabel("Taille : ");
        panel.add(tailleLabel);

        tailleField = new JTextField();
        panel.add(tailleField);

        litLabel = new JLabel("Lit : ");
        panel.add(litLabel);

        litField = new JTextField();
        panel.add(litField);

        descriptionLabel = new JLabel("Description : ");
        panel.add(descriptionLabel);

        descriptionField = new JTextField();
        panel.add(descriptionField);

        // Connexion à la base de données et récupération des informations de la chambre correspondant à l'id
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/alotel", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM chambre WHERE numero = ?")) {
            stmt.setInt(1, chambreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    numeroField.setText(Integer.toString(rs.getInt("numero")));
                    personneField.setText(Integer.toString(rs.getInt("personne")));
                    tailleField.setText(rs.getString("taille"));
                    litField.setText(Integer.toString(rs.getInt("lit")));
                    descriptionField.setText(rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Bouton modifier
        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Récupération des nouvelles valeurs entrées par l'utilisateur
                int numero = Integer.parseInt(numeroField.getText());
                int personne = Integer.parseInt(personneField.getText());
                String taille = tailleField.getText();
                int lit = Integer.parseInt(litField.getText());
                String description = descriptionField.getText();

            // Connexion à la base de données et exécution de la requête UPDATE
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/alotel", "root", "");
                     PreparedStatement stmt = conn.prepareStatement("UPDATE chambre SET personne=?, taille=?, lit=?, description=? WHERE numero=?")) {
                    stmt.setInt(1, personne);
                    stmt.setString(2, taille);
                    stmt.setInt(3, lit);
                    stmt.setString(4, description);
                    stmt.setInt(5, numero);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        // Affichage d'un message de réussite
                        JOptionPane.showMessageDialog(null, "La chambre a été modifiée avec succès.", "Modification réussie", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        // Affichage d'un message d'erreur
                        JOptionPane.showMessageDialog(null, "La chambre n'a pas pu être modifiée.", "Modification échouée", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    // Affichage d'un message d'erreur
                    JOptionPane.showMessageDialog(null, "La chambre n'a pas pu être modifiée.", "Modification échouée", JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        panel.add(modifierButton);

        this.add(panel);
    }
}
