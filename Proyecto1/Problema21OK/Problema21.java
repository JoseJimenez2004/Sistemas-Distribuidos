//Escribe un programa que lea un arreglo bidimensional de 5x5 y muestre la suma del total del 
//arreglo.  
import java.util.Scanner;
public class Problema21 {   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[][] matriz = new int[5][5];
        int sumaTotal = 0;

        System.out.println("Ingrese los elementos de la matriz 5x5:");

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print("Elemento [" + i + "][" + j + "]: ");
                matriz[i][j] = scanner.nextInt();
                sumaTotal += matriz[i][j];
            }
        }

        System.out.println("La suma total de los elementos de la matriz es: " + sumaTotal);

        scanner.close();
    }
}