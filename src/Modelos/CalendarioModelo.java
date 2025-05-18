/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author Anthony
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class CalendarioModelo {
    private Connection conexion;
    
    public CalendarioModelo(Connection conexion) {
        this.conexion = conexion;
    }
    
    public boolean guardarFecha(String usuario, LocalDate fecha) throws SQLException {
        String sql = "INSERT INTO eventos (ID_EVE, ID_USU, TIT_EVE, DES_EVE, FEC_EVE, HOR_EVE) " +
                     "VALUES (?, ?, ?, ?, ?, ?)" ;       
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(2, usuario);
            pstmt.setDate(5, java.sql.Date.valueOf(fecha));
            return pstmt.executeUpdate() > 0;
        }
    }
    
    public boolean existeFecha(String usuario, LocalDate fecha) throws SQLException {
        String sql = "SELECT 1 FROM eventos WHERE ID_UDU = ? AND FEC_EVE = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(2, usuario);
            pstmt.setDate(5, java.sql.Date.valueOf(fecha));
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public List<LocalDate> obtenerFechasGuardadas(String usuario) throws SQLException {
        List<LocalDate> fechas = new ArrayList<>();
        String sql = "SELECT FEC_EVE FROM eventos  WHERE ID_USU = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(2, usuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    fechas.add(rs.getDate("fecha").toLocalDate());
                }
            }
        }
        return fechas;
    }
}


    
