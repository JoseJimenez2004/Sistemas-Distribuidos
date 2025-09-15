package ProgramaClase7;

public abstract class Figura implements Desplazable {
    protected Coordenada centro;
    protected Coordenada[] vertices;

    public Figura(Coordenada centro) {
        this.centro = centro;
    }

    public Coordenada getCentro() {
        return centro;
    }

    public Coordenada[] getVertices() {
        return vertices;
    }

    // Método abstracto para el área
    public abstract double area();

    // Implementación de desplazar (afecta a centro y vértices)
    @Override
    public void desplazar(double dx, double dy) {
        centro.desplazar(dx, dy);
        for (Coordenada v : vertices) {
            v.desplazar(dx, dy);
        }
    }
}
