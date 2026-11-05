package co.edu.unal.lab4.dominio;

public interface ClimaRepository {
    Clima actual(double latitud, double longitud) throws ClimaNoDisponible;
}
