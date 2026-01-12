package com.example.entidades;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "hechizo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_hechizo")
public abstract class Hechizo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    protected String nombreHechizo;
    protected int efecto;

    public Hechizo() {}

    public Hechizo(String nombreHechizo, int efecto) {
        this.nombreHechizo = nombreHechizo;
        this.efecto = efecto;
    }

    public int getId() { return id; }
    public String getNombreHechizo() { return nombreHechizo; }
    public int getEfecto() { return efecto; }

    public abstract void aplicar(Monstruo m);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hechizo)) return false;
        Hechizo h = (Hechizo) o;
        return nombreHechizo != null && nombreHechizo.equalsIgnoreCase(h.nombreHechizo);
    }

    @Override
    public int hashCode() {
        return nombreHechizo == null ? 0 : nombreHechizo.toLowerCase().hashCode();
    }
}
