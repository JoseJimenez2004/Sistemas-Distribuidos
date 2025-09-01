//La Comisión Federal de Electricidad cobra el consumo de electricidad de acuerdo con un 
//tabulador basado en los kilowatts consumidos en el periodo. La tabla es la siguiente: 
//Costo del kW para Hogares: 
//De 0 a 250 kW el costo por kW es de $0.65 
//De 251 a 500 kW el costo por kW es de $0.85 
//De 501 a 1200 kW el costo por kW es de $1.50 
//De 1201 a 2100 kW el costo por kW es de $2.50 
//De 2101 kW hacia arriba el costo por kW es de $3.00 
//Costo del kW para Negocios:  
//$5.00, el costo es fijo por kilowatt sin importar la cantidad consumida. 
//Ejemplo: 
//a) Si en el hogar se consumen 737 kW durante el periodo entonces el total a pagar es de: 
//250*0.65 + 250*0,85 + 237* 1.50 = 162.50 + 212.50 + 355.50 = $730.50 
//b) Si en un negocio se consumen 250 kW, el valor a pagar serían de: 250* 5 = $1,250 
//Escribe un programa que pregunte la cantidad de kW consumidos, si el cliente tiene un contrato de 
//Hogar o de Negocio y dé como resultado la cantidad a pagar. 

import java.util.Scanner;
public class Problema7 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese la cantidad de kW consumidos: ");
        int kwConsumidos = scanner.nextInt();

        System.out.print("Ingrese el tipo de contrato (H para Hogar, N para Negocio): ");
        String tipoContrato = scanner.next().toUpperCase();

        double totalPagar = 0;

        if (kwConsumidos < 0) {
            System.out.println("Cantidad de kW no válida. Debe ser un número positivo.");
            scanner.close();
            return;
        }

        if (tipoContrato.equals("H")) {
            if (kwConsumidos <= 250) {
                totalPagar = kwConsumidos * 0.65;
            } else if (kwConsumidos <= 500) {
                totalPagar = 250 * 0.65 + (kwConsumidos - 250) * 0.85;
            } else if (kwConsumidos <= 1200) {
                totalPagar = 250 * 0.65 + 250 * 0.85 + (kwConsumidos - 500) * 1.50;
            } else if (kwConsumidos <= 2100) {
                totalPagar = 250 * 0.65 + 250 * 0.85 + 700 * 1.50 + (kwConsumidos - 1200) * 2.50;
            } else {
                totalPagar = 250 * 0.65 + 250 * 0.85 + 700 * 1.50 + 900 * 2.50 + (kwConsumidos - 2100) * 3.00;
            }
        } else if (tipoContrato.equals("N")) {
            totalPagar = kwConsumidos * 5.00;
        } else {
            System.out.println("Tipo de contrato no válido. Debe ser H o N.");
            scanner.close();
            return;
        }

        System.out.printf("El total a pagar es: $%.2f%n", totalPagar);

        scanner.close();
    }
}