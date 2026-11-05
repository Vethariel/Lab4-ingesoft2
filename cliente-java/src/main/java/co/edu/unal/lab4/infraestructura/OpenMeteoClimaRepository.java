package co.edu.unal.lab4.infraestructura;

import co.edu.unal.lab4.dominio.Clima;
import co.edu.unal.lab4.dominio.ClimaNoDisponible;
import co.edu.unal.lab4.dominio.ClimaRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

public class OpenMeteoClimaRepository implements ClimaRepository {
    private static final String URL =
        "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s"
        + "&current=temperature_2m,wind_speed_10m";
    private final HttpClient http = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5)).build();

    @Override
    public Clima actual(double lat, double lon) throws ClimaNoDisponible {
        HttpRequest req = HttpRequest.newBuilder(URI.create(String.format(URL, lat, lon)))
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(5)).GET().build();
        try {
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

            if (res.statusCode() != 200) {
                throw new ClimaNoDisponible("Open-Meteo respondió " + res.statusCode()
                    + motivoDelError(res.body()));
            }

            JsonObject actual = JsonParser.parseString(res.body())
                .getAsJsonObject().getAsJsonObject("current");
            return new Clima(
                actual.get("temperature_2m").getAsDouble(),
                actual.get("wind_speed_10m").getAsDouble());
        } catch (HttpTimeoutException e) {
            throw new ClimaNoDisponible("Open-Meteo no respondió en 5 segundos", e);
        } catch (IOException e) {
            throw new ClimaNoDisponible("No hubo conexión con Open-Meteo", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ClimaNoDisponible("La consulta del clima se interrumpió", e);
        } catch (JsonParseException | IllegalStateException
                 | UnsupportedOperationException | NullPointerException e) {
            throw new ClimaNoDisponible("Open-Meteo respondió en un formato inesperado", e);
        }
    }

    private static String motivoDelError(String cuerpo) {
        try {
            return ": " + JsonParser.parseString(cuerpo).getAsJsonObject().get("reason").getAsString();
        } catch (RuntimeException e) {
            return "";
        }
    }
}
