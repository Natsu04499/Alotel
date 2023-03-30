package main;

import java.sql.*;

public class BddManager {

    public static void addUser(){
        Connection conn = null;
        Statement stmt = null;

        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/alotel?" + "user=root&password=");
            stmt = conn.createStatement();
            String request = "INSERT INTO users (`username`, `email`, `password`) VALUES('Natsu', 'natsu@gmail.com', 'azerty')";
            stmt.executeUpdate(request);

            System.out.println("------------------------------------------------------------------");
        }catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
                    System.out.println("SQLState: " + ex.getSQLState());
                    System.out.println("VendorError: " + ex.getErrorCode());
                }
            }
        }
    }

    public static boolean Connection(String u, String m, String p){
        System.out.println(u);
        System.out.println(m);
        System.out.println(p);

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean isConnected = false;

        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/alotel?" + "user=root&password=");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users WHERE username='"+u+"' AND email='"+m+"' AND password ='"+p+"'");

            while (rs.next()){
                if( m.equals(rs.getString(3))){
                    isConnected = true;
                }
            }

        }catch (SQLException ex){
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }

        return isConnected;

    }


}
