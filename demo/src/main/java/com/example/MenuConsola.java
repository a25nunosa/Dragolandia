package com.example;

import java.util.Scanner;

import com.example.Controllers.BosqueController;
import com.example.Controllers.Controller;
import com.example.Controllers.DragonController;
import com.example.Controllers.MagoController;
import com.example.Controllers.MonstruoController;

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
                case 1 -> magoController.menuMagos();
                case 2 -> monstruoController.menuMonstruos();
                case 3 -> dragonController.menuDragones();
                case 4 -> bosqueController.menuBosques();
                case 5 -> retroceder = true;
                default -> mostrarError("Opción no válida");
            }
        }
    }



    private void iniciarNuevaPartida() {
        limpiarConsola();
        Controller controller = new Controller(this);
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
