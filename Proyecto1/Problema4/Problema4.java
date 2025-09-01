//Una universidad comenzara a asignar a sus estudiantes a diferentes dormitorios segun su sexo y
// edad. Escribe un programa que solicite el sexo (H/M) y edad e indique de acuerdo con la siguiente tabla
//en que edicio deben ser asignados. Validar que el sexo y edad sean valores correctos.
// Hombre, 18 años = Edificio A
// Mujer, 18 años = Edificio B
// Hombre, 19 a 22 años = Edificio C
// Mujer, 19 a 22 años = Edificio D
// Hombre, Mayor de 22 años = Edificio E1
// Mujer, Mayor de 22 años = Edificio E2

import java.util.Scanner;
public class Problema4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el sexo (H/M): ");
        String sexo = scanner.nextLine().toUpperCase();

        System.out.print("Ingrese la edad: ");
        int edad = scanner.nextInt();

        if ((sexo.equals("H") || sexo.equals("M")) && edad > 0) {
            if (sexo.equals("H")) {
                if (edad == 18) {
                    System.out.println("Asignado al Edificio A");
                } else if (edad >= 19 && edad <= 22) {
                    System.out.println("Asignado al Edificio C");
                } else if (edad > 22) {
                    System.out.println("Asignado al Edificio E1");
                } else {
                    System.out.println("Edad no válida para asignación.");
                }
            } else if (sexo.equals("M")) {
                if (edad == 18) {
                    System.out.println("Asignado al Edificio B");
                } else if (edad >= 19 && edad <= 22) {
                    System.out.println("Asignado al Edificio D");
                } else if (edad > 22) {
                    System.out.println("Asignado al Edificio E2");
                } else {
                    System.out.println("Edad no válida para asignación.");
                }
            }
        } else {
            System.out.println("Sexo o edad no válidos. Por favor ingrese H o M para sexo y una edad positiva.");
        }

        scanner.close();
    }
}