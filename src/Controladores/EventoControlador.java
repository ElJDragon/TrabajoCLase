package Controladores;

import BD.ConexionBD;
import Modelos.Evento;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventoControlador {

    private ConexionBD conn;

    public EventoControlador() {
        conn = new ConexionBD();
    }

    public boolean crear(Evento evento) {
        String sql = "INSERT INTO EVENTOS (ID_EVE, ID_USU, TIT_EVE, DES_EVE, FEC_EVE, HOR_EVE) VALUES (?, ?, ?, ?, ?, ?)";

        try ( Connection conexion = conn.conectar();  PreparedStatement stmt = conexion.prepareStatement(sql)) {

            // Verificar que el usuario existe primero
            if (!usuarioExiste(evento.getIdUsuario())) {
                throw new SQLException("El usuario no existe en la base de datos");
            }

            stmt.setString(1, generarNuevoIdEvento());
            stmt.setString(2, evento.getIdUsuario()); // Asegúrate que esto sea USE_USU
            stmt.setString(3, evento.getTitulo());
            stmt.setString(4, evento.getDescripcion());
            stmt.setDate(5, Date.valueOf(evento.getFecha()));
            stmt.setTime(6, Time.valueOf(evento.getHora()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al crear evento: " + e.getMessage());
            return false;
        }
    }

    private boolean usuarioExiste(String useUsu) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE USE_USU = ?";
        try ( Connection conexion = conn.conectar();  PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, useUsu);
            return stmt.executeQuery().next();
        }
    }

    public List<Evento> obtenerPorUsuario(String idUsuario) {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM EVENTOS WHERE ID_USU = ? ORDER BY FEC_EVE DESC, HOR_EVE DESC";

        try ( Connection conexion = conn.conectar();  PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Evento evento = new Evento();
                evento.setIdEvento(rs.getString("ID_EVE"));
                evento.setIdUsuario(rs.getString("ID_USU"));
                evento.setTitulo(rs.getString("TIT_EVE"));
                evento.setDescripcion(rs.getString("DES_EVE"));
                evento.setFecha(rs.getDate("FEC_EVE").toLocalDate());
                evento.setHora(rs.getTime("HOR_EVE").toLocalTime());
                eventos.add(evento);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener eventos por usuario: " + e.getMessage());
        }
        return eventos;
    }

    public String generarNuevoIdEvento() {
        String sql = "SELECT MAX(ID_EVE) AS ultimo_id FROM EVENTOS";

        try ( Connection conexion = conn.conectar();  Statement stmt = conexion.createStatement();  ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String ultimoId = rs.getString("ultimo_id");
                if (ultimoId != null) {
                    int numero = Integer.parseInt(ultimoId.substring(2));
                    return "EV" + (numero + 1);
                }
            }
            return "EV1";
        } catch (SQLException | NumberFormatException e) {
            System.err.println("Error al generar nuevo ID: " + e.getMessage());
            return "EV" + System.currentTimeMillis();
        }
    }

    public boolean eliminar(String idEvento) {
        String sql = "DELETE FROM EVENTOS WHERE ID_EVE = ?";

        try ( Connection conexion = conn.conectar();  PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, idEvento);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar evento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Evento evento) {
        String sql = "UPDATE EVENTOS SET TIT_EVE = ?, DES_EVE = ?, FEC_EVE = ?, HOR_EVE = ? WHERE ID_EVE = ?";

        try ( Connection conexion = conn.conectar();  PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, evento.getTitulo());
            stmt.setString(2, evento.getDescripcion());
            stmt.setDate(3, Date.valueOf(evento.getFecha()));
            stmt.setTime(4, Time.valueOf(evento.getHora()));
            stmt.setString(5, evento.getIdEvento());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar evento: " + e.getMessage());
            return false;
        }
    }
}

