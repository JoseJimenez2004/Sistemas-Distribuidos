package Programa1;

import java.util.Random;

public class Programa1 {
    public static void main(String[] args) {
        
        if (args.length == 0) {
            System.out.println("Por favor, proporciona el número de palabras como argumento.");
            return;
        }

        int n = 0;
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("El argumento debe ser un número entero.");
            return;
        }

        char[] cadenota = new char[n * 4]; // 3 letras + 1 espacio por palabra
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String objetivo = "IPN";
        Random rand = new Random();

        long inicio = System.currentTimeMillis();

        // Generar palabras y almacenar en cadenota
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                cadenota[i * 4 + j] = letras.charAt(rand.nextInt(letras.length()));
            }
            cadenota[i * 4 + 3] = ' '; // separador
        }

        int ocurrencias = 0;

        // Buscar "IPN" en cadenota
        for (int i = 0; i < n * 4; i += 4) { // saltamos palabra por palabra
            if (cadenota[i] == 'I' && cadenota[i + 1] == 'P' && cadenota[i + 2] == 'N') {
                ocurrencias++;
                System.out.println("¡Coincidencia encontrada en la posición " + i + "!");
            }
        }

        long fin = System.currentTimeMillis();
        double tiempoTotal = (fin - inicio) / 1000.0;

        System.out.println("\nResumen:");
        System.out.println("Total de ocurrencias de 'IPN': " + ocurrencias);
        System.out.println("Tiempo total transcurrido: " + tiempoTotal + " segundos.");
    }
}
