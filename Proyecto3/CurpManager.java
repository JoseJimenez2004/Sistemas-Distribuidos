/*
 * PROYECTO 3 - Sistema de Votación Electrónica
 * Nombre: Jose Bryan Omar Jimenez Velazquez
 * Grupo: 7CM4
 */


import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class CurpManager {
    private static final String LETRAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMEROS = "0123456789";
    private static final String SEXO = "HM";
    private static final String CONSONANTES = "BCDFGHJKLMNPQRSTVWXYZ";
    private static final String[] ENTIDADES = {
        "AS", "BC", "BS", "CC", "CS", "CH", "CL", "CM", "DF", "DG", "GT", "GR", 
        "HG", "JC", "MC", "MN", "MS", "NT", "NL", "OC", "PL", "QT", "QR", "SP", 
        "SL", "SR", "TC", "TL", "TS", "VZ", "YN", "ZS"
    };

    public static String getCURP() {
        StringBuilder sb = new StringBuilder(18);

        // 4 letras (iniciales)
        for (int i = 0; i < 4; i++) {
            sb.append(LETRAS.charAt(ThreadLocalRandom.current().nextInt(LETRAS.length())));
        }

        // 6 números (fecha de nacimiento)
        for (int i = 0; i < 6; i++) {
            sb.append(NUMEROS.charAt(ThreadLocalRandom.current().nextInt(NUMEROS.length())));
        }

        // Sexo
        sb.append(SEXO.charAt(ThreadLocalRandom.current().nextInt(SEXO.length())));

        // Entidad federativa
        sb.append(ENTIDADES[ThreadLocalRandom.current().nextInt(ENTIDADES.length)]);

        // 3 consonantes
        for (int i = 0; i < 3; i++) {
            sb.append(CONSONANTES.charAt(ThreadLocalRandom.current().nextInt(CONSONANTES.length())));
        }

        // 2 dígitos homoclave
        for (int i = 0; i < 2; i++) {
            sb.append(NUMEROS.charAt(ThreadLocalRandom.current().nextInt(NUMEROS.length())));
        }

        return sb.toString();
    }

    // Método para obtener el sexo de una CURP
    public static char getSexoFromCURP(String curp) {
        return curp.charAt(10);
    }

    // Método para obtener el estado de una CURP
    public static String getEstadoFromCURP(String curp) {
        return curp.substring(11, 13);
    }

    // Método para obtener la edad aproximada de una CURP
    public static int getEdadFromCURP(String curp) {
        try {
            int anioNacimiento = 1900 + Integer.parseInt(curp.substring(4, 6));
            return 2024 - anioNacimiento; // Ajustar según el año actual
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}