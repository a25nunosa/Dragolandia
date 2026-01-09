package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class MagoController {
    private Vista vista;
    private Integer editarMagoId = null;

    public MagoController(Vista vista) {
        this.vista = vista;
    }

    public void guardarMago(EntityManager em) {
        if (vista.getNombreMago() != null && !vista.getNombreMago().isBlank()) {
            if (editarMagoId != null) {
                Mago m = em.find(Mago.class, editarMagoId);
                if (m != null) {
                    m.setNombre(vista.getNombreMago());
                    m.setVida(vista.getVidaMago());
                    m.setNivelMagia(vista.getNivelMagia());
                    vista.mostrarResultado("Mago actualizado: " + m.getNombre());
                }
                editarMagoId = null;
            } else {
                Mago mago = new Mago(vista.getNombreMago(), vista.getVidaMago(), vista.getNivelMagia());
                em.persist(mago);
                vista.mostrarResultado("Mago creado: " + mago.getNombre());
            }
        }
    }

    public void cargarMagos(EntityManager em) {
        java.util.List<Mago> magos = em.createQuery("from Mago", Mago.class).getResultList();
        vista.getListMagos().setListData(magos.stream().map(m -> m.getId() + " - " + m.getNombre()).toArray(String[]::new));
        vista.getCbSelMago().removeAllItems();
        magos.forEach(m -> vista.getCbSelMago().addItem(m.getId() + " - " + m.getNombre()));
    }

    public void eliminarMago() {
        String sel = vista.getListMagos().getSelectedValue();
        if (sel == null) {
            vista.mostrarResultado("Selecciona un mago para eliminar.");
            return;
        }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Mago m = em.find(Mago.class, id);
        if (m != null) em.remove(m);
        tx.commit();
        em.close();
        vista.mostrarResultado("Mago eliminado: " + sel);
    }

    public void modificarMago() {
        String sel = vista.getListMagos().getSelectedValue();
        if (sel == null) {
            vista.mostrarResultado("Selecciona un mago para modificar.");
            return;
        }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        EntityManager em = HibernateUtil.getEntityManager();
        Mago m = em.find(Mago.class, id);
        em.close();
        if (m != null) {
            editarMagoId = id;
            vista.mostrarResultado("Rellena campos y pulsa Guardar para actualizar el mago (id=" + id + ").");
        }
    }

    public Integer getEditarMagoId() {
        return editarMagoId;
    }

    public void setEditarMagoId(Integer editarMagoId) {
        this.editarMagoId = editarMagoId;
    }
}
