package com.example.aplicacinftc.Models;

public class Reunion {

    private String nombreAsig;
    private String solicitado;
    private String id;

    public Reunion(String nombreAsig, String solicitado, String id) {
        this.nombreAsig = nombreAsig;
        this.solicitado = solicitado;
        this.id = id;
    }

    public String getNombreAsig() {
        return nombreAsig;
    }

    public void setNombreAsig(String nombreAsig) {
        this.nombreAsig = nombreAsig;
    }

    public String getSolicitado() {
        return solicitado;
    }

    public void setSolicitado(String solicitado) {
        this.solicitado = solicitado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
