/*
 * PROYECTO 3 - Sistema de Votación Electrónica
 * Nombre: Jose Bryan Omar Jimenez Velazquez
 * Grupo: 7CM4
 */

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

public class GeneradorVotos {
    private static final String[] PARTIDOS = {
        "PRI", "PAN", "PRD", "MORENA", "VERDE", "PT", "MC", "PES"
    };
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java GeneradorVotos <registros_por_segundo>");
            return;
        }
        
        int registrosPorSegundo = Integer.parseInt(args[0]);
        long intervaloMicros = 1000000 / registrosPorSegundo; // Microsegundos entre registros
        
        System.out.println("Iniciando generador de votos: " + registrosPorSegundo + " votos/segundo");
        
        try (FileOutputStream fos = new FileOutputStream("VOTOS.dat", true);
             OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
             BufferedWriter writer = new BufferedWriter(osw)) {
            
            while (true) {
                long inicioCiclo = System.nanoTime();
                
                for (int i = 0; i < registrosPorSegundo; i++) {
                    String curp = CurpManager.getCURP();
                    String partido = PARTIDOS[ThreadLocalRandom.current().nextInt(PARTIDOS.length)];
                    
                    writer.write(curp + "," + partido);
                    writer.newLine();
                    writer.flush();
                }
                
                long finCiclo = System.nanoTime();
                long tiempoTranscurrido = (finCiclo - inicioCiclo) / 1000; // Convertir a microsegundos
                long tiempoRestante = intervaloMicros - tiempoTranscurrido;
                
                if (tiempoRestante > 0) {
                    Thread.sleep(tiempoRestante / 1000, (int)(tiempoRestante % 1000) * 1000);
                }
            }
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}