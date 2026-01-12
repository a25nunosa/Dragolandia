package com.example.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="bosque")
public class Bosque {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private int peligro;
    @jakarta.persistence.OneToOne
    @jakarta.persistence.JoinColumn(name = "jefe_id")
    private Monstruo jefeMonstruo;
    @jakarta.persistence.OneToMany(cascade = jakarta.persistence.CascadeType.ALL)
    @jakarta.persistence.JoinColumn(name = "bosque_id")
    private java.util.List<Monstruo> monstruos = new java.util.ArrayList<>();

    @jakarta.persistence.OneToOne(cascade = jakarta.persistence.CascadeType.ALL)
    @jakarta.persistence.JoinColumn(name = "dragon_id")
    private Dragon dragon;

    public Bosque () {}

     public Bosque(String nombre, int peligro, Monstruo jefeMonstruo) {

        this.nombre = nombre;
        this.peligro = peligro;
        this.jefeMonstruo = jefeMonstruo;
    }

    public Bosque(int id, String nombre, int peligro, Monstruo jefeMonstruo) {
        this.id = id;
        this.nombre = nombre;
        this.peligro = peligro;
        this.jefeMonstruo = jefeMonstruo;
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
    
    public int getPeligro() {
        return peligro;
    }

    public void setPeligro(int peligro) {
        this.peligro = peligro;
    }

     public Monstruo getJefeMonstruo() {
        return jefeMonstruo;
    }

    public void setJefeMonstruo(Monstruo jefeMonstruo) {
        this.jefeMonstruo = jefeMonstruo;
    }

    public java.util.List<Monstruo> getMonstruos() {
        return monstruos;
    }

    public void addMonstruo(Monstruo m) {
        if (m != null) {
            monstruos.add(m);
        }
    }

    public Dragon getDragon() { return dragon; }
    public void setDragon(Dragon d) { this.dragon = d; }

    public void cambiarJefe(Monstruo nuevoJefe) {
            this.jefeMonstruo = nuevoJefe;
        }
}
