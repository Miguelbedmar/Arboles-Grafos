package modelo;

public class Arista {
    public enum Modo { // DECLARACION DE UN ENUM
        PIE, BICI, BUS, METRO
    }

    // ATRIBUTOS
    private final String origen;
    private final String destino;
    private final Modo modo; // ENUM CON PIE ,BICI,BUS,METRO
    private final String linea;
    private final String minuto;

    // CONTRUCTOR
    public Arista(String origen, String destino, Modo modo, String linea, String minuto) {
        this.origen = origen;
        this.destino = destino;
        this.modo = modo;
        this.linea = linea;
        this.minuto = minuto;
    }

    // METODOS GETTERS
    public String getOrigen() {
        return this.origen;
    }

    public String getDestino() {
        return this.destino;
    }

    public Modo getModo() {
        return this.modo;
    }

    public String getLinea() {
        return this.linea;
    }

    public String getMinuto() {
        return this.minuto;
    }

}
