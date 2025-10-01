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
    private static StringBuilder entradaEdad = new StringBuilder();

    public static void main(String[] args) {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            terminal.enterPrivateMode();
            terminal.clearScreen();

            // Hilos para diferentes tareas
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
            e.printStackTrace();
        }
    }

    static class LectorArchivo implements Runnable {
        @Override
        public void run() {
            long ultimaPosicion = 0;
            
            while (ejecutando) {
                try {
                    File archivo = new File("VOTOS.dat");
                    if (!archivo.exists()) {
                        Thread.sleep(1000);
                        continue;
                    }
                    
                    RandomAccessFile raf = new RandomAccessFile(archivo, "r");
                    if (raf.length() > ultimaPosicion) {
                        raf.seek(ultimaPosicion);
                        
                        String linea;
                        while ((linea = raf.readLine()) != null) {
                            procesarVoto(linea);
                        }
                        
                        ultimaPosicion = raf.getFilePointer();
                    }
                    raf.close();
                    Thread.sleep(1000);
                } catch (IOException | InterruptedException e) {
                    // Continuar ejecución
                }
            }
        }
        
        private void procesarVoto(String linea) {
            try {
                String[] partes = linea.split(",");
                if (partes.length != 2) return;
                
                String curp = partes[0];
                String partido = partes[1];
                
                // Actualizar estadísticas
                votosPorPartido.merge(partido, 1, Integer::sum);
                
                char sexo = curp.charAt(10);
                votosPorSexo.merge(sexo, 1, Integer::sum);
                
                String estado = curp.substring(11, 13);
                votosPorEstado.merge(estado, 1, Integer::sum);
                
                int anioNacimiento = 1900 + Integer.parseInt(curp.substring(4, 6));
                int edad = 2024 - anioNacimiento;
                votosPorEdad.computeIfAbsent(edad, k -> new ConcurrentHashMap<>())
                            .merge(sexo, 1, Integer::sum);
                
                totalVotos++;
            } catch (Exception e) {
                // Ignorar líneas mal formadas
            }
        }
    }

    static class ActualizadorInterfaz implements Runnable {
        private Terminal terminal;
        
        public ActualizadorInterfaz(Terminal terminal) {
            this.terminal = terminal;
        }
        
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
            
            // Colores
            tg.setForegroundColor(TextColor.ANSI.CYAN);
            tg.putString(2, 1, "=== SISTEMA DE VOTACIÓN ELECTRÓNICA ===");
            
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(2, 3, "Total de votos: " + totalVotos);
            
            // Votos por partido
            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            tg.putString(2, 5, "VOTOS POR PARTIDO:");
            
            int fila = 6;
            for (Map.Entry<String, Integer> entry : votosPorPartido.entrySet()) {
                double porcentaje = totalVotos > 0 ? (entry.getValue() * 100.0) / totalVotos : 0;
                tg.putString(4, fila++, String.format("%s: %d (%.1f%%)", 
                    entry.getKey(), entry.getValue(), porcentaje));
            }
            
            // Votos por sexo
            fila += 2;
            tg.setForegroundColor(TextColor.ANSI.YELLOW);
            tg.putString(2, fila++, "VOTOS POR SEXO:");
            
            for (Map.Entry<Character, Integer> entry : votosPorSexo.entrySet()) {
                String sexo = entry.getKey() == 'H' ? "Hombres" : "Mujeres";
                double porcentaje = totalVotos > 0 ? (entry.getValue() * 100.0) / totalVotos : 0;
                tg.putString(4, fila++, String.format("%s: %d (%.1f%%)", sexo, entry.getValue(), porcentaje));
            }
            
            // Consultas
            fila += 2;
            tg.setForegroundColor(TextColor.ANSI.GREEN);
            tg.putString(2, fila++, "MENÚ DE CONSULTAS:");
            tg.setForegroundColor(TextColor.ANSI.WHITE);
            tg.putString(4, fila++, "1 - Votos por estado");
            tg.putString(4, fila++, "2 - Votos por edad específica");
            tg.putString(4, fila++, "Q - Salir");
            
            // Mensajes de consulta
            fila += 2;
            tg.setForegroundColor(TextColor.ANSI.MAGENTA);
            tg.putString(2, fila++, mensajeConsulta);
            tg.putString(2, fila++, resultadoConsulta);
            
            if (esperandoEdad) {
                tg.putString(2, fila++, "Ingrese edad: " + entradaEdad.toString());
            }
            
            terminal.flush();
        }
    }

    static class ProcesadorInput implements Runnable {
        private Terminal terminal;
        
        public ProcesadorInput(Terminal terminal) {
            this.terminal = terminal;
        }
        
        @Override
        public void run() {
            while (ejecutando) {
                try {
                    KeyStroke key = terminal.pollInput();
                    if (key != null) {
                        procesarTecla(key);
                    }
                    Thread.sleep(50);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void procesarTecla(KeyStroke key) {
            if (esperandoEdad) {
                if (key.getKeyType() == KeyType.Enter) {
                    procesarEdadIngresada();
                } else if (key.getKeyType() == KeyType.Character && Character.isDigit(key.getCharacter())) {
                    entradaEdad.append(key.getCharacter());
                } else if (key.getKeyType() == KeyType.Backspace && entradaEdad.length() > 0) {
                    entradaEdad.setLength(entradaEdad.length() - 1);
                }
                return;
            }
            
            if (key.getKeyType() == KeyType.Character) {
                char c = key.getCharacter();
                switch (c) {
                    case '1':
                        consultaVotosPorEstado();
                        break;
                    case '2':
                        iniciarConsultaEdad();
                        break;
                    case 'q':
                    case 'Q':
                        ejecutando = false;
                        break;
                }
            }
        }
        
        private void iniciarConsultaEdad() {
            esperandoEdad = true;
            entradaEdad.setLength(0);
            mensajeConsulta = "Consulta: Votos por edad específica";
            resultadoConsulta = "";
        }
        
        private void procesarEdadIngresada() {
            try {
                int edad = Integer.parseInt(entradaEdad.toString());
                Map<Character, Integer> votos = votosPorEdad.get(edad);
                
                if (votos != null) {
                    StringBuilder resultado = new StringBuilder();
                    resultado.append("Edad ").append(edad).append(": ");
                    for (Map.Entry<Character, Integer> entry : votos.entrySet()) {
                        String sexo = entry.getKey() == 'H' ? "H" : "M";
                        resultado.append(sexo).append("=").append(entry.getValue()).append(" ");
                    }
                    resultadoConsulta = resultado.toString();
                } else {
                    resultadoConsulta = "No hay votos para edad " + edad;
                }
            } catch (NumberFormatException e) {
                resultadoConsulta = "Edad no válida: " + entradaEdad.toString();
            }
            
            esperandoEdad = false;
            entradaEdad.setLength(0);
        }
        
        private void consultaVotosPorEstado() {
            mensajeConsulta = "Consulta: Votos por estado";
            StringBuilder resultado = new StringBuilder();
            for (Map.Entry<String, Integer> entry : votosPorEstado.entrySet()) {
                resultado.append(entry.getKey()).append(":").append(entry.getValue()).append(" ");
            }
            resultadoConsulta = resultado.toString();
        }
    }
}