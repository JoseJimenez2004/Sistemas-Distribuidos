//Escribe un programa que lea un archivo de texto plano, lo analice y obtenga la cantidad de veces 
//que se encuentra cada letra en el archivo. 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Problema24 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre del archivo de texto (con extensión .txt): ");
        String nombreArchivo = scanner.nextLine();

        Map<Character, Integer> conteoLetras = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                for (char c : linea.toCharArray()) {
                    if (Character.isLetter(c)) {
                        c = Character.toLowerCase(c); // Convertir a minúscula para contar sin distinción
                        conteoLetras.put(c, conteoLetras.getOrDefault(c, 0) + 1);
                    }
                }
            }

            System.out.println("Conteo de letras en el archivo:");
            for (Map.Entry<Character, Integer> entry : conteoLetras.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

        } catch (IOException e) {
            System.out.println("Ocurrió un error al leer el archivo: " + e.getMessage());
        }

        scanner.close();
    }
}