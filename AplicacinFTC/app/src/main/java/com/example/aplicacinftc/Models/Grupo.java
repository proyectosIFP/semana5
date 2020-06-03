package com.example.aplicacinftc.Models;

public class Grupo {

    public String id;
    private String nombre;
    private String numero;

    public Grupo(String nombre, String numero,String id) {
        this.nombre = nombre;
        this.numero = numero;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
