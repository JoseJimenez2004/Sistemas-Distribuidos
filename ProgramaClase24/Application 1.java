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

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList; // Importar ArrayList

public class Application {
    // Actualizar las direcciones de los workers al endpoint /searchtoken [cite: 42]
    private static final String WORKER_ADDRESS_1 = "http://localhost:8081/searchtoken";
    private static final String WORKER_ADDRESS_2 = "http://localhost:8082/searchtoken";

    public static void main(String[] args) {
        Aggregator aggregator = new Aggregator();

        // Crear la lista de tareas especificada en el PDF [cite: 37-41]
        List<String> tasks = new ArrayList<>();
        tasks.add("1757600, IPN");
        tasks.add("17576, SAL");
        tasks.add("70000, MAS");
        tasks.add("1757600, PEZ");
        tasks.add("175700, SOL");

        // Imprimir las tareas como en la salida de ejemplo [cite: 49-53]
        System.out.println("Las tareas a resolver son las siguientes:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("Tarea " + i + ":" + tasks.get(i));
        }

        // Enviar las tareas a los workers
        List<String> results = aggregator.sendTasksToWorkers(
                Arrays.asList(WORKER_ADDRESS_1, WORKER_ADDRESS_2),
                tasks
        );

        // Imprimir los resultados en el formato solicitado [cite: 56-60]
        for (int i = 0; i < tasks.size(); i++) {
            // El resultado ya viene formateado como "El numero de apariciones es X"
            System.out.println("Para la tarea " + tasks.get(i) + " " + results.get(i));
        }
    }
}