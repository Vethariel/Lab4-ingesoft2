package co.edu.unal.lab4.infraestructura;

import co.edu.unal.lab4.dominio.Pais;
import co.edu.unal.lab4.dominio.PaisNoDisponible;
import co.edu.unal.lab4.dominio.PaisNoEncontrado;
import co.edu.unal.lab4.dominio.PaisRepository;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;

public class CountriesDevPaisRepository implements PaisRepository {
    private static final String URL =
        "https://countries.dev/name/%s?fields=name,capital,population,latlng";
    private final HttpClient http = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5)).build();
    private final Gson gson = new Gson();

    private record PaisJson(String name, String capital, Long population, double[] latlng) { }

    @Override
    public Pais buscar(String nombre) throws PaisNoEncontrado, PaisNoDisponible {
        HttpRequest req = HttpRequest.newBuilder(URI.create(String.format(URL, codificarParaRuta(nombre))))
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(5)).GET().build();
        try {
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() == 404) {
                throw new PaisNoEncontrado(nombre);
            }
            if (res.statusCode() != 200) {
                throw new PaisNoDisponible("countries.dev respondió " + res.statusCode());
            }

            PaisJson[] encontrados = gson.fromJson(res.body(), PaisJson[].class);
            if (encontrados == null || encontrados.length == 0) {
                throw new PaisNoEncontrado(nombre);
            }
            return aDominio(preferirNombreExacto(encontrados, nombre.trim()));
        } catch (HttpTimeoutException e) {
            throw new PaisNoDisponible("countries.dev no respondió en 5 segundos", e);
        } catch (IOException e) {
            throw new PaisNoDisponible("No hubo conexión con countries.dev", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PaisNoDisponible("La búsqueda del país se interrumpió", e);
        } catch (JsonParseException e) {
            throw new PaisNoDisponible("countries.dev respondió en un formato inesperado", e);
        }
    }

    private static String codificarParaRuta(String nombre) {
        return URLEncoder.encode(nombre.trim(), StandardCharsets.UTF_8).replace("+", "%20");
    }

    private static PaisJson preferirNombreExacto(PaisJson[] encontrados, String nombre) {
        return Arrays.stream(encontrados)
            .filter(p -> nombre.equalsIgnoreCase(p.name()))
            .findFirst()
            .orElse(encontrados[0]);
    }

    private static Pais aDominio(PaisJson p) throws PaisNoDisponible {
        if (p.name() == null || p.population() == null || p.latlng() == null || p.latlng().length != 2) {
            throw new PaisNoDisponible("countries.dev devolvió un país con datos incompletos");
        }
        return new Pais(p.name(), p.capital(), p.population(), p.latlng()[0], p.latlng()[1]);
    }
}
