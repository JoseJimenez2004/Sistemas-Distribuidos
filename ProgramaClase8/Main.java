package ProgramaClase8;

public class Main {
    public static void main(String[] args) {
        // Crear polígono con 7 vértices aleatorios
        PoligonoIrreg poli = new PoligonoIrreg(7);

        // Imprimir polígono antes de ordenar
        System.out.println("=== Polígono antes de ordenar ===");
        System.out.println(poli);

        // Ordenar por magnitud
        poli.ordenaVertices();

        // Imprimir polígono después de ordenar
        System.out.println("=== Polígono después de ordenar ===");
        System.out.println(poli);
    }
}
