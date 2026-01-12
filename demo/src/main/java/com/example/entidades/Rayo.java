package com.example.entidades;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RAYO")
public class Rayo extends Hechizo {
    public Rayo() { super("Rayo", 7); }
    public Rayo(int efecto) { super("Rayo", efecto); }

    @Override
    public void aplicar(Monstruo m) {
        if (m != null) {
            System.out.println("Rayo golpea a " + m.getNombre() + " con " + efecto + " daño");
            m.recibirDaño(efecto);
        }
    }
}
