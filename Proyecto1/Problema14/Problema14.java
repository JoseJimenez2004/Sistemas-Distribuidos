//Escribe un programa que lea un número X y un número Y. Mostrar los números de Y en Y, 
//comenzando en X hasta llegar a 200. Ejmplo: X = 8, Y = 2, el resultado comenzaría de la siguiente 
//manera: 8, 10, 12, 14, 16, 18 … 
import java.util.Scanner;
public class Problema14 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el número X (punto de inicio): ");
        int x = scanner.nextInt();

        System.out.print("Ingrese el número Y (incremento): ");
        int y = scanner.nextInt();

        if (y <= 0) {
            System.out.println("El número Y debe ser un entero positivo mayor que 0.");
            scanner.close();
            return;
        }

        System.out.println("Números desde " + x + " hasta 200, incrementando de " + y + " en " + y + ":");
        for (int i = x; i <= 200; i += y) {
            System.out.println(i);
        }

        scanner.close();
    }
}