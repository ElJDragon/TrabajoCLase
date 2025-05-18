/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarioModelo {
    private Connection conexion;
    
    public CalendarioModelo(Connection conexion) {
        this.conexion = conexion;
    }
    
   public boolean guardarEvento(String usuario, String titulo, 
        String descripcion, LocalDate fecha, Time hora) throws SQLException {
    String sql = "INSERT INTO eventos (ID_USU, TIT_EVE, DES_EVE, FEC_EVE, HOR_EVE) VALUES (?, ?, ?, ?, ?)";

    try (PreparedStatement pstmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        pstmt.setString(1, usuario);
        pstmt.setString(2, titulo);
        pstmt.setString(3, descripcion);
        pstmt.setDate(4, java.sql.Date.valueOf(fecha));
        pstmt.setTime(5, hora);
        pstmt.executeUpdate();
        
        // Obtener el ID generado (aunque no lo usemos directamente aquí)
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
            return rs.next(); // Retorna true si se insertó correctamente
        }
    }
}
    
    public boolean existeEvento(String usuario, LocalDate fecha) throws SQLException {
        String sql = "SELECT 1 FROM eventos WHERE ID_USU = ? AND FEC_EVE = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            pstmt.setDate(2, java.sql.Date.valueOf(fecha));
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public List<LocalDate> obtenerFechasGuardadas(String usuario) throws SQLException {
        List<LocalDate> fechas = new ArrayList<>();
        String sql = "SELECT FEC_EVE FROM eventos WHERE ID_USU = ?";
        
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.setString(1, usuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    fechas.add(rs.getDate("FEC_EVE").toLocalDate());
                }
            }
        }
        return fechas;
    }
}

    
