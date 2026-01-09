package com.example;

import com.example.Monstruo.TipoMonstruo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class MonstruoController {
    private MenuConsola menu;
    private Integer editarMonstruoId = null;

    public MonstruoController(MenuConsola menu) {
        this.menu = menu;
    }

    public void menuMonstruos() {
        boolean retroceder = false;
        while (!retroceder) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║        👹 MONSTRUOS 👹            ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();
            listarMonstruos();
            System.out.println();
            System.out.println("  1. Crear Monstruo");
            System.out.println("  2. Modificar Monstruo");
            System.out.println("  3. Eliminar Monstruo");
            System.out.println("  4. Volver");
            System.out.println();
            System.out.print("Selecciona una opción: ");
            
            int opcion = menu.leerOpcion();
            
            switch (opcion) {
                case 1 -> crearMonstruo();
                case 2 -> modificarMonstruo();
                case 3 -> eliminarMonstruo();
                case 4 -> retroceder = true;
                default -> menu.mostrarError("Opción no válida");
            }
        }
    }

    private void crearMonstruo() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║     ➕ CREAR NUEVO MONSTRUO ➕      ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        
        String nombre = menu.leerTexto("Nombre del monstruo: ");
        int vida = menu.leerNumero("Vida: ");
        int fuerza = menu.leerNumero("Fuerza: ");
        
        System.out.println("Tipo de Monstruo:");
        System.out.println("  1. OGRO");
        System.out.println("  2. TROLL");
        System.out.println("  3. ESPECTRO");
        System.out.print("Selecciona el tipo: ");
        
        int tipoOpc = menu.leerOpcion();
        TipoMonstruo tipo = switch (tipoOpc) {
            case 1 -> TipoMonstruo.OGRO;
            case 2 -> TipoMonstruo.TROLL;
            case 3 -> TipoMonstruo.ESPECTRO;
            default -> TipoMonstruo.OGRO;
        };
        
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Monstruo monstruo = new Monstruo(nombre, vida, tipo, fuerza);
        em.persist(monstruo);
        
        tx.commit();
        em.close();
        
        menu.mostrarMensaje("Monstruo '" + nombre + "' creado correctamente");
        menu.pausa();
    }

    private void listarMonstruos() {
        EntityManager em = HibernateUtil.getEntityManager();
        java.util.List<Monstruo> monstruos = em.createQuery("from Monstruo", Monstruo.class).getResultList();
        em.close();
        
        if (monstruos.isEmpty()) {
            System.out.println("  [No hay monstruos registrados]");
            return;
        }
        
        System.out.println("┌────┬──────────────────┬───────┬────────┬──────────┐");
        System.out.println("│ ID │ Nombre           │ Vida  │ Fuerza │ Tipo     │");
        System.out.println("├────┼──────────────────┼───────┼────────┼──────────┤");
        for (Monstruo m : monstruos) {
            System.out.printf("│ %2d │ %-16s │ %5d │ %6d │ %-8s │%n", m.getId(), m.getNombre(), m.getVida(), m.getFuerza(), m.getTipo());
        }
        System.out.println("└────┴──────────────────┴───────┴────────┴──────────┘");
    }


    public void guardarMonstruo(EntityManager em, String nombre, int vida, TipoMonstruo tipo, int fuerza) {
        if (nombre != null && !nombre.isBlank()) {
            Monstruo monstruo = new Monstruo(nombre, vida, tipo, fuerza);
            em.persist(monstruo);
        }
    }

    public void cargarMonstruos(EntityManager em) {
        // Método para compatibilidad
    }

    private void modificarMonstruo() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║      ✏️  MODIFICAR MONSTRUO ✏️      ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        listarMonstruos();
        System.out.println();
        
        int id = menu.leerNumero("ID del monstruo a modificar: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        Monstruo mo = em.find(Monstruo.class, id);
        em.close();
        
        if (mo == null) {
            menu.mostrarError("Monstruo no encontrado");
            return;
        }
        
        limpiarConsola();
        System.out.println("Modificando: " + mo.getNombre());
        System.out.println();
        String nombre = menu.leerTexto("Nuevo nombre (Enter para mantener): ");
        int vida = menu.leerNumero("Nueva vida (0 para mantener): ");
        int fuerza = menu.leerNumero("Nueva fuerza (0 para mantener): ");
        
        em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        mo = em.find(Monstruo.class, id);
        if (!nombre.isBlank()) mo.setNombre(nombre);
        if (vida > 0) mo.setVida(vida);
        if (fuerza > 0) mo.setFuerza(fuerza);
        
        tx.commit();
        em.close();
        
        menu.mostrarMensaje("Monstruo actualizado correctamente");
        menu.pausa();
    }

    private void eliminarMonstruo() {
        limpiarConsola();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║      🗑️  ELIMINAR MONSTRUO 🗑️      ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        listarMonstruos();
        System.out.println();
        
        int id = menu.leerNumero("ID del monstruo a eliminar: ");
        
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Monstruo mo = em.find(Monstruo.class, id);
        if (mo != null) {
            String nombre = mo.getNombre();
            em.remove(mo);
            tx.commit();
            em.close();
            menu.mostrarMensaje("Monstruo '" + nombre + "' eliminado correctamente");
        } else {
            tx.rollback();
            em.close();
            menu.mostrarError("Monstruo no encontrado");
        }
        menu.pausa();
    }

    private void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
