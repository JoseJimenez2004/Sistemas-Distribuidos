//Leer un arreglo de 10 nombres de ciudades e indicar cuál es la que tiene el nombre más largo. 
import java.util.Scanner;
public class Problema18 {    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] ciudades = new String[10];

        System.out.println("Ingrese 10 nombres de ciudades:");

        for (int i = 0; i < 10; i++) {
            System.out.print("Ciudad " + (i + 1) + ": ");
            ciudades[i] = scanner.nextLine();
        }

        String ciudadMasLarga = "";
        for (String ciudad : ciudades) {
            if (ciudad.length() > ciudadMasLarga.length()) {
                ciudadMasLarga = ciudad;
            }
        }

        System.out.println("La ciudad con el nombre más largo es: " + ciudadMasLarga);

        scanner.close();
    }
}