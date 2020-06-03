package com.example.aplicacinftc.Models;

public class Asignaturas {

    private String nombre;
    private String curso;
    private String descripcion;
    private String imagen;
    private String id;

    public Asignaturas(String nombre, String curso, String descripcion, String imagen, String id) {
        this.nombre = nombre;
        this.curso = curso;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.id = id;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
