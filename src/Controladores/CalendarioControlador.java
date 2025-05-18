package Controladores;

import BD.ConexionBD;
import Modelos.CalendarioModelo;
import Vistas.Agenda;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.JOptionPane;

public class CalendarioControlador {    
    private Agenda vista;
    private CalendarioModelo modelo;
    private Connection conexion;
    private String usuarioActual;

    public CalendarioControlador(Agenda vista, String usuario) {
        this.vista = vista;
        this.usuarioActual = usuario;
        this.conexion = new ConexionBD().conectar();
        
        if (this.conexion != null) {
            this.modelo = new CalendarioModelo(conexion);
            configurarListeners();
            cargarFechasGuardadas();
        } else {
            JOptionPane.showMessageDialog(vista, 
                "Error al conectar con la base de datos", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void configurarListeners() {
    vista.setAgregarEventListener(() -> {
        LocalDate fecha = vista.getFechaSeleccionada();
        String titulo = vista.getTituloEvento();
        String descripcion = vista.getDescripcionEvento();
        Time hora = vista.getHoraEvento();
        
        if (fecha == null || titulo.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Debe seleccionar una fecha y proporcionar un título", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (modelo.guardarEvento(usuarioActual, titulo, descripcion, fecha, hora)) {
                JOptionPane.showMessageDialog(vista, 
                    "Evento guardado correctamente", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarFechasGuardadas();
                // Nota: Para recordatorios necesitarías obtener el ID generado
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, 
                "Error al guardar evento: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}
    

    
    private void cargarFechasGuardadas() {
        try {
            List<LocalDate> fechas = modelo.obtenerFechasGuardadas(usuarioActual);
            vista.marcarFechasGuardadas(fechas);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, 
                "Error al cargar fechas: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void cerrarConexion() {
        try {
            if (conexion != null) conexion.close();
        } catch (SQLException ex) {
            System.err.println("Error al cerrar conexión: " + ex.getMessage());
        }
    }
}