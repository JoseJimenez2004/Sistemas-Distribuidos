/*
 * MIT License
 *
 * Copyright (c) 2019 Michael Pogrebinsky - Distributed Systems & Cloud Computing with Java
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import networking.WebClient;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays; // Importar
import java.util.LinkedList; // Importar
import java.util.Queue; // Importar
import java.util.concurrent.CompletableFuture;

public class Aggregator {
    private WebClient webClient;

    public Aggregator() {
        this.webClient = new WebClient();
    }

    /**
     * Clase auxiliar para envolver el resultado de una tarea con metadatos.
     * Esto nos permite saber qué trabajador/tarea acaba de terminar.
     */
    private class TaskResult {
        String result;
        String worker;
        String task;
        int originalIndex;

        public TaskResult(String result, String worker, String task, int originalIndex) {
            this.result = result;
            this.worker = worker;
            this.task = task;
            this.originalIndex = originalIndex;
        }
    }

    public List<String> sendTasksToWorkers(List<String> workersAddresses, List<String> tasks) {
        // Cola para las tareas pendientes
        Queue<String> taskQueue = new LinkedList<>(tasks);
        
        // Arreglo para almacenar los resultados finales en el orden original
        String[] finalResults = new String[tasks.size()];
        
        // Lista para rastrear los futuros que están actualmente en ejecución
        List<CompletableFuture<TaskResult>> activeFutures = new ArrayList<>();
        
        int tasksAssigned = 0;
        int tasksCompleted = 0;

        // 1. Asignación inicial: Asigna una tarea a cada worker disponible [cite: 43]
        for (String worker : workersAddresses) {
            if (taskQueue.isEmpty()) {
                break; // No hay más tareas para asignar
            }

            String task = taskQueue.poll(); // Saca la siguiente tarea de la cola
            int taskIndex = tasksAssigned++;
            
            System.out.println("Al servidor " + worker + " se le asigna la tarea: " + task); // [cite: 54]

            // Envía la tarea y envuelve el futuro en un TaskResult
            CompletableFuture<TaskResult> taskFuture = webClient.sendTask(worker, task.getBytes())
                .thenApply(result -> new TaskResult(result, worker, task, taskIndex));
            
            activeFutures.add(taskFuture);
        }

        // 2. Loop principal: Espera a que CUALQUIER tarea termine y asigna la siguiente [cite: 44, 45]
        while (tasksCompleted < tasks.size()) {
            // Espera a que cualquiera de los futuros activos se complete
            CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(activeFutures.toArray(new CompletableFuture[0]));

            try {
                // Obtiene el resultado (un TaskResult) del futuro que terminó
                TaskResult result = (TaskResult) anyFuture.get(); // .get() bloquea hasta que uno termine

                // Procesa el resultado
                tasksCompleted++;
                finalResults[result.originalIndex] = result.result; // Almacena en el índice original
                System.out.println("El servidor " + result.worker + " completó la tarea " + result.task); // [cite: 55]

                // Elimina el futuro que acaba de completarse de la lista activa
                // Se busca comparando la instancia del resultado
                activeFutures.removeIf(f -> f.isDone() && f.getNow(null) == result);

                // 3. Asigna una nueva tarea al worker que acaba de terminar, si quedan tareas
                if (!taskQueue.isEmpty()) {
                    String worker = result.worker; // El worker que se desocupó
                    String task = taskQueue.poll(); // La siguiente tarea
                    int taskIndex = tasksAssigned++;

                    System.out.println("Al servidor " + worker + " se le asigna la tarea: " + task); // [cite: 55]
                    
                    // Envía la nueva tarea y añade el nuevo futuro a la lista activa
                    CompletableFuture<TaskResult> newTaskFuture = webClient.sendTask(worker, task.getBytes())
                        .thenApply(res -> new TaskResult(res, worker, task, taskIndex));
                    
                    activeFutures.add(newTaskFuture);
                }

            } catch (Exception e) {
                // Manejo básico de excepciones
                e.printStackTrace();
            }
        } // El loop termina cuando todas las tareas (tasks.size()) han sido completadas

        return Arrays.asList(finalResults);
    }
}