package ProgramaClase19;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
 
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
 
public class WebServerTokens {
    private static final String STATUS_ENDPOINT = "/status";
    private static final String SEARCHTOKEN_ENDPOINT = "/searchtoken";
 
    private final int port;
    private HttpServer server;
 
    public static void main(String[] args) {
        int serverPort = 8081;
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]);
        }
 
        WebServerTokens webServer = new WebServerTokens(serverPort);
        webServer.startServer();
 
        System.out.println("Servidor escuchando en el puerto " + serverPort);
    }
 
    public WebServerTokens(int port) {
        this.port = port;
    }
 
    public void startServer() {
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
 
        HttpContext statusContext = server.createContext(STATUS_ENDPOINT);
        HttpContext searchTokenContext = server.createContext(SEARCHTOKEN_ENDPOINT);
 
        statusContext.setHandler(this::handleStatusCheckRequest);
        searchTokenContext.setHandler(this::handleSearchTokenRequest);
 
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }
 
    private void handleStatusCheckRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }
        String responseMessage = "El servidor está vivo\n";
        sendResponse(responseMessage.getBytes(), exchange);
    }
 
    private void handleSearchTokenRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }
 
        long startTime = System.nanoTime();
 
        // Leer cuerpo de la petición
        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String body = new String(requestBytes).trim();
 
        // Ejemplo: "34567890,SOL"
        String[] parts = body.split(",");
        if (parts.length != 2) {
            String error = "Formato incorrecto. Use: <numTokens>,<SUBCADENA>\n";
            sendResponse(error.getBytes(), exchange);
            return;
        }
 
        int n;
        try {
            n = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            String error = "El número de tokens no es válido.\n";
            sendResponse(error.getBytes(), exchange);
            return;
        }
 
        String subcadena = parts[1].toUpperCase();
 
        // === Generar cadenota con n tokens ===
        Random random = new Random();
        StringBuilder cadenota = new StringBuilder();
 
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                char letra = (char) (random.nextInt(26) + 65); // A-Z
                cadenota.append(letra);
            }
            cadenota.append(" ");
        }
 
        // === Buscar subcadena ===
        int contador = 0;
        int index = cadenota.indexOf(subcadena);
        while (index != -1) {
            contador++;
            index = cadenota.indexOf(subcadena, index + 1);
        }
 
        long finishTime = System.nanoTime();
        long elapsedNs = finishTime - startTime;
        long seconds = elapsedNs / 1_000_000_000;
        long millis = (elapsedNs / 1_000_000) % 1000;
 
        // Revisar headers
        Headers headers = exchange.getRequestHeaders();
        boolean debug = headers.containsKey("X-Debug") &&
                headers.get("X-Debug").get(0).equalsIgnoreCase("true");
 
        if (debug) {
            exchange.getResponseHeaders().put("X-Debug-Info",
                    Arrays.asList(String.format("La operación tomó %d nanosegundos = %d segundos con %d milisegundos",
                            elapsedNs, seconds, millis)));
        }
 
        String responseMessage = String.format("La subcadena '%s' apareció %d veces.\n", subcadena, contador);
        sendResponse(responseMessage.getBytes(), exchange);
    }
 
    private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
        exchange.close();
    }
}
