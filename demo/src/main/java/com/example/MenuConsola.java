package com.example;

import java.util.Scanner;

import com.example.controllers.BosqueController;
import com.example.controllers.DragonController;
import com.example.controllers.MagoController;
import com.example.controllers.MonstruoController;

public class MenuConsola {
    private Scanner scanner;
    private MagoController magoController;
    private MonstruoController monstruoController;
    private BosqueController bosqueController;
    private DragonController dragonController;

    public MenuConsola() {
        this.scanner = new Scanner(System.in);
        this.magoController = new MagoController(this);
        this.monstruoController = new MonstruoController(this);
        this.bosqueController = new BosqueController(this);
        this.dragonController = new DragonController(this);
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            limpiarConsola();
            mostrarMenuPrincipal();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1 -> menuGestion();
                case 2 -> iniciarNuevaPartida();
                case 3 -> {
                    mostrarMensaje("¡Hasta luego!");
                    salir = true;
                }
                default -> mostrarError("Opción no válida");
            }
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║         DRAGOLANDIA                  ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1. Gestión de Personajes");
        System.out.println("  2. Batalla");
        System.out.println("  3. Salir");
        System.out.println();
        System.out.print("Selecciona una opción: ");
    }

    private void menuGestion() {
        boolean retroceder = false;
        while (!retroceder) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║    GESTIÓN DE PERSONAJES           ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();
            System.out.println("  1. Magos");
            System.out.println("  2. Monstruos");
            System.out.println("  3. Dragones");
            System.out.println("  4. Bosques");
            System.out.println("  5. Volver");
            System.out.println();
            System.out.print("Selecciona una opción: ");
            
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1 -> menuMagos();
                case 2 -> menuMonstruos();
                case 3 -> menuDragones();
                case 4 -> menuBosques();
                case 5 -> retroceder = true;
                default -> mostrarError("Opción no válida");
            }
        }
    }

    private void menuMonstruos() {
        boolean retroceder = false;
        while (!retroceder) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║             MONSTRUOS              ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();
            // listar
            java.util.List<com.example.entidades.Monstruo> monstruos;
            try {
                monstruos = monstruoController.obtenerMonstruos();
            } catch (Exception e) {
                mostrarError("No se pudieron obtener monstruos: " + e.getMessage());
                return;
            }
            System.out.println();
            if (monstruos.isEmpty()) {
                System.out.println("  [No hay monstruos registrados]");
            } else {
                System.out.println("┌────┬──────────────────┬───────┬────────┬──────────┐");
                System.out.println("│ ID │ Nombre           │ Vida  │ Fuerza │ Tipo     │");
                System.out.println("├────┼──────────────────┼───────┼────────┼──────────┤");
                for (com.example.entidades.Monstruo m : monstruos) {
                    System.out.printf("│ %2d │ %-16s │ %5d │ %6d │ %-8s │%n", m.getId(), m.getNombre(), m.getVida(), m.getFuerza(), m.getTipo());
                }
                System.out.println("└────┴──────────────────┴───────┴────────┴──────────┘");
            }
            System.out.println();
            System.out.println("  1. Crear Monstruo");
            System.out.println("  2. Modificar Monstruo");
            System.out.println("  3. Eliminar Monstruo");
            System.out.println("  4. Volver");
            System.out.println();
            System.out.print("Selecciona una opción: ");
            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║        CREAR NUEVO MONSTRUO        ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    String nombre = leerTexto("Nombre del monstruo: ");
                    int vida = leerNumero("Vida: ");
                    int fuerza = leerNumero("Fuerza: ");
                    System.out.println("Tipo de Monstruo:");
                    System.out.println("  1. OGRO");
                    System.out.println("  2. TROLL");
                    System.out.println("  3. ESPECTRO");
                    System.out.print("Selecciona el tipo: ");
                    int tipoOpc = leerOpcion();
                    com.example.entidades.Monstruo.TipoMonstruo tipo = switch (tipoOpc) {
                        case 1 -> com.example.entidades.Monstruo.TipoMonstruo.OGRO;
                        case 2 -> com.example.entidades.Monstruo.TipoMonstruo.TROLL;
                        case 3 -> com.example.entidades.Monstruo.TipoMonstruo.ESPECTRO;
                        default -> com.example.entidades.Monstruo.TipoMonstruo.OGRO;
                    };
                    try {
                        monstruoController.guardarMonstruo(nombre, vida, tipo, fuerza);
                        mostrarMensaje("Monstruo '" + nombre + "' creado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al crear monstruo: " + e.getMessage());
                    }
                    pausa();
                }
                case 2 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║         MODIFICAR MONSTRUO         ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    int id = leerNumero("ID del monstruo a modificar: ");
                    com.example.entidades.Monstruo existente = null;
                    try {
                        existente = monstruoController.obtenerMonstruoPorId(id);
                    } catch (Exception e) {
                        mostrarError("Error al buscar monstruo: " + e.getMessage());
                        pausa();
                        break;
                    }
                    if (existente == null) {
                        mostrarError("Monstruo no encontrado");
                        pausa();
                        break;
                    }
                    System.out.println("Modificando: " + existente.getNombre());
                    String nombre = leerTexto("Nuevo nombre (Enter para mantener): ");
                    int vida = leerNumero("Nueva vida (0 para mantener): ");
                    int fuerza = leerNumero("Nueva fuerza (0 para mantener): ");
                    try {
                        monstruoController.modificarMonstruoPorId(id, nombre, vida, fuerza);
                        mostrarMensaje("Monstruo actualizado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al actualizar monstruo: " + e.getMessage());
                    }
                    pausa();
                }
                case 3 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║         ELIMINAR MONSTRUO        ║");
                     System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    int id = leerNumero("ID del monstruo a eliminar: ");
                    try {
                        monstruoController.eliminarMonstruoPorId(id);
                        mostrarMensaje("Monstruo eliminado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al eliminar monstruo: " + e.getMessage());
                    }
                    pausa();
                }
                case 4 -> retroceder = true;
                default -> mostrarError("Opción no válida");
            }
        }
    }

    private void menuMagos() {
        boolean retroceder = false;
        while (!retroceder) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║                MAGOS               ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();
            java.util.List<com.example.entidades.Mago> magos;
            try {
                magos = magoController.obtenerMagos();
            } catch (Exception e) {
                mostrarError("No se pudieron obtener magos: " + e.getMessage());
                return;
            }
            System.out.println();
            if (magos.isEmpty()) {
                System.out.println("  [No hay magos registrados]");
            } else {
                System.out.println("┌────┬──────────────────┬───────┬────────────┐");
                System.out.println("│ ID │ Nombre           │ Vida  │ Magia      │");
                System.out.println("├────┼──────────────────┼───────┼────────────┤");
                for (com.example.entidades.Mago m : magos) {
                    System.out.printf("│ %2d │ %-16s │ %5d │ %10d │%n", m.getId(), m.getNombre(), m.getVida(), m.getNivelMagia());
                }
                System.out.println("└────┴──────────────────┴───────┴────────────┘");
            }
            System.out.println();
            System.out.println("  1. Crear Mago");
            System.out.println("  2. Modificar Mago");
            System.out.println("  3. Eliminar Mago");
            System.out.println("  4. Volver");
            System.out.println();
            System.out.print("Selecciona una opción: ");
            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║      ➕ CREAR NUEVO MAGO ➕        ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    String nombre = leerTexto("Nombre del mago: ");
                    int vida = leerNumero("Vida: ");
                    int nivelMagia = leerNumero("Nivel de Magia: ");
                    try {
                        magoController.guardarMago(nombre, vida, nivelMagia);
                        mostrarMensaje("Mago '" + nombre + "' creado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al crear mago: " + e.getMessage());
                    }
                    pausa();
                }
                case 2 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║       ✏️  MODIFICAR MAGO ✏️        ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    int id = leerNumero("ID del mago a modificar: ");
                    com.example.entidades.Mago existente = null;
                    try {
                        existente = magoController.obtenerMagoPorId(id);
                    } catch (Exception e) {
                        mostrarError("Error al buscar mago: " + e.getMessage());
                        pausa();
                        break;
                    }
                    if (existente == null) {
                        mostrarError("Mago no encontrado");
                        pausa();
                        break;
                    }
                    System.out.println("Modificando: " + existente.getNombre());
                    String nombre = leerTexto("Nuevo nombre (Enter para mantener): ");
                    int vida = leerNumero("Nueva vida (0 para mantener): ");
                    int nivelMagia = leerNumero("Nuevo nivel de magia (0 para mantener): ");
                    try {
                        magoController.modificarMagoPorId(id, nombre, vida, nivelMagia);
                        mostrarMensaje("Mago actualizado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al actualizar mago: " + e.getMessage());
                    }
                    pausa();
                }
                case 3 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║       🗑️  ELIMINAR MAGO 🗑️         ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    int id = leerNumero("ID del mago a eliminar: ");
                    try {
                        magoController.eliminarMagoPorId(id);
                        mostrarMensaje("Mago eliminado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al eliminar mago: " + e.getMessage());
                    }
                    pausa();
                }
                case 4 -> retroceder = true;
                default -> mostrarError("Opción no válida");
            }
        }
    }

    private void menuDragones() {
        boolean retroceder = false;
        while (!retroceder) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║         🐉 DRAGONES 🐉            ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();
            java.util.List<com.example.entidades.Dragon> dragones;
            try {
                dragones = dragonController.obtenerDragones();
            } catch (Exception e) {
                mostrarError("No se pudieron obtener dragones: " + e.getMessage());
                return;
            }
            System.out.println();
            if (dragones.isEmpty()) {
                System.out.println("  [No hay dragones registrados]");
            } else {
                System.out.println("┌────┬──────────────────┬────────────┬──────────────┐");
                System.out.println("│ ID │ Nombre           │ Intensidad │ Resistencia  │");
                System.out.println("├────┼──────────────────┼────────────┼──────────────┤");
                for (com.example.entidades.Dragon d : dragones) {
                    System.out.printf("│ %2d │ %-16s │ %10d │ %12d │%n", d.getId(), d.getNombre(), d.getIntensidadFuego(), d.getResistencia());
                }
                System.out.println("└────┴──────────────────┴────────────┴──────────────┘");
            }
            System.out.println();
            System.out.println("  1. Crear Dragón");
            System.out.println("  2. Modificar Dragón");
            System.out.println("  3. Eliminar Dragón");
            System.out.println("  4. Volver");
            System.out.println();
            System.out.print("Selecciona una opción: ");
            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║      ➕ CREAR NUEVO DRAGÓN ➕       ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    String nombre = leerTexto("Nombre del dragón: ");
                    int intensidad = leerNumero("Intensidad de fuego: ");
                    int resistencia = leerNumero("Resistencia: ");
                    try {
                        dragonController.guardarDragon(nombre, intensidad, resistencia);
                        mostrarMensaje("Dragón '" + nombre + "' creado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al crear dragón: " + e.getMessage());
                    }
                    pausa();
                }
                case 2 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║       ✏️  MODIFICAR DRAGÓN ✏️       ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    int id = leerNumero("ID del dragón a modificar: ");
                    com.example.entidades.Dragon existente = null;
                    try {
                        existente = dragonController.obtenerDragonPorId(id);
                    } catch (Exception e) {
                        mostrarError("Error al buscar dragón: " + e.getMessage());
                        pausa();
                        break;
                    }
                    if (existente == null) {
                        mostrarError("Dragón no encontrado");
                        pausa();
                        break;
                    }
                    System.out.println("Modificando: " + existente.getNombre());
                    String nombre = leerTexto("Nuevo nombre (Enter para mantener): ");
                    int intensidad = leerNumero("Nueva intensidad (0 para mantener): ");
                    int resistencia = leerNumero("Nueva resistencia (0 para mantener): ");
                    try {
                        dragonController.modificarDragonPorId(id, nombre, intensidad, resistencia);
                        mostrarMensaje("Dragón actualizado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al actualizar dragón: " + e.getMessage());
                    }
                    pausa();
                }
                case 3 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║        🗑️  ELIMINAR DRAGÓN 🗑️      ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    int id = leerNumero("ID del dragón a eliminar: ");
                    try {
                        dragonController.eliminarDragonPorId(id);
                        mostrarMensaje("Dragón eliminado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al eliminar dragón: " + e.getMessage());
                    }
                    pausa();
                }
                case 4 -> retroceder = true;
                default -> mostrarError("Opción no válida");
            }
        }
    }

    private void menuBosques() {
        boolean retroceder = false;
        while (!retroceder) {
            limpiarConsola();
            System.out.println("╔════════════════════════════════════╗");
            System.out.println("║             BOSQUES               ║");
            System.out.println("╚════════════════════════════════════╝");
            System.out.println();
            java.util.List<com.example.entidades.Bosque> bosques;
            try {
                bosques = bosqueController.obtenerBosques();
            } catch (Exception e) {
                mostrarError("No se pudieron obtener bosques: " + e.getMessage());
                return;
            }
            System.out.println();
            if (bosques.isEmpty()) {
                System.out.println("  [No hay bosques registrados]");
            } else {
                System.out.println("┌────┬──────────────────┬───────────┐");
                System.out.println("│ ID │ Nombre           │ Peligro   │");
                System.out.println("├────┼──────────────────┼───────────┤");
                for (com.example.entidades.Bosque b : bosques) {
                    System.out.printf("│ %2d │ %-16s │ %9d │%n", b.getId(), b.getNombre(), b.getPeligro());
                }
                System.out.println("└────┴──────────────────┴───────────┘");
            }
            System.out.println();
            System.out.println("  1. Crear Bosque");
            System.out.println("  2. Modificar Bosque");
            System.out.println("  3. Eliminar Bosque");
            System.out.println("  4. Volver");
            System.out.println();
            System.out.print("Selecciona una opción: ");
            int opcion = leerOpcion();
            switch (opcion) {
                case 1 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║        CREAR NUEVO BOSQUE         ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    String nombre = leerTexto("Nombre del bosque: ");
                    int peligro = leerNumero("Nivel de peligro: ");
                    try {
                        bosqueController.guardarBosque(nombre, peligro);
                        mostrarMensaje("Bosque '" + nombre + "' creado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al crear bosque: " + e.getMessage());
                    }
                    pausa();
                }
                case 2 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║      MODIFICAR BOSQUE             ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    int id = leerNumero("ID del bosque a modificar: ");
                    com.example.entidades.Bosque existente = null;
                    try {
                        existente = bosqueController.obtenerBosquePorId(id);
                    } catch (Exception e) {
                        mostrarError("Error al buscar bosque: " + e.getMessage());
                        pausa();
                        break;
                    }
                    if (existente == null) {
                        mostrarError("Bosque no encontrado");
                        pausa();
                        break;
                    }
                    System.out.println("Modificando: " + existente.getNombre());
                    String nombre = leerTexto("Nuevo nombre (Enter para mantener): ");
                    int peligro = leerNumero("Nuevo nivel de peligro (0 para mantener): ");
                    try {
                        bosqueController.modificarBosquePorId(id, nombre, peligro);
                        mostrarMensaje("Bosque actualizado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al actualizar bosque: " + e.getMessage());
                    }
                    pausa();
                }
                case 3 -> {
                    limpiarConsola();
                    System.out.println("╔════════════════════════════════════╗");
                    System.out.println("║      ELIMINAR BOSQUE              ║");
                    System.out.println("╚════════════════════════════════════╝");
                    System.out.println();
                    int id = leerNumero("ID del bosque a eliminar: ");
                    try {
                        bosqueController.eliminarBosquePorId(id);
                        mostrarMensaje("Bosque eliminado correctamente");
                    } catch (Exception e) {
                        mostrarError("Error al eliminar bosque: " + e.getMessage());
                    }
                    pausa();
                }
                case 4 -> retroceder = true;
                default -> mostrarError("Opción no válida");
            }
        }
    }



    private void iniciarNuevaPartida() {
        limpiarConsola();
        Batalla controller = new Batalla(this);
        controller.setupInicial();
        controller.jugar();
        pausa();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }

    public void mostrarError(String error) {
        System.out.println("[ERROR] " + error);
        pausa();
    }

    public int leerOpcion() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    public String leerTexto(String prompt) {
        System.out.print(prompt);
        scanner.nextLine(); // consumir salto de línea
        return scanner.nextLine();
    }

    public int leerNumero(String prompt) {
        System.out.print(prompt);
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            return -1;
        }
    }

    public void pausa() {
        System.out.println();
        System.out.print("Presiona Enter para continuar...");
        scanner.nextLine();
    }

    private void limpiarConsola() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public Scanner getScanner() {
        return scanner;
    }

    public MagoController getMagoController() {
        return magoController;
    }

    public MonstruoController getMonstruoController() {
        return monstruoController;
    }

    public BosqueController getBosqueController() {
        return bosqueController;
    }

    public DragonController getDragonController() {
        return dragonController;
    }
}
