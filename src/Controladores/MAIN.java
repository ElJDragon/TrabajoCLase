/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Vistas.Login_VIEW;

/**
 *
 * @author User
 */
public class MAIN {
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            Login_VIEW login = new Login_VIEW();
            LoginControlador con = new LoginControlador(login);
            login.setVisible(true);
        });
    }
}
