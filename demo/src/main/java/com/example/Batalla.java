package com.example;

import java.util.List;
import java.util.Random;

import com.example.entidades.BolaDeFuego;
import com.example.entidades.BolaDeNieve;
import com.example.entidades.Bosque;
import com.example.entidades.Dragon;
import com.example.entidades.Hechizo;
import com.example.entidades.Mago;
import com.example.entidades.Monstruo;
import com.example.entidades.Rayo;
import com.util.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Batalla {
    private Bosque bosque;
    private List<Mago> magos;
    private List<Dragon> dragones;
    private List<Hechizo> todosLosHechizos;
    private Random random;
    private int ronda;
    private MenuConsola menu;

    public Batalla(MenuConsola menu) {
        this.menu = menu;
        this.random = new Random();
        this.ronda = 0;
    }

    public void setupInicial() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║      SETUP INICIAL DEL JUEGO       ║");
        System.out.println("╚════════════════════════════════════╝\n");

        // 1. Crear bosque
        System.out.println("PASO 1: Crear Bosque");
        System.out.println("─────────────────────");
        String nombreBosque = menu.leerTexto("Nombre del bosque: ");
        int peligro = menu.leerNumero("Nivel de peligro: ");
        menu.getBosqueController().guardarBosque(nombreBosque, peligro);

        // 2. Crear mínimo 3 monstruos
        System.out.println("PASO 2: Crear Monstruos (mínimo 3)");
        System.out.println("──────────────────────────────────");
        for (int i = 0; i < 3; i++) {
            String nombre = menu.leerTexto("Nombre del monstruo " + (i + 1) + ": ");
            int vida = menu.leerNumero("Vida: ");
            int fuerza = menu.leerNumero("Fuerza: ");
            System.out.println("  Tipo: 1=OGRO, 2=TROLL, 3=ESPECTRO");
            int tipoOpt = menu.leerOpcion();
            Monstruo.TipoMonstruo tipo = switch (tipoOpt) {
                case 1 -> Monstruo.TipoMonstruo.OGRO;
                case 2 -> Monstruo.TipoMonstruo.TROLL;
                case 3 -> Monstruo.TipoMonstruo.ESPECTRO;
                default -> Monstruo.TipoMonstruo.OGRO;
            };
            menu.getMonstruoController().guardarMonstruo(nombre, vida, tipo, fuerza);
        }

        // 3. Crear dragón
        System.out.println("PASO 3: Crear Dragón");
        System.out.println("──────────────────");
        String nombreD = menu.leerTexto("Nombre del dragón: ");
        int intensidad = menu.leerNumero("Intensidad de fuego: ");
        int resistencia = menu.leerNumero("Resistencia: ");
        menu.getDragonController().guardarDragon(nombreD, intensidad, resistencia);

        // 4. Crear mínimo 2 magos con conjuros
        System.out.println("PASO 4: Crear Magos (mínimo 2) con Conjuros");
        System.out.println("──────────────────────────────────────────");
        for (int i = 0; i < 2; i++) {
            String nombre = menu.leerTexto("Nombre del mago " + (i + 1) + ": ");
            int vida = menu.leerNumero("Vida: ");
            int nivelMagia = menu.leerNumero("Nivel de Magia: ");

            Mago m = new Mago(nombre, vida, nivelMagia);
            // Añadir conjuros
            System.out.println("Conjuros disponibles:");
            System.out.println("  1. Bola de Fuego (efecto 5)");
            System.out.println("  2. Rayo (efecto 7)");
            System.out.println("  3. Bola de Nieve (efecto variable)");
            System.out.println("Selecciona 2 conjuros para " + nombre + ":");
            for (int j = 0; j < 2; j++) {
                System.out.print("Conjuro " + (j + 1) + ": ");
                int conjOpt = menu.leerOpcion();
                Hechizo h = switch (conjOpt) {
                    case 1 -> new BolaDeFuego(5);
                    case 2 -> new Rayo(7);
                    case 3 -> new BolaDeNieve();
                    default -> new BolaDeFuego(5);
                };
                m.addHechizo(h);
            }
            // Persistir mago usando su controlador
            menu.getMagoController().guardarMago(m.getNombre(), m.getVida(), m.getNivelMagia());
        }

        // Cargar datos desde los controladores
        cargarDatos();

        System.out.println("\n[OK] Setup completado. Presiona Enter para comenzar la batalla...");
        menu.pausa();
    }

    private void cargarDatos() {
        java.util.List<Bosque> bosques = menu.getBosqueController().obtenerBosques();
        if (!bosques.isEmpty()) {
            this.bosque = bosques.get(0);
        } else {
            this.bosque = null;
        }

        this.magos = menu.getMagoController().obtenerMagos();
        this.dragones = menu.getDragonController().obtenerDragones();
        // Hechizos se obtienen a partir de los magos cargados
        java.util.List<Hechizo> hechizos = new java.util.ArrayList<>();
        for (Mago m : this.magos) {
            hechizos.addAll(m.getConjuros());
        }
        this.todosLosHechizos = hechizos;

        // Si no hay monstruo jefe, asignar el primero disponible
        if (this.bosque != null && this.bosque.getJefeMonstruo() == null) {
            java.util.List<Monstruo> monstruos = menu.getMonstruoController().obtenerMonstruos();
            if (!monstruos.isEmpty()) {
                menu.getBosqueController().asignarJefe(this.bosque.getId(), monstruos.get(0).getId());
                // recargar bosque local
                java.util.List<Bosque> b2 = menu.getBosqueController().obtenerBosques();
                if (!b2.isEmpty()) this.bosque = b2.get(0);
            }
        }
    }

    public void jugar() {
        EntityManager em = HibernateUtil.getEntityManager();
        
        while (tieneVida(em)) {
            ronda++;
            mostrarRonda();
            jugarRonda(em);
            mostrarEstado(em);
            menu.pausa();
        }
        
        mostrarFinal(em);
        em.close();
    }

    private void jugarRonda(EntityManager em) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        recargarDatos(em);
        
        // Cada mago lanza un conjuro
        for (Mago mago : magos) {
            if (!mago.estaVivo()) continue;
            
            List<Hechizo> conjurosDisponibles = mago.getConjuros();
            if (conjurosDisponibles.isEmpty()) {
                mago.recibirDaño(1);
                System.out.println(mago.getNombre() + " no tiene conjuros y pierde 1 vida");
                continue;
            }
            
            // Seleccionar un conjuro aleatorio de todos
            Hechizo conjuro = todosLosHechizos.get(random.nextInt(todosLosHechizos.size()));
            
            if (conjurosDisponibles.contains(conjuro)) {
                // Conocido: ataca al monstruo jefe
                    List<Monstruo> monstruos = bosque.getMonstruos();
                    if (!monstruos.isEmpty()) {
                        Monstruo jefe = bosque.getJefeMonstruo();
                        if (jefe != null && jefe.estaVivo()) {
                            mago.lanzarHechizo(jefe, conjuro);
                            System.out.println(mago.getNombre() + " lanza " + conjuro.getNombreHechizo() + " al jefe " + jefe.getNombre());
                        }
                    }
            } else {
                // Desconocido: pierde 1 vida
                mago.recibirDaño(1);
                System.out.println(mago.getNombre() + " intenta " + conjuro.getNombreHechizo() + " (desconocido) y pierde 1 vida");
            }
        }
        
        // Cada monstruo ataca a un mago
        List<Monstruo> monstruos = bosque.getMonstruos();
        for (Monstruo monstruo : monstruos) {
            if (!monstruo.estaVivo()) continue;
            if (magos.isEmpty() || magos.stream().noneMatch(Mago::estaVivo)) break;
            
            Mago magoObjetivo = magos.stream().filter(Mago::estaVivo).findAny().orElse(null);
            if (magoObjetivo != null) {
                monstruo.atacar(magoObjetivo);
                System.out.println(monstruo.getNombre() + " ataca a " + magoObjetivo.getNombre());
            }
        }
        
        // Dragón ataca al monstruo jefe
        if (!dragones.isEmpty()) {
            Dragon dragon = dragones.get(0);
            Monstruo jefe = bosque.getJefeMonstruo();
            if (jefe != null && jefe.estaVivo()) {
                dragon.exhalar(jefe);
                System.out.println(dragon.getNombre() + " exhala fuego al jefe " + jefe.getNombre());
            }
        }
        
        // Eliminar muertos
        eliminarMuertos(em);
        
        // Si el jefe murió, asignar otro
        Monstruo jefe = bosque.getJefeMonstruo();
        if (jefe == null || !jefe.estaVivo()) {
            List<Monstruo> vivosEnum = em.createQuery("from Monstruo where vida > 0", Monstruo.class).getResultList();
            if (!vivosEnum.isEmpty()) {
                bosque.setJefeMonstruo(vivosEnum.get(0));
                System.out.println("Nuevo jefe: " + vivosEnum.get(0).getNombre());
            }
        }
        
        tx.commit();
    }

    private void recargarDatos(EntityManager em) {
        magos = em.createQuery("from Mago", Mago.class).getResultList();
        dragones = em.createQuery("from Dragon", Dragon.class).getResultList();
        bosque = em.find(Bosque.class, bosque.getId());
    }

    private void eliminarMuertos(EntityManager em) {
        // Eliminar magos muertos
        List<Mago> magosMuertos = magos.stream().filter(m -> !m.estaVivo()).toList();
        for (Mago m : magosMuertos) {
            em.remove(em.find(Mago.class, m.getId()));
            System.out.println("[MUERTO] " + m.getNombre() + " ha muerto");
        }
        
        // Eliminar monstruos muertos
        List<Monstruo> monstruosMuertos = bosque.getMonstruos().stream().filter(m -> !m.estaVivo()).toList();
        for (Monstruo m : monstruosMuertos) {
            em.remove(em.find(Monstruo.class, m.getId()));
            System.out.println("[MUERTO] " + m.getNombre() + " ha muerto");
        }
        
        // Eliminar dragones muertos
        List<Dragon> dragonesMuertos = dragones.stream().filter(d -> !d.estaVivo()).toList();
        for (Dragon d : dragonesMuertos) {
            em.remove(em.find(Dragon.class, d.getId()));
            System.out.println("[MUERTO] " + d.getNombre() + " ha muerto");
        }
    }

    private void mostrarRonda() {
        System.out.println();
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║           RONDA " + ronda);
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
    }

    private void mostrarEstado(EntityManager em) {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         ESTADO DEL JUEGO             ║");
        System.out.println("╚════════════════════════════════════╝\n");
        
        List<Mago> magosVivos = em.createQuery("from Mago where vida > 0", Mago.class).getResultList();
        List<Monstruo> monstruosVivos = em.createQuery("from Monstruo where vida > 0", Monstruo.class).getResultList();
        List<Dragon> dragonesVivos = em.createQuery("from Dragon where resistencia > 0", Dragon.class).getResultList();
        
        System.out.println("MAGOS (" + magosVivos.size() + "):");
        for (Mago m : magosVivos) {
            System.out.printf("  %s - Vida: %d\n", m.getNombre(), m.getVida());
        }
        
        System.out.println("\nMONSTRUOS (" + monstruosVivos.size() + "):");
        for (Monstruo m : monstruosVivos) {
            String jefe = bosque.getJefeMonstruo() != null && bosque.getJefeMonstruo().getId() == m.getId() ? " [JEFE]" : "";
            System.out.printf("  %s - Vida: %d (Tipo: %s)%s\n", m.getNombre(), m.getVida(), m.getTipo(), jefe);
        }
        
        System.out.println("\nDRAGONES (" + dragonesVivos.size() + "):");
        for (Dragon d : dragonesVivos) {
            System.out.printf("  %s - Resistencia: %d\n", d.getNombre(), d.getResistencia());
        }
    }

    private void mostrarFinal(EntityManager em) {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║          FIN DEL JUEGO              ║");
        System.out.println("╚════════════════════════════════════╝\n");
        
        List<Mago> magosVivos = em.createQuery("from Mago where vida > 0", Mago.class).getResultList();
        List<Monstruo> monstruosVivos = em.createQuery("from Monstruo where vida > 0", Monstruo.class).getResultList();
        
        if (magosVivos.isEmpty()) {
            System.out.println("[DERROTA] No hay magos vivos. ¡Los monstruos ganan!");
        } else if (monstruosVivos.isEmpty()) {
            System.out.println("[VICTORIA] No hay monstruos vivos. ¡Los magos ganan!");
        }

        // Al finalizar la partida, eliminar todos los datos relacionados
        try {
            // Eliminar bosques primero para evitar restricciones de FK
            menu.getBosqueController().eliminarTodos();
            menu.getMonstruoController().eliminarTodos();
            menu.getDragonController().eliminarTodos();
            menu.getMagoController().eliminarTodos();
            menu.mostrarMensaje("Todos los datos de la partida han sido eliminados de la base de datos.");
        } catch (Exception e) {
            menu.mostrarError("Error al limpiar la base de datos tras la partida: " + e.getMessage());
        }
    }

    private boolean tieneVida(EntityManager em) {
        List<Mago> magosList = em.createQuery("from Mago where vida > 0", Mago.class).getResultList();
        List<Monstruo> monstruosList = em.createQuery("from Monstruo where vida > 0", Monstruo.class).getResultList();
        
        return !magosList.isEmpty() && !monstruosList.isEmpty();
    }
}
