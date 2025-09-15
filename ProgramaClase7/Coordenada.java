package ProgramaClase7;

public class Coordenada {
    private double x, y;

    public Coordenada(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getter de x
    public double abcisa() { return x; }

    // Getter de y
    public double ordenada() { return y; }

    // Método para desplazar coordenada
    public void desplazar(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
