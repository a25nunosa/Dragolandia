package com.example;

import com.example.Monstruo.TipoMonstruo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Controller {

    private Vista vista;

    private Mago mago;
    private Monstruo monstruo;
    private Bosque bosque;
    private Hechizo hechizo;
    // estados de edición
    private Integer editarMagoId = null;
    private Integer editarMonstruoId = null;
    private Integer editarBosqueId = null;
    private Integer editarDragonId = null;

public Controller(Vista vista) {
        this.vista = vista;

        // al pulsar el botón GUARDAR
        vista.getBtnCrear().addActionListener(e -> guardar());

        // al pulsar el botón BATALLA
        vista.getBtnBatalla().addActionListener(e -> pelear());

        // acciones de gestión (eliminar/editar)
        vista.getBtnEliminarMago().addActionListener(e -> eliminarMago());
        vista.getBtnModificarMago().addActionListener(e -> modificarMago());
        vista.getBtnEliminarBosque().addActionListener(e -> eliminarBosque());
        vista.getBtnModificarBosque().addActionListener(e -> modificarBosque());
        vista.getBtnEliminarDragon().addActionListener(e -> eliminarDragon());
        vista.getBtnModificarDragon().addActionListener(e -> modificarDragon());

        // cargar inicialmente listas desde BD
        cargarListas();
    }

    private void guardar() {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // Crear o actualizar Mago si hay nombre
        if (vista.getNombreMago() != null && !vista.getNombreMago().isBlank()) {
            if (editarMagoId != null) {
                Mago m = em.find(Mago.class, editarMagoId);
                if (m != null) {
                    m.setNombre(vista.getNombreMago());
                    m.setVida(vista.getVidaMago());
                    m.setNivelMagia(vista.getNivelMagia());
                    // managed entity; changes flushed on commit
                    vista.mostrarResultado("Mago actualizado: " + m.getNombre());
                }
                editarMagoId = null;
            } else {
                mago = new Mago(vista.getNombreMago(), vista.getVidaMago(), vista.getNivelMagia());
                em.persist(mago);
                vista.mostrarResultado("Mago creado: " + mago.getNombre());
            }
        }

        // Crear o actualizar Monstruo
        if (vista.getNombreMonstruo() != null && !vista.getNombreMonstruo().isBlank()) {
            TipoMonstruo tipo = TipoMonstruo.valueOf(vista.getTipoMonstruo());
            if (editarMonstruoId != null) {
                Monstruo mo = em.find(Monstruo.class, editarMonstruoId);
                if (mo != null) {
                    mo.setNombre(vista.getNombreMonstruo());
                    mo.setVida(vista.getVidaMonstruo());
                    mo.setTipo(tipo);
                    mo.setFuerza(vista.getFuerzaMonstruo());
                    // managed entity; changes flushed on commit
                    vista.mostrarResultado("Monstruo actualizado: " + mo.getNombre());
                }
                editarMonstruoId = null;
            } else {
                monstruo = new Monstruo(vista.getNombreMonstruo(), vista.getVidaMonstruo(), tipo, vista.getFuerzaMonstruo());
                em.persist(monstruo);
                vista.mostrarResultado("Monstruo creado: " + monstruo.getNombre());
            }
        }

        // Crear o actualizar Dragon
        if (vista.getNombreDragon() != null && !vista.getNombreDragon().isBlank()) {
            if (editarDragonId != null) {
                Dragon d = em.find(Dragon.class, editarDragonId);
                if (d != null) {
                    d.setNombre(vista.getNombreDragon());
                    d.setIntensidadFuego(vista.getIntensidadDragon());
                    d.setResistencia(vista.getResistenciaDragon());
                    // managed entity; changes flushed on commit
                    vista.mostrarResultado("Dragon actualizado: " + d.getNombre());
                }
                editarDragonId = null;
            } else {
                Dragon d = new Dragon(vista.getNombreDragon(), vista.getIntensidadDragon(), vista.getResistenciaDragon());
                em.persist(d);
                vista.mostrarResultado("Dragon creado: " + d.getNombre());
            }
        }

        // Crear o actualizar Bosque (si hay nombre). Permite seleccionar jefe de la lista de monstruos
        if (vista.getNombreBosque() != null && !vista.getNombreBosque().isBlank()) {
            Monstruo jefe = null;
            // si se ha seleccionado un monstruo en la lista de creación
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
                    // managed entity; changes flushed on commit
                    vista.mostrarResultado("Bosque actualizado: " + b.getNombre());
                }
                editarBosqueId = null;
            } else {
                Bosque b = new Bosque(vista.getNombreBosque(), vista.getPeligroBosque(), jefe);
                em.persist(b);
                // si hay jefe, asociarlo (jefe está gestionado si fue obtenido con em.find)
                if (jefe != null) {
                    jefe.setBosque(b);
                }
                vista.mostrarResultado("Bosque creado: " + b.getNombre());
            }
        }

        tx.commit();
        em.close();

        cargarListas();
    }

    private void pelear() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            // obtener selecciones desde combos
            Mago m = null; Monstruo mo = null; Bosque b = null; Dragon d = null;
            try {
                String sm = (String) vista.getCbSelMago().getSelectedItem();
                if (sm != null && sm.contains(" - ")) m = em.find(Mago.class, Integer.parseInt(sm.split(" - ")[0]));
            } catch (Exception ex) {}
            try {
                String sm = (String) vista.getCbSelMonstruo().getSelectedItem();
                if (sm != null && sm.contains(" - ")) mo = em.find(Monstruo.class, Integer.parseInt(sm.split(" - ")[0]));
            } catch (Exception ex) {}
            try {
                String sm = (String) vista.getCbSelBosque().getSelectedItem();
                if (sm != null && sm.contains(" - ")) b = em.find(Bosque.class, Integer.parseInt(sm.split(" - ")[0]));
            } catch (Exception ex) {}
            try {
                String sm = (String) vista.getCbSelDragon().getSelectedItem();
                if (sm != null && sm.contains(" - ")) d = em.find(Dragon.class, Integer.parseInt(sm.split(" - ")[0]));
            } catch (Exception ex) {}

            if (m == null || mo == null) {
                vista.mostrarResultado("Selecciona al menos un mago y un monstruo para la pelea.");
                return;
            }

            // asociar dragon al bosque si se ha seleccionado
            if (b != null && d != null) {
                EntityTransaction tx2 = em.getTransaction();
                tx2.begin();
                b.setDragon(d);
                em.merge(b);
                tx2.commit();
            }

            //pelea
            vista.mostrarResultado("Inicia la pelea entre Mago: " + m.getNombre() + " y Monstruo: " + mo.getNombre());
            while (m.estaVivo() && mo.estaVivo()) {
                // lanzar primer hechizo conocido (si hay)
                if (!m.getConjuros().isEmpty()) {
                    Hechizo h = m.getConjuros().get(0);
                    m.lanzarHechizo(mo, h);
                } else {
                    vista.mostrarResultado(m.getNombre() + " no conoce hechizos. Penalty -1 vida");
                    m.recibirDaño(1);
                }
                if (!mo.estaVivo()) break;
                mo.atacar(m);
                // si hay dragon en el bosque, ataca al monstruo
                if (b != null && b.getDragon() != null) {
                    b.getDragon().exhalar(mo);
                }
            }

            if (m.estaVivo()) vista.mostrarResultado("El mago ganó la batalla."); else vista.mostrarResultado("El monstruo ganó la batalla.");
        } finally {
            em.close();
        }
    }

    private void cargarListas() {
        EntityManager em = HibernateUtil.getEntityManager();
        try {
            java.util.List<Mago> magos = em.createQuery("from Mago", Mago.class).getResultList();
            java.util.List<Monstruo> monstruos = em.createQuery("from Monstruo", Monstruo.class).getResultList();
            java.util.List<Bosque> bosques = em.createQuery("from Bosque", Bosque.class).getResultList();
            java.util.List<Dragon> dragones = em.createQuery("from Dragon", Dragon.class).getResultList();

            vista.getListMagos().setListData(magos.stream().map(m -> m.getId() + " - " + m.getNombre()).toArray(String[]::new));
            vista.getListMonstruos().setListData(monstruos.stream().map(m -> m.getId() + " - " + m.getNombre()).toArray(String[]::new));
            vista.getListBosques().setListData(bosques.stream().map(b -> b.getId() + " - " + b.getNombre()).toArray(String[]::new));
            vista.getListDragones().setListData(dragones.stream().map(d -> d.getId() + " - " + d.getNombre()).toArray(String[]::new));

            // combos para batalla
            vista.getCbSelMago().removeAllItems();
            magos.forEach(m -> vista.getCbSelMago().addItem(m.getId() + " - " + m.getNombre()));
            vista.getCbSelMonstruo().removeAllItems();
            monstruos.forEach(m -> vista.getCbSelMonstruo().addItem(m.getId() + " - " + m.getNombre()));
            vista.getCbSelBosque().removeAllItems();
            bosques.forEach(b -> vista.getCbSelBosque().addItem(b.getId() + " - " + b.getNombre()));
            vista.getCbSelDragon().removeAllItems();
            dragones.forEach(d -> vista.getCbSelDragon().addItem(d.getId() + " - " + d.getNombre()));
        } finally {
            em.close();
        }
    }

    private void eliminarMago() {
        String sel = vista.getListMagos().getSelectedValue();
        if (sel == null) { vista.mostrarResultado("Selecciona un mago para eliminar."); return; }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        EntityManager em = HibernateUtil.getEntityManager(); EntityTransaction tx = em.getTransaction(); tx.begin();
        Mago m = em.find(Mago.class, id);
        if (m != null) em.remove(m);
        tx.commit(); em.close();
        vista.mostrarResultado("Mago eliminado: " + sel);
        cargarListas();
    }

    private void modificarMago() {
        String sel = vista.getListMagos().getSelectedValue();
        if (sel == null) { vista.mostrarResultado("Selecciona un mago para modificar."); return; }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        EntityManager em = HibernateUtil.getEntityManager();
        Mago m = em.find(Mago.class, id);
        em.close();
        if (m != null) {
            // precargar campos de creación para editar
            editarMagoId = id;
            vista.mostrarResultado("Rellena campos y pulsa Guardar para actualizar el mago (id=" + id + ").");
        }
    }

    private void eliminarBosque() {
        String sel = vista.getListBosques().getSelectedValue();
        if (sel == null) { vista.mostrarResultado("Selecciona un bosque para eliminar."); return; }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        EntityManager em = HibernateUtil.getEntityManager(); EntityTransaction tx = em.getTransaction(); tx.begin();
        Bosque b = em.find(Bosque.class, id);
        if (b != null) em.remove(b);
        tx.commit(); em.close();
        vista.mostrarResultado("Bosque eliminado: " + sel);
        cargarListas();
    }

    private void modificarBosque() {
        String sel = vista.getListBosques().getSelectedValue();
        if (sel == null) { vista.mostrarResultado("Selecciona un bosque para modificar."); return; }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        editarBosqueId = id;
        vista.mostrarResultado("Rellena campos y pulsa Guardar para actualizar el bosque (id=" + id + ").");
    }

    private void eliminarDragon() {
        String sel = vista.getListDragones().getSelectedValue();
        if (sel == null) { vista.mostrarResultado("Selecciona un dragon para eliminar."); return; }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        EntityManager em = HibernateUtil.getEntityManager(); EntityTransaction tx = em.getTransaction(); tx.begin();
        Dragon d = em.find(Dragon.class, id);
        if (d != null) em.remove(d);
        tx.commit(); em.close();
        vista.mostrarResultado("Dragon eliminado: " + sel);
        cargarListas();
    }

    private void modificarDragon() {
        String sel = vista.getListDragones().getSelectedValue();
        if (sel == null) { vista.mostrarResultado("Selecciona un dragon para modificar."); return; }
        int id = Integer.parseInt(sel.split(" - ")[0]);
        editarDragonId = id;
        vista.mostrarResultado("Rellena campos y pulsa Guardar para actualizar el dragon (id=" + id + ").");
    }

    
}

