//Calcular el cobro que una caseta de cuota debe realizar a un vehiculo de acuerdo con las
//siguientes reglas. Motocicletas = $20, 2 ejes(automoviles) = $40, 3 ejes (camionetas) = $60, 4, 5 y ejes
//(caiones de carga) = $250, eje adicional $50. Por ejemplo, si llega un trailer de 8 ejes se deben cobrar $350

import java.util.Scanner;
public class Problema6 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el número de ejes del vehículo: ");
        int numeroEjes = scanner.nextInt();

        int cobro;

        if (numeroEjes == 2) {
            cobro = 40;
        } else if (numeroEjes == 3) {
            cobro = 60;
        } else if (numeroEjes >= 4 && numeroEjes <= 5) {
            cobro = 250;
        } else if (numeroEjes > 5) {
            cobro = 250 + (numeroEjes - 5) * 50;
        } else if (numeroEjes == 1) {
            cobro = 20;
        } else {
            System.out.println("Número de ejes no válido. Debe ser un número positivo.");
            scanner.close();
            return;
        }

        System.out.println("El cobro para el vehículo es: $" + cobro);

        scanner.close();
    }
}