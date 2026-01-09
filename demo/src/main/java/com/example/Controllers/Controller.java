package com.example.Controllers;

import java.util.List;
import java.util.Random;

import com.example.Entidades.BolaDeFuego;
import com.example.Entidades.BolaDeNieve;
import com.example.Entidades.Bosque;
import com.example.Entidades.Dragon;
import com.example.Entidades.Hechizo;
import com.example.Entidades.Mago;
import com.example.Entidades.Monstruo;
import com.example.Entidades.Rayo;
import com.example.HibernateUtil;
import com.example.MenuConsola;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class Controller {
    private Bosque bosque;
    private List<Mago> magos;
    private List<Dragon> dragones;
    private List<Hechizo> todosLosHechizos;
    private Random random;
    private int ronda;
    private MenuConsola menu;

    public Controller(MenuConsola menu) {
        this.menu = menu;
        this.random = new Random();
        this.ronda = 0;
    }

    public void setupInicial() {
        EntityManager em = HibernateUtil.getEntityManager();
        
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║      SETUP INICIAL DEL JUEGO        ║");
        System.out.println("╚════════════════════════════════════╝\n");
        
        // 1. Crear bosque
        crearBosqueInicial(em);
        
        // 2. Crear mínimo 3 monstruos
        crearMonstruosIniciales(em);
        
        // 3. Crear dragón
        crearDragonInicial(em);
        
        // 4. Crear mínimo 2 magos con conjuros
        crearMagosIniciales(em);
        
        // Cargar datos
        cargarDatos(em);
        
        System.out.println("\n[OK] Setup completado. Presiona Enter para comenzar la batalla...");
        menu.pausa();
    }

    private void crearBosqueInicial(EntityManager em) {
        System.out.println("PASO 1: Crear Bosque");
        System.out.println("─────────────────────");
        
        String nombre = menu.leerTexto("Nombre del bosque: ");
        int peligro = menu.leerNumero("Nivel de peligro: ");
        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Bosque b = new Bosque(nombre, peligro, null);
        em.persist(b);
        tx.commit();
        
        System.out.println("[OK] Bosque '" + nombre + "' creado.\n");
    }

    private void crearMonstruosIniciales(EntityManager em) {
        System.out.println("PASO 2: Crear Monstruos (mínimo 3)");
        System.out.println("──────────────────────────────────");
        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
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
            
            Monstruo m = new Monstruo(nombre, vida, tipo, fuerza);
            em.persist(m);
            System.out.println("[OK] Monstruo '" + nombre + "' creado.\n");
        }
        
        tx.commit();
    }

    private void crearDragonInicial(EntityManager em) {
        System.out.println("PASO 3: Crear Dragón");
        System.out.println("──────────────────");
        
        String nombre = menu.leerTexto("Nombre del dragón: ");
        int intensidad = menu.leerNumero("Intensidad de fuego: ");
        int resistencia = menu.leerNumero("Resistencia: ");
        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
        Dragon d = new Dragon(nombre, intensidad, resistencia);
        em.persist(d);
        
        tx.commit();
        
        System.out.println("[OK] Dragón '" + nombre + "' creado.\n");
    }

    private void crearMagosIniciales(EntityManager em) {
        System.out.println("PASO 4: Crear Magos (mínimo 2) con Conjuros");
        System.out.println("──────────────────────────────────────────");
        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        
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
            
            em.persist(m);
            System.out.println("[OK] Mago '" + nombre + "' creado con 2 conjuros.\n");
        }
        
        tx.commit();
    }

    private void cargarDatos(EntityManager em) {
        this.bosque = em.createQuery("from Bosque", Bosque.class).getResultList().get(0);
        this.magos = em.createQuery("from Mago", Mago.class).getResultList();
        this.dragones = em.createQuery("from Dragon", Dragon.class).getResultList();
        this.todosLosHechizos = em.createQuery("from Hechizo", Hechizo.class).getResultList();
        
        // Si no hay monstruo jefe, asignar el primero
        if (bosque.getMonstruo() == null) {
            List<Monstruo> monstruos = em.createQuery("from Monstruo", Monstruo.class).getResultList();
            if (!monstruos.isEmpty()) {
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                bosque.setMonstruo(monstruos.get(0));
                em.merge(bosque);
                tx.commit();
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
                    Monstruo jefe = bosque.getMonstruo();
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
            Monstruo jefe = bosque.getMonstruo();
            if (jefe != null && jefe.estaVivo()) {
                dragon.exhalar(jefe);
                System.out.println(dragon.getNombre() + " exhala fuego al jefe " + jefe.getNombre());
            }
        }
        
        // Eliminar muertos
        eliminarMuertos(em);
        
        // Si el jefe murió, asignar otro
        Monstruo jefe = bosque.getMonstruo();
        if (jefe == null || !jefe.estaVivo()) {
            List<Monstruo> vivosEnum = em.createQuery("from Monstruo where vida > 0", Monstruo.class).getResultList();
            if (!vivosEnum.isEmpty()) {
                bosque.setMonstruo(vivosEnum.get(0));
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
            String jefe = bosque.getMonstruo() != null && bosque.getMonstruo().getId() == m.getId() ? " [JEFE]" : "";
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
    }

    private boolean tieneVida(EntityManager em) {
        List<Mago> magosList = em.createQuery("from Mago where vida > 0", Mago.class).getResultList();
        List<Monstruo> monstruosList = em.createQuery("from Monstruo where vida > 0", Monstruo.class).getResultList();
        
        return !magosList.isEmpty() && !monstruosList.isEmpty();
    }
}
