package com.example;

import java.util.Scanner;

import com.example.Monstruo.TipoMonstruo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Main {
    public static void main(String[] args) {

        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        Scanner sc = new Scanner(System.in);

        try {
            tx.begin();

            Vista vista = new Vista();
            new Controller(vista);
            vista.setVisible(true);

            // === MONSTRUO ===
            System.out.println("=== crear monstruo ===");
            System.out.print("nombre del monstruo: ");
            String nombreMonstruo = sc.nextLine();

            System.out.print("vida del monstruo: ");
            int vidaMonstruo = sc.nextInt();
            sc.nextLine(); 

            System.out.print("tipo de monstruo (puede ser: OGRO, TROLL o ESPECTRO): ");
            TipoMonstruo tipoMonstruo = null;

            while (tipoMonstruo == null) {
                try {
                    tipoMonstruo = TipoMonstruo.valueOf(sc.nextLine().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.print("ese monstruo no existe, pon uno de los enumerados: ");
                }
            }

            System.out.print("fuerza del monstruo: ");
            int fuerzaMonstruo = sc.nextInt();
            sc.nextLine();

            Monstruo monstruo = new Monstruo(nombreMonstruo, vidaMonstruo, tipoMonstruo, fuerzaMonstruo);


            // === MAGO ===
            System.out.println("=== crea tu Mago ===");
            System.out.print("nombre del mago: ");
            String nombreMago = sc.nextLine();

            System.out.print("vida del mago: ");
            int vidaMago = sc.nextInt();
            sc.nextLine();

            System.out.print("nivel de magia del mago: ");
            int nivelMagia = sc.nextInt();
            sc.nextLine();

            Mago mago = new Mago(nombreMago, vidaMago, nivelMagia);

            // --- añadir hechizos al mago ---
            Hechizo bola = new BolaDeFuego(5);
            Hechizo rayo = new Rayo(7);
            Hechizo nieve = new BolaDeNieve();
            mago.addHechizo(bola);
            mago.addHechizo(rayo);
            mago.addHechizo(nieve);



            // === BOSQUE ===
            System.out.println("=== crear bosque ===");
            System.out.print("nombre del bosque: ");
            String nombreBosque = sc.nextLine();

            System.out.print("nivel de peligro del bosque: ");
            int nivelPeligro = sc.nextInt();

            Bosque bosque = new Bosque(nombreBosque, nivelPeligro, monstruo);

            // Añadir un dragon al bosque y otro monstruo de ejemplo
            Dragon drogon = new Dragon("Drogon", 8, 30);
            bosque.setDragon(drogon);

            Monstruo otro = new Monstruo("Lurker", 12, TipoMonstruo.ESPECTRO, 4);
            bosque.addMonstruo(otro);


            // === GUARDAR EN BD ===
            em.persist(monstruo);
            em.persist(mago);
            em.persist(bosque);
            em.persist(drogon);

            tx.commit();

            System.out.println("monstruo guardado con ID: " + monstruo.getId());
            System.out.println("mago guardado con ID: " + mago.getId());
            System.out.println("bosque guardado con ID: " + bosque.getId());

            // === DEMOSTRAR HECHIZOS ===
            System.out.println("=== demostración de hechizos ===");
            Monstruo jefe = bosque.getMonstruo();
            // Mago usa hechizo conocido
            mago.lanzarHechizo(jefe, bola);
            // Mago usa hechizo desconocido (penaliza al mago)
            mago.lanzarHechizo(jefe, nieve);

            // Dragon exhala fuego al jefe
            if (bosque.getDragon() != null) {
                bosque.getDragon().exhalar(jefe);
            }

            // Pelear usando hechizo básico hasta que muera uno
            while (mago.estaVivo() && jefe.estaVivo()) {
                mago.lanzarHechizo(jefe, nieve);
                if (!jefe.estaVivo()) break;
                jefe.atacar(mago);
                System.out.println("--------------------------------");
            }

            System.out.println("========= resultado =========");
            if (mago.estaVivo()) {
                System.out.println(mago.getNombre() + " ganó la batalla (bieeen)");
            } else {
                System.out.println(jefe.getNombre() + " mató al mago (game over)");
            }

        } finally {
            if (tx.isActive()) tx.rollback();
            em.close();
            sc.close();
        }
    }
}
