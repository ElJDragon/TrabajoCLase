/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Vistas.Agenda;
import Vistas.Login_VIEW;

/**
 *
 * @author User
 */
public class MAIN {
public static void main(String args[]) {
    // Configuración del look and feel...
    
    java.awt.EventQueue.invokeLater(() -> {
        // Simular usuario logueado
        String usuario = "juan";
        
        Agenda vista = new Agenda();
        CalendarioControlador controlador = new CalendarioControlador(vista, usuario);
        
        vista.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                controlador.cerrarConexion();
            }
        });
        
        vista.setVisible(true);
    });
}
}
