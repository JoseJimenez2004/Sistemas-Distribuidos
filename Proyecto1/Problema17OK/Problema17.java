//Leer 10 números enteros, guardarlos en orden inverso al que fueron introducidos y mostrarlos 
//en pantalla. 
import java.util.Scanner;
public class Problema17 {   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numeros = new int[10];

        System.out.println("Ingrese 10 números enteros:");

        for (int i = 0; i < 10; i++) {
            System.out.print("Número " + (i + 1) + ": ");
            numeros[i] = scanner.nextInt();
        }

        System.out.println("Números en orden inverso:");
        for (int i = 9; i >= 0; i--) {
            System.out.println(numeros[i]);
        }

        scanner.close();
    }
}