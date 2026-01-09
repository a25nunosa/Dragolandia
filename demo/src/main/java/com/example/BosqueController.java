package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class BosqueController {
    private Vista vista;
    private Integer editarBosqueId = null;

    public BosqueController(Vista vista) {
        this.vista = vista;
    }

    public void guardarBosque(EntityManager em) {
        if (vista.getNombreBosque() != null && !vista.getNombreBosque().isBlank()) {
            Monstruo jefe = null;
            try {
                String sel = vista.getListMonstruos().getSelectedValue();
                if (sel != null && sel.contains(" - ")) {
                    int idSel = Integer.parseInt(sel.split(" - ")[0]);
                    jefe = em.find(Monstruo.class, idSel);
                }
            } catch (Exception ex) {
                // ignorar
            }

            if (editarBosqueId != null) {
                Bosque b = em.find(Bosque.class, editarBosqueId);
                if (b != null) {
                    b.setNombre(vista.getNombreBosque());
                    b.setPeligro(vista.getPeligroBosque());
                    if (jefe != null) b.setMonstruo(jefe);
                    vista.mostrarResultado("Bosque actualizado: " + b.getNombre());
                }
                editarBosqueId = null;
            } else {
                Bosque b = new Bosque(vista.getNombreBosque(), vista.getPeligroBosque(), jefe);
                em.persist(b);
                if (jefe != null) {
                    jefe.setBosque(b);
                }
                vista.mostrarResultado("Bosque creado: " + b.getNombre());
            }
        }
    }

    public void cargarBosques(EntityManager em) {
        java.util.List<Bosque> bosques = em.createQuery("from Bosque", Bosque.class).getResultList();
        vista.getListBosques().setListData(bosques.stream().map(b -> b.getId() + " - " + b.getNombre()).toArray(String[]::new));
        vista.getCbSelBosque().removeAllItems();
        bosques.forEach(b -> vista.getCbSelBosque().addItem(b.getId() + " - " + b.getNombre()));
    }

    public void eliminarBosque() {
        String sel = vista.getListBosques().getSelectedValue();
        if (sel == null) {
            vista.mostrarResultado("Selecciona un bosque para eliminar.");
            return;
        }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Bosque b = em.find(Bosque.class, id);
        if (b != null) em.remove(b);
        tx.commit();
        em.close();
        vista.mostrarResultado("Bosque eliminado: " + sel);
    }

    public void modificarBosque() {
        String sel = vista.getListBosques().getSelectedValue();
        if (sel == null) {
            vista.mostrarResultado("Selecciona un bosque para modificar.");
            return;
        }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        editarBosqueId = id;
        vista.mostrarResultado("Rellena campos y pulsa Guardar para actualizar el bosque (id=" + id + ").");
    }

    public Integer getEditarBosqueId() {
        return editarBosqueId;
    }

    public void setEditarBosqueId(Integer editarBosqueId) {
        this.editarBosqueId = editarBosqueId;
    }
}
