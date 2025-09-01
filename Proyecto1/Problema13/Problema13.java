//Calcular cuánto dinero tendría en una cuenta de ahorro al final de 20 años si al inicio de cada 
//año añado $10,000, el rendimiento anual es 5% y reinvierto las ganancias obtenidas cada año.
import java.util.Scanner;
public class Problema13 {   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double ahorroAnual = 10000;
        double tasaInteres = 0.05;
        int años = 20;

        double totalAhorro = 0;

        for (int i = 1; i <= años; i++) {
            totalAhorro += ahorroAnual; // Añadir el ahorro anual
            totalAhorro += totalAhorro * tasaInteres; // Añadir el interés ganado
        }

        System.out.printf("El total en la cuenta de ahorro después de %d años es: $%.2f%n", años, totalAhorro);

        scanner.close();
    }
}