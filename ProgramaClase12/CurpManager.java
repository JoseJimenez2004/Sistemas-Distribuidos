package ProgramaClase12;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class CurpManager {

    public static String getCURP() {
        String letra = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numero = "0123456789";
        String sexo = "HM";
        String[] entidad = {"AS", "BC", "BS", "CC", "CS", "CH", "CL", "CM", "DF", "DG", "GT", "GR", "HG", "JC",
                "MC", "MN", "MS", "NT", "NL", "OC", "PL", "QT", "QR", "SP", "SL", "SR", "TC", "TL", "TS",
                "VZ", "YN", "ZS"};

        StringBuilder sb = new StringBuilder(18);

        // 4 letras
        for (int i = 0; i < 4; i++) {
            sb.append(letra.charAt(ThreadLocalRandom.current().nextInt(letra.length())));
        }

        // 6 números
        for (int i = 0; i < 6; i++) {
            sb.append(numero.charAt(ThreadLocalRandom.current().nextInt(numero.length())));
        }

        // Sexo
        sb.append(sexo.charAt(ThreadLocalRandom.current().nextInt(sexo.length())));

        // Entidad
        sb.append(entidad[ThreadLocalRandom.current().nextInt(entidad.length)]);

        // 3 consonantes
        String consonante = "BCDFGHJKLMNPQRSTVWXYZ";
        for (int i = 0; i < 3; i++) {
            sb.append(consonante.charAt(ThreadLocalRandom.current().nextInt(consonante.length())));
        }

        // 2 dígitos homoclave
        for (int i = 0; i < 2; i++) {
            sb.append(numero.charAt(ThreadLocalRandom.current().nextInt(numero.length())));
        }

        return sb.toString();
    }

    public static ArrayList<String> ordenarCurps(ArrayList<String> curps) {
        ArrayList<String> ordenadas = new ArrayList<>();

        for (String newCURP : curps) {
            int insertIndex = 0;

            // recorre la lista para encontrar la posición correcta
            for (int j = 0; j < ordenadas.size(); j++) {
                if (newCURP.substring(0, 4).compareTo(ordenadas.get(j).substring(0, 4)) > 0) {
                    insertIndex = j + 1;
                } else {
                    break;
                }
            }
            ordenadas.add(insertIndex, newCURP);
        }
        return ordenadas;
    }

    public static void main(String[] args) {
        int m, n;
        try {
            m = Integer.parseInt(args[0]); // número de listas
            n = Integer.parseInt(args[1]); // número de CURPs por lista
        } catch (NumberFormatException e) {
            System.out.println("Error: Los argumentos deben ser números enteros.");
            return;
        }

        // Contenedor: ArrayList de ArrayLists
        ArrayList<ArrayList<String>> listas = new ArrayList<>();

        // Generar m listas con n CURPs cada una
        for (int i = 0; i < m; i++) {
            ArrayList<String> curps = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                curps.add(getCURP());
            }
            listas.add(curps);
        }

        // Imprimir listas desordenadas
        System.out.println("===== CURPs DESORDENADAS =====");
        for (int i = 0; i < listas.size(); i++) {
            System.out.println("Lista " + (i + 1) + ": " + listas.get(i));
        }

        // Ordenar e imprimir listas
        System.out.println("\n===== CURPs ORDENADAS =====");
        for (int i = 0; i < listas.size(); i++) {
            ArrayList<String> ordenadas = ordenarCurps(listas.get(i));
            System.out.println("Lista " + (i + 1) + ": " + ordenadas);
        }
    }
}
