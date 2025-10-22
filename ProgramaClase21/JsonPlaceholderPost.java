package ProgramaClase21;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


public class JsonPlaceholderPost {

    public static void main(String[] args) {
        final String ENDPOINT = "https://jsonplaceholder.typicode.com/posts";

        // === JSON A ENVIAR ===
        String jsonBody = """
                {
                    "userId": 1,
                    "title": "7CM4",
                    "body": "JOSE BRYAN OMAR JIMENEZ VELAZQUEZ"
                }
                """;

        try {
            System.out.println("=====================================");
            System.out.println("   🌐 PETICIÓN POST JSONPlaceholder   ");
            System.out.println("=====================================\n");

            // Configuración de la conexión
            URL url = new URL(ENDPOINT);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            // Enviar el cuerpo JSON
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Leer código de estado
            int status = conn.getResponseCode();
            System.out.println("📡 Código HTTP: " + status + "\n");

            // === MOSTRAR HEADERS ===
            System.out.println("===== HEADERS =====");
            for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
                System.out.printf("%-25s: %s%n", entry.getKey(), entry.getValue());
            }

            // === MOSTRAR BODY ===
            System.out.println("\n===== BODY =====");
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            System.out.println("\n✅ Solicitud completada exitosamente.");

        } catch (IOException e) {
            System.err.println("\n❌ Error de conexión o E/S:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("\n⚠️ Error inesperado:");
            e.printStackTrace();
        }
    }
}
