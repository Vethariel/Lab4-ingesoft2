package co.edu.unal.lab4.dominio;

public class PaisNoEncontrado extends Exception {
    public PaisNoEncontrado(String nombre) {
        super("No existe un país llamado \"" + nombre + "\"");
    }
}
