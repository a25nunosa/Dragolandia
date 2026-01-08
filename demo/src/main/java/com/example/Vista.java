package com.example;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Vista extends JFrame {

    private JTextField txtNombreMago, txtVidaMago, txtNivelMagia;
    private JTextField txtNombreMonstruo, txtVidaMonstruo, txtFuerza;
    private JTextField txtNombreBosque, txtPeligroBosque;
    private JTextField txtNombreDragon, txtIntensidadFuego, txtResistenciaDragon;
    private JComboBox<String> cbTipo;
    private JComboBox<String> cbHechizo;
    private JButton btnCrear, btnBatalla;
    private JTextArea txtResultado;

    // Pestañas y componentes para gestión
    private JTabbedPane tabs;
    private DefaultListModel<String> lmMonstruos, lmMagos, lmBosques, lmDragones;
    private JList<String> listMonstruos, listMagos, listBosques, listDragones;
    private JButton btnEliminarMonstruo, btnModificarMonstruo;
    private JButton btnEliminarMago, btnModificarMago;
    private JButton btnEliminarBosque, btnModificarBosque;
    private JButton btnEliminarDragon, btnModificarDragon;
    // Combos para batalla
    private JComboBox<String> cbSelMago, cbSelMonstruo, cbSelBosque, cbSelDragon;

    public Vista() {
        setTitle("Dragolandia");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabs = new JTabbedPane();

        JPanel panelCrear = new JPanel(new GridLayout(18, 2));

        panelCrear.add(new JLabel("Nombre Mago:"));
        txtNombreMago = new JTextField();
        panelCrear.add(txtNombreMago);

        panelCrear.add(new JLabel("Vida Mago:"));
        txtVidaMago = new JTextField();
        panelCrear.add(txtVidaMago);

        panelCrear.add(new JLabel("Hechizo:"));
        cbHechizo = new JComboBox<>(new String[]{"Rayo", "BolaDeFuego", "BolaDeNieve"});
        panelCrear.add(cbHechizo);

        panelCrear.add(new JLabel("Nivel Magia:"));
        txtNivelMagia = new JTextField();
        panelCrear.add(txtNivelMagia);

        panelCrear.add(new JLabel("Nombre Monstruo:"));
        txtNombreMonstruo = new JTextField();
        panelCrear.add(txtNombreMonstruo);

        panelCrear.add(new JLabel("Vida Monstruo:"));
        txtVidaMonstruo = new JTextField();
        panelCrear.add(txtVidaMonstruo);

        panelCrear.add(new JLabel("Tipo de Monstruo:"));
        cbTipo = new JComboBox<>(new String[]{"OGRO", "TROLL", "ESPECTRO"});
        panelCrear.add(cbTipo);

        panelCrear.add(new JLabel("Fuerza Monstruo:"));
        txtFuerza = new JTextField();
        panelCrear.add(txtFuerza);

        panelCrear.add(new JLabel("Nombre Bosque:"));
        txtNombreBosque = new JTextField();
        panelCrear.add(txtNombreBosque);

        panelCrear.add(new JLabel("Peligro Bosque:"));
        txtPeligroBosque = new JTextField();
        panelCrear.add(txtPeligroBosque);

        panelCrear.add(new JLabel("Nombre Dragon:"));
        txtNombreDragon = new JTextField();
        panelCrear.add(txtNombreDragon);

        panelCrear.add(new JLabel("Intensidad Fuego Dragon:"));
        txtIntensidadFuego = new JTextField();
        panelCrear.add(txtIntensidadFuego);

        panelCrear.add(new JLabel("Resistencia Dragon:"));
        txtResistenciaDragon = new JTextField();
        panelCrear.add(txtResistenciaDragon);

        // Lista de monstruos existentes para asignar como jefe al crear un bosque
        panelCrear.add(new JLabel("Seleccionar Jefe (monstruo existente):"));
        lmMonstruos = new DefaultListModel<>();
        listMonstruos = new JList<>(lmMonstruos);
        panelCrear.add(new JScrollPane(listMonstruos));

        btnCrear = new JButton("Guardar en BD");
        panelCrear.add(btnCrear);

        // Pestaña gestión: listas y acciones
        JPanel panelGestion = new JPanel(new GridLayout(2, 3));
        lmMagos = new DefaultListModel<>(); listMagos = new JList<>(lmMagos);
        lmBosques = new DefaultListModel<>(); listBosques = new JList<>(lmBosques);
        lmDragones = new DefaultListModel<>(); listDragones = new JList<>(lmDragones);

        panelGestion.add(new JScrollPane(listMagos));
        panelGestion.add(new JScrollPane(listBosques));
        panelGestion.add(new JScrollPane(listDragones));

        btnEliminarMago = new JButton("Eliminar Mago"); btnModificarMago = new JButton("Modificar Mago");
        btnEliminarBosque = new JButton("Eliminar Bosque"); btnModificarBosque = new JButton("Modificar Bosque");
        btnEliminarDragon = new JButton("Eliminar Dragon"); btnModificarDragon = new JButton("Modificar Dragon");

        JPanel acciones = new JPanel(new GridLayout(3,2));
        acciones.add(btnModificarMago); acciones.add(btnEliminarMago);
        acciones.add(btnModificarBosque); acciones.add(btnEliminarBosque);
        acciones.add(btnModificarDragon); acciones.add(btnEliminarDragon);

        JPanel gestionWrapper = new JPanel(new BorderLayout());
        gestionWrapper.add(panelGestion, BorderLayout.CENTER);
        gestionWrapper.add(acciones, BorderLayout.SOUTH);

        // Pestaña batalla
        JPanel panelBatalla = new JPanel(new GridLayout(5, 2));
        panelBatalla.add(new JLabel("Seleccionar Mago:")); cbSelMago = new JComboBox<>(); panelBatalla.add(cbSelMago);
        panelBatalla.add(new JLabel("Seleccionar Monstruo:")); cbSelMonstruo = new JComboBox<>(); panelBatalla.add(cbSelMonstruo);
        panelBatalla.add(new JLabel("Seleccionar Bosque:")); cbSelBosque = new JComboBox<>(); panelBatalla.add(cbSelBosque);
        panelBatalla.add(new JLabel("Seleccionar Dragon:")); cbSelDragon = new JComboBox<>(); panelBatalla.add(cbSelDragon);

        btnBatalla = new JButton("Iniciar Batalla"); panelBatalla.add(btnBatalla);

        tabs.addTab("Crear", panelCrear);
        tabs.addTab("Gestionar", gestionWrapper);
        tabs.addTab("Batalla", panelBatalla);

        add(tabs, BorderLayout.CENTER);

        txtResultado = new JTextArea();
        add(new JScrollPane(txtResultado), BorderLayout.SOUTH);
    }

    public JButton getBtnCrear() { return btnCrear; }
    public JButton getBtnBatalla() { return btnBatalla; }

    // Getters para listas y combos de gestión
    public JList<String> getListMonstruos() { return listMonstruos; }
    public JList<String> getListMagos() { return listMagos; }
    public JList<String> getListBosques() { return listBosques; }
    public JList<String> getListDragones() { return listDragones; }

    public JButton getBtnEliminarMago() { return btnEliminarMago; }
    public JButton getBtnModificarMago() { return btnModificarMago; }
    public JButton getBtnEliminarBosque() { return btnEliminarBosque; }
    public JButton getBtnModificarBosque() { return btnModificarBosque; }
    public JButton getBtnEliminarDragon() { return btnEliminarDragon; }
    public JButton getBtnModificarDragon() { return btnModificarDragon; }

    public JComboBox<String> getCbSelMago() { return cbSelMago; }
    public JComboBox<String> getCbSelMonstruo() { return cbSelMonstruo; }
    public JComboBox<String> getCbSelBosque() { return cbSelBosque; }
    public JComboBox<String> getCbSelDragon() { return cbSelDragon; }

    // Getters para los campos de creación
    public String getNombreMago() { return txtNombreMago.getText(); }
    public int getVidaMago() { return parseIntSafe(txtVidaMago.getText()); }
    public int getNivelMagia() { return parseIntSafe(txtNivelMagia.getText()); }

    public String getNombreMonstruo() { return txtNombreMonstruo.getText(); }
    public int getVidaMonstruo() { return parseIntSafe(txtVidaMonstruo.getText()); }
    public String getTipoMonstruo() { return (String) cbTipo.getSelectedItem(); }
    public int getFuerzaMonstruo() { return parseIntSafe(txtFuerza.getText()); }

    public String getNombreBosque() { return txtNombreBosque.getText(); }
    public int getPeligroBosque() { return parseIntSafe(txtPeligroBosque.getText()); }

    public String getNombreDragon() { return txtNombreDragon.getText(); }
    public int getIntensidadDragon() { return parseIntSafe(txtIntensidadFuego.getText()); }
    public int getResistenciaDragon() { return parseIntSafe(txtResistenciaDragon.getText()); }

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    public void mostrarResultado(String texto) {
        txtResultado.append(texto + "\n");
    }
}
