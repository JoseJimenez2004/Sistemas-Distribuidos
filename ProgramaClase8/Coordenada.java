package ProgramaClase8;

public class Coordenada {
    private double x;
    private double y;
    private double magnitud;

    public Coordenada(double x, double y) {
        this.x = x;
        this.y = y;
        this.magnitud = Math.sqrt(x * x + y * y);
    }

    public double abcisa() { return x; }
    public double ordenada() { return y; }
    public double getMagnitud() { return magnitud; }

    @Override
    public String toString() {
        return "(" + String.format("%.3f", x) + ", " + String.format("%.3f", y) + 
               ")  | Magnitud: " + String.format("%.3f", magnitud);
    }
}
