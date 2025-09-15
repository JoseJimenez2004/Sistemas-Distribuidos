package ProgramaClase6;

public class PoligonoIrreg {
    private Coordenada[] vertices;  // Arreglo de vértices

    // Constructor: recibe el número de vértices y los inicializa con valores aleatorios
    public PoligonoIrreg(int numVertices) {
        if (numVertices < 3) {
            throw new IllegalArgumentException("Un polígono debe tener al menos 3 vértices");
        }

        vertices = new Coordenada[numVertices];
        for (int i = 0; i < numVertices; i++) {
            // Valores aleatorios entre -100 y 100 para abarcar los 4 cuadrantes
            double x = Math.random() * 200 - 100;
            double y = Math.random() * 200 - 100;
            vertices[i] = new Coordenada(x, y);
        }
    }

    // Método para modificar un vértice n-ésimo (índice desde 0)
    public void modificaVertice(int n, Coordenada nuevoVertice) {
        if (n < 0 || n >= vertices.length) {
            throw new IndexOutOfBoundsException("Índice de vértice fuera de rango");
        }
        // Crear copia independiente para mantener encapsulación
        vertices[n] = new Coordenada(nuevoVertice.abcisa(), nuevoVertice.ordenada());
    }

    // Sobrescribir toString para imprimir todos los vértices
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Polígono irregular con " + vertices.length + " vértices:\n");
        for (int i = 0; i < vertices.length; i++) {
            sb.append("Vértice ").append(i + 1).append(": ").append(vertices[i]).append("\n");
        }
        return sb.toString();
    }
}
