package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

import modelo.Arista;
import modelo.Arista.Modo;

/**
 * Diseñar un planificador de rutas en una ciudad que combine peatonal,
 * bicicleta, autobús y metro. Debe calcular rutas óptimas por tiempo total y
 * permitir restricciones (evitar bicicleta, minimizar transbordos, hora punta,
 * etc.). El grafo es ponderado y dirigido (algunas calles y líneas son
 * unidireccionales).
 **/
public class Ejercicio3 {
    private static final String ERROR = "ERROR! VALOR NO VÁLIDO";

    private static final double ESPERA_TRANSBORDO = 3; // minutos reales que se pierden al cambiar de línea
    private static final double PENALIZACION_TRANSBORDO = 15; // coste extra si se pide minimizar transbordos
    private static final double FACTOR_BUS_PUNTA = 1.5; // en hora punta el bus tarda más
    private static final double FACTOR_METRO_PUNTA = 1.2;

    // Lista de adyacencia: parada -> tramos que SALEN de ella
    private final Map<String, List<Arista>> grafo = new LinkedHashMap<>();

    public Ejercicio3() {
        cargarDatosEjemplo();
    }

    // ================= GRAFO =================

    private void agregarTramo(String a, String b, Modo modo, String linea, double min, boolean bidireccional) {
        grafo.computeIfAbsent(a, k -> new ArrayList<>()).add(new Arista(a, b, modo, linea, min));
        grafo.computeIfAbsent(b, k -> new ArrayList<>());
        if (bidireccional) {
            grafo.get(b).add(new Arista(b, a, modo, linea, min));
        }
    }

    private void cargarDatosEjemplo() {
        // A pie (ambos sentidos)
        agregarTramo("Casa", "Plaza", Modo.PIE, "PIE", 12, true);
        agregarTramo("Plaza", "Centro", Modo.PIE, "PIE", 10, true);
        agregarTramo("Centro", "Mercado", Modo.PIE, "PIE", 6, true);
        agregarTramo("Parque", "Universidad", Modo.PIE, "PIE", 14, true);
        agregarTramo("Estacion", "Centro", Modo.PIE, "PIE", 9, true);
        agregarTramo("Universidad", "Hospital", Modo.PIE, "PIE", 8, true);
        // Bicicleta (ambos sentidos)
        agregarTramo("Casa", "Parque", Modo.BICI, "BICI", 7, true);
        agregarTramo("Parque", "Centro", Modo.BICI, "BICI", 6, true);
        agregarTramo("Centro", "Universidad", Modo.BICI, "BICI", 10, true);
        // Bus (UNIDIRECCIONALES)
        agregarTramo("Casa", "Plaza", Modo.BUS, "L1", 5, false);
        agregarTramo("Plaza", "Estacion", Modo.BUS, "L1", 8, false);
        agregarTramo("Estacion", "Hospital", Modo.BUS, "L1", 12, false);
        agregarTramo("Mercado", "Centro", Modo.BUS, "L2", 4, false);
        agregarTramo("Centro", "Universidad", Modo.BUS, "L2", 14, false);
        // Metro (ambos sentidos)
        agregarTramo("Estacion", "Centro", Modo.METRO, "M1", 4, true);
        agregarTramo("Centro", "Universidad", Modo.METRO, "M1", 5, true);
        agregarTramo("Universidad", "Hospital", Modo.METRO, "M1", 6, true);
    }

    // ================= ESTADO DE LINEA  =================

    private static class Estado implements Comparable<Estado> {
        final String parada;
        final String linea; // última línea de transporte (BUS/METRO/BICI) usada; null si solo se ha
                            // caminado
        final double coste; // lo que optimiza Dijkstra (tiempo + penalizaciones)
        final double tiempo; // tiempo real
        final int transbordos;
        final Estado previo;
        final Arista via;

        Estado(String parada, String linea, double coste, double tiempo, int transbordos, Estado previo, Arista via) {
            this.parada = parada;
            this.linea = linea;
            this.coste = coste;
            this.tiempo = tiempo;
            this.transbordos = transbordos;
            this.previo = previo;
            this.via = via;
        }

        String clave() {
            return parada + "|" + linea;
        }

        @Override
        public int compareTo(Estado o) {
            return Double.compare(coste, o.coste);
        }
    }

    private double tiempoReal(Arista a, boolean horaPunta) {
        if (!horaPunta)
            return a.getMinuto();
        if (a.getModo() == Modo.BUS)
            return a.getMinuto() * FACTOR_BUS_PUNTA;
        if (a.getModo() == Modo.METRO)
            return a.getMinuto() * FACTOR_METRO_PUNTA;
        return a.getMinuto();
    }

