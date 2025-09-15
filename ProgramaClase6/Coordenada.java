package ProgramaClase6;

public class Coordenada {
    private double x, y;

    public Coordenada(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Método getter de x
    public double abcisa() { return x; }

    // Método getter de y
    public double ordenada() { return y; }
    
    // Sobrescritura de toString() para imprimir el paso de parametros
    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}
