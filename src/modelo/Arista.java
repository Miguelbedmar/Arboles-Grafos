package modelo;

public class Arista {
    public enum Modo {
        PIE, BICI, BUS, METRO
    }

    private final String origen;
    private final String destino;
    private final Modo modo;
    private final String linea;
    private final String minuto;

    public Arista(String origen, String destino, Modo modo, String linea, String minuto) {
        this.origen = origen;
        this.destino = destino;
        this.modo = modo;
        this.linea = linea;
        this.minuto = minuto;
    }

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
