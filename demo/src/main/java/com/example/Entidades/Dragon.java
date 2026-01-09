package com.example.Entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dragon")
public class Dragon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private int intensidadFuego;
    private int resistencia;

    public Dragon() {}

    public Dragon(String nombre, int intensidadFuego, int resistencia) {
        this.nombre = nombre;
        this.intensidadFuego = Math.max(0, intensidadFuego);
        this.resistencia = Math.max(0, resistencia);
    }

    public Dragon(int id, String nombre, int intensidadFuego, int resistencia) {
        this.id = id;
        this.nombre = nombre;
        this.intensidadFuego = Math.max(0, intensidadFuego);
        this.resistencia = Math.max(0, resistencia);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getIntensidadFuego() {
        return intensidadFuego;
    }
    public void setIntensidadFuego(int intensidadFuego) {   
        this.intensidadFuego = Math.max(0, intensidadFuego);
    }
    public int getResistencia() {
        return resistencia;
    }
    public void setResistencia(int resistencia) {   
        this.resistencia = Math.max(0, resistencia);
    }   

    public void exhalar(Monstruo m) {
        if (m != null) {
            System.out.println(nombre + " exhala fuego a " + m.getNombre() + " con intensidad " + intensidadFuego);
            m.recibirDaño(intensidadFuego);
        }
    }

    public void recibirDaño(int d) {
        resistencia -= d;
        if (resistencia < 0) resistencia = 0;
    }

    public boolean estaVivo() {
        return resistencia > 0;
    }
}
