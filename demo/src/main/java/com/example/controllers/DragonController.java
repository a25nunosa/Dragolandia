package com.example.controllers;

import com.example.MenuConsola;
import com.example.entidades.Dragon;
import com.util.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class DragonController {
    private MenuConsola menu;
    private Integer editarDragonId = null;

    public DragonController(MenuConsola menu) {
        this.menu = menu;
    }

    public void menuDragones() {
        // Interactive menu moved to MenuConsola (view)
    }

    private void crearDragon() {
        // Interactive creation moved to MenuConsola
    }

    private void modificarDragon() {
        // Interactive modification moved to MenuConsola
    }

    private void eliminarDragon() {
        // Interactive deletion moved to MenuConsola
    }

    private void listarDragones() {
        // Listing moved to MenuConsola
    }

    public void guardarDragon(EntityManager em, String nombre, int intensidad, int resistencia) {
        // Deprecated: use guardarDragon(String,int,int)
        if (nombre != null && !nombre.isBlank()) {
            Dragon dragon = new Dragon(nombre, intensidad, resistencia);
            em.persist(dragon);
        }
    }

    public void cargarDragones(EntityManager em) {
        // Método para compatibilidad
    }

    /**
     * Persiste un nuevo dragón gestionando su propia transacción.
     */
    public void guardarDragon(String nombre, int intensidad, int resistencia) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            if (nombre != null && !nombre.isBlank()) {
                Dragon dragon = new Dragon(nombre, intensidad, resistencia);
                em.persist(dragon);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al guardar dragón", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Recupera la lista de dragones desde la base de datos.
     */
    public java.util.List<Dragon> obtenerDragones() {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            return em.createQuery("from Dragon", Dragon.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener dragones", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public Dragon obtenerDragonPorId(int id) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            return em.find(Dragon.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener dragón por id", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public void modificarDragonPorId(int id, String nombre, int intensidad, int resistencia) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Dragon d = em.find(Dragon.class, id);
            if (d != null) {
                if (nombre != null && !nombre.isBlank()) d.setNombre(nombre);
                if (intensidad > 0) d.setIntensidadFuego(intensidad);
                if (resistencia > 0) d.setResistencia(resistencia);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al modificar dragón", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public void eliminarDragonPorId(int id) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Dragon d = em.find(Dragon.class, id);
            if (d != null) em.remove(d);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al eliminar dragón", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Elimina todos los dragones de la base de datos.
     */
    public void eliminarTodos() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            em.createQuery("delete from Dragon").executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al eliminar todos los dragones", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    private void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
