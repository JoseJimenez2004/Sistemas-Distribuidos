package ProgramaClase9;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GeneradorCURP {
     
    public static String getCURP() {
        String Letra = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String Numero = "0123456789";
        String Sexo = "HM";
        String[] Entidad = {"AS", "BC", "BS", "CC", "CS", "CH", "CL", "CM", "DF", "DG", "GT", "GR", "HG", "JC", "MC", "MN", "MS", "NT", "NL", "OC", "PL", "QT", "QR", "SP", "SL", "SR", "TC", "TL", "TS", "VZ", "YN", "ZS"};

        StringBuilder sb = new StringBuilder(18);

        // letras Random
        for (int i = 0; i < 4; i++) {
            sb.append(Letra.charAt(ThreadLocalRandom.current().nextInt(Letra.length())));
        }

        // Numeros Random
        for (int i = 0; i < 6; i++) {
            sb.append(Numero.charAt(ThreadLocalRandom.current().nextInt(Numero.length())));
        }

        // Sexo Random
        sb.append(Sexo.charAt(ThreadLocalRandom.current().nextInt(Sexo.length())));

        // Entidad Random
        sb.append(Entidad[ThreadLocalRandom.current().nextInt(Entidad.length)]);

        // Consonantas Random
        String Consonante = "BCDFGHJKLMNPQRSTVWXYZ";
        for (int i = 0; i < 3; i++) {
            sb.append(Consonante.charAt(ThreadLocalRandom.current().nextInt(Consonante.length())));
        }

        // Homoclave Random
        for (int i = 0; i < 2; i++) {
            sb.append(Numero.charAt(ThreadLocalRandom.current().nextInt(Numero.length())));
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Uso: java GeneradorCURP <numero_de_curps> <sexo_a_eliminar H|M>");
            return;
        }

        int n;
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Error: El primer argumento debe ser un número entero.");
            return;
        }

        String sexoAEliminar = args[1].toUpperCase();
        if (!sexoAEliminar.equals("H") && !sexoAEliminar.equals("M")) {
            System.out.println("Error: El segundo argumento debe ser 'H' (hombre) o 'M' (mujer).");
            return;
        }

        // 1. Almacenar n CURPs en un ArrayList
        List<String> curps = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            curps.add(getCURP());
        }

        System.out.println("CURPs generadas (" + n + " en total):");
        System.out.println(curps);
        System.out.println("--------------------");

        // 2. Usar un Iterator para eliminar CURPs según el sexo especificado
        Iterator<String> iterator = curps.iterator();
        while (iterator.hasNext()) {
            String curp = iterator.next();
            // La posición 10 en la CURP indica el sexo
            if (curp.substring(10, 11).equals(sexoAEliminar)) {
                iterator.remove();
            }
        }

        // 3. Imprimir la lista filtrada
        System.out.println("CURPs filtradas (se eliminaron las de sexo " + (sexoAEliminar.equals("H") ? "Masculino" : "Femenino") + "):");
        System.out.println(curps);
    }
}