    private Estado calcularRuta(String origen, String destino, boolean evitarBici, boolean minimizarTransbordos,
            boolean horaPunta) {
        PriorityQueue<Estado> cola = new PriorityQueue<>();
        Map<String, Double> mejor = new HashMap<>();

        Estado inicio = new Estado(origen, null, 0, 0, 0, null, null);
        cola.add(inicio);
        mejor.put(inicio.clave(), 0.0);

        while (!cola.isEmpty()) {
            Estado actual = cola.poll();
            if (actual.coste > mejor.getOrDefault(actual.clave(), Double.MAX_VALUE))
                continue; // entrada obsoleta
            if (actual.parada.equals(destino))
                return actual; // el primero que sale de la cola es el óptimo

            for (Arista a : grafo.get(actual.parada)) {
                if (evitarBici && a.getModo() == Modo.BICI)
                    continue;

                double tiempo = tiempoReal(a, horaPunta);
                double coste = tiempo;
                String nuevaLinea = actual.linea;
                int transbordos = actual.transbordos;

                if (a.getModo() != Modo.PIE) {
                    if (actual.linea != null && !actual.linea.equals(a.getLinea())) {
                        tiempo += ESPERA_TRANSBORDO;
                        coste += ESPERA_TRANSBORDO + (minimizarTransbordos ? PENALIZACION_TRANSBORDO : 0);
                        transbordos++;
                    }
                    nuevaLinea = a.getLinea();
                }

                Estado sig = new Estado(a.getDestino(), nuevaLinea, actual.coste + coste, actual.tiempo + tiempo,
                        transbordos, actual, a);
                if (sig.coste < mejor.getOrDefault(sig.clave(), Double.MAX_VALUE)) {
                    mejor.put(sig.clave(), sig.coste);
                    cola.add(sig);
                }
            }
        }
        return null; // no hay camino con esas restricciones
    }

    // ================= MENÚ =================

    public void ejecutar(Scanner sc) {
        boolean salir = false;
        do {
            System.out.println("\n===== EJERCICIO 3: PLANIFICADOR DE RUTAS =====");
            System.out.println("1) Calcular ruta");
            System.out.println("2) Ver red de transporte");
            System.out.println("3) Volver");
            int opcion;
            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println(ERROR);
                opcion = 0;
            }
            switch (opcion) {
            case 1:
                menuCalcular(sc);
                break;
            case 2:
                mostrarRed();
                break;
            case 3:
                salir = true;
                break;
            default:
                System.err.println("Opción no disponible..");
            }
        } while (!salir);
    }

    private void menuCalcular(Scanner sc) {
        List<String> paradas = new ArrayList<>(grafo.keySet());
        System.out.println("Paradas:");
        for (int i = 0; i < paradas.size(); i++)
            System.out.println((i + 1) + ") " + paradas.get(i));

        String origen = paradas.get(leerIndice(sc, "Origen (número): ", paradas.size()) - 1);
        String destino = paradas.get(leerIndice(sc, "Destino (número): ", paradas.size()) - 1);
        if (origen.equals(destino)) {
            System.out.println("Ya estás en el destino.");
            return;
        }
        boolean evitarBici = leerSiNo(sc, "¿Evitar bicicleta? (s/n): ");
        boolean minTrans = leerSiNo(sc, "¿Minimizar transbordos? (s/n): ");
        boolean punta = leerSiNo(sc, "¿Hora punta? (s/n): ");

        Estado fin = calcularRuta(origen, destino, evitarBici, minTrans, punta);
        if (fin == null) {
            System.out.println("No existe ruta de " + origen + " a " + destino + " con esas restricciones.");
            return;
        }

        LinkedList<Arista> tramos = new LinkedList<>();
        for (Estado e = fin; e.via != null; e = e.previo)
            tramos.addFirst(e.via);

        System.out.println("\nRuta de " + origen + " a " + destino + ":");
        for (Arista a : tramos) {
            System.out.printf("  %s --[%s %s, %.1f min]--> %s%n", a.getOrigen(), a.getModo(), a.getLinea(),
                    tiempoReal(a, punta), a.getDestino());
        }
        System.out.printf("Tiempo total: %.1f min | Transbordos: %d%n", fin.tiempo, fin.transbordos);
    }

    private void mostrarRed() {
        System.out.println("Tramos (origen -> destino) [modo línea, minutos]:");
        for (List<Arista> lista : grafo.values()) {
            for (Arista a : lista) {
                System.out.printf("  %s -> %s [%s %s, %.0f]%n", a.getOrigen(), a.getDestino(), a.getModo(),
                        a.getLinea(), a.getMinuto());
            }
        }
    }

    private int leerIndice(Scanner sc, String mensaje, int max) {
        while (true) {
            System.out.print(mensaje);
            try {
                int n = Integer.parseInt(sc.nextLine().trim());
                if (n >= 1 && n <= max)
                    return n;
            } catch (NumberFormatException e) {
                // cae al mensaje de error
            }
            System.err.println(ERROR + " (1-" + max + ")");
        }
    }

    private boolean leerSiNo(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String s = sc.nextLine().trim();
            if (s.equalsIgnoreCase("s"))
                return true;
            if (s.equalsIgnoreCase("n"))
                return false;
            System.err.println(ERROR + ": responde s o n.");
        }
    }

}
