package modelo;

import java.text.Normalizer;

public class Contacto implements Comparable<Contacto> {
    // ATRIBUTOS
    private String nombre; // Nombres y apellido del contacto
    private String apellido;
    private final String telefono; // Como el telefono es unico indicamos que la varia e privada y final
    private String email;

    // Claves de usuario
    private String claveNombre;
    private String claveApelldio;

    // CONTRUCTOR.
    public Contacto(String nombre, String apellido, String telefono, String email) {
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.telefono = telefono.trim();
        this.email = email.trim();


    
        //METODOS DE CLASE
        /***En este metodo vamos a realizar una tranformacion del texto
         *  previemante eliminado los espacios  a hacia su forma descompuesta
         * canónica  siguendo el estandar Unicode.
         * Primero descomponemos el texto en caracteres unicos  depues los
         * estenderizamos  y  nos aseguramos de que tenga
         * la misma composición binaria
         * por último reordenamos los acentos .
        */
        public static String nomalizarClaves(String s){ 
            return  Normalizer.normalize(scl.trim(),Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase();
        }

        private void actualizarClavesContacto(){
            
        }



        //GETTERS Y SETTERS.
    }

    @Override
    public int compareTo(Contacto arg0) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
}
