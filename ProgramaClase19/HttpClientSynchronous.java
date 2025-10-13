package ProgramaClase19;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;

public class HttpClientSynchronous {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        // Cuerpo del mensaje: <numTokens>,<subcadena>
        String requestBody = "17576000,IPN";

        // Construir la solicitud POST al endpoint /searchtoken
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8081/searchtoken"))
                .timeout(Duration.ofSeconds(300))
                .header("Content-Type", "text/plain")
                .header("X-Debug", "true") // para que el servidor devuelva el tiempo de ejecución
                .POST(BodyPublishers.ofString(requestBody))
                .build();

        System.out.println("Enviando solicitud al servidor...\n");

        long clientStart = System.nanoTime(); // tiempo cliente antes de enviar
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        long clientEnd = System.nanoTime(); // tiempo cliente después de recibir

        long totalClientTimeMs = (clientEnd - clientStart) / 1_000_000;

        // --- Resultados ---
        System.out.println("--- Encabezados de Respuesta ---");
        HttpHeaders headers = response.headers();
        headers.map().forEach((k, v) -> System.out.println(k + ": " + v));

        System.out.println("\n--- Código de Estado ---");
        System.out.println(response.statusCode());

        System.out.println("\n--- Cuerpo de Respuesta ---");
        System.out.println(response.body());

        System.out.println("\n--- Tiempo reportado por el servidor ---");
        if (headers.firstValue("X-Debug-Info").isPresent()) {
            System.out.println(headers.firstValue("X-Debug-Info").get());
        } else {
            System.out.println("El servidor no envió el encabezado X-Debug-Info.");
        }

        System.out.println("\n--- Tiempo total medido por el cliente ---");
        System.out.println(totalClientTimeMs + " milisegundos");
    }
}
