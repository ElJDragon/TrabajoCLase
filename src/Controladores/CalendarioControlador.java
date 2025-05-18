/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

/**
 *
 * @author Anthony
 */
import BD.ConexionBD;
import Modelos.CalendarioModelo;
import Vistas.Agenda;
import java.sql.Connection;
import java.sql.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;
import com.toedter.calendar.JCalendar;
import java.util.List;

public class CalendarioControlador {    
    private Agenda vista;
    private CalendarioModelo modelo;
    private Connection conexion;
    private String usuarioActual = "user1";

    public CalendarioControlador(Agenda vista) {
        this.vista = vista;
        this.conexion = new ConexionBD().conectar();
        if (this.conexion != null) {
            this.modelo = new CalendarioModelo(conexion);
            configurarController();
            cargarFechasGuardadas();
        }
    }
    
    private void configurarController() {
        vista.setCalendarListener(fecha -> {
            if (fecha.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(vista, 
                    "No se pueden agendar fechas anteriores al día actual", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!fechaYaExiste(fecha)) {
                guardarFechaEnBD(fecha);
            }
        });
    }
    
    private boolean fechaYaExiste(LocalDate fecha) {
        try {
            return modelo.existeFecha(usuarioActual, fecha);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, 
                "Error al verificar fecha: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }
    
    private void guardarFechaEnBD(LocalDate fecha) {
        try {
            if (modelo.guardarFecha(usuarioActual, fecha)) {
                cargarFechasGuardadas();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, 
                "Error al guardar fecha: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
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