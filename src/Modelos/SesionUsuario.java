/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author User
 */
public class SesionUsuario {
     private static Usuario_Model usuarioActual;

    public static void iniciarSesion(Usuario_Model usuario) {
        usuarioActual = usuario;
    }

    public static Usuario_Model getUsuarioActual() {
        return usuarioActual;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static boolean sesionActiva() {
        return usuarioActual != null;
    }
}
