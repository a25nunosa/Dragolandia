package com.example;

import com.example.Monstruo.TipoMonstruo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class MonstruoController {
    private Vista vista;
    private Integer editarMonstruoId = null;

    public MonstruoController(Vista vista) {
        this.vista = vista;
    }

    public void guardarMonstruo(EntityManager em) {
        if (vista.getNombreMonstruo() != null && !vista.getNombreMonstruo().isBlank()) {
            TipoMonstruo tipo = TipoMonstruo.valueOf(vista.getTipoMonstruo());
            if (editarMonstruoId != null) {
                Monstruo mo = em.find(Monstruo.class, editarMonstruoId);
                if (mo != null) {
                    mo.setNombre(vista.getNombreMonstruo());
                    mo.setVida(vista.getVidaMonstruo());
                    mo.setTipo(tipo);
                    mo.setFuerza(vista.getFuerzaMonstruo());
                    vista.mostrarResultado("Monstruo actualizado: " + mo.getNombre());
                }
                editarMonstruoId = null;
            } else {
                Monstruo monstruo = new Monstruo(vista.getNombreMonstruo(), vista.getVidaMonstruo(), tipo, vista.getFuerzaMonstruo());
                em.persist(monstruo);
                vista.mostrarResultado("Monstruo creado: " + monstruo.getNombre());
            }
        }
    }

    public void cargarMonstruos(EntityManager em) {
        java.util.List<Monstruo> monstruos = em.createQuery("from Monstruo", Monstruo.class).getResultList();
        vista.getListMonstruos().setListData(monstruos.stream().map(m -> m.getId() + " - " + m.getNombre()).toArray(String[]::new));
        vista.getCbSelMonstruo().removeAllItems();
        monstruos.forEach(m -> vista.getCbSelMonstruo().addItem(m.getId() + " - " + m.getNombre()));
    }

    public void eliminarMonstruo() {
        String sel = vista.getListMonstruos().getSelectedValue();
        if (sel == null) {
            vista.mostrarResultado("Selecciona un monstruo para eliminar.");
            return;
        }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Monstruo mo = em.find(Monstruo.class, id);
        if (mo != null) em.remove(mo);
        tx.commit();
        em.close();
        vista.mostrarResultado("Monstruo eliminado: " + sel);
    }

    public void modificarMonstruo() {
        String sel = vista.getListMonstruos().getSelectedValue();
        if (sel == null) {
            vista.mostrarResultado("Selecciona un monstruo para modificar.");
            return;
        }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        editarMonstruoId = id;
        vista.mostrarResultado("Rellena campos y pulsa Guardar para actualizar el monstruo (id=" + id + ").");
    }

    public Integer getEditarMonstruoId() {
        return editarMonstruoId;
    }

    public void setEditarMonstruoId(Integer editarMonstruoId) {
        this.editarMonstruoId = editarMonstruoId;
    }
}
