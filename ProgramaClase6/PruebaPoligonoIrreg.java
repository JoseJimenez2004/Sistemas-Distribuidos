 package ProgramaClase6;

public class PruebaPoligonoIrreg {
    public static void main(String[] args) {
        // Crear un polígono irregular con 7 vértices
        PoligonoIrreg poligono = new PoligonoIrreg(7);

        System.out.println("=== Polígono original ===");
        System.out.println(poligono);

        // Modificar el vértice 3 (índice 2)
        Coordenada nuevoVertice = new Coordenada(50, 50);
        poligono.modificaVertice(2, nuevoVertice);

        System.out.println("\n=== Polígono después de modificar vértice 3 ===");
        System.out.println(poligono);
    }
}
