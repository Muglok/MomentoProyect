package com.dam.jesus.practica_2;

public class Contacto {

    private String telefono;
    private String nombre;

    private int id;


    public Contacto(String telefono, String nombre) {
        this.telefono = telefono;
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}