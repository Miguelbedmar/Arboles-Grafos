package modelo;

public class Nodo<T> {
    //ATRIBUTO
    private T dato;
    private Nodo<T> siguienteDatoDerecha;
    private Nodo<T>siguienteDatoIzquierda;


    public Nodo(T datoT){
        this.dato=datoT;
        this.siguienteDatoDerecha=null;

    }

    public T getDato() {
        return dato;
    }

    public void setDato(T datoT) {
        this.dato = datoT;
    }

    public Nodo<T> getsiguienteDatoDerecha() {
        return siguienteDatoDerecha;
    }

    public void setsiguienteDatoDerecha(Nodo<T> siguienteDatoDerecha) {
        this.siguienteDatoDerecha = siguienteDatoDerecha;
    }

    public Nodo<T> getSiguienteDatoIzquierda() {
        return siguienteDatoIzquierda;
    }

    public void setSiguienteDatoIzquierda(Nodo<T> siguienteDatoIzquierda) {
        this.siguienteDatoIzquierda = siguienteDatoIzquierda;
    }
}