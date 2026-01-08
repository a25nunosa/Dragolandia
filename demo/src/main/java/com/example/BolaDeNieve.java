package com.example;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BOLA_NIEVE")
public class BolaDeNieve extends Hechizo {
    public BolaDeNieve() { super("BolaDeNieve", Integer.MAX_VALUE); }

    @Override
    public void aplicar(Monstruo m) {
        if (m != null) {
            System.out.println("Bola de nieve congela a " + m.getNombre() + ". Vida a 0.");
            m.recibirDaño(m.getVida());
        }
    }
}
