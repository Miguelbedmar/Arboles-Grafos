package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import modelo.Contacto;
import modelo.Nodo;

/**
 * Implementar un listín telefónico que gestione contactos en tiempo real usando
 * un Árbol de Búsqueda Binaria dinámico. Debe permitir altas, bajas,
 * modificaciones y búsquedas, incluidas búsquedas binarias, búsquedas por
 * prefijo y rango alfabético.
 **/
public class Ejercicio2 {
    private static final String ERROR = "ERROR! VALOR NO VÁLIDO";

    private Nodo<Contacto> raiz;
    private final Map<String, Contacto> indiceTelefono = new HashMap<>();

    public Ejercicio2() {
        cargarDatosEjemplo();
    }

    private void cargarDatosEjemplo() {
        alta(new Contacto("Luis", "Martín", "600111111", "luis@mail.com"));
        alta(new Contacto("Ana", "García", "600222222", ""));
        alta(new Contacto("Pedro", "Ruiz", "600333333", "pedro@mail.com"));
        alta(new Contacto("Marta", "Álvarez", "600444444", ""));
        alta(new Contacto("Juan", "López", "600555555", "juan@mail.com"));
        alta(new Contacto("Eva", "Torres", "600666666", ""));
    }

    // ================= OPERACIONES =================

    /** Alta: false si el teléfono ya existe. */
    public boolean alta(Contacto c) {
        if (indiceTelefono.containsKey(c.getTelefono()))
            return false;
        raiz = insertar(raiz, c);
        indiceTelefono.put(c.getTelefono(), c);
        return true;
    }

    private Nodo<Contacto> insertar(Nodo<Contacto> nodo, Contacto c) {
        if (nodo == null)
            return new Nodo<>(c);
        if (c.compareTo(nodo.getDato()) < 0) {
            nodo.setSiguienteDatoIzquierda(insertar(nodo.getSiguienteDatoIzquierda(), c));
        } else {
            nodo.setsiguienteDatoDerecha(insertar(nodo.getsiguienteDatoDerecha(), c));
        }
        return nodo;
    }

    /** Baja por teléfono. */
    public boolean baja(String telefono) {
        Contacto c = indiceTelefono.remove(telefono);
        if (c == null)
            return false;
        raiz = eliminar(raiz, c);
        return true;
    }

    /**
     * Borrado clásico en un ABB: 0 hijos, 1 hijo o 2 hijos (se sustituye por el
     * sucesor in-order).
     */
    private Nodo<Contacto> eliminar(Nodo<Contacto> nodo, Contacto c) {
        if (nodo == null)
            return null;
        int cmp = c.compareTo(nodo.getDato());
        if (cmp < 0) {
            nodo.setSiguienteDatoIzquierda(eliminar(nodo.getSiguienteDatoIzquierda(), c));
        } else if (cmp > 0) {
            nodo.setsiguienteDatoDerecha(eliminar(nodo.getsiguienteDatoDerecha(), c));
        } else {
            if (nodo.getSiguienteDatoIzquierda() == null)
                return nodo.getsiguienteDatoDerecha();
            if (nodo.getsiguienteDatoDerecha() == null)
                return nodo.getSiguienteDatoIzquierda();
            Nodo<Contacto> sucesor = nodo.getsiguienteDatoDerecha();
            while (sucesor.getSiguienteDatoIzquierda() != null)
                sucesor = sucesor.getSiguienteDatoIzquierda();
            nodo.setDato(sucesor.getDato());
            nodo.setsiguienteDatoDerecha(eliminar(nodo.getsiguienteDatoDerecha(), sucesor.getDato()));
        }
        return nodo;
    }

    /**
     * Modificación: cadena vacía = mantener el valor. Si cambia la clave de orden,
     * se saca y se reinserta.
     */
    public boolean modificar(String telefono, String nombre, String apellidos, String email) {
        Contacto c = indiceTelefono.get(telefono);
        if (c == null)
            return false;
        raiz = eliminar(raiz, c); // lo quitamos con su clave ANTIGUA
        if (!nombre.isEmpty())
            c.setNombre(nombre);
        if (!apellidos.isEmpty())
            c.setApellido(apellidos);
        if (!email.isEmpty())
            c.setEmail(email);
        raiz = insertar(raiz, c); // lo metemos con su clave NUEVA
        return true;
    }

    public Contacto buscarPorTelefono(String telefono) {
        return indiceTelefono.get(telefono);
    }

    /** Búsqueda binaria por apellidos + nombre. */
    public List<Contacto> buscarExacto(String apellidos, String nombre) {
        List<Contacto> res = new ArrayList<>();
        buscarExacto(raiz, Contacto.nomalizarClaves(apellidos), Contacto.nomalizarClaves(nombre), res);
        return res;
    }

    private void buscarExacto(Nodo<Contacto> n, String ape, String nom, List<Contacto> res) {
        if (n == null)
            return;
        Contacto c = n.getDato();
        int cmp = ape.compareTo(c.getClaveApellido());
        if (cmp == 0)
            cmp = nom.compareTo(c.getClaveNombre());
        if (cmp <= 0)
            buscarExacto(n.getSiguienteDatoIzquierda(), ape, nom, res);
        if (cmp == 0)
            res.add(c);
        if (cmp >= 0)
            buscarExacto(n.getsiguienteDatoDerecha(), ape, nom, res);
    }

    /**
     * Búsqueda por prefijo de apellidos (podando ramas que no pueden contener
     * coincidencias).
     */
    public List<Contacto> buscarPrefijo(String prefijo) {
        List<Contacto> res = new ArrayList<>();
        buscarPrefijo(raiz, Contacto.nomalizarClaves(prefijo), res);
        return res;
    }

