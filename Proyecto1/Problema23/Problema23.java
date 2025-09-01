//Escribe un programa que lea un archivo de texto y que escriba en otro archivo solo las líneas 
//impares del archivo original.
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
public class Problema23 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre del archivo de entrada (con extensión .txt): ");
        String archivoEntrada = scanner.nextLine();

        System.out.print("Ingrese el nombre del archivo de salida (con extensión .txt): ");
        String archivoSalida = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada));
             BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {

            String linea;
            int numeroLinea = 1;

            while ((linea = br.readLine()) != null) {
                if (numeroLinea % 2 != 0) { // Si la línea es impar
                    bw.write(linea);
                    bw.newLine();
                }
                numeroLinea++;
            }

            System.out.println("Las líneas impares se han escrito en " + archivoSalida);

        } catch (IOException e) {
            System.out.println("Ocurrió un error al procesar los archivos: " + e.getMessage());
        }

        scanner.close();
    }
}