/*
 * PROYECTO 3 - Sistema de Votación Electrónica
 * Nombre: Jose Bryan Omar Jimenez Velazquez
 * Grupo: 7CM4
 */
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.input.KeyStroke;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class EstadisticasVotos {
    private static Map<String, Integer> votosPorPartido = new ConcurrentHashMap<>();
    private static Map<Character, Integer> votosPorSexo = new ConcurrentHashMap<>();
    private static Map<String, Integer> votosPorEstado = new ConcurrentHashMap<>();
    private static Map<Integer, Map<Character, Integer>> votosPorEdad = new ConcurrentHashMap<>();
    private static volatile int totalVotos = 0;
    private static volatile boolean ejecutando = true;
    private static volatile String mensajeConsulta = "";
    private static volatile String resultadoConsulta = "";
    private static volatile boolean esperandoEdad = false;
    private static volatile boolean esperandoEstado = false;
    private static StringBuilder entradaUsuario = new StringBuilder();
    private static volatile int consultaActiva = 0; // 0: ninguna, 1: sexo, 2: estado, 3: edad, 4: gráficas

    // Mapa de códigos de estado de México
    private static final Map<String, String> ESTADOS_MEXICO = new HashMap<>();
    static {
        ESTADOS_MEXICO.put("AS", "Aguascalientes");
        ESTADOS_MEXICO.put("BC", "Baja California");
        ESTADOS_MEXICO.put("BS", "Baja California Sur");
        ESTADOS_MEXICO.put("CC", "Campeche");
        ESTADOS_MEXICO.put("CL", "Coahuila");
        ESTADOS_MEXICO.put("CM", "Colima");
        ESTADOS_MEXICO.put("CS", "Chiapas");
        ESTADOS_MEXICO.put("CH", "Chihuahua");
        ESTADOS_MEXICO.put("DF", "Ciudad de México");
        ESTADOS_MEXICO.put("DG", "Durango");
        ESTADOS_MEXICO.put("GT", "Guanajuato");
        ESTADOS_MEXICO.put("GR", "Guerrero");
        ESTADOS_MEXICO.put("HG", "Hidalgo");
        ESTADOS_MEXICO.put("JC", "Jalisco");
        ESTADOS_MEXICO.put("MC", "México");
        ESTADOS_MEXICO.put("MN", "Michoacán");
        ESTADOS_MEXICO.put("MS", "Morelos");
        ESTADOS_MEXICO.put("NT", "Nayarit");
        ESTADOS_MEXICO.put("NL", "Nuevo León");
        ESTADOS_MEXICO.put("OC", "Oaxaca");
        ESTADOS_MEXICO.put("PL", "Puebla");
        ESTADOS_MEXICO.put("QT", "Querétaro");
        ESTADOS_MEXICO.put("QR", "Quintana Roo");
        ESTADOS_MEXICO.put("SP", "San Luis Potosí");
        ESTADOS_MEXICO.put("SL", "Sinaloa");
        ESTADOS_MEXICO.put("SR", "Sonora");
        ESTADOS_MEXICO.put("TC", "Tabasco");
        ESTADOS_MEXICO.put("TS", "Tamaulipas");
        ESTADOS_MEXICO.put("TL", "Tlaxcala");
        ESTADOS_MEXICO.put("VZ", "Veracruz");
        ESTADOS_MEXICO.put("YN", "Yucatán");
        ESTADOS_MEXICO.put("ZS", "Zacatecas");
    }

    public static void main(String[] args) {
        try {
            // Configuración para Windows
            System.setProperty("java.awt.headless", "true");
            
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
            terminalFactory.setForceTextTerminal(true);
            terminalFactory.setPreferTerminalEmulator(true);
            
            Terminal terminal = terminalFactory.createTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();

            Thread hiloLectura = new Thread(new LectorArchivo());
            Thread hiloInterfaz = new Thread(new ActualizadorInterfaz(terminal));
            Thread hiloInput = new Thread(new ProcesadorInput(terminal));

            hiloLectura.start();
            hiloInterfaz.start();
            hiloInput.start();

            hiloLectura.join();
            hiloInterfaz.join();
            hiloInput.join();

            terminal.exitPrivateMode();
        } catch (Exception e) {
            System.err.println("Error al inicializar la terminal: " + e.getMessage());
            ejecutarModoConsola();
        }
    }

    private static void ejecutarModoConsola() {
        Thread hiloLectura = new Thread(new LectorArchivo());
        hiloLectura.start();
        
        Scanner scanner = new Scanner(System.in);
        while (ejecutando) {
            try {
                mostrarMenuConsola(scanner);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                break;
            }
        }
        
        hiloLectura.interrupt();
        scanner.close();
    }

    private static void mostrarMenuConsola(Scanner scanner) {
        System.out.println("\n=== SISTEMA DE VOTACIÓN ELECTRÓNICA ===");
        System.out.println("Total de votos: " + totalVotos);
        
        System.out.println("\nMENÚ DE CONSULTAS:");
        System.out.println("1 - Votos por sexo");
        System.out.println("2 - Votos por estado");
        System.out.println("3 - Votos por edad y sexo");
        System.out.println("4 - Gráficas de barras");
        System.out.println("Q - Salir");
        System.out.print("Seleccione opción: ");
        
        String opcion = scanner.nextLine();
        switch (opcion.toUpperCase()) {
            case "1":
                mostrarVotosPorSexoConsola();
                break;
            case "2":
                mostrarVotosPorEstadoConsola();
                break;
            case "3":
                System.out.print("Ingrese edad: ");
                String edadStr = scanner.nextLine();
                mostrarVotosPorEdadConsola(edadStr);
                break;
            case "4":
                mostrarGraficasConsola(scanner);
                break;
            case "Q":
                ejecutando = false;
                break;
            default:
                System.out.println("Opción no válida");
        }
    }

    private static void mostrarVotosPorSexoConsola() {
        System.out.println("\n=== VOTOS POR SEXO ===");
        for (Map.Entry<Character, Integer> entry : votosPorSexo.entrySet()) {
            String sexo = entry.getKey() == 'H' ? "Hombres" : "Mujeres";
            double porcentaje = totalVotos > 0 ? (entry.getValue() * 100.0) / totalVotos : 0;
            System.out.printf("%s: %d votos (%.1f%%)%n", sexo, entry.getValue(), porcentaje);
        }
    }

    private static void mostrarVotosPorEstadoConsola() {
        System.out.println("\n=== VOTOS POR ESTADO ===");
        for (Map.Entry<String, Integer> entry : votosPorEstado.entrySet()) {
            String nombreEstado = ESTADOS_MEXICO.getOrDefault(entry.getKey(), entry.getKey());
            double porcentaje = totalVotos > 0 ? (entry.getValue() * 100.0) / totalVotos : 0;
            System.out.printf("%-25s: %d votos (%.1f%%)%n", nombreEstado, entry.getValue(), porcentaje);
        }
    }

    private static void mostrarVotosPorEdadConsola(String edadStr) {
        try {
            int edad = Integer.parseInt(edadStr);
            Map<Character, Integer> votos = votosPorEdad.get(edad);
            if (votos != null) {
                System.out.println("\n=== VOTOS PARA EDAD " + edad + " ===");
                for (Map.Entry<Character, Integer> entry : votos.entrySet()) {
                    String sexo = entry.getKey() == 'H' ? "Hombres" : "Mujeres";
                    System.out.println(sexo + ": " + entry.getValue() + " votos");
                }
            } else {
                System.out.println("No hay votos para edad " + edad);
            }
        } catch (NumberFormatException e) {
            System.out.println("Edad no válida: " + edadStr);
        }
    }

    private static void mostrarGraficasConsola(Scanner scanner) {
        System.out.println("\n=== GRÁFICAS DE BARRAS ===");
        System.out.println("1 - Gráfica de barras por partido");
        System.out.println("2 - Gráfica de barras por estado");
        System.out.println("3 - Gráfica de barras por sexo");
        System.out.print("Seleccione tipo de gráfica: ");
        
        String opcion = scanner.nextLine();
        
        switch (opcion) {
            case "1":
                dibujarGraficaBarrasPartidosConsola();
                break;
            case "2":
                dibujarGraficaBarrasEstadosConsola();
                break;
            case "3":
                dibujarGraficaBarrasSexoConsola();
                break;
            default:
                System.out.println("Opción no válida");
        }
    }

    private static void dibujarGraficaBarrasPartidosConsola() {
        System.out.println("\n=== GRÁFICA DE BARRAS - VOTOS POR PARTIDO ===");
        
        List<Map.Entry<String, Integer>> partidosOrdenados = new ArrayList<>(votosPorPartido.entrySet());
        partidosOrdenados.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        int maxVotos = partidosOrdenados.isEmpty() ? 1 : partidosOrdenados.get(0).getValue();
        int anchoMaximo = 50;
        
        for (Map.Entry<String, Integer> entry : partidosOrdenados) {
            String partido = entry.getKey();
            int votos = entry.getValue();
            double porcentaje = totalVotos > 0 ? (votos * 100.0) / totalVotos : 0;
            
            int longitudBarra = maxVotos > 0 ? (votos * anchoMaximo) / maxVotos : 0;
            
            System.out.printf("%-15s: %3d votos %5.1f%% ", partido, votos, porcentaje);
            for (int i = 0; i < longitudBarra; i++) {
                System.out.print("█");
            }
            System.out.println();
        }
    }

    private static void dibujarGraficaBarrasEstadosConsola() {
        System.out.println("\n=== GRÁFICA DE BARRAS - VOTOS POR ESTADO (Top 15) ===");
        
        List<Map.Entry<String, Integer>> estadosOrdenados = new ArrayList<>(votosPorEstado.entrySet());
        estadosOrdenados.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        int maxVotos = estadosOrdenados.isEmpty() ? 1 : estadosOrdenados.get(0).getValue();
        int anchoMaximo = 50;
        int contador = 0;
        
        for (Map.Entry<String, Integer> entry : estadosOrdenados) {
            if (contador++ >= 15) break; // Mostrar solo top 15
            
            String nombreEstado = ESTADOS_MEXICO.getOrDefault(entry.getKey(), entry.getKey());
            int votos = entry.getValue();
            double porcentaje = totalVotos > 0 ? (votos * 100.0) / totalVotos : 0;
            
            int longitudBarra = maxVotos > 0 ? (votos * anchoMaximo) / maxVotos : 0;
            
            System.out.printf("%-25s: %3d votos %5.1f%% ", nombreEstado, votos, porcentaje);
            for (int i = 0; i < longitudBarra; i++) {
                System.out.print("█");
            }
            System.out.println();
        }
    }

    private static void dibujarGraficaBarrasSexoConsola() {
        System.out.println("\n=== GRÁFICA DE BARRAS - VOTOS POR SEXO ===");
        
        int hombres = votosPorSexo.getOrDefault('H', 0);
        int mujeres = votosPorSexo.getOrDefault('M', 0);
        int maxVotos = Math.max(hombres, mujeres);
        int anchoMaximo = 50;
        
        double porcHombres = totalVotos > 0 ? (hombres * 100.0) / totalVotos : 0;
        double porcMujeres = totalVotos > 0 ? (mujeres * 100.0) / totalVotos : 0;
        
        int longHombres = maxVotos > 0 ? (hombres * anchoMaximo) / maxVotos : 0;
        int longMujeres = maxVotos > 0 ? (mujeres * anchoMaximo) / maxVotos : 0;
        
        System.out.printf("%-15s: %3d votos %5.1f%% ", "Hombres", hombres, porcHombres);
        for (int i = 0; i < longHombres; i++) {
            System.out.print("█");
        }
        System.out.println();
        
        System.out.printf("%-15s: %3d votos %5.1f%% ", "Mujeres", mujeres, porcMujeres);
        for (int i = 0; i < longMujeres; i++) {
            System.out.print("█");
        }
        System.out.println();
    }

    static class LectorArchivo implements Runnable {
        @Override
        public void run() {
            long ultimaPosicion = 0;
            try (RandomAccessFile raf = new RandomAccessFile("VOTOS.dat", "r")) {
                while (ejecutando) {
                    if (raf.length() > ultimaPosicion) {
                        raf.seek(ultimaPosicion);
                        String linea;
                        while ((linea = raf.readLine()) != null) {
                            procesarVoto(linea);
                        }
                        ultimaPosicion = raf.getFilePointer();
                    }
                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void procesarVoto(String linea) {
            try {
                String[] partes = linea.split(",");
                if (partes.length != 2) return;

                String curp = partes[0];
                String partido = partes[1];

                votosPorPartido.merge(partido, 1, Integer::sum);
                char sexo = CurpManager.getSexoFromCURP(curp);
                votosPorSexo.merge(sexo, 1, Integer::sum);
                String estado = CurpManager.getEstadoFromCURP(curp);
                // Validar que el estado exista en el mapa de estados de México
                if (ESTADOS_MEXICO.containsKey(estado)) {
                    votosPorEstado.merge(estado, 1, Integer::sum);
                }
                int edad = CurpManager.getEdadFromCURP(curp);
                votosPorEdad.computeIfAbsent(edad, k -> new ConcurrentHashMap<>())
                            .merge(sexo, 1, Integer::sum);

                totalVotos++;
            } catch (Exception e) {
                // ignorar líneas mal formadas
            }
        }
    }

    static class ActualizadorInterfaz implements Runnable {
        private Terminal terminal;
        public ActualizadorInterfaz(Terminal terminal) { this.terminal = terminal; }

        @Override
        public void run() {
            while (ejecutando) {
                try {
                    dibujarInterfaz();
                    Thread.sleep(1000);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void dibujarInterfaz() throws IOException {
            terminal.clearScreen();
            TextGraphics tg = terminal.newTextGraphics();

            // Encabezado
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(2, 1, "=== SISTEMA DE VOTACIÓN ELECTRÓNICA ===");

            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(2, 3, "Total de votos: " + totalVotos);

            // Mostrar estadísticas según la consulta activa
            switch (consultaActiva) {
                case 1:
                    mostrarVotosPorSexo(tg);
                    break;
                case 2:
                    mostrarVotosPorEstado(tg);
                    break;
                case 3:
                    mostrarVotosPorEdadEspecifica(tg);
                    break;
                case 4:
                    mostrarGraficas(tg);
                    break;
                default:
                    mostrarMenuPrincipal(tg);
                    break;
            }

            // Mensajes y entrada de usuario
            tg.setForegroundColor(TextColor.ANSI.MAGENTA);
            tg.putString(2, 25, mensajeConsulta);
            tg.putString(2, 26, resultadoConsulta);

            if (esperandoEdad || esperandoEstado) {
                tg.putString(2, 27, "Ingrese: " + entradaUsuario.toString() + "_");
            }

            terminal.flush();
        }

        private void mostrarMenuPrincipal(TextGraphics tg) {
            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            tg.putString(2, 5, "VOTOS POR PARTIDO (Resumen):");
            int fila = 6;
            for (Map.Entry<String, Integer> entry : votosPorPartido.entrySet()) {
                double porcentaje = totalVotos > 0 ? (entry.getValue() * 100.0) / totalVotos : 0;
                tg.putString(4, fila++, String.format("%s: %d (%.1f%%)", 
                    entry.getKey(), entry.getValue(), porcentaje));
            }

            fila += 2;
            tg.setForegroundColor(TextColor.ANSI.GREEN);
            tg.putString(2, fila++, "CONSULTAS DISPONIBLES:");
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(4, fila++, "1 - Votos por sexo");
            tg.putString(4, fila++, "2 - Votos por estado");
            tg.putString(4, fila++, "3 - Votos por edad específica");
            tg.putString(4, fila++, "4 - Gráficas de barras");
            tg.putString(4, fila++, "Q - Salir");
        }

        private void mostrarVotosPorSexo(TextGraphics tg) {
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(2, 5, "=== VOTOS POR SEXO ===");
            
            int fila = 7;
            int totalHombres = votosPorSexo.getOrDefault('H', 0);
            int totalMujeres = votosPorSexo.getOrDefault('M', 0);
            
            // Hombres
            tg.setForegroundColor(TextColor.ANSI.BLUE);
            tg.putString(4, fila, "Hombres: " + totalHombres);
            dibujarBarraHorizontal(tg, 20, fila, totalHombres, totalVotos, TextColor.ANSI.BLUE);
            fila += 2;
            
            // Mujeres
            tg.setForegroundColor(TextColor.ANSI.MAGENTA);
            tg.putString(4, fila, "Mujeres: " + totalMujeres);
            dibujarBarraHorizontal(tg, 20, fila, totalMujeres, totalVotos, TextColor.ANSI.MAGENTA);
            fila += 2;
            
            // Porcentajes
            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            double porcHombres = totalVotos > 0 ? (totalHombres * 100.0) / totalVotos : 0;
            double porcMujeres = totalVotos > 0 ? (totalMujeres * 100.0) / totalVotos : 0;
            tg.putString(4, fila++, String.format("Hombres: %.1f%%", porcHombres));
            tg.putString(4, fila++, String.format("Mujeres: %.1f%%", porcMujeres));
        }

        private void mostrarVotosPorEstado(TextGraphics tg) {
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(2, 5, "=== VOTOS POR ESTADO ===");
            
            int fila = 7;
            // Ordenar estados por número de votos (descendente)
            List<Map.Entry<String, Integer>> estadosOrdenados = new ArrayList<>(votosPorEstado.entrySet());
            estadosOrdenados.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            for (Map.Entry<String, Integer> entry : estadosOrdenados) {
                if (fila > 20) break; // Limitar para no salir de pantalla
                
                String nombreEstado = ESTADOS_MEXICO.getOrDefault(entry.getKey(), entry.getKey());
                tg.setForegroundColor(TextColor.ANSI.WHITE);
                tg.putString(4, fila, String.format("%s: %d", nombreEstado, entry.getValue()));
                
                double porcentaje = totalVotos > 0 ? (entry.getValue() * 100.0) / totalVotos : 0;
                tg.setForegroundColor(TextColor.ANSI.GREEN);
                tg.putString(25, fila, String.format("(%.1f%%)", porcentaje));
                
                fila++;
            }
        }

        private void mostrarVotosPorEdadEspecifica(TextGraphics tg) {
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(2, 5, "=== VOTOS POR EDAD ESPECÍFICA ===");
            
            if (!resultadoConsulta.isEmpty()) {
                tg.setForegroundColor(TextColor.ANSI.YELLOW);
                tg.putString(4, 7, resultadoConsulta);
            } else {
                tg.setForegroundColor(TextColor.ANSI.WHITE);
                tg.putString(4, 7, "Ingrese la edad a consultar");
            }
        }

        private void mostrarGraficas(TextGraphics tg) {
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(2, 5, "=== GRÁFICAS DE BARRAS ===");
            
            if (resultadoConsulta.isEmpty()) {
                tg.setForegroundColor(TextColor.ANSI.YELLOW);
                tg.putString(4, 7, "Seleccione tipo de gráfica:");
                tg.putString(4, 8, "1 - Barras por Partido");
                tg.putString(4, 9, "2 - Barras por Estado (Top 10)");
                tg.putString(4, 10, "3 - Barras por Sexo");
                tg.putString(4, 11, "0 - Volver al menú principal");
            } else {
                // Mostrar la gráfica seleccionada
                String[] lineas = resultadoConsulta.split("\n");
                int fila = 7;
                for (String linea : lineas) {
                    if (fila > 23) break;
                    tg.putString(4, fila++, linea);
                }
            }
        }

        private void dibujarBarraHorizontal(TextGraphics tg, int x, int y, int valor, int max, TextColor color) {
            int longitudMaxima = 30;
            int longitud = max > 0 ? (valor * longitudMaxima) / max : 0;
            
            tg.setForegroundColor(color);
            for (int i = 0; i < longitud; i++) {
                tg.putString(x + i, y, "█");
            }
            
            // Mostrar porcentaje al final de la barra
            double porcentaje = max > 0 ? (valor * 100.0) / max : 0;
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(x + longitudMaxima + 2, y, String.format("%.1f%%", porcentaje));
        }
    }

    static class ProcesadorInput implements Runnable {
        private Terminal terminal;
        public ProcesadorInput(Terminal terminal) { this.terminal = terminal; }

        @Override
        public void run() {
            while (ejecutando) {
                try {
                    KeyStroke key = terminal.pollInput();
                    if (key != null) procesarTecla(key);
                    Thread.sleep(50);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void procesarTecla(KeyStroke key) {
            if (esperandoEdad || esperandoEstado) {
                procesarEntradaUsuario(key);
                return;
            }

            if (key.getKeyType() == KeyType.Character) {
                char c = key.getCharacter();
                switch (c) {
                    case '1': 
                        consultaVotosPorSexo(); 
                        break;
                    case '2': 
                        consultaVotosPorEstado(); 
                        break;
                    case '3': 
                        iniciarConsultaEdad(); 
                        break;
                    case '4':
                        consultaGraficas();
                        break;
                    case 'q': case 'Q': 
                        ejecutando = false; 
                        break;
                    case '0':
                        consultaActiva = 0; // Volver al menú principal
                        mensajeConsulta = "";
                        resultadoConsulta = "";
                        break;
                }
            }
            
            // Procesar sub-opciones de gráficas
            if (consultaActiva == 4 && key.getKeyType() == KeyType.Character) {
                char c = key.getCharacter();
                switch (c) {
                    case '1':
                        mostrarGraficaBarrasPartidos();
                        break;
                    case '2':
                        mostrarGraficaBarrasEstados();
                        break;
                    case '3':
                        mostrarGraficaBarrasSexo();
                        break;
                }
            }
        }

        private void procesarEntradaUsuario(KeyStroke key) {
            if (key.getKeyType() == KeyType.Enter) {
                if (esperandoEdad) {
                    procesarEdadIngresada();
                } else if (esperandoEstado) {
                    procesarEstadoIngresado();
                }
            } else if (key.getKeyType() == KeyType.Character && Character.isDigit(key.getCharacter())) {
                entradaUsuario.append(key.getCharacter());
            } else if (key.getKeyType() == KeyType.Backspace && entradaUsuario.length() > 0) {
                entradaUsuario.setLength(entradaUsuario.length() - 1);
            } else if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'c') {
                // Cancelar entrada
                esperandoEdad = false;
                esperandoEstado = false;
                entradaUsuario.setLength(0);
                consultaActiva = 0;
                mensajeConsulta = "Consulta cancelada";
            }
        }

        private void consultaVotosPorSexo() {
            consultaActiva = 1;
            mensajeConsulta = "Consulta: Votos por sexo (Presione 0 para volver)";
            resultadoConsulta = "";
        }

        private void consultaVotosPorEstado() {
            consultaActiva = 2;
            mensajeConsulta = "Consulta: Votos por estado (Presione 0 para volver)";
            resultadoConsulta = "";
        }

        private void iniciarConsultaEdad() {
            consultaActiva = 3;
            esperandoEdad = true;
            entradaUsuario.setLength(0);
            mensajeConsulta = "Consulta: Votos por edad específica - Ingrese edad (Presione C para cancelar)";
            resultadoConsulta = "";
        }

        private void consultaGraficas() {
            consultaActiva = 4;
            mensajeConsulta = "Gráficas - Seleccione: 1(Barras Partidos), 2(Barras Estados), 3(Barras Sexo), 0(Volver)";
            resultadoConsulta = "";
        }

        private void mostrarGraficaBarrasPartidos() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== BARRAS POR PARTIDO ===\n");
            
            List<Map.Entry<String, Integer>> partidosOrdenados = new ArrayList<>(votosPorPartido.entrySet());
            partidosOrdenados.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            int maxVotos = partidosOrdenados.isEmpty() ? 1 : partidosOrdenados.get(0).getValue();
            int anchoMaximo = 30;
            
            for (Map.Entry<String, Integer> entry : partidosOrdenados) {
                String partido = entry.getKey();
                int votos = entry.getValue();
                double porcentaje = totalVotos > 0 ? (votos * 100.0) / totalVotos : 0;
                
                int longitudBarra = maxVotos > 0 ? (votos * anchoMaximo) / maxVotos : 0;
                
                sb.append(String.format("%-10s: %2d %3.0f%% ", partido, votos, porcentaje));
                for (int i = 0; i < longitudBarra; i++) {
                    sb.append("█");
                }
                sb.append("\n");
            }
            
            resultadoConsulta = sb.toString();
        }

        private void mostrarGraficaBarrasEstados() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== BARRAS POR ESTADO (Top 10) ===\n");
            
            List<Map.Entry<String, Integer>> estadosOrdenados = new ArrayList<>(votosPorEstado.entrySet());
            estadosOrdenados.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            
            int maxVotos = estadosOrdenados.isEmpty() ? 1 : estadosOrdenados.get(0).getValue();
            int anchoMaximo = 30;
            int contador = 0;
            
            for (Map.Entry<String, Integer> entry : estadosOrdenados) {
                if (contador++ >= 10) break;
                
                String nombreEstado = ESTADOS_MEXICO.getOrDefault(entry.getKey(), entry.getKey());
                int votos = entry.getValue();
                double porcentaje = totalVotos > 0 ? (votos * 100.0) / totalVotos : 0;
                
                int longitudBarra = maxVotos > 0 ? (votos * anchoMaximo) / maxVotos : 0;
                
                // Abreviar nombre del estado si es muy largo
                String estadoAbreviado = nombreEstado.length() > 12 ? nombreEstado.substring(0, 12) : nombreEstado;
                sb.append(String.format("%-12s: %2d %3.0f%% ", estadoAbreviado, votos, porcentaje));
                for (int i = 0; i < longitudBarra; i++) {
                    sb.append("█");
                }
                sb.append("\n");
            }
            
            resultadoConsulta = sb.toString();
        }

        private void mostrarGraficaBarrasSexo() {
            StringBuilder sb = new StringBuilder();
            sb.append("=== BARRAS POR SEXO ===\n");
            
            int hombres = votosPorSexo.getOrDefault('H', 0);
            int mujeres = votosPorSexo.getOrDefault('M', 0);
            int maxVotos = Math.max(hombres, mujeres);
            int anchoMaximo = 30;
            
            double porcHombres = totalVotos > 0 ? (hombres * 100.0) / totalVotos : 0;
            double porcMujeres = totalVotos > 0 ? (mujeres * 100.0) / totalVotos : 0;
            
            int longHombres = maxVotos > 0 ? (hombres * anchoMaximo) / maxVotos : 0;
            int longMujeres = maxVotos > 0 ? (mujeres * anchoMaximo) / maxVotos : 0;
            
            sb.append(String.format("%-7s: %2d %3.0f%% ", "Hombres", hombres, porcHombres));
            for (int i = 0; i < longHombres; i++) {
                sb.append("█");
            }
            sb.append("\n");
            
            sb.append(String.format("%-7s: %2d %3.0f%% ", "Mujeres", mujeres, porcMujeres));
            for (int i = 0; i < longMujeres; i++) {
                sb.append("█");
            }
            sb.append("\n");
            
            resultadoConsulta = sb.toString();
        }

        private void procesarEdadIngresada() {
            try {
                int edad = Integer.parseInt(entradaUsuario.toString());
                Map<Character, Integer> votos = votosPorEdad.get(edad);
                if (votos != null) {
                    int hombres = votos.getOrDefault('H', 0);
                    int mujeres = votos.getOrDefault('M', 0);
                    resultadoConsulta = String.format("Edad %d: Hombres=%d, Mujeres=%d, Total=%d", 
                        edad, hombres, mujeres, hombres + mujeres);
                } else {
                    resultadoConsulta = "No hay votos para edad " + edad;
                }
            } catch (NumberFormatException e) {
                resultadoConsulta = "Edad no válida: " + entradaUsuario.toString();
            }
            esperandoEdad = false;
            entradaUsuario.setLength(0);
        }

        private void procesarEstadoIngresado() {
            String estado = entradaUsuario.toString().toUpperCase();
            Integer votos = votosPorEstado.get(estado);
            if (votos != null) {
                String nombreEstado = ESTADOS_MEXICO.getOrDefault(estado, estado);
                double porcentaje = totalVotos > 0 ? (votos * 100.0) / totalVotos : 0;
                resultadoConsulta = String.format("Estado %s: %d votos (%.1f%%)", nombreEstado, votos, porcentaje);
            } else {
                resultadoConsulta = "No hay votos para el estado " + estado;
            }
            esperandoEstado = false;
            entradaUsuario.setLength(0);
        }
    }

    // Clase auxiliar para manejar CURP
    static class CurpManager {
        public static char getSexoFromCURP(String curp) {
            // El décimo carácter de la CURP representa el sexo
            return curp.length() > 10 ? curp.charAt(10) : 'H';
        }
        
        public static String getEstadoFromCURP(String curp) {
            // Los primeros dos caracteres representan el estado
            return curp.length() > 1 ? curp.substring(0, 2) : "XX";
        }
        
        public static int getEdadFromCURP(String curp) {
            // Los caracteres 5-8 representan año y mes de nacimiento
            if (curp.length() < 8) return 0;
            try {
                String anioStr = curp.substring(4, 6);
                int anio = Integer.parseInt(anioStr);
                // Asumimos que son personas nacidas después de 2000 para simplificar
                return 2024 - (2000 + anio);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }
}