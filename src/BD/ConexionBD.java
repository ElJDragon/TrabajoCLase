package BD;
import java.sql.*;
import javax.swing.JOptionPane;

public class ConexionBD {
    Connection cc = null;

    public Connection conectar() {
        try {
            // Cargar el driver de PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Cambiar la cadena de conexión
            String url = "jdbc:postgresql://ballast.proxy.rlwy.net:37561/railway";
            String user = "postgres";
            String password = "sjpWvDMCOsqkGRdjtABZABPCLYKzpNWW";

            cc = DriverManager.getConnection(url, user, password);
            System.out.println("Conectado a PostgreSQL con éxito.");
        } catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar el driver: " + ex.getMessage());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + ex.getMessage());
        }
        return cc;
        //postgresql://postgres:@  
    }
}
