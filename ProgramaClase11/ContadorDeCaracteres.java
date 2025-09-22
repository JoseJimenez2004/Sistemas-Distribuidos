    package ProgramaClase11;

    import java.io.*;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.*;
    import java.util.*;

    public class ContadorDeCaracteres {

        public static void main(String[] args) {
            // 1) Paso de argumento > proyecto root > carpeta del paquete
            Path ruta = null;
            if (args.length > 0) {
                ruta = Paths.get(args[0]);
            } else {
                ruta = Paths.get("El_viejo_y_el_mar.txt");
                if (!Files.exists(ruta)) {
                    ruta = Paths.get("ProgramaClase11", "El_viejo_y_el_mar.txt");
                }
            }

            System.out.println("Directorio de trabajo: " + System.getProperty("user.dir"));
            System.out.println("Probando archivo: " + ruta.toAbsolutePath());

            Map<Character, Long> contador = new HashMap<>();

            // 2) Si existe en el filesystem, se lee el archivo directamente
            if (Files.exists(ruta)) {
                try (BufferedReader r = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
                    int cp;
                    while ((cp = r.read()) != -1) {
                        char c = (char) cp;
                        contador.put(c, contador.getOrDefault(c, 0L) + 1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // 3) Si no existe, intentamos cargarlo como recurso en el mismo paquete (classpath)
                try (InputStream is = ContadorDeCaracteres.class.getResourceAsStream("El_viejo_y_el_mar.txt")) {
                    if (is == null) {
                        System.err.println("Archivo no encontrado en filesystem ni en classpath (recurso).");
                        System.err.println("Prueba a pasar la ruta como argumento, o mueve el archivo al directorio de trabajo.");
                        return;
                    }
                    try (BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        int cp;
                        while ((cp = r.read()) != -1) {
                            char c = (char) cp;
                            contador.put(c, contador.getOrDefault(c, 0L) + 1);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 4) resultados en formato tabla
            System.out.println("\nNúmero de caracteres distintos encontrados: " + contador.size());
            System.out.println("\nOcurrencias de cada caracter (ordenadas por frecuencia):");

            // Encabezado
            System.out.printf("%-15s | %-10s%n", "Carácter", "Frecuencia");
            System.out.println("-----------------|------------");

            // Filas de la tabla
            contador.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .forEach(e -> System.out.printf("%-15s | %-10d%n", repr(e.getKey()), e.getValue()));
        }

        private static String repr(char c) {
            switch (c) {
                case ' ':  return "' ' (SPACE)";
                case '\n': return "\\n (NEWLINE)";
                case '\r': return "\\r (CR)";
                case '\t': return "\\t (TAB)";
                default:
                    if (Character.isISOControl(c)) {
                        return String.format("0x%04X (control)", (int) c);
                    } else {
                        return "'" + c + "'";
                    }
            }
        }
    }
