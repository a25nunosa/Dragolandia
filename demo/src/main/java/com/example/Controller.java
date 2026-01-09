package com.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Controller {

    private Vista vista;
    private MagoController magoController;
    private MonstruoController monstruoController;
    private BosqueController bosqueController;
    private DragonController dragonController;
    private Hechizo hechizo;

public Controller(Vista vista) {
        this.vista = vista;
        this.magoController = new MagoController(vista);
        this.monstruoController = new MonstruoController(vista);
        this.bosqueController = new BosqueController(vista);
        this.dragonController = new DragonController(vista);

        // al pulsar el botón GUARDAR
        vista.getBtnCrear().addActionListener(e -> guardar());

        // al pulsar el botón BATALLA
        vista.getBtnBatalla().addActionListener(e -> pelear());

        // acciones de gestión (eliminar/editar)
        vista.getBtnEliminarMago().addActionListener(e -> magoController.eliminarMago());
        vista.getBtnModificarMago().addActionListener(e -> magoController.modificarMago());
        vista.getBtnEliminarBosque().addActionListener(e -> bosqueController.eliminarBosque());
        vista.getBtnModificarBosque().addActionListener(e -> bosqueController.modificarBosque());
        vista.getBtnEliminarDragon().addActionListener(e -> dragonController.eliminarDragon());
        vista.getBtnModificarDragon().addActionListener(e -> dragonController.modificarDragon());

        // cargar inicialmente listas desde BD
        cargarListas();
    }

    private void guardar() {
        EntityManager em = HibernateUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        magoController.guardarMago(em);
        monstruoController.guardarMonstruo(em);
        dragonController.guardarDragon(em);
        bosqueController.guardarBosque(em);

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
            magoController.cargarMagos(em);
            monstruoController.cargarMonstruos(em);
            bosqueController.cargarBosques(em);
            dragonController.cargarDragones(em);
        } finally {
            em.close();
        }
    }

}


