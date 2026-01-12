package com.example.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Monstruo")
public class Monstruo {

    public enum TipoMonstruo {
        OGRO, TROLL, ESPECTRO
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    
    private String nombre;
    private int vida;
    private TipoMonstruo tipo;
    private int fuerza;
    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "bosque_id")
    private Bosque bosque;


    public Monstruo() {}

    public Monstruo(String nombre, int vida, TipoMonstruo tipo, int fuerza) {
        this.nombre = nombre;
        this.vida = vida;
        this.tipo = tipo;
        this.fuerza = fuerza;
    }

    public Monstruo(int id, String nombre, int vida, TipoMonstruo tipo, int fuerza) {
        this.id = id;
        this.nombre = nombre;
        this.vida = vida;
        this.tipo = tipo;
        this.fuerza = fuerza;
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
        this.vida = Math.max(0, vida);
    }

    public TipoMonstruo getTipo() {
        return tipo;
    }

    public void setTipo(TipoMonstruo tipo) {
        this.tipo = tipo;
    }

    public int getFuerza() {
        return fuerza;
    }

    public void setFuerza(int fuerza) {
        this.fuerza = fuerza;
    }

    public Bosque getBosque() { return bosque; }
    public void setBosque(Bosque b) { this.bosque = b; }

    public void atacar(Mago mago) {
            int danio = fuerza;
            if (tipo == TipoMonstruo.OGRO) {
                danio = (int) Math.round(fuerza * 1.5);
            } else if (tipo == TipoMonstruo.TROLL) {
                danio = fuerza; // normal
            } else if (tipo == TipoMonstruo.ESPECTRO) {
                danio = (int) Math.round(fuerza * 0.8);
            }
            System.out.println(nombre + " (" + tipo + ") ataca a " + mago.getNombre() + " con " + danio + " puntos de daño");
            mago.recibirDaño(danio);
        }

        public void recibirDaño(int daño) {
            vida -= daño;
            if (vida < 0) vida = 0;
            System.out.println(nombre + " ha recibido " + daño + " puntos de daño y le quedan " + vida + " puntos de vida");
        }

        public boolean estaVivo() {
            return vida > 0;
        }

        @Override
        public String toString() {
            return "nombre: " + nombre + ", tipo: " + tipo + ", vida: " + vida + ", fuerza: " + fuerza;
        }
}
