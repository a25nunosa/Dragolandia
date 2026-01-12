package com.example.controllers;

import com.example.MenuConsola;
import com.example.entidades.Mago;
import com.util.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class MagoController {
    private MenuConsola menu;
    private Integer editarMagoId = null;

    public MagoController(MenuConsola menu) {
        this.menu = menu;
    }

    public void menuMagos() {
        // Interactive menu moved to MenuConsola (view)
    }

    // Interactive creation moved to MenuConsola

    // Interactive modification moved to MenuConsola

    // Interactive deletion moved to MenuConsola

    // Listing moved to MenuConsola

    public void guardarMago(EntityManager em, String nombre, int vida, int nivelMagia) {
        // Deprecated: use guardarMago(String, int, int)
        if (nombre != null && !nombre.isBlank()) {
            Mago mago = new Mago(nombre, vida, nivelMagia);
            em.persist(mago);
        }
    }

    public void cargarMagos(EntityManager em) {
        // Método para compatibilidad, puede ser eliminado si no se usa
    }

    /**
     * Persiste un nuevo mago en la base de datos gestionando la transacción.
     */
    public void guardarMago(String nombre, int vida, int nivelMagia) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            if (nombre != null && !nombre.isBlank()) {
                Mago mago = new Mago(nombre, vida, nivelMagia);
                em.persist(mago);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al guardar mago", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Recupera la lista de magos desde la base de datos.
     */
    public java.util.List<Mago> obtenerMagos() {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            // Asegurar que la colección 'conjuros' se inicialice dentro del EntityManager
            return em.createQuery("select distinct m from Mago m left join fetch m.conjuros", Mago.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener magos", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public Mago obtenerMagoPorId(int id) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            return em.find(Mago.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener mago por id", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public void modificarMagoPorId(int id, String nombre, int vida, int nivelMagia) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Mago m = em.find(Mago.class, id);
            if (m != null) {
                if (nombre != null && !nombre.isBlank()) m.setNombre(nombre);
                if (vida > 0) m.setVida(vida);
                if (nivelMagia > 0) m.setNivelMagia(nivelMagia);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al modificar mago", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public void eliminarMagoPorId(int id) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Mago m = em.find(Mago.class, id);
            if (m != null) em.remove(m);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al eliminar mago", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Elimina todos los magos de la base de datos.
     */
    public void eliminarTodos() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            em.createQuery("delete from Mago").executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al eliminar todos los magos", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
}
