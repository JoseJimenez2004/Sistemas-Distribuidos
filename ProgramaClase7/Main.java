package ProgramaClase7;

public class Main {
    public static void main(String[] args) {
        // Crear figuras
        TrianguloEq t = new TrianguloEq(new Coordenada(0, 0), 6);
        Rectangulo r = new Rectangulo(new Coordenada(2, 2), 8, 4);

        // Mostrar info inicial
        System.out.println("=== Figuras en posición inicial ===");
        System.out.println(t);
        System.out.println("Área Triángulo = " + t.area());
        System.out.println(r);
        System.out.println("Área Rectángulo = " + r.area());

        // Desplazar figuras
        t.desplazar(3, 4);
        r.desplazar(-2, 3);

        // Mostrar info desplazada
        System.out.println("\n=== Figuras después de desplazar ===");
        System.out.println(t);
        System.out.println("Área Triángulo = " + t.area());
        System.out.println(r);
        System.out.println("Área Rectángulo = " + r.area());

        // 🔹 Ejecutar el dibujo automáticamente
        DibujoFiguras.main(null);
    }
}
