package com.example.Entidades;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="mago")
public class Mago {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private int vida;
    private int nivelMagia;
        @jakarta.persistence.ManyToMany(cascade = {jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE})
        @jakarta.persistence.JoinTable(name = "mago_hechizo",
            joinColumns = @jakarta.persistence.JoinColumn(name = "mago_id"),
            inverseJoinColumns = @jakarta.persistence.JoinColumn(name = "hechizo_id"))
        private java.util.List<Hechizo> conjuros = new java.util.ArrayList<>();

    public Mago () {}

     public Mago(String nombre, int vida, int nivelMagia) {

        this.nombre = nombre;
        this.vida = vida;
        this.nivelMagia = nivelMagia;
    }

    public Mago(int id, String nombre, int vida, int nivelMagia) {
        this.id = id;
        this.nombre = nombre;
        this.vida = vida;
        this.nivelMagia = nivelMagia;
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

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }
    
    public int getNivelMagia() {
        return nivelMagia;
    }

    public void setNivelMagia(int nivelMagia) {
        this.nivelMagia = Math.max(0, nivelMagia);
    }

    

    public void lanzarHechizo(Monstruo monstruo, Hechizo nombreHechizo) {
        Hechizo hechizo = conjuros.stream().filter(h -> h.getNombreHechizo().equalsIgnoreCase(nombreHechizo.getNombreHechizo())).findFirst().orElse(null);
        if (hechizo != null) {
            System.out.println(nombre + " lanza el hechizo " + hechizo.getNombreHechizo() + " a " + monstruo.getNombre());
            hechizo.aplicar(monstruo);
        } else {
            System.out.println(nombreHechizo + " no es conocido por " + nombre + ". Penalización: -1 vida.");
            recibirDaño(1);
        }
    }

    public void addHechizo(Hechizo h) {
        if (h != null && conjuros.stream().noneMatch(x -> x.getNombreHechizo().equalsIgnoreCase(h.getNombreHechizo()))) {
            conjuros.add(h);
        }
    }

    public java.util.List<Hechizo> getConjuros() { return conjuros; }

    public void recibirDaño(int daño) {
        vida -= daño;
        if (vida < 0) vida = 0;
        System.out.println(nombre + " ha recibido " + daño + " puntos de daño y le quedan " + vida + " puntos de vida");
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    public void setConjuros(List<Hechizo> conjuros) {
        this.conjuros = conjuros;
    }
}
