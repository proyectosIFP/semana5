package com.example.aplicacinftc.Models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class User {

    private String nombre;
    private String apellidos;
    private String email;
    private String edad;
    private String foto;
    private String recordar;
    private String rol;
    private String grupo;
    private ArrayList<String> asignaturas;
    private String id;


    public User(String nombre, String apellidos, String email, String edad, String foto, String recordar, String rol, String grupo, ArrayList<String> asignaturas, String id) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.edad = edad;
        this.foto = foto;
        this.recordar = recordar;
        this.rol = rol;
        this.grupo = grupo;
        this.asignaturas = asignaturas;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getRecordar() {
        return recordar;
    }

    public void setRecordar(String recordar) {
        this.recordar = recordar;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public ArrayList<String> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(ArrayList<String> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
