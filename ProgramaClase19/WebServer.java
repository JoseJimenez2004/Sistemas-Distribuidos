package ProgramaClase19;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;

public class WebServer {
    private static final String TASK_ENDPOINT = "/task";
    private static final String STATUS_ENDPOINT = "/status";
    private static final String SEARCHTOKEN_ENDPOINT = "/searchtoken";

    private final int port;
    private HttpServer server;

    public static void main(String[] args) {
        int serverPort = 8081;
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]);
        }

        WebServer webServer = new WebServer(serverPort);
        webServer.startServer();

        System.out.println("Servidor escuchando en el puerto " + serverPort);
    }

    public WebServer(int port) {
        this.port = port;
    }

    public void startServer() {
        try {
            this.server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Registrar endpoints
        server.createContext(STATUS_ENDPOINT, this::handleStatusCheckRequest);
        server.createContext(TASK_ENDPOINT, this::handleTaskRequest);
        server.createContext(SEARCHTOKEN_ENDPOINT, this::handleSearchTokenRequest);

        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    // ------------------ /task ------------------
    private void handleTaskRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }

        Headers headers = exchange.getRequestHeaders();
        boolean isDebugMode = headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true");

        long startTime = System.nanoTime();

        byte[] requestBytes = exchange.getRequestBody().readAllBytes();

        // Imprimir headers
        System.out.println("Cantidad de headers recibidos: " + headers.size());
        for (String key : headers.keySet()) {
            System.out.println("Header: " + key + " -> " + headers.get(key));
        }

        // Imprimir cuerpo
        System.out.println("El cuerpo del mensaje contiene " + requestBytes.length + " bytes");
        String bodyContent = new String(requestBytes);
        System.out.println("Contenido del cuerpo recibido: " + bodyContent);

        // Calcular multiplicación
        byte[] responseBytes = calculateResponse(requestBytes);

        long finishTime = System.nanoTime();
        if (isDebugMode) {
            addDebugHeader(exchange, startTime, finishTime);
        }

        sendResponse(responseBytes, exchange);
    }

    // ------------------ /searchtoken ------------------
    private void handleSearchTokenRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("post")) {
            exchange.close();
            return;
        }

        Headers headers = exchange.getRequestHeaders();
        boolean isDebugMode = headers.containsKey("X-Debug") && headers.get("X-Debug").get(0).equalsIgnoreCase("true");

        long startTime = System.nanoTime();

        byte[] requestBytes = exchange.getRequestBody().readAllBytes();
        String body = new String(requestBytes).trim();
        System.out.println("Cuerpo recibido para /searchtoken: " + body);

        String[] parts = body.split(",");
        if (parts.length != 2) {
            sendResponse("Formato incorrecto. Use: <numTokens>,<subcadena>".getBytes(), exchange);
            return;
        }

        int numTokens;
        String subcadena = parts[1].toUpperCase();
        try {
            numTokens = Integer.parseInt(parts[0]);
        } catch (NumberFormatException e) {
            sendResponse("Número de tokens inválido.".getBytes(), exchange);
            return;
        }

        // Generar cadenota aleatoria
        char[] cadenota = new char[numTokens * 4]; // 3 letras + 1 espacio
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        for (int i = 0; i < numTokens; i++) {
            for (int j = 0; j < 3; j++) {
                cadenota[i * 4 + j] = letras.charAt(rand.nextInt(letras.length()));
            }
            cadenota[i * 4 + 3] = ' ';
        }

        // Contar ocurrencias de la subcadena
        int ocurrencias = 0;
        for (int i = 0; i < numTokens * 4; i += 4) {
            if (cadenota[i] == subcadena.charAt(0) &&
                cadenota[i + 1] == subcadena.charAt(1) &&
                cadenota[i + 2] == subcadena.charAt(2)) {
                ocurrencias++;
            }
        }

        String resultado = "Total de ocurrencias de '" + subcadena + "': " + ocurrencias;
        byte[] responseBytes = resultado.getBytes();

        long finishTime = System.nanoTime();
        if (isDebugMode) {
            addDebugHeader(exchange, startTime, finishTime);
        }

        sendResponse(responseBytes, exchange);
    }

    // ------------------ /status ------------------
    private void handleStatusCheckRequest(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equalsIgnoreCase("get")) {
            exchange.close();
            return;
        }
        sendResponse("El servidor está vivo\n".getBytes(), exchange);
    }

    // ------------------ Helpers ------------------
    private void addDebugHeader(HttpExchange exchange, long startTime, long finishTime) {
        long durationNs = finishTime - startTime;
        long seconds = durationNs / 1_000_000_000;
        long milliseconds = (durationNs / 1_000_000) % 1000;
        String debugMessage = String.format(
                "La operación tomó %d nanosegundos = %d segundos con %d milisegundos",
                durationNs, seconds, milliseconds);
        exchange.getResponseHeaders().put("X-Debug-Info", Arrays.asList(debugMessage));
    }

    private byte[] calculateResponse(byte[] requestBytes) {
        String bodyString = new String(requestBytes);
        String[] stringNumbers = bodyString.split(",");

        BigInteger result = BigInteger.ONE;
        for (String number : stringNumbers) {
            BigInteger bigInteger = new BigInteger(number);
            result = result.multiply(bigInteger);
        }

        return String.format("El resultado de la multiplicación es %s\n", result).getBytes();
    }

    private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.flush();
        os.close();
        exchange.close();
    }
}
