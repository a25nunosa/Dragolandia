package com.example.controllers;

import com.example.MenuConsola;
import com.example.entidades.Bosque;
import com.example.entidades.Monstruo;
import com.util.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class BosqueController {
    private MenuConsola menu;
    private Integer editarBosqueId = null;

    public BosqueController(MenuConsola menu) {
        this.menu = menu;
    }
    // Interactive UI moved to MenuConsola (view). The controller exposes non-UI CRUD operations below.

    public void guardarBosque(EntityManager em, String nombre, int peligro) {
        // Deprecated: use guardarBosque(String,int)
        if (nombre != null && !nombre.isBlank()) {
            Bosque bosque = new Bosque(nombre, peligro, null);
            em.persist(bosque);
        }
    }

    public void cargarBosques(EntityManager em) {
        // Método para compatibilidad
    }

    /**
     * Persiste un nuevo bosque gestionando su propia transacción.
     */
    public void guardarBosque(String nombre, int peligro) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            if (nombre != null && !nombre.isBlank()) {
                Bosque bosque = new Bosque(nombre, peligro, null);
                em.persist(bosque);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            menu.mostrarError("Error al guardar bosque: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Recupera la lista de bosques desde la base de datos.
     */
    public java.util.List<Bosque> obtenerBosques() {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            return em.createQuery("from Bosque", Bosque.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener bosques", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Asigna un monstruo como jefe del bosque y lo persiste.
     */
    public void asignarJefe(int bosqueId, int monstruoId) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Bosque b = em.find(Bosque.class, bosqueId);
            Monstruo m = em.find(Monstruo.class, monstruoId);
            if (b != null && m != null) {
                b.setJefeMonstruo(m);
                em.merge(b);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al asignar jefe", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public Bosque obtenerBosquePorId(int id) {
        EntityManager em = null;
        try {
            em = HibernateUtil.getEntityManager();
            return em.find(Bosque.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener bosque por id", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public void modificarBosquePorId(int id, String nombre, int peligro) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Bosque b = em.find(Bosque.class, id);
            if (b != null) {
                if (nombre != null && !nombre.isBlank()) b.setNombre(nombre);
                if (peligro > 0) b.setPeligro(peligro);
                em.merge(b);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al modificar bosque", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public void eliminarBosquePorId(int id) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            Bosque b = em.find(Bosque.class, id);
            if (b != null) {
                em.remove(b);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al eliminar bosque", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    /**
     * Elimina todos los bosques de la base de datos.
     */
    public void eliminarTodos() {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = HibernateUtil.getEntityManager();
            tx = em.getTransaction();
            tx.begin();

            em.createQuery("delete from Bosque").executeUpdate();

            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                try { tx.rollback(); } catch (Exception ex) { /* ignore */ }
            }
            throw new RuntimeException("Error al eliminar todos los bosques", e);
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }
}
