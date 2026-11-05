package co.edu.unal.lab4;

import co.edu.unal.lab4.dominio.Clima;
import co.edu.unal.lab4.dominio.ClimaNoDisponible;
import co.edu.unal.lab4.dominio.ClimaRepository;
import co.edu.unal.lab4.dominio.Pais;
import co.edu.unal.lab4.dominio.PaisNoDisponible;
import co.edu.unal.lab4.dominio.PaisNoEncontrado;
import co.edu.unal.lab4.dominio.PaisRepository;
import co.edu.unal.lab4.infraestructura.CountriesDevPaisRepository;
import co.edu.unal.lab4.infraestructura.OpenMeteoClimaRepository;
import java.text.NumberFormat;
import java.util.Locale;

public class App {
    private static final Locale ES_CO = Locale.forLanguageTag("es-CO");

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Uso: indique el nombre del país en inglés, por ejemplo: Colombia");
            System.exit(2);
        }

        PaisRepository paises = new CountriesDevPaisRepository();
        ClimaRepository climas = new OpenMeteoClimaRepository();

        try {
            System.out.println(resumen(String.join(" ", args), paises, climas));
        } catch (PaisNoEncontrado e) {
            System.err.println(e.getMessage() + ". Escriba el nombre en inglés, por ejemplo Spain o Germany.");
            System.exit(1);
        } catch (PaisNoDisponible | ClimaNoDisponible e) {
            System.err.println("No se pudo completar la consulta: " + e.getMessage() + ". Intente más tarde.");
            System.exit(1);
        }
    }

    static String resumen(String nombre, PaisRepository paises, ClimaRepository climas)
            throws PaisNoEncontrado, PaisNoDisponible, ClimaNoDisponible {
        Pais pais = paises.buscar(nombre);
        Clima clima = climas.actual(pais.lat(), pais.lon());

        String capital = pais.capital() == null ? "sin capital" : "capital " + pais.capital();
        String poblacion = NumberFormat.getIntegerInstance(ES_CO).format(pais.poblacion());
        return String.format(ES_CO, "%s (%s, %s hab.): %.1f °C, viento %.1f km/h",
            pais.nombre(), capital, poblacion, clima.temperatura(), clima.viento());
    }
}
