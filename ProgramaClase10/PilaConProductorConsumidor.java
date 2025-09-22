package ProgramaClase10;

import java.util.Random;

public class PilaConProductorConsumidor {

    // Pila de 10 elementos
    private static final char[] pila = new char[10];
    private static int tope = 0; // indica dónde está el tope
    private static final Random random = new Random();

    // Método sincronizado para operar sobre la pila
    public static synchronized void operar(String tipo) {
        switch (tipo) {
            case "PRODUCIR":
                if (tope < pila.length) {
                    char c = (char) ('A' + random.nextInt(26)); // letra aleatoria
                    pila[tope] = c;
                    tope++;
                    notificarCambio("Productor insertó: " + c);
                }
                break;

            case "CONSUMIR":
                if (tope > 0) {
                    char c = pila[tope - 1];
                    pila[tope - 1] = '\0'; // limpiar posición
                    tope--;
                    notificarCambio("Consumidor extrajo: " + c);
                }
                break;
        }
    }

    // Notificar y mostrar pila
    private static void notificarCambio(String mensaje) {
        // Borrar pantalla (funciona en la mayoría de consolas)
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println(mensaje);
        System.out.println("Contenido de la pila:");

        for (int i = pila.length - 1; i >= 0; i--) {
            if (i < tope) {
                System.out.println("[" + i + "] " + pila[i]);
            } else {
                System.out.println("[" + i + "] ");
            }
        }
        System.out.println("Tope actual: " + tope);
    }

    // Hilo Productor
    static class Productor implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(200 + random.nextInt(800)); // tiempo tp aleatorio
                    operar("PRODUCIR");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Hilo Consumidor
    static class Consumidor implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(2000 + random.nextInt(800)); // tiempo tc aleatorio
                    operar("CONSUMIR");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // El observador se integra en notificarCambio, así que no requiere hilo separado

    public static void main(String[] args) {
        Thread productor = new Thread(new Productor(), "Productor");
        Thread consumidor = new Thread(new Consumidor(), "Consumidor");

        productor.start();
        consumidor.start();
    }
}
