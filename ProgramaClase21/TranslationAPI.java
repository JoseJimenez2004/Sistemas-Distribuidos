package ProgramaClase21;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class TranslationAPI {
    public static void main(String[] args) {
        String texto = "People have the right to disagree with your opinions and to dissent.";
        String apiKey = "AIzaSyDPkRv3Gos4bSysGTXSGzNX9KdNx23EbR4";
        try {
            String urlStr = "https://translation.googleapis.com/language/translate/v2?target=es&key="
                            + apiKey + "&q=" + URLEncoder.encode(texto, StandardCharsets.UTF_8);
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            System.out.println("Código HTTP: " + con.getResponseCode());
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null)
                System.out.println(line);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
