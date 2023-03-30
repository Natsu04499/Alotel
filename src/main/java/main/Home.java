package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Home extends JFrame {

    public Home (){
        super("Page d'accueil"); // définir le titre de la fenêtre
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);

        // Menu bar
        JMenuBar menuBar = new JMenuBar();

        // Menu "Actions"
        JMenu actionsMenu = new JMenu("Actions");

        // Ajouter une chambre item
        JMenuItem ajouterChambreItem = new JMenuItem("Ajouter une chambre");
        ajouterChambreItem.addActionListener(e -> {
            // code à exécuter lors du clic sur le menu item "Ajouter une chambre"
            Add addPage = new Add();
            addPage.setVisible(true);
        });

        // Filtrer les chambres item
        JMenuItem filtrerChambresItem = new JMenuItem("Filtrer les chambres");
        filtrerChambresItem.addActionListener(e -> {
            // code à exécuter lors du clic sur le menu item "Filtrer les chambres"
        });

        actionsMenu.add(ajouterChambreItem);
        actionsMenu.add(filtrerChambresItem);
        menuBar.add(actionsMenu);

        // Connexion à la base de données
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/alotel", "root", "");
            Statement stmt = con.createStatement();

            // Récupération des données de la table "chambre"
            ResultSet rs = stmt.executeQuery("select * from chambre");

            // Affichage des données dans la liste
            DefaultListModel<String> model = new DefaultListModel<>();
            while (rs.next()) {
                String numero = rs.getString("numero");
                String personne = rs.getString("personne");
                model.addElement("Chambre numéro : " + numero + "   /   " + personne + " personnes possible");
            }
            JList<String> list = new JList<>(model);
            // Bouton de rafraîchissement
            JButton refreshButton = new JButton("Rafraîchir");
            refreshButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Action à exécuter lors du clic sur le bouton de rafraîchissement
                    // Réexécutez votre requête pour récupérer les données les plus récentes de la table
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/alotel", "root", "");
                        Statement stmt = con.createStatement();

                        ResultSet rs = stmt.executeQuery("select * from chambre");

                        // Effacer le modèle de la liste
                        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
                        model.clear();

                        // Ajouter les nouvelles données dans le modèle de la liste
                        while (rs.next()) {
                            String numero = rs.getString("numero");
                            String personne = rs.getString("personne");
                            model.addElement("Chambre numéro : " + numero + "   /   " + personne + " personnes possible");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
//TESTlol
            menuBar.add(refreshButton);

            JScrollPane scrollPane = new JScrollPane(list); // Ajouter la liste à un JScrollPane

            JPopupMenu popupMenu = new JPopupMenu(); // Créer un menu contextuel
            JMenuItem deleteItem = new JMenuItem("Supprimer"); // Créer un item "Supprimer"
            popupMenu.add(deleteItem); // Ajouter l'item au menu contextuel

        // Ajouter un listener pour afficher le menu contextuel lorsque l'utilisateur fait un clic droit sur un élément de la liste
            list.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        int index = list.locationToIndex(e.getPoint());
                        list.setSelectedIndex(index); // Sélectionner l'élément de la liste qui a été cliqué avec le bouton droit
                        popupMenu.show(list, e.getX(), e.getY()); // Afficher le menu contextuel
                    }
                }
            });

        // Ajouter un listener pour supprimer l'élément sélectionné lors du clic sur l'item "Supprimer" du menu contextuel
            deleteItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int index = list.getSelectedIndex(); // Récupérer l'index de l'élément sélectionné dans la liste
                    if (index != -1) { // Vérifier si un élément est bien sélectionné
                        String selectedItem = list.getSelectedValue(); // Récupérer la valeur de l'élément sélectionné
                        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
                        model.remove(index); // Supprimer l'élément sélectionné du modèle de la liste

                        // Supprimer l'élément sélectionné de la base de données
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/alotel", "root", "");
                            Statement stmt = con.createStatement();

                            // Récupérer l'id de l'élément sélectionné
                            String[] parts = selectedItem.split("   /   ");
                            String numero = parts[0].replace("Chambre numéro : ", "");
                            String personne = parts[1].replace(" personnes possible", "");
                            String query = "SELECT id FROM chambre WHERE numero = '" + numero + "' AND personne = '" + personne + "'";
                            ResultSet rs = stmt.executeQuery(query);
                            rs.next();
                            int id = rs.getInt("id");

                            // Supprimer l'élément de la base de données en utilisant son id
                            String deleteQuery = "DELETE FROM chambre WHERE id = " + id;
                            stmt.executeUpdate(deleteQuery);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

            // Ajouter un listener pour ouvrir la page de détails lors d'un double-clic sur un élément de la liste
            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) { // double-clic détecté
                        String chambreInfo = list.getSelectedValue(); // extraire les informations de la chambre à partir du texte de l'élément sélectionné
                        System.out.println(chambreInfo);
                        String[] parts = chambreInfo.split("   /   ");
                        int chambreId = Integer.parseInt(parts[0].replaceAll("\\D+", ""));
                        DetailPage detailPage = new DetailPage(chambreId);
                        detailPage.setVisible(true);
                    }
                }
            });

            // Ajouter les composants à la fenêtre
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(menuBar, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER); // Ajouter le JScrollPane au centre du JPanel
            this.setContentPane(panel);

            // Fermeture de la connexion à la base de données
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main (String[] args ) {
        new Home().setVisible(true);
    }
}
