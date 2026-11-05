package co.edu.unal.lab4.dominio;

public interface PaisRepository {
    Pais buscar(String nombre) throws PaisNoEncontrado, PaisNoDisponible;
}
