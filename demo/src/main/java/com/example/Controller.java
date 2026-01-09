package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Controller {

    private MenuConsola menu;

    public Controller(MenuConsola menu) {
        this.menu = menu;
    }

    public void pelear() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            System.out.println();
            System.out.println("Selecciona un Mago:");
            java.util.List<Mago> magos = em.createQuery("from Mago", Mago.class).getResultList();
            if (magos.isEmpty()) {
                menu.mostrarError("No hay magos registrados");
                return;
            }
            
            for (int i = 0; i < magos.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + magos.get(i).getNombre());
            }
            System.out.print("Opción: ");
            int magoOpt = menu.leerOpcion();
            if (magoOpt < 1 || magoOpt > magos.size()) {
                menu.mostrarError("Opción inválida");
                return;
            }
            Mago m = magos.get(magoOpt - 1);
            
            System.out.println();
            System.out.println("Selecciona un Monstruo:");
            java.util.List<Monstruo> monstruos = em.createQuery("from Monstruo", Monstruo.class).getResultList();
            if (monstruos.isEmpty()) {
                menu.mostrarError("No hay monstruos registrados");
                return;
            }
            
            for (int i = 0; i < monstruos.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + monstruos.get(i).getNombre());
            }
            System.out.print("Opción: ");
            int monOpt = menu.leerOpcion();
            if (monOpt < 1 || monOpt > monstruos.size()) {
                menu.mostrarError("Opción inválida");
                return;
            }
            Monstruo mo = monstruos.get(monOpt - 1);
            
            System.out.println();
            System.out.println("¿Deseas añadir un Dragón? (1=Sí, 0=No)");
            int dragOpt = menu.leerOpcion();
            Dragon d = null;
            Bosque b = null;
            
            if (dragOpt == 1) {
                java.util.List<Dragon> dragones = em.createQuery("from Dragon", Dragon.class).getResultList();
                java.util.List<Bosque> bosques = em.createQuery("from Bosque", Bosque.class).getResultList();
                
                if (!dragones.isEmpty() && !bosques.isEmpty()) {
                    System.out.println("Selecciona un Bosque:");
                    for (int i = 0; i < bosques.size(); i++) {
                        System.out.println("  " + (i + 1) + ". " + bosques.get(i).getNombre());
                    }
                    System.out.print("Opción: ");
                    int bosOpt = menu.leerOpcion();
                    if (bosOpt > 0 && bosOpt <= bosques.size()) {
                        b = bosques.get(bosOpt - 1);
                        
                        System.out.println("Selecciona un Dragón:");
                        for (int i = 0; i < dragones.size(); i++) {
                            System.out.println("  " + (i + 1) + ". " + dragones.get(i).getNombre());
                        }
                        System.out.print("Opción: ");
                        int drOpt = menu.leerOpcion();
                        if (drOpt > 0 && drOpt <= dragones.size()) {
                            d = dragones.get(drOpt - 1);
                            EntityTransaction tx = em.getTransaction();
                            tx.begin();
                            b.setDragon(d);
                            em.merge(b);
                            tx.commit();
                        }
                    }
                }
            }
            
            // Batalla
            System.out.println();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║        ⚔️ BATALLA INICIADA ⚔️      ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println("Mago: " + m.getNombre() + " (Vida: " + m.getVida() + ")");
            System.out.println("Monstruo: " + mo.getNombre() + " (Vida: " + mo.getVida() + ")");
            if (d != null) System.out.println("Dragón: " + d.getNombre());
            System.out.println();
            
            while (m.estaVivo() && mo.estaVivo()) {
                // Mago ataca
                if (!m.getConjuros().isEmpty()) {
                    Hechizo h = m.getConjuros().get(0);
                    m.lanzarHechizo(mo, h);
                    System.out.println(m.getNombre() + " lanza " + h.getNombreHechizo() + " a " + mo.getNombre());
                } else {
                    System.out.println(m.getNombre() + " no tiene hechizos y pierde 1 vida");
                    m.recibirDaño(1);
                }
                System.out.println("  " + mo.getNombre() + " vida: " + Math.max(0, mo.getVida()));
                
                if (!mo.estaVivo()) break;
                
                // Monstruo ataca
                mo.atacar(m);
                System.out.println(mo.getNombre() + " ataca a " + m.getNombre());
                System.out.println("  " + m.getNombre() + " vida: " + Math.max(0, m.getVida()));
                
                // Dragón ataca
                if (b != null && b.getDragon() != null) {
                    b.getDragon().exhalar(mo);
                    System.out.println("  " + mo.getNombre() + " vida: " + Math.max(0, mo.getVida()));
                }
                
                System.out.println();
            }
            
            System.out.println("╔════════════════════════════════════╗");
            if (m.estaVivo()) {
                System.out.println("║       ✨ ¡MAGO GANADOR! ✨        ║");
            } else {
                System.out.println("║    ✨ ¡MONSTRUO GANADOR! ✨      ║");
            }
            System.out.println("╚════════════════════════════════════╝");
        } finally {
            em.close();
        }
    }
}


