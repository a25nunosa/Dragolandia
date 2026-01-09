package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class DragonController {
    private Vista vista;
    private Integer editarDragonId = null;

    public DragonController(Vista vista) {
        this.vista = vista;
    }

    public void guardarDragon(EntityManager em) {
        if (vista.getNombreDragon() != null && !vista.getNombreDragon().isBlank()) {
            if (editarDragonId != null) {
                Dragon d = em.find(Dragon.class, editarDragonId);
                if (d != null) {
                    d.setNombre(vista.getNombreDragon());
                    d.setIntensidadFuego(vista.getIntensidadDragon());
                    d.setResistencia(vista.getResistenciaDragon());
                    vista.mostrarResultado("Dragon actualizado: " + d.getNombre());
                }
                editarDragonId = null;
            } else {
                Dragon d = new Dragon(vista.getNombreDragon(), vista.getIntensidadDragon(), vista.getResistenciaDragon());
                em.persist(d);
                vista.mostrarResultado("Dragon creado: " + d.getNombre());
            }
        }
    }

    public void cargarDragones(EntityManager em) {
        java.util.List<Dragon> dragones = em.createQuery("from Dragon", Dragon.class).getResultList();
        vista.getListDragones().setListData(dragones.stream().map(d -> d.getId() + " - " + d.getNombre()).toArray(String[]::new));
        vista.getCbSelDragon().removeAllItems();
        dragones.forEach(d -> vista.getCbSelDragon().addItem(d.getId() + " - " + d.getNombre()));
    }

    public void eliminarDragon() {
        String sel = vista.getListDragones().getSelectedValue();
        if (sel == null) {
            vista.mostrarResultado("Selecciona un dragon para eliminar.");
            return;
        }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Dragon d = em.find(Dragon.class, id);
        if (d != null) em.remove(d);
        tx.commit();
        em.close();
        vista.mostrarResultado("Dragon eliminado: " + sel);
    }

    public void modificarDragon() {
        String sel = vista.getListDragones().getSelectedValue();
        if (sel == null) {
            vista.mostrarResultado("Selecciona un dragon para modificar.");
            return;
        }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        editarDragonId = id;
        vista.mostrarResultado("Rellena campos y pulsa Guardar para actualizar el dragon (id=" + id + ").");
    }

    public Integer getEditarDragonId() {
        return editarDragonId;
    }

    public void setEditarDragonId(Integer editarDragonId) {
        this.editarDragonId = editarDragonId;
    }
}
