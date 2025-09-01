//Lee la definición de números capicúa y escribe un programa que reciba un número entre 0 y 
//9999 e indique si es un número capicúa. Nota, no es necesario utilizar ciclos, es posible realizarlo 
//utilizando divisiones enteras. 
import java.util.Scanner;
public class Problema9 {    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese un número entre 0 y 9999: ");
        int numero = scanner.nextInt();

        if (numero < 0 || numero > 9999) {
            System.out.println("Número fuera de rango. Debe estar entre 0 y 9999.");
            scanner.close();
            return;
        }

        int digito1 = numero / 1000; // Primer dígito
        int digito2 = (numero / 100) % 10; // Segundo dígito
        int digito3 = (numero / 10) % 10; // Tercer dígito
        int digito4 = numero % 10; // Cuarto dígito

        boolean esCapicua = false;

        if (numero < 10) { // Números de un dígito
            esCapicua = true;
        } else if (numero < 100) { // Números de dos dígitos
            esCapicua = (digito1 == digito4);
        } else if (numero < 1000) { // Números de tres dígitos
            esCapicua = (digito1 == digito3);
        } else { // Números de cuatro dígitos
            esCapicua = (digito1 == digito4) && (digito2 == digito3);
        }

        if (esCapicua) {
            System.out.println(numero + " es un número capicúa.");
        } else {
            System.out.println(numero + " no es un número capicúa.");
        }

        scanner.close();
    }
}