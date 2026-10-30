package controlador;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
/**Genera una aplicación de consola que construya un árbol binario de letras, pida
 una letra al usuario y devuelva un camino desde la raíz hasta el nodo que contiene
 la letra, mostrando la secuencia de letras visitadas y las direcciones tomadas
 (L=izq, R=dech)**/
import java.util.Scanner;
import java.util.logging.Logger;

import modelo.Cola;
import modelo.Nodo;
import modelo.Pila; // Importo libreria personal de la actividad anterior

public class Ejercicio1 {
    private static final Logger logger = Logger.getLogger(Ejercicio1.class.getName());

    private Nodo<Character> raiz;

    public Ejercicio1() {
        diseniarArbol("ABCDEFHGHI");
    }

    /***
     * Con este metodo nos encargamos de construir el arbol de derecha a izquierda
     * dividido en niveles utilizando una cola
     */
    private void diseniarArbol(String lets) {
        raiz = null;

        Cola<Nodo<Character>> padres = new LinkedList<>();
        for (char let : lets.toCharArray()) {
            Nodo<Character> raizhijo = new Nodo<>(let);
            if (raiz == null) {
                raiz = raizhijo;
            } else {
                Nodo<Character> padre = padres.desencolar();

                if (padre.getSiguienteDatoIzquierda() == null) {
                    padre.setSiguienteDatoIzquierda(raizhijo);
                    padres.encolar(padre); // Encuelo a el nodo padre  con su hija 
                } else {
                    padre.setsiguienteDatoDerecha(raizhijo); // Señalo ahora a la rama derecha.
                }

            }
            padres.encolar(raizhijo);

        }
    }

    /** Con este metodo haremos visible los distintos niveles del usuario */
    public void mostrarNivelesArboles() {
        if (raiz == null) {
            logger.warning("El arbol esta vacío");
            return;
        }
        Cola<Nodo<Character>> cola = new LinkedList<>(); // Instacia de una linkelist es decir una lista entrelazada
        cola.add(raiz);

        int nivel = 0;

        while (!cola.estaVacio()) {
            int tamanoNivel = cola.getTamanio();
            StringBuilder sb = new StringBuilder("Nivel" + nivel + ":");
            for (int i = 0; i < tamanoNivel; i++) {
                Nodo<Character> n = cola.desencolar();
                sb.append(n.getDato()).append(' ');
                if (n.getSiguienteDatoIzquierda() != null)
                    cola.add(n.getSiguienteDatoIzquierda());
                if (n.getsiguienteDatoDerecha() != null)
                    cola.add(n.getsiguienteDatoDerecha());
            }
            System.out.println(sb);
            nivel++;
        }
    }

    /***
     * Este metodo es recursivo porque nos permite validar cada rama del arbol una
     * vez que termina una rama retrocde y cambia a la siguiente rama
     */
    private boolean dfs(Nodo<Character> nodo, char objetivo, List<Character> letras, List<String> dirs) {
        if (nodo == null)
            return false;
        letras.add(nodo.getDato());
        if (nodo.getDato() == objetivo)
            return true;
        dirs.add("L");
        if (dfs(nodo.getSiguienteDatoIzquierda(), objetivo, letras, dirs))
            return true;
        dirs.remove(dirs.size() - 1);

        dirs.add("R");
        logger.info("La rama izquierda ha terminado");
        if (dfs(nodo.getsiguienteDatoDerecha(), objetivo, letras, dirs))
            return true;

        dirs.remove(dirs.size() - 1);
        logger.info("La rama derecha ha terminado");
        letras.remove(letras.size() - 1);
        return false;

    }

    public void mostrarEjercicio1(Scanner sc) {
        System.out.println("\n<=====ARBOL DE LETRAS=====>");
        mostrarNivelesArboles();

        boolean seguir = false;
        int opcion = 0;
        do {
            try {
                System.out.println("\n1 Buscar Letras ");
                System.out.println("2. Mostrar Niveles");
                System.out.println("3.Salir");
                System.out.println("Seleccionar opcion");
                opcion = Integer.parseInt(sc.nextLine().trim());

                switch (opcion) {
                case 1:
                    buscarLetra(sc);
                    break;
                case 2:
                    mostrarNivelesArboles();
                    break;

                case 3:
                    System.out.println("Hasta luego...");
                    seguir = true;
                    break;
                default:
                    System.err.println("Error! Opcion no válida");
                    break;
                }
            } catch (NumberFormatException | NoSuchElementException e) {
                logger.warning("El parametro recibido por el usuario no ha sido válido");
                System.err.println("ERROR! EL PARAMETRO NO ES VÁLIDO ");
            }
        } while (!seguir);

    }

    public void buscarLetra(Scanner sc) {
        System.out.print("Introduce una letra a buscar: ");
        String en = sc.nextLine().trim();

        if (en.isEmpty()) {
            System.err.println("ERROR: no has escrito nada.");
        } else if (en.length() != 1 || !Character.isLetter(en.charAt(0))) {
            System.err.println("ERROR: escribe UNA sola letra.");
        } else {
            char letra = Character.toUpperCase(en.charAt(0));
            List<Character> letras = new ArrayList<>();
            List<String> dirs = new ArrayList<>();

            if (dfs(raiz, letra, letras, dirs)) {
                System.out.println("Letra '" + letra + "' encontrada.");
                System.out.println("Letras visitadas: " + letras);
                System.out.println("Direcciones (L=izq, R=dech): " + dirs);
            } else {
                System.out.println("La letra '" + letra + "' NO existe en el árbol.");
            }
        }
    }

}
