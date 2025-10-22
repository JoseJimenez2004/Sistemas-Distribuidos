package ProgramaClase21;

import java.io.IOException;
import java.net.*;
import java.net.http.*;
import java.time.Duration;
import java.util.Optional;
import java.util.List;

public class HttpClientSynchronous {

    private static final String GOOGLE_STORAGE_TOKEN = "ya29.c.c0ASRK0GYDAn8CiWL4KMUd8ej-G84Z4Ni1-tHZ4YlW5vtMI4udJdLgrEw_vp1qScYZvhCbwxr0fZIZ3LqZ41ujdVGtupV3UPBvLMTpYluSNZ5DwyzGMgN9E9Gb2J8Ku1x9pwsr6XjPXyI93055XBhy7o3yUEWvTsgLXdvdaggEsYBTlRxK0LwDgxzQSitMiRBQWIkGxEHnapIL4JljF39XHo4pR6osFeAGckGvEvStnj12pk8RcpgGuerygoZBUKxQZneJsYii5_6jolO0w1kTkCLaepfz09f7RVTfcqR4QzeSiBfu9Q5jvChrY1-P8iQuRH6XZOvJou1UjRuAzzCQ32i2IOHfhsRi9p7Whz6_gK125i_MaS4wlMwiTCOZRLoX3Cixb-8T400A23dJy3iSvrZ3OzonW5Uix96whBnQWqztgIoUQr0XwvffIkwukpm6paxIMQnM9kS2xIYU12jsrBJzzoxwoQjvpz_odMFZj2ekmvsRZhI2I9r3W5O6V49fscMvhmq0lulVOekhOz8QlJFQboJ9VRw7yMtj32p-1hc1BOcvQYBS99dVsgxqlUOQ9uSjo0n4lov6ZyVYe2bW2vykY7anjZtt1i76gXUFnlWwIuleJZpxQYJtSX-Q_6-kxtSljr4fIU9byfStYSnl6Y2BIsZsnxOoSYRvar6924l8jR7iSth-xe008Oe8OqtS8cQR1llqtI7Y598k6fiuy6wmB8bnp8VWpY4_B5WbW4q4t2xFe1Ovljoo0ag6k0_2Zb0Rnyiy3QlOzY-rMsi4bco1aaZw4Mxhhsrd9eB04zrcfJ4rosU3MMW1mB6xJyXXwiYtmMpIp_rM_kM20wsWqS7JvSm6335Qq_px7mZqeUzMFjqsWSM6zSWQ0Btt1hXq9lBXuc0ztotW97jth4Z21mglMJun2F_12rpJa99Yt6j1hRSdXYxUbqiFSeo5Vhfc3c7g1VMt7YxgJmWn0S3lzule_aWosB017WW3rn5WB_-iSwfyuwbSXe4";
    private static final String STORAGE_IMAGE_URL = "https://storage.googleapis.com/storage/v1/b/bucket-bicho/o/perros.jpg?alt=media";
    private static final String QUOTES_URL = "https://api.breakingbadquotes.xyz/v1/quotes/1";
    
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    
    /**
     * Muestra la respuesta HTTP, manejando correctamente las respuestas binarias (como imágenes).
     * @param title El título a imprimir.
     * @param url La URL de la solicitud.
     * @param accessToken El token de acceso Bearer opcional.
     * @param bodyHandler Manejador del cuerpo para la respuesta (String, byte[], etc.)
     * @param <T> Tipo del cuerpo de la respuesta.
     */
    private static <T> void getAndPrint(String title, String url, Optional<String> accessToken, HttpResponse.BodyHandler<T> bodyHandler) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    
                    .header("User-Agent", "Java/17 HttpClientSynchronous (Practica ESCOM)") 
                    .GET();
            
            
            accessToken.ifPresent(token -> 
                requestBuilder.header("Authorization", "Bearer " + token)
            );

            HttpRequest request = requestBuilder.build();

            // 3. Uso del BodyHandler genérico para mayor flexibilidad
            HttpResponse<T> response =
                    httpClient.send(request, bodyHandler);

            System.out.println("\n=== " + title + " ===");
            System.out.println("GET " + url);
            System.out.println("HTTP " + response.statusCode());
            
            
            System.out.println("--- Encabezados de Respuesta ---");
            response.headers().map().forEach((k, v) -> 
                System.out.println(k + ": " + String.join(", ", v)) 
            );

            System.out.println("--- Cuerpo ---");
            
            // 4. Manejo más robusto del cuerpo
            Object body = response.body();
            if (body instanceof String) {
                String bodyStr = (String) body;
                if (bodyStr.length() > 500) {
                    System.out.println(bodyStr.substring(0, 500) + "...\n[Cuerpo de texto truncado.]");
                } else {
                    System.out.println(bodyStr);
                }
            } else if (body instanceof byte[]) {
                byte[] bodyBytes = (byte[]) body;
                System.out.printf("[Contenido Binario: %d bytes. Código de estado: %d]\n", bodyBytes.length, response.statusCode());
                
                // System.out.println("Primeros bytes: " + new String(bodyBytes, 0, Math.min(20, bodyBytes.length)));
            } else {
                System.out.println("[Tipo de cuerpo no manejado: " + body.getClass().getSimpleName() + "]");
            }


        } catch (Exception e) {
            System.out.println("\n=== " + title + " ===");
            System.out.println("GET " + url);
            // 5. Manejo específico de Timeout
            if (e instanceof HttpTimeoutException) {
                System.out.println("FALLÓ: Tiempo de espera (Timeout) agotado.");
            } else {
                System.out.println("FALLÓ: " + e.getClass().getSimpleName() + " -> " + e.getMessage());
            }
        }
    }

    // Wrapper para llamadas con body string y sin token
    private static void getAndPrintString(String title, String url) {
        getAndPrint(title, url, Optional.empty(), HttpResponse.BodyHandlers.ofString());
    }
    
    // Wrapper para llamadas con body string y token
    private static void getAndPrintStringWithToken(String title, String url, String token) {
        getAndPrint(title, url, Optional.of(token), HttpResponse.BodyHandlers.ofString());
    }

    // Wrapper para llamadas con body bytes (para imágenes) y token
    private static void getAndPrintBytesWithToken(String title, String url, String token) {
        getAndPrint(title, url, Optional.of(token), HttpResponse.BodyHandlers.ofByteArray());
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        
        // 1. Acceso a Google Storage (BINARIO - IMAGEN)
        
        getAndPrintBytesWithToken(
            "Google Storage (Bucket bicho/perros.jpg - BINARIO)", 
            STORAGE_IMAGE_URL, 
            GOOGLE_STORAGE_TOKEN
        ); 
        
        // 2. Breaking Bad Quotes (TEXTO - JSON)
        
        getAndPrintString(
            "Breaking Bad Quotes (JSON)", 
            QUOTES_URL
        );
    }
}