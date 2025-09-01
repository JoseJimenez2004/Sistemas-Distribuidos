//El programa debe leer números mientras sean diferentes de 0. Posteriormente calcular el 
//promedio de los números leídos.
import java.util.Scanner;
public class Problema16 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int suma = 0;
        int contador = 0;

        System.out.println("Ingrese números enteros (ingrese 0 para finalizar):");

        while (true) {
            int numero = scanner.nextInt();

            if (numero == 0) {
                break;
            }

            suma += numero;
            contador++;
        }

        if (contador > 0) {
            double promedio = (double) suma / contador;
            System.out.println("El promedio de los números ingresados es: " + promedio);
        } else {
            System.out.println("No se ingresaron números.");
        }

        scanner.close();
    }
}