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

public class DetailPage extends JFrame {
    private JPanel panel;
    private JLabel idLabel;
    private JLabel numeroLabel;
    private JLabel personneLabel;
    private JLabel tailleLabel;
    private JLabel litLabel;
    private JLabel descriptionLabel;

    public DetailPage(int chambreId) {
        this.setSize(400, 300);
        this.setTitle("Détails de la chambre");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        panel = new JPanel(new GridLayout(6, 2));

        idLabel = new JLabel("ID : ");
        panel.add(idLabel);

        numeroLabel = new JLabel("Numéro : ");
        panel.add(numeroLabel);

        personneLabel = new JLabel("Personne : ");
        panel.add(personneLabel);

        tailleLabel = new JLabel("Taille : ");
        panel.add(tailleLabel);

        litLabel = new JLabel("Lit : ");
        panel.add(litLabel);

        descriptionLabel = new JLabel("Description : ");
        panel.add(descriptionLabel);

        // Connexion à la base de données et récupération des informations de la chambre correspondant à l'id
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/alotel", "root", "");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM chambre WHERE numero = ?")) {
            stmt.setInt(1, chambreId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idLabel.setText("ID : " + rs.getInt("id"));
                    numeroLabel.setText("Numéro : " + rs.getInt("numero"));
                    personneLabel.setText("Personne : " + rs.getInt("personne"));
                    tailleLabel.setText("Taille : " + rs.getString("taille"));
                    litLabel.setText("Lit : " + rs.getInt("lit"));
                    descriptionLabel.setText("Description : " + rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JButton modifierButton = new JButton("Modifier");
        modifierButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ModifierPage modifierPage = new ModifierPage(chambreId);
                modifierPage.setVisible(true);
                dispose();
            }
        });
        panel.add(modifierButton);


        this.add(panel);
    }
}
