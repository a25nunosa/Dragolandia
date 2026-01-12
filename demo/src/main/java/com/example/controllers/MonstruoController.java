package com.example.controllers;

import com.example.MenuConsola;
import com.example.entidades.Monstruo;
import com.example.entidades.Monstruo.TipoMonstruo;
import com.util.HibernateUtil;

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
        System.out.println("║        CREAR NUEVO MONSTRUO        ║");
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
        
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Monstruo monstruo = new Monstruo(nombre, vida, tipo, fuerza);
            em.persist(monstruo);

            tx.commit();
            menu.mostrarMensaje("Monstruo '" + nombre + "' creado correctamente");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            menu.mostrarError("Error al crear monstruo: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        menu.pausa();
    }

    private void listarMonstruos() {
        EntityManager em = null;
        java.util.List<Monstruo> monstruos = new java.util.ArrayList<>();
        try {
            em = HibernateUtil.getEntityManager();
            monstruos = em.createQuery("from Monstruo", Monstruo.class).getResultList();
        } catch (Exception e) {
            menu.mostrarError("Error al listar monstruos: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        
        if (monstruos.isEmpty()) {
            System.out.println("  [No hay monstruos registrados]");
            return;
        }
    }


    public void guardarMonstruo(EntityManager em, String nombre, int vida, TipoMonstruo tipo, int fuerza) {
        // Deprecated: use guardarMonstruo(String, int, TipoMonstruo, int)
        if (nombre != null && !nombre.isBlank()) {
            Monstruo monstruo = new Monstruo(nombre, vida, tipo, fuerza);
            em.persist(monstruo);
        }
    }

    public void cargarMonstruos(EntityManager em) {
        // Método para compatibilidad
    }

    /**
     * Persiste un nuevo monstruo gestionando su propia transacción.
     */
    public void guardarMonstruo(String nombre, int vida, TipoMonstruo tipo, int fuerza) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            if (nombre != null && !nombre.isBlank()) {
                Monstruo monstruo = new Monstruo(nombre, vida, tipo, fuerza);
                em.persist(monstruo);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al guardar monstruo", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Recupera la lista de monstruos desde la base de datos.
     */
    public java.util.List<Monstruo> obtenerMonstruos() {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            return em.createQuery("from Monstruo", Monstruo.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener monstruos", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Recupera un monstruo por su id.
     */
    public Monstruo obtenerMonstruoPorId(int id) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            return em.find(Monstruo.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener monstruo por id", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Modifica un monstruo identificador por `id` con los nuevos valores indicados.
     */
    public void modificarMonstruoPorId(int id, String nombre, int vida, int fuerza) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Monstruo mo = em.find(Monstruo.class, id);
            if (mo != null) {
                if (nombre != null && !nombre.isBlank()) mo.setNombre(nombre);
                if (vida > 0) mo.setVida(vida);
                if (fuerza > 0) mo.setFuerza(fuerza);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al modificar monstruo", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Elimina un monstruo por su id.
     */
    public void eliminarMonstruoPorId(int id) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Monstruo mo = em.find(Monstruo.class, id);
            if (mo != null) {
                em.remove(mo);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al eliminar monstruo", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Elimina todos los monstruos de la base de datos.
     */
    public void eliminarTodos() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            em.createQuery("delete from Monstruo").executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al eliminar todos los monstruos", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    private void modificarMonstruo() {
        
        int id = menu.leerNumero("ID del monstruo a modificar: ");
        
        EntityManager em = null;
        Monstruo mo = null;
        try {
            em = HibernateUtil.getEntityManager();
            mo = em.find(Monstruo.class, id);
        } catch (Exception e) {
            menu.mostrarError("Error al buscar monstruo: " + e.getMessage());
            menu.pausa();
            return;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        
        limpiarConsola();
        System.out.println("Modificando: " + mo.getNombre());
        System.out.println();
        String nombre = menu.leerTexto("Nuevo nombre (Enter para mantener): ");
        int vida = menu.leerNumero("Nueva vida (0 para mantener): ");
        int fuerza = menu.leerNumero("Nueva fuerza (0 para mantener): ");
        
        em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            mo = em.find(Monstruo.class, id);
            if (!nombre.isBlank()) mo.setNombre(nombre);
            if (vida > 0) mo.setVida(vida);
            if (fuerza > 0) mo.setFuerza(fuerza);

            tx.commit();
            menu.mostrarMensaje("Monstruo actualizado correctamente");
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            menu.mostrarError("Error al actualizar monstruo: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        menu.pausa();
    }

    private void eliminarMonstruo() {
        
        int id = menu.leerNumero("ID del monstruo a eliminar: ");
        
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Monstruo mo = em.find(Monstruo.class, id);
            if (mo != null) {
                String nombre = mo.getNombre();
                em.remove(mo);
                tx.commit();
                menu.mostrarMensaje("Monstruo '" + nombre + "' eliminado correctamente");
            } else {
                if (tx != null && tx.isActive()) tx.rollback();
                menu.mostrarError("Monstruo no encontrado");
            }
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            menu.mostrarError("Error al eliminar monstruo: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
        menu.pausa();
    }

    private void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
