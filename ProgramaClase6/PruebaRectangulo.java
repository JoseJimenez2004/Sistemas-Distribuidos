package ProgramaClase6;

public class PruebaRectangulo {
    public static void main (String[] args) {
        
        // Prueba con constructor original (valores numéricos)
        try {
            // superior izquierda (2,5), inferior derecha (5,2)
            Rectangulo rect1 = new Rectangulo(2,5,5,2);
            double ancho = rect1.inferiorDerecha().abcisa() - rect1.superiorIzquierda().abcisa();
            double alto = rect1.superiorIzquierda().ordenada() - rect1.inferiorDerecha().ordenada();
            
            System.out.println("=== Prueba con constructor original ===");
            System.out.println(rect1);
            System.out.println("El área del rectángulo es = " + ancho * alto);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al crear el objeto Rectangulo: " + e.getMessage());
        }
        
        // Prueba con constructor nuevo (objetos Coordenada)
        Coordenada c1 = new Coordenada(1,6);  // superior izquierda
        Coordenada c2 = new Coordenada(4,2);  // inferior derecha
        try {
            Rectangulo rect2 = new Rectangulo(c1, c2);
            double ancho = rect2.inferiorDerecha().abcisa() - rect2.superiorIzquierda().abcisa();
            double alto = rect2.superiorIzquierda().ordenada() - rect2.inferiorDerecha().ordenada();
            
            System.out.println("\n=== Prueba con constructor nuevo (objetos Coordenada) ===");
            System.out.println(rect2);
            System.out.println("El área del rectángulo es = " + ancho * alto);
        } catch (IllegalArgumentException e) {
            System.out.println("Error al crear el objeto Rectangulo: " + e.getMessage());
        }
        
      
    }
}
