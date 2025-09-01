//Escribe un programa que solicite al usuario los tamaños de las dos matrices a multiplicar y luego 
//solicite los valores, realice la multiplicación y muestre el resultado.  
import java.util.Scanner;
public class Problema22 {   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el número de filas de la primera matriz: ");
        int filasA = scanner.nextInt();
        System.out.print("Ingrese el número de columnas de la primera matriz (y filas de la segunda): ");
        int columnasA = scanner.nextInt();
        System.out.print("Ingrese el número de columnas de la segunda matriz: ");
        int columnasB = scanner.nextInt();

        int[][] matrizA = new int[filasA][columnasA];
        int[][] matrizB = new int[columnasA][columnasB];
        int[][] resultado = new int[filasA][columnasB];

        System.out.println("Ingrese los elementos de la primera matriz:");
        for (int i = 0; i < filasA; i++) {
            for (int j = 0; j < columnasA; j++) {
                System.out.print("Elemento [" + i + "][" + j + "]: ");
                matrizA[i][j] = scanner.nextInt();
            }
        }

        System.out.println("Ingrese los elementos de la segunda matriz:");
        for (int i = 0; i < columnasA; i++) {
            for (int j = 0; j < columnasB; j++) {
                System.out.print("Elemento [" + i + "][" + j + "]: ");
                matrizB[i][j] = scanner.nextInt();
            }
        }

        // Multiplicación de matrices
        for (int i = 0; i < filasA; i++) {
            for (int j = 0; j < columnasB; j++) {
                for (int k = 0; k < columnasA; k++) {
                    resultado[i][j] += matrizA[i][k] * matrizB[k][j];
                }
            }
        }

        System.out.println("El resultado de la multiplicación de las matrices es:");
        for (int i = 0; i < filasA; i++) {
            for (int j = 0; j < columnasB; j++) {
                System.out.print(resultado[i][j] + " ");
            }
            System.out.println();
        }

        scanner.close();
    }
}