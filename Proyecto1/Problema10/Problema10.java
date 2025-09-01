//scribe un programa que solicite al usuario un número entero y dé como resultado la suma de 
//todos los números desde el 1 hasta dicho número. 
import java.util.Scanner;
public class Problema10 {   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese un número entero positivo: ");
        int numero = scanner.nextInt();

        if (numero < 1) {
            System.out.println("El número debe ser un entero positivo mayor o igual a 1.");
            scanner.close();
            return;
        }

        int suma = 0;
        for (int i = 1; i <= numero; i++) {
            suma += i;
        }

        System.out.println("La suma de todos los números desde 1 hasta " + numero + " es: " + suma);

        scanner.close();
    }
}