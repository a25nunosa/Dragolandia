package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class BosqueController {
    private MenuConsola menu;
    private Integer editarBosqueId = null;

    public BosqueController(MenuConsola menu) {
        this.menu = menu;
    }

    public void menuBosques() {
        boolean retroceder = false;
        while (!retroceder) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║         🌲 BOSQUES 🌲             ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();
            listarBosques();
            System.out.println();
            System.out.println("  1. Crear Bosque");
            System.out.println("  2. Modificar Bosque");
            System.out.println("  3. Eliminar Bosque");
            System.out.println("  4. Volver");
            System.out.println();
            System.out.print("Selecciona una opción: ");
            
            int opcion = menu.leerOpcion();
            
            switch (opcion) {
                case 1 -> crearBosque();
                case 2 -> modificarBosque();
                case 3 -> eliminarBosque();
                case 4 -> retroceder = true;
                default -> menu.mostrarError("Opción no válida");
            }
        }
    }

    private void crearBosque() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║       ➕ CREAR NUEVO BOSQUE ➕      ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        
        String nombre = menu.leerTexto("Nombre del bosque: ");
        int peligro = menu.leerNumero("Nivel de peligro: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Bosque bosque = new Bosque(nombre, peligro, null);
        em.persist(bosque);
        
        tx.commit();
        em.close();
        
        menu.mostrarMensaje("Bosque '" + nombre + "' creado correctamente");
        menu.pausa();
    }

    private void modificarBosque() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║       ✏️  MODIFICAR BOSQUE ✏️       ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        listarBosques();
        System.out.println();
        
        int id = menu.leerNumero("ID del bosque a modificar: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        Bosque b = em.find(Bosque.class, id);
        em.close();
        
        if (b == null) {
            menu.mostrarError("Bosque no encontrado");
            return;
        }
        
        limpiarConsola();
        System.out.println("Modificando: " + b.getNombre());
        System.out.println();
        String nombre = menu.leerTexto("Nuevo nombre (Enter para mantener): ");
        int peligro = menu.leerNumero("Nuevo nivel de peligro (0 para mantener): ");
        
        em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        b = em.find(Bosque.class, id);
        if (!nombre.isBlank()) b.setNombre(nombre);
        if (peligro > 0) b.setPeligro(peligro);
        
        tx.commit();
        em.close();
        
        menu.mostrarMensaje("Bosque actualizado correctamente");
        menu.pausa();
    }

    private void eliminarBosque() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║        🗑️  ELIMINAR BOSQUE 🗑️      ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        listarBosques();
        System.out.println();
        
        int id = menu.leerNumero("ID del bosque a eliminar: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Bosque b = em.find(Bosque.class, id);
        if (b != null) {
            String nombre = b.getNombre();
            em.remove(b);
            tx.commit();
            em.close();
            menu.mostrarMensaje("Bosque '" + nombre + "' eliminado correctamente");
        } else {
            tx.rollback();
            em.close();
            menu.mostrarError("Bosque no encontrado");
        }
        menu.pausa();
    }

    private void listarBosques() {
        EntityManager em = HibernateUtil.getEntityManager();
        java.util.List<Bosque> bosques = em.createQuery("from Bosque", Bosque.class).getResultList();
        em.close();
        
        if (bosques.isEmpty()) {
            System.out.println("  [No hay bosques registrados]");
            return;
        }
        
        System.out.println("┌────┬──────────────────┬───────────┐");
        System.out.println("│ ID │ Nombre           │ Peligro   │");
        System.out.println("├────┼──────────────────┼───────────┤");
        for (Bosque b : bosques) {
            System.out.printf("│ %2d │ %-16s │ %9d │%n", b.getId(), b.getNombre(), b.getPeligro());
        }
        System.out.println("└────┴──────────────────┴───────────┘");
    }

    public void guardarBosque(EntityManager em, String nombre, int peligro) {
        if (nombre != null && !nombre.isBlank()) {
            Bosque bosque = new Bosque(nombre, peligro, null);
            em.persist(bosque);
        }
    }

    public void cargarBosques(EntityManager em) {
        // Método para compatibilidad
    }

    private void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
