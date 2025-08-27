// Escribe un programa que reciba una cantidad en grados Centígrados
// e indique a cuánto equivale en grados Fahrenheit.
// Fórmula: F = 32 + (9 * C / 5).

import java.util.Scanner;

public class Problema1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la temperatura en grados Centígrados: ");
        double centigrados = scanner.nextDouble();

        double fahrenheit = 32 + (9 * centigrados / 5);

        System.out.println("La temperatura en grados Fahrenheit es: " + fahrenheit);

        scanner.close();
    }
}
