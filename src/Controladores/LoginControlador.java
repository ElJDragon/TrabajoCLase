/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import BD.ConexionBD;
import Modelos.SesionUsuario;
import Modelos.Usuario_Model;
import Vistas.Login_VIEW;
import Vistas.MenuPrincipal;
import java.awt.Color;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.text.View;
import org.w3c.dom.UserDataHandler;

/**
 *
 * @author User
 */
public class LoginControlador implements ActionListener {

    private final Login_VIEW vista;
    private final ConexionBD conexionBD;
    private final Connection cc;

    public LoginControlador(Login_VIEW vista) {
        this.vista = vista;
        this.conexionBD = new ConexionBD();
        this.cc = conexionBD.conectar();
        agregarListeners();
        inicializarVista();
        Limpiartextos();
    }

    private void inicializarVista() {
        vista.jBtnIngresar.setActionCommand("INGRESAR");
        vista.jbtnSalir.setActionCommand("SALIR");

    }

    private void agregarListeners() {
        vista.jBtnIngresar.addActionListener(this);
        vista.jbtnSalir.addActionListener(this);
        vista.jTxtUsuario.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                bordesTextos();
            }

            @Override
            public void focusLost(FocusEvent e) {
                bordesTextos();
            }
        });
    }

    private void bordesTextos() {
        if (vista.jTxtUsuario.getText().trim().isEmpty()) {
            vista.jTxtUsuario.setBorder(new LineBorder(Color.RED, 2));

        } else {
            vista.jTxtUsuario.setBorder(new LineBorder(Color.white));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "INGRESAR":
                buscarUsuario();
                break;
            case "SALIR":
                vista.dispose();
                break;
            default:
                throw new AssertionError();
        }
    }

    private void Limpiartextos() {
        vista.jTxtUsuario.setText("");
        vista.jPswContrasenia.setText("");

    }

    private boolean buscarUsuario() {
        try {
            String user = vista.getUsername();
            String pass = vista.getPassword();
            String sql = "SELECT * FROM usuarios WHERE USE_USU = ? AND PAS_USU = ?";
            PreparedStatement ps = cc.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("USE_USU").equals(user) && rs.getString("PAS_USU").equals(pass)) {
                    Usuario_Model usuario = new Usuario_Model(
                            // o el ID si lo tienes
                            rs.getString("USE_USU"),
                            rs.getString("PAS_USU"),
                            rs.getString("NOM_USU"),
                            rs.getString("APE_USU")
                    );
                    SesionUsuario.iniciarSesion(usuario);
                    
                    JOptionPane.showMessageDialog(vista, "Bienvenido: " + usuario.getNombre());
                    MenuPrincipal menu = new MenuPrincipal();
                    menu.setVisible(true);
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showMessageDialog(vista, "Usuario o Contraseña Incorrectos");
        return false;
    }

}
