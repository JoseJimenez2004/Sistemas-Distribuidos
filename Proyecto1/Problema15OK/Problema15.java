 //En este programa la computadora debe elegir un número al azar entre 1 y 100 y solicitará al 
//usuario que “adivine” el número. A cada intento del usuario la computadora debe indicar si el número 
//que el usuario introdujo es mayor o menor que el número prefijado. El programa debe terminar cuando 
//el usuario “adivine” el número o introduzca el número “0” para salir. 
import java.util.Scanner;
import java.util.Random;
public class Problema15 {   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        int numeroSecreto = random.nextInt(100) + 1; // Número aleatorio entre 1 y 100
        int intento;

        System.out.println("Adivina el número entre 1 y 100 (ingresa 0 para salir):");

        while (true) {
            System.out.print("Tu intento: ");
            intento = scanner.nextInt();

            if (intento == 0) {
                System.out.println("Has salido del juego. El número era: " + numeroSecreto);
                break;
            }

            if (intento < numeroSecreto) {
                System.out.println("El número es mayor.");
            } else if (intento > numeroSecreto) {
                System.out.println("El número es menor.");
            } else {
                System.out.println("¡Felicidades! Has adivinado el número.");
                break;
            }
        }

        scanner.close();
    }
}