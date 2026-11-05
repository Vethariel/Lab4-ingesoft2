package co.edu.unal.lab4.dominio;

public class PaisNoDisponible extends Exception {
    public PaisNoDisponible(String mensaje) {
        super(mensaje);
    }

    public PaisNoDisponible(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
