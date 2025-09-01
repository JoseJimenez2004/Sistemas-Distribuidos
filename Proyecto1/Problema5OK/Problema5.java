//Escribir un programa que indique cuantos dias de vacaciones le corresponde a un empleado
//considerando los años que ha trabajado en la empresa. Entre 1 y 5 años corresponden 5 dias de
//vacaciones, entre 6 y 10 años deben ser 10 dias de vacaciones, de alli en adelante, es un dia de
//vacaciones extra por cada año trabajando (a partir de 10), a partir de 20 años de trabajo se añaden 2 dias
//de vacaciones por cada año hasta un maximo de 45 dias.

import java.util.Scanner;
public class Problema5 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese los años trabajados en la empresa: ");
        int añosTrabajados = scanner.nextInt();

        int diasVacaciones;

        if (añosTrabajados >= 1 && añosTrabajados <= 5) {
            diasVacaciones = 5;
        } else if (añosTrabajados >= 6 && añosTrabajados <= 10) {
            diasVacaciones = 10;
        } else if (añosTrabajados > 10 && añosTrabajados < 20) {
            diasVacaciones = 10 + (añosTrabajados - 10);
        } else if (añosTrabajados >= 20) {
            diasVacaciones = Math.min(10 + (10) + 2 * (añosTrabajados - 20), 45);
        } else {
            System.out.println("Años trabajados no válidos. Deben ser un número positivo.");
            scanner.close();
            return;
        }

        System.out.println("Le corresponden " + diasVacaciones + " días de vacaciones.");

        scanner.close();
    }
}