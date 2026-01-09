package com.example.Entidades;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BOLA_FUEGO")
public class BolaDeFuego extends Hechizo {
    public BolaDeFuego() { super("BolaDeFuego", 5); }
    public BolaDeFuego(int efecto) { super("BolaDeFuego", efecto); }

    @Override
    public void aplicar(Monstruo m) {
        if (m != null) {
            System.out.println("Bola de fuego impacta a " + m.getNombre() + " con " + efecto + " daño");
            m.recibirDaño(efecto);
        }
    }
}
