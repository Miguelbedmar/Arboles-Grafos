package modelo;

import java.text.Normalizer;
import java.util.Locale;

public class Contacto implements Comparable<Contacto> {
    // ATRIBUTOS
    private String nombre; // Nombres y apellido del contacto
    private String apellido;
    private final String telefono; // Como el telefono es unico indicamos que la varia e privada y final
    private String email;

    // Claves de usuario
    private String claveNombre;
    private String claveApellido;

    // CONTRUCTOR.
    public Contacto(String nombre, String apellido, String telefono, String email) {
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.telefono = telefono.trim();
        this.email = email.trim();
        actualizarClavesContacto();
    }

    // METODOS DE CLASE
    /***
     * En este metodo vamos a realizar una tranformacion del texto previemante
     * eliminado los espacios a hacia su forma descompuesta canónica siguendo el
     * estandar Unicode. Primero descomponemos el texto en caracteres unicos depues
     * los estenderizamos y nos aseguramos de que tenga la misma composición binaria
     * por último reordenamos los acentos .
     */
    public static String nomalizarClaves(String s) {
        return Normalizer.normalize(s.trim(), Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
        /**
         * por temas de normalizacion indicamos que la transformacion aplique a las
         * reglas de ASCII sim que tenga importancia el sistema operativo*
         */

    }
    // GETTERS Y SETTERS.

    private void actualizarClavesContacto() {
        this.claveApellido = nomalizarClaves(apellido);
        this.claveNombre = nomalizarClaves(nombre);
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return this.apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClaveNombre() {
        return this.claveNombre;
    }

    public void setClaveNombre(String claveNombre) {
        this.claveNombre = claveNombre;
    }

    public String getClaveApellido() {
        return this.claveApellido;
    }

    public void setClaveApellido(String claveApellido) {
        this.claveApellido = claveApellido;
    }

    /***
     * Comparamos los nombre apellidos y telefono de dos personas para comprobar si
     * coinciden.
     */
    @Override
    public int compareTo(Contacto co) {
        int con = claveApellido.compareTo(co.claveApellido);
        if (con == 0)
            con = claveNombre.compareTo(co.claveNombre);
        if (con == 0)
            con = telefono.compareTo(co.telefono);
        return con;
    }

    @Override
    public String toString() {
        return "{" + " nombre='" + getNombre() + "'" + ", apellido='" + getApellido() + "'" + ", telefono='"
                + getTelefono() + "'" + ", email='" + getEmail() + "'" + ", claveNombre='" + getClaveNombre() + "'"
                + ", claveApellido='" + getClaveApellido() + "'" + "}";
    }

}
