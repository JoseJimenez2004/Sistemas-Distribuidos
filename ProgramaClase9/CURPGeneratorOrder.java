package ProgramaClase9;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CURPGeneratorOrder {

    public static String getCURP() {
        String letra = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numero = "0123456789";
        String sexo = "HM";
        String[] entidad = {"AS", "BC", "BS", "CC", "CS", "CH", "CL", "CM", "DF", "DG", "GT", "GR", "HG", "JC", "MC", "MN", "MS", "NT", "NL", "OC", "PL", "QT", "QR", "SP", "SL", "SR", "TC", "TL", "TS", "VZ", "YN", "ZS"};

        StringBuilder sb = new StringBuilder(18);

        // letras Random
        for (int i = 0; i < 4; i++) {
            sb.append(letra.charAt(ThreadLocalRandom.current().nextInt(letra.length())));
        }

        // Numeros Random
        for (int i = 0; i < 6; i++) {
            sb.append(numero.charAt(ThreadLocalRandom.current().nextInt(numero.length())));
        }

        // Sexo Random
        sb.append(sexo.charAt(ThreadLocalRandom.current().nextInt(sexo.length())));

        // Entidad Random
        sb.append(entidad[ThreadLocalRandom.current().nextInt(entidad.length)]);

        // Consonantas Random
        String consonante = "BCDFGHJKLMNPQRSTVWXYZ";
        for (int i = 0; i < 3; i++) {
            sb.append(consonante.charAt(ThreadLocalRandom.current().nextInt(consonante.length())));
        }

        // Homoclave Random
        for (int i = 0; i < 2; i++) {
            sb.append(numero.charAt(ThreadLocalRandom.current().nextInt(numero.length())));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java CURPGeneratorOrdered <numero_de_curps>");
            return;
        }

        int numCURPs;
        try {
            numCURPs = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Error: El argumento debe ser un número entero.");
            return;
        }

        List<String> curpsList = new ArrayList<>();

        for (int i = 0; i < numCURPs; i++) {
            String newCURP = getCURP();
            int insertIndex = 0;

            // corre a través de la lista para encontrar la posición correcta
            for (int j = 0; j < curpsList.size(); j++) {
                // compara los primeros 4 caracteres 
                if (newCURP.substring(0, 4).compareTo(curpsList.get(j).substring(0, 4)) > 0) {
                    insertIndex = j + 1;
                } else {
                    break; // no necesita para continuar si ya encontró la posición
                }
            }
            curpsList.add(insertIndex, newCURP);

            System.out.println("CURP generada: " + newCURP);
            System.out.println("Lista actual:");
            System.out.println(curpsList);
            System.out.println("--------------------");
        }
    }
}