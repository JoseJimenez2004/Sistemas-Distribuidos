package ProgramaClase7;


public class TrianguloEq extends Figura {
    private double lado;

    public TrianguloEq(Coordenada centro, double lado) {
        super(centro);
        this.lado = lado;

        // Altura del triángulo equilátero
        double h = Math.sqrt(3) / 2 * lado;

        // Vértices (orientado con un vértice arriba)
        vertices = new Coordenada[3];
        vertices[0] = new Coordenada(centro.abcisa(), centro.ordenada() + (2.0/3.0)*h); // arriba
        vertices[1] = new Coordenada(centro.abcisa() - lado/2, centro.ordenada() - (1.0/3.0)*h); // abajo izq
        vertices[2] = new Coordenada(centro.abcisa() + lado/2, centro.ordenada() - (1.0/3.0)*h); // abajo der
    }

    @Override
    public double area() {
        return (Math.sqrt(3) / 4) * lado * lado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Triángulo equilátero\n");
        for (int i = 0; i < vertices.length; i++) {
            sb.append("V").append(i+1).append(": ").append(vertices[i]).append("\n");
        }
        return sb.toString();
    }
}
