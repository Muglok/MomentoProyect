package com.dam.jesus.practica_2;
import java.util.Date;

public class Momento2
{
    private int id;
    private String titulo;
    private String descripcion;
    private String cancion;
    private double latitud;
    private double longitud;
    private String fecha;
    private String hora;
    private int idUsuario;
    private int compartido;

    public Momento2(int id, String titulo, String descripcion, String cancion, double latitud, double longitud, String fecha, String hora, int idUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cancion = cancion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
        this.hora = hora;
        this.idUsuario = idUsuario;
        this.compartido = 0;
    }

    public Momento2(int id, String titulo, String descripcion, String cancion, double latitud, double longitud, String fecha, String hora, int idUsuario,int compartido) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.cancion = cancion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
        this.hora = hora;
        this.idUsuario = idUsuario;
        this.compartido = compartido;
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

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getCompartido() {
        return compartido;
    }

    public void setCompartido(int compartido) {
        this.compartido = compartido;
    }
}
