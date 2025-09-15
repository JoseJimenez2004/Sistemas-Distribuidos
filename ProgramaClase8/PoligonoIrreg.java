package ProgramaClase8;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class PoligonoIrreg {
    private ArrayList<Coordenada> vertices;

    // Constructor vacío
    public PoligonoIrreg() {
        vertices = new ArrayList<>();
    }

    // Constructor que recibe número de vértices y los genera aleatorios
    public PoligonoIrreg(int numVertices) {
        vertices = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            vertices.add(generarVerticeAleatorio());
        }
    }

    public void anadeVertice(Coordenada v) {
        vertices.add(v);
    }

    // Método para ordenar usando Comparator
    public void ordenaVertices() {
        Collections.sort(vertices, new Comparator<Coordenada>() {
            @Override
            public int compare(Coordenada c1, Coordenada c2) {
                return Double.compare(c1.getMagnitud(), c2.getMagnitud());
            }
        });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Polígono irregular con " + vertices.size() + " vértices:\n");
        int i = 1;
        for (Coordenada v : vertices) {
            sb.append("Vértice ").append(i++).append(": ").append(v).append("\n");
        }
        return sb.toString();
    }

    // Generar vértice aleatorio entre -100 y +100
    public static Coordenada generarVerticeAleatorio() {
        Random rand = new Random();
        double x = -100 + (200 * rand.nextDouble());
        double y = -100 + (200 * rand.nextDouble());
        return new Coordenada(x, y);
    }
}
