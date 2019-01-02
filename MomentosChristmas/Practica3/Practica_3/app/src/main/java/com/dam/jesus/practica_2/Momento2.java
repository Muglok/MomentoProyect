package com.dam.jesus.practica_2;
import java.util.Date;

public class Momento2
{
    private int id;
    private String titulo;
    private String descripcion;
    private String cancion;
    private float latitud;
    private float longitud;
    private Date fechaYhora;
    private int idUsuario;

    public Momento2(int id, String titulo, String descripcion, String cancion, float latitud, float longitud, Date fechaYhora, int idUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cancion = cancion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fechaYhora = fechaYhora;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCancion() {
        return cancion;
    }

    public void setCancion(String cancion) {
        this.cancion = cancion;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public Date getFechaYhora() {
        return fechaYhora;
    }

    public void setFechaYhora(Date fechaYhora) {
        this.fechaYhora = fechaYhora;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
