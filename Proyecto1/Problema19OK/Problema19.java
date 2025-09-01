//Leer en un arreglo una serie de 10 números e indicar si todos los elementos están ordenados de 
//forma descendente, es decir, si cumplen la regla de que cada elemento del arreglo es menor o igual que 
//el elemento anterior. 
import java.util.Scanner;
public class Problema19 {    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numeros = new int[10];

        System.out.println("Ingrese 10 números:");

        for (int i = 0; i < 10; i++) {
            System.out.print("Número " + (i + 1) + ": ");
            numeros[i] = scanner.nextInt();
        }

        boolean esDescendente = true;
        for (int i = 1; i < numeros.length; i++) {
            if (numeros[i] > numeros[i - 1]) {
                esDescendente = false;
                break;
            }
        }

        if (esDescendente) {
            System.out.println("Los números están ordenados de forma descendente.");
        } else {
            System.out.println("Los números no están ordenados de forma descendente.");
        }

        scanner.close();
    }
}