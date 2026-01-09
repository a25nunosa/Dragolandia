package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class MagoController {
    private MenuConsola menu;
    private Integer editarMagoId = null;

    public MagoController(MenuConsola menu) {
        this.menu = menu;
    }

    public void menuMagos() {
        boolean retroceder = false;
        while (!retroceder) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║           🧙‍♂️ MAGOS 🧙‍♂️            ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();
            listarMagos();
            System.out.println();
            System.out.println("  1. Crear Mago");
            System.out.println("  2. Modificar Mago");
            System.out.println("  3. Eliminar Mago");
            System.out.println("  4. Volver");
            System.out.println();
            System.out.print("Selecciona una opción: ");
            
            int opcion = menu.leerOpcion();
            
            switch (opcion) {
                case 1 -> crearMago();
                case 2 -> modificarMago();
                case 3 -> eliminarMago();
                case 4 -> retroceder = true;
                default -> menu.mostrarError("Opción no válida");
            }
        }
    }

    private void crearMago() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║      ➕ CREAR NUEVO MAGO ➕        ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        
        String nombre = menu.leerTexto("Nombre del mago: ");
        int vida = menu.leerNumero("Vida: ");
        int nivelMagia = menu.leerNumero("Nivel de Magia: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Mago mago = new Mago(nombre, vida, nivelMagia);
        em.persist(mago);
        
        tx.commit();
        em.close();
        
        menu.mostrarMensaje("Mago '" + nombre + "' creado correctamente");
        menu.pausa();
    }

    private void modificarMago() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║       ✏️  MODIFICAR MAGO ✏️        ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        listarMagos();
        System.out.println();
        
        int id = menu.leerNumero("ID del mago a modificar: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        Mago m = em.find(Mago.class, id);
        em.close();
        
        if (m == null) {
            menu.mostrarError("Mago no encontrado");
            return;
        }
        
        limpiarConsola();
        System.out.println("Modificando: " + m.getNombre());
        System.out.println();
        String nombre = menu.leerTexto("Nuevo nombre (Enter para mantener): ");
        int vida = menu.leerNumero("Nueva vida (0 para mantener): ");
        int nivelMagia = menu.leerNumero("Nuevo nivel de magia (0 para mantener): ");
        
        em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        m = em.find(Mago.class, id);
        if (!nombre.isBlank()) m.setNombre(nombre);
        if (vida > 0) m.setVida(vida);
        if (nivelMagia > 0) m.setNivelMagia(nivelMagia);
        
        tx.commit();
        em.close();
        
        menu.mostrarMensaje("Mago actualizado correctamente");
        menu.pausa();
    }

    private void eliminarMago() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║       🗑️  ELIMINAR MAGO 🗑️         ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        listarMagos();
        System.out.println();
        
        int id = menu.leerNumero("ID del mago a eliminar: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Mago m = em.find(Mago.class, id);
        if (m != null) {
            String nombre = m.getNombre();
            em.remove(m);
            tx.commit();
            em.close();
            menu.mostrarMensaje("Mago '" + nombre + "' eliminado correctamente");
        } else {
            tx.rollback();
            em.close();
            menu.mostrarError("Mago no encontrado");
        }
        menu.pausa();
    }

    private void listarMagos() {
        EntityManager em = HibernateUtil.getEntityManager();
        java.util.List<Mago> magos = em.createQuery("from Mago", Mago.class).getResultList();
        em.close();
        
        if (magos.isEmpty()) {
            System.out.println("  [No hay magos registrados]");
            return;
        }
        
        System.out.println("┌────┬──────────────────┬───────┬────────────┐");
        System.out.println("│ ID │ Nombre           │ Vida  │ Magia      │");
        System.out.println("├────┼──────────────────┼───────┼────────────┤");
        for (Mago m : magos) {
            System.out.printf("│ %2d │ %-16s │ %5d │ %10d │%n", m.getId(), m.getNombre(), m.getVida(), m.getNivelMagia());
        }
        System.out.println("└────┴──────────────────┴───────┴────────────┘");
    }

    public void guardarMago(EntityManager em, String nombre, int vida, int nivelMagia) {
        if (nombre != null && !nombre.isBlank()) {
            Mago mago = new Mago(nombre, vida, nivelMagia);
            em.persist(mago);
        }
    }

    public void cargarMagos(EntityManager em) {
        // Método para compatibilidad, puede ser eliminado si no se usa
    }

    private void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
