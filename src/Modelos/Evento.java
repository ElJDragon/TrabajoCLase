package Modelos;

import java.time.LocalDate;
import java.time.LocalTime;

public class Evento {

    private String idEvento;
    private String idUsuario;
    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private LocalTime hora;

    // Getters y Setters
    public String getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(String idEvento) {
        this.idEvento = idEvento;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public int getIdUsuarioAsInt() {
        try {
            return Integer.parseInt(this.idUsuario);
        } catch (NumberFormatException e) {
            return -1; // O manejar el error como prefieras
        }
    }
}