    private void buscarPrefijo(Nodo<Contacto> n, String p, List<Contacto> res) {
        if (n == null)
            return;
        String a = n.getDato().getClaveApellido();
        boolean empieza = a.startsWith(p);
        if (a.compareTo(p) >= 0)
            buscarPrefijo(n.getSiguienteDatoIzquierda(), p, res);
        if (empieza)
            res.add(n.getDato());
        if (a.compareTo(p) < 0 || empieza)
            buscarPrefijo(n.getsiguienteDatoDerecha(), p, res);
    }

    /** Rango alfabético de apellidos [desde, hasta] (ambos incluidos). */
    public List<Contacto> buscarRango(String desde, String hasta) {
        List<Contacto> res = new ArrayList<>();
        String d = Contacto.nomalizarClaves(desde);
        String h = Contacto.nomalizarClaves(hasta);
        if (d.compareTo(h) > 0) {
            String t = d;
            d = h;
            h = t;
        }
        buscarRango(raiz, d, h, res);
        return res;
    }

    private void buscarRango(Nodo<Contacto> n, String d, String h, List<Contacto> res) {
        if (n == null)
            return;
        String a = n.getDato().getClaveApellido();
        if (a.compareTo(d) >= 0)
            buscarRango(n.getSiguienteDatoIzquierda(), d, h, res);
        if (a.compareTo(d) >= 0 && a.compareTo(h) <= 0)
            res.add(n.getDato());
        if (a.compareTo(h) <= 0)
            buscarRango(n.getsiguienteDatoDerecha(), d, h, res);
    }

    /** Recorrido in-order = listado ordenado. */
    public List<Contacto> listar() {
        List<Contacto> res = new ArrayList<>();
        inOrden(raiz, res);
        return res;
    }

    private void inOrden(Nodo<Contacto> n, List<Contacto> res) {
        if (n == null)
            return;
        inOrden(n.getSiguienteDatoIzquierda(), res);
        res.add(n.getDato());
        inOrden(n.getsiguienteDatoDerecha(), res);
    }

    // ================= MENÚ =================

    public void ejecutar(Scanner sc) {
        boolean salir = false;
        do {
            System.out.println("\n===== EJERCICIO 2: LISTÍN TELEFÓNICO =====");
            System.out.println("1) Alta contacto");
            System.out.println("2) Baja (por teléfono)");
            System.out.println("3) Modificar (por teléfono)");
            System.out.println("4) Buscar por teléfono");
            System.out.println("5) Buscar por apellidos y nombre");
            System.out.println("6) Buscar por prefijo de apellidos");
            System.out.println("7) Buscar por rango alfabético de apellidos");
            System.out.println("8) Listar todos (ordenados)");
            System.out.println("9) Volver");
            int opcion;
            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.err.println(ERROR);
                opcion = 0;
            }
            switch (opcion) {
            case 1:
                menuAlta(sc);
                break;
            case 2:
                menuBaja(sc);
                break;
            case 3:
                menuModificar(sc);
                break;
            case 4:
                Contacto c = buscarPorTelefono(leer(sc, "Teléfono: ", true));
                System.out.println(c == null ? "El telefono no existe, disculpe ." : c);
                break;
            case 5:
                mostrar(buscarExacto(leer(sc, "Apellidos: ", true), leer(sc, "Nombre: ", true)));
                break;
            case 6:
                mostrar(buscarPrefijo(leer(sc, "Prefijo de apellidos: ", true)));
                break;
            case 7:
                mostrar(buscarRango(leer(sc, "Desde (apellidos): ", true), leer(sc, "Hasta (apellidos): ", true)));
                break;
            case 8:
                mostrar(listar());
                break;
            case 9:
                salir = true;
                break;
            default:
                System.err.println("Opción no disponible..");
            }
        } while (!salir);
    }

    private void menuAlta(Scanner sc) {
        String nombre = leer(sc, "Nombre: ", true);
        String apellidos = leer(sc, "Apellidos: ", true);
        String telefono = leer(sc, "Teléfono (9 dígitos): ", true);
        if (!telefono.matches("\\d{9}")) {
            System.err.println(ERROR + ": el teléfono debe tener 9 dígitos.");
            return;
        }
        String email = leer(sc, "Email (opcional, Enter para omitir): ", false);
        System.out.println(alta(new Contacto(nombre, apellidos, telefono, email)) ? "Contacto añadido."
                : "Ya existe un contacto con ese teléfono.");
    }

    private void menuBaja(Scanner sc) {
        System.out.println(
                baja(leer(sc, "Teléfono a eliminar: ", true)) ? "Contacto eliminado." : "No existe ese teléfono.");
    }

    private void menuModificar(Scanner sc) {
        String tel = leer(sc, "Teléfono del contacto: ", true);
        if (buscarPorTelefono(tel) == null) {
            System.out.println("No existe ese teléfono.");
            return;
        }
        System.out.println("(Pulse enter para mantener el valor actual)");
        String nombre = leer(sc, "Nuevo nombre: ", false);
        String apellidos = leer(sc, "Nuevos apellidos: ", false);
        String email = leer(sc, "Nuevo email: ", false);
        modificar(tel, nombre, apellidos, email);
        System.out.println("Contacto modificado: " + buscarPorTelefono(tel));
    }

    private void mostrar(List<Contacto> lista) {
        if (lista.isEmpty()) {
            System.out.println("Erro! no habido  resultados.");
            return;
        }
        for (Contacto c : lista)
            System.out.println(c);
    }

    private String leer(Scanner sc, String mensaje, boolean obligatorio) {
        while (true) {
            System.out.print(mensaje);
            String s = sc.nextLine().trim();
            if (!s.isEmpty() || !obligatorio)
                return s;
            System.err.println(ERROR + ": no puede estar vacío.");
        }
    }
}
