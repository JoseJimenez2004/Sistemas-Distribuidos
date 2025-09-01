//Leer una serie de 10 números, moverlos una posición hacia adelante en el arreglo y mostrar el 
//arreglo resultante. Ejemplo, tenemos el siguiente arreglo 1, 2, 3, 4, 5, si desplazamos los elementos una 
//posición hacia adelante debemos obtener 5, 1, 2, 3, 4. Es decir, el primer elemento se mueve hacia el 
//segundo lugar, el segundo al tercero, etc., y el último al primero. 
import java.util.Scanner;
public class Problema20 {    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] numeros = new int[10];

        System.out.println("Ingrese 10 números:");

        for (int i = 0; i < 10; i++) {
            System.out.print("Número " + (i + 1) + ": ");
            numeros[i] = scanner.nextInt();
        }

        // Desplazar los elementos una posición hacia adelante
        int ultimo = numeros[numeros.length - 1];
        for (int i = numeros.length - 1; i > 0; i--) {
            numeros[i] = numeros[i - 1];
        }
        numeros[0] = ultimo;

        System.out.println("Números después de desplazar una posición hacia adelante:");
        for (int numero : numeros) {
            System.out.println(numero);
        }

        scanner.close();
    }
}