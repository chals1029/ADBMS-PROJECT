
package connectors;

import java.sql.*;
import javax.swing.JOptionPane;


public class dbconnect {
    private static String url = "jdbc:mysql://localhost:3306/palbookingsystembycharles";
    private static String user = "root";
    private static String pass = "";
    
    private static Connection connect;
    private static dbconnect dbcon;
    
    public dbconnect(){
        connect = null;
        try{
            connect = DriverManager.getConnection(url,user,pass);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    public static dbconnect getdbconnection(){
        if(dbcon == null){
            dbcon = new dbconnect();
        }
        return dbcon;
    }
    
    public static Connection getConnection(){
        return connect;
    }
}
