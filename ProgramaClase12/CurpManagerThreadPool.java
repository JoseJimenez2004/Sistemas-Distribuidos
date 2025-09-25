package ProgramaClase12;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CurpManagerThreadPool {

    public static String getCURP() {
        String letra = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numero = "0123456789";
        String sexo = "HM";
        String[] entidad = {"AS","BC","BS","CC","CS","CH","CL","CM","DF","DG","GT","GR","HG","JC",
                "MC","MN","MS","NT","NL","OC","PL","QT","QR","SP","SL","SR","TC","TL","TS",
                "VZ","YN","ZS"};

        StringBuilder sb = new StringBuilder(18);

        for (int i = 0; i < 4; i++) sb.append(letra.charAt(ThreadLocalRandom.current().nextInt(letra.length())));
        for (int i = 0; i < 6; i++) sb.append(numero.charAt(ThreadLocalRandom.current().nextInt(numero.length())));
        sb.append(sexo.charAt(ThreadLocalRandom.current().nextInt(sexo.length())));
        sb.append(entidad[ThreadLocalRandom.current().nextInt(entidad.length)]);
        String consonante = "BCDFGHJKLMNPQRSTVWXYZ";
        for (int i = 0; i < 3; i++) sb.append(consonante.charAt(ThreadLocalRandom.current().nextInt(consonante.length())));
        for (int i = 0; i < 2; i++) sb.append(numero.charAt(ThreadLocalRandom.current().nextInt(numero.length())));
        return sb.toString();
    }

    // Ordenamiento optimizado usando Collections.sort
    public static void ordenarCurps(ArrayList<String> curps) {
        curps.sort((a, b) -> a.substring(0, 4).compareTo(b.substring(0, 4)));
    }

    public static void main(String[] args) {
        int m, n, poolSize;
        try {
            m = Integer.parseInt(args[0]);
            n = Integer.parseInt(args[1]);
            poolSize = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.out.println("Uso: java ProgramaClase12.CurpManagerThreadPoolOptimizado <m> <n> <poolSize>");
            return;
        }

        ArrayList<ArrayList<String>> listas = new ArrayList<>();

        // Generar m listas con n CURPs cada una
        for (int i = 0; i < m; i++) {
            ArrayList<String> curps = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                curps.add(getCURP());
            }
            listas.add(curps);
        }

        // Crear pool de threads
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        // Enviar tareas de ordenamiento al pool
        for (int i = 0; i < listas.size(); i++) {
            int index = i;
            pool.execute(() -> ordenarCurps(listas.get(index)));
        }

        // Esperar a que todas las tareas terminen
        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
