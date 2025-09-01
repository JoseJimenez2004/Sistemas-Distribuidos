//Escribe un programa que calcule el radio de la circunferencia inscruta en un triangulo.
import java.util.Scanner;
public class Problema3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el valor del lado a: ");
        double a = scanner.nextDouble();

        System.out.print("Ingrese el valor del lado b: ");
        double b = scanner.nextDouble();

        System.out.print("Ingrese el valor del lado c: ");
        double c = scanner.nextDouble();

        double s = (a + b + c) / 2; // Semiperimetro
        double area = Math.sqrt(s * (s - a) * (s - b) * (s - c)); // Area usando la formula de Heron
        double radio = area / s; // Radio de la circunferencia inscrita

        System.out.printf("El radio de la circunferencia inscrita es: %.2f\n", radio);
        
        scanner.close();
    }
}
