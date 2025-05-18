package Modelos;

import java.util.Date;

public class Recordatorio {
    private int id;
    private String idEvento;
    private String tituloEvento;
    private Date fechaEvento;
    private Date horaEvento;
    private String descripcionEvento;
    private int minutosAntes;
    private boolean activo;
    private String usuario;
    public Recordatorio(){
        
    }
    public Recordatorio(String idEvento,  int minutosAntes, boolean activo, String usuario) {
        
        this.idEvento = idEvento;
       
        this.minutosAntes = minutosAntes;
        this.activo = activo;
        this.usuario = usuario;
    }
    public String getTituloEvento() {
        return tituloEvento;
    }

    public void setTituloEvento(String tituloEvento) {
        this.tituloEvento = tituloEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public void setHoraEvento(Date horaEvento) {
        this.horaEvento = horaEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public Date getHoraEvento() {
        return horaEvento;
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }
    public int getId() {
        return id;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public int getMinutosAntes() {
        return minutosAntes;
    }

    public boolean isActivo() {
        return activo;
    }
    public void setId(int id) {
        this.id=id;
    }

    public void setIdEvento(String evento) {
        this.idEvento=evento;
    }

    public void setMinutosAntes(int minutosAntes) {
        this. minutosAntes=minutosAntes;
    }

    public void setActivo(boolean activo) {
        this.activo=activo;
    }
    
}

