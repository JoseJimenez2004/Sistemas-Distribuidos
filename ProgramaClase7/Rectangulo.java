package ProgramaClase7;


public class Rectangulo extends Figura {
    private double base, altura;

    public Rectangulo(Coordenada centro, double base, double altura) {
        super(centro);
        this.base = base;
        this.altura = altura;

        // Vértices (rectángulo centrado en el punto)
        double halfB = base / 2;
        double halfH = altura / 2;

        vertices = new Coordenada[4];
        vertices[0] = new Coordenada(centro.abcisa() - halfB, centro.ordenada() + halfH); // sup izq
        vertices[1] = new Coordenada(centro.abcisa() + halfB, centro.ordenada() + halfH); // sup der
        vertices[2] = new Coordenada(centro.abcisa() + halfB, centro.ordenada() - halfH); // inf der
        vertices[3] = new Coordenada(centro.abcisa() - halfB, centro.ordenada() - halfH); // inf izq
    }

    @Override
    public double area() {
        return base * altura;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Rectángulo\n");
        for (int i = 0; i < vertices.length; i++) {
            sb.append("V").append(i+1).append(": ").append(vertices[i]).append("\n");
        }
        return sb.toString();
    }
}
