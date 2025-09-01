//Encontrar los números entre el 1 y el 5000 que cumplen la regla de que la suma del cubo de sus 
//dígitos es igual al número mismo. Ejemplo: 153 = 13 + 53 + 33 
import java.util.Scanner;
public class Problema11 {       
    public static void main(String[] args) {
        System.out.println("Números entre 1 y 5000 que cumplen la regla de que la suma del cubo de sus dígitos es igual al número mismo:");

        for (int numero = 1; numero <= 5000; numero++) {
            int sumaCubos = 0;
            int temp = numero;

            while (temp > 0) {
                int digito = temp % 10;
                sumaCubos += digito * digito * digito;
                temp /= 10;
            }

            if (sumaCubos == numero) {
                System.out.println(numero);
            }
        }
    }
}