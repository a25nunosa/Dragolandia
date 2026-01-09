package com.example.Controllers;

import com.example.Entidades.Dragon;
import com.example.HibernateUtil;
import com.example.MenuConsola;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class DragonController {
    private MenuConsola menu;
    private Integer editarDragonId = null;

    public DragonController(MenuConsola menu) {
        this.menu = menu;
    }

    public void menuDragones() {
        boolean retroceder = false;
        while (!retroceder) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║         🐉 DRAGONES 🐉            ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();
            listarDragones();
            System.out.println();
            System.out.println("  1. Crear Dragón");
            System.out.println("  2. Modificar Dragón");
            System.out.println("  3. Eliminar Dragón");
            System.out.println("  4. Volver");
            System.out.println();
            System.out.print("Selecciona una opción: ");
            
            int opcion = menu.leerOpcion();
            
            switch (opcion) {
                case 1 -> crearDragon();
                case 2 -> modificarDragon();
                case 3 -> eliminarDragon();
                case 4 -> retroceder = true;
                default -> menu.mostrarError("Opción no válida");
            }
        }
    }

    private void crearDragon() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║      ➕ CREAR NUEVO DRAGÓN ➕       ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        
        String nombre = menu.leerTexto("Nombre del dragón: ");
        int intensidad = menu.leerNumero("Intensidad de fuego: ");
        int resistencia = menu.leerNumero("Resistencia: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Dragon dragon = new Dragon(nombre, intensidad, resistencia);
        em.persist(dragon);
        
        tx.commit();
        em.close();
        
        menu.mostrarMensaje("Dragón '" + nombre + "' creado correctamente");
        menu.pausa();
    }

    private void modificarDragon() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║       ✏️  MODIFICAR DRAGÓN ✏️       ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        listarDragones();
        System.out.println();
        
        int id = menu.leerNumero("ID del dragón a modificar: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        Dragon d = em.find(Dragon.class, id);
        em.close();
        
        if (d == null) {
            menu.mostrarError("Dragón no encontrado");
            return;
        }
        
        limpiarConsola();
        System.out.println("Modificando: " + d.getNombre());
        System.out.println();
        String nombre = menu.leerTexto("Nuevo nombre (Enter para mantener): ");
        int intensidad = menu.leerNumero("Nueva intensidad (0 para mantener): ");
        int resistencia = menu.leerNumero("Nueva resistencia (0 para mantener): ");
        
        em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        d = em.find(Dragon.class, id);
        if (!nombre.isBlank()) d.setNombre(nombre);
        if (intensidad > 0) d.setIntensidadFuego(intensidad);
        if (resistencia > 0) d.setResistencia(resistencia);
        
        tx.commit();
        em.close();
        
        menu.mostrarMensaje("Dragón actualizado correctamente");
        menu.pausa();
    }

    private void eliminarDragon() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║        🗑️  ELIMINAR DRAGÓN 🗑️      ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        listarDragones();
        System.out.println();
        
        int id = menu.leerNumero("ID del dragón a eliminar: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Dragon d = em.find(Dragon.class, id);
        if (d != null) {
            String nombre = d.getNombre();
            em.remove(d);
            tx.commit();
            em.close();
            menu.mostrarMensaje("Dragón '" + nombre + "' eliminado correctamente");
        } else {
            tx.rollback();
            em.close();
            menu.mostrarError("Dragón no encontrado");
        }
        menu.pausa();
    }

    private void listarDragones() {
        EntityManager em = HibernateUtil.getEntityManager();
        java.util.List<Dragon> dragones = em.createQuery("from Dragon", Dragon.class).getResultList();
        em.close();
        
        if (dragones.isEmpty()) {
            System.out.println("  [No hay dragones registrados]");
            return;
        }
        
        System.out.println("┌────┬──────────────────┬────────────┬──────────────┐");
        System.out.println("│ ID │ Nombre           │ Intensidad │ Resistencia  │");
        System.out.println("├────┼──────────────────┼────────────┼──────────────┤");
        for (Dragon d : dragones) {
            System.out.printf("│ %2d │ %-16s │ %10d │ %12d │%n", d.getId(), d.getNombre(), d.getIntensidadFuego(), d.getResistencia());
        }
        System.out.println("└────┴──────────────────┴────────────┴──────────────┘");
    }

    public void guardarDragon(EntityManager em, String nombre, int intensidad, int resistencia) {
        if (nombre != null && !nombre.isBlank()) {
            Dragon dragon = new Dragon(nombre, intensidad, resistencia);
            em.persist(dragon);
        }
    }

    public void cargarDragones(EntityManager em) {
        // Método para compatibilidad
    }

    private void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
