package ProgramaClase21;
import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.time.Duration;

public class HttpClientSynchronous {

    // Usa HTTP/1.1 (seguro) y sube el timeout de conexión
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private static void getAndPrint(String title, String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("User-Agent", "Java HttpClient/17 (ESCOM)")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("\n=== " + title + " ===");
            System.out.println("GET " + url);
            System.out.println("HTTP " + response.statusCode());

            // Encabezados de respuesta
            response.headers().map().forEach((k, v) -> System.out.println(k + ": " + v));

            System.out.println("--- body ---");
            System.out.println(response.body());

        } catch (Exception e) {
            System.out.println("\n=== " + title + " ===");
            System.out.println("GET " + url);
            System.out.println("FALLÓ: " + e.getClass().getSimpleName() + " -> " + e.getMessage());
            // Para depurar rápido, descomenta si quieres el stacktrace completo:
            // e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // 1) Numbers API por HTTPS (evita bloqueo del puerto 80)
        getAndPrint("NumbersAPI (text/plain)", "https://numbersapi.com/42");

        // 2) Breaking Bad Quotes (JSON)
        getAndPrint("Breaking Bad Quotes (JSON)", "https://api.breakingbadquotes.xyz/v1/quotes/1");
    }
}
