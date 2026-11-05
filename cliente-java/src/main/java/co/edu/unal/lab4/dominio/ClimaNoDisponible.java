package co.edu.unal.lab4.dominio;

public class ClimaNoDisponible extends Exception {
    public ClimaNoDisponible(String mensaje) {
        super(mensaje);
    }

    public ClimaNoDisponible(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
