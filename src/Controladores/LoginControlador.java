/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controladores;

import Vistas.AgendaPersonalizada;
import BD.ConexionBD;
import Modelos.SesionUsuario;
import Modelos.Usuario_Model;
import Vistas.Login_VIEW;
import Vistas.Menu_principal;
import java.awt.Color;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

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
        vista.setLocationRelativeTo(null);
        vista.jBtnIngresar.setActionCommand("INGRESAR");
        vista.jbtnSalir.setActionCommand("SALIR");
        vista.jBtnCrear.setActionCommand("CREAR");

    }

    private void agregarListeners() {
        vista.jBtnIngresar.addActionListener(this);
        vista.jbtnSalir.addActionListener(this);
        vista.jTxtUsuario.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
//                bordesTextos();
            }

            @Override
            public void focusLost(FocusEvent e) {
                bordesTextos();
            }
        });
        vista.jPswContrasenia.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
//                bordesTextos();
            }

            @Override
            public void focusLost(FocusEvent e) {
                bordesTextos();
            }
        });
        vista.jBtnCrear.addActionListener(this);

    }

    private void bordesTextos() {
        if (!vista.getUsername().isEmpty() && !vista.getPassword().isEmpty()) {
            vista.jTxtUsuario.setBorder(new LineBorder(Color.BLACK));
            vista.jPswContrasenia.setBorder(new LineBorder(Color.black));
        }
        if (vista.getUsername().isEmpty()) {
            vista.jTxtUsuario.setBorder(new LineBorder(Color.RED, 2));

        }
        if (vista.getPassword().isEmpty()) {
            vista.jPswContrasenia.setBorder(new LineBorder(Color.red, 2));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "INGRESAR":
                Usuario_Model usuario = buscarUsuarioPorCredenciales(
                        vista.getUsername(), vista.getPassword()
                );
                if (usuario != null) {
                    SesionUsuario.iniciarSesion(usuario);
                    JOptionPane.showMessageDialog(vista, "Bienvenido: " + usuario.getNombre());
                    Menu_principal menu = new Menu_principal(cc, Integer.toString(SesionUsuario.getUsuarioActual().getId()));
                    vista.dispose(); // Cerrar la vista de login
                    break;
                } else {
                    JOptionPane.showMessageDialog(vista, "Usuario o contraseña incorrectos.");
                    vista.jTxtUsuario.setBorder(new LineBorder(Color.RED, 2));
                    vista.jPswContrasenia.setBorder(new LineBorder(Color.red, 2));
                    Limpiartextos();
                    bordesTextos();

                }

                break;
            case "SALIR":
                vista.dispose();
                break;
            case "CREAR":
                mostrarFormularioRegistro();
                break;

            default:
                throw new AssertionError();
        }
    }

    private void Limpiartextos() {
        vista.jTxtUsuario.setText("");
        vista.jPswContrasenia.setText("");

    }

//    private boolean buscarUsuario() {
//        try {
//            String user = vista.getUsername();
//            String pass = vista.getPassword();
//            String sql = "SELECT * FROM usuarios WHERE USE_USU = ? AND PAS_USU = ?";
//            PreparedStatement ps = cc.prepareStatement(sql);
//            ps.setString(1, user);
//            ps.setString(2, pass);
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                if (rs.getString("USE_USU").equals(user) && rs.getString("PAS_USU").equals(pass)) {
//                    Usuario_Model usuario = new Usuario_Model(
//                            // o el ID si lo tienes
//                            rs.getString("USE_USU"),
//                            rs.getString("PAS_USU"),
//                            rs.getString("NOM_USU"),
//                            rs.getString("APE_USU")
//                    );
//                    SesionUsuario.iniciarSesion(usuario);
//                    
//                    JOptionPane.showMessageDialog(vista, "Bienvenido: " + usuario.getNombre());
//                    MenuPrincipal menu = new MenuPrincipal();
//                    menu.setVisible(true);
//                    return true;
//                }
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(LoginControlador.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        JOptionPane.showMessageDialog(vista, "Usuario o Contraseña Incorrectos");
//        return false;
//    }
    private Usuario_Model buscarUsuarioPorCredenciales(String username, String password) {
        String sql = "SELECT * FROM USUARIOS WHERE USE_USU = ? AND PAS_USU = ?";
        try (PreparedStatement ps = cc.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario_Model(
                        rs.getInt("iD_USU"),
                        rs.getString("USE_USU"),
                        rs.getString("PAS_USU"),
                        rs.getString("NOM_USU"),
                        rs.getString("APE_USU")
                );

            }

        } catch (SQLException ex) {
            Logger.getLogger(LoginControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private void mostrarFormularioRegistro() {
        Vistas.registro_View registro = new Vistas.registro_View();
        new RegistroControlador(registro);
        registro.setVisible(true);
    }
}
