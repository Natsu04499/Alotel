package main;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

public class MyWindow extends JFrame
{

    private JOptionPane BoxUser = new JOptionPane();

    private JOptionPane BoxMail = new JOptionPane();

    private JOptionPane BoxPass = new JOptionPane();
    public MyWindow(String title){
        super(title);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.showBoxesAccount();

    }

    private void showBoxesAccount(){
        String User = this.BoxUser.showInputDialog("Username");
        String Mail = this.BoxMail.showInputDialog("Mail");
        String Pass = this.BoxPass.showInputDialog("Password");

        if(User != null && Mail != null && Pass != null){
            boolean isConnected = BddManager.Connection(User, Mail, Pass);
            if(isConnected == true){
                System.out.println("Connected");
                dispose();
                Home home = new Home();
                home.setVisible(true);
            }else{

                System.out.println("Error");

            }

        }
    }
    public static void main(String[] args){
        try{
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        }catch (UnsupportedLookAndFeelException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MyWindow myWindow = new MyWindow("Alotel");


        JButton add = new JButton("Add");


        myWindow.add(add);

    }
}
