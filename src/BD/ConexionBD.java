/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BD;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class ConexionBD {
    Connection cc=null;
    public Connection conectar(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            cc = DriverManager.getConnection("jdbc:mysql://localhost/cv_ape","root","");
            System.out.println("COnectado");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "ERROR"+ex.getMessage());  
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"error"+ex.getMessage());
        }
        return cc;
    }
}
