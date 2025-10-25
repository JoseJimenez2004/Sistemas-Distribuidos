package ProgramaClase23;

import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import com.google.gson.*;

public class GsonExample {
    private static final String GOOGLE_API_KEY = "AIzaSyDPkRv3Gos4bSysGTXSGzNX9KdNx23EbR4";
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        String BREAKING_BAD_URL = "https://api.breakingbadquotes.xyz/v1/quotes/3";

        HttpRequest reqQuotes = HttpRequest.newBuilder()
            .uri(URI.create(BREAKING_BAD_URL))
            .GET()
            .build();

        HttpResponse<String> respQuotes = client.send(reqQuotes, HttpResponse.BodyHandlers.ofString());
        String bodyQuotes = respQuotes.body();

        System.out.println("---- Respuesta API Breaking Bad ----");
        System.out.println(bodyQuotes);
        System.out.println("------------------------------------");

        // 2) Parsear JSON de las 3 citas
        JsonArray quotesArray = JsonParser.parseString(bodyQuotes).getAsJsonArray();

        for (int i = 0; i < quotesArray.size(); i++) {
            JsonObject quoteObj = quotesArray.get(i).getAsJsonObject();
            String quoteText = quoteObj.get("quote").getAsString();
            String author = quoteObj.get("author").getAsString();

            // 3) Llamar a Google Cloud Translation
            String encodedText = URLEncoder.encode(quoteText, StandardCharsets.UTF_8);
            String googleUrl = "https://translation.googleapis.com/language/translate/v2?key=" + GOOGLE_API_KEY
                + "&q=" + encodedText + "&target=es";

            HttpRequest reqTranslate = HttpRequest.newBuilder()
                .uri(URI.create(googleUrl))
                .GET()
                .build();

            HttpResponse<String> respTranslate = client.send(reqTranslate, HttpResponse.BodyHandlers.ofString());
            String bodyTranslate = respTranslate.body();

            // 4) Extraer texto traducido
            String translatedText = "";
            try {
                JsonObject respJson = JsonParser.parseString(bodyTranslate).getAsJsonObject();
                JsonObject data = respJson.getAsJsonObject("data");
                JsonArray translations = data.getAsJsonArray("translations");
                translatedText = translations.get(0).getAsJsonObject().get("translatedText").getAsString();
            } catch (Exception e) {
                translatedText = "[Error al traducir]";
            }

            // 5) Mostrar resultados
            System.out.println("Cita (EN): " + quoteText);
            System.out.println("Autor: " + author);
            System.out.println("Traducción (ES): " + translatedText);
            System.out.println("----------------------------------------------------");
        }
    }
}
