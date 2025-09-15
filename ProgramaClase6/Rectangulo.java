package ProgramaClase6;

public class Rectangulo {
    private Coordenada superiorIzq, inferiorDer;
    
    // Constructor por defecto
    public Rectangulo() {
        superiorIzq = new Coordenada(0,0);
        inferiorDer = new Coordenada(0,0);
    }
    
    // Constructor con valores numéricos
    public Rectangulo(double xSupIzq, double ySupIzq, double xInfDer, double yInfDer) {
        // Validación de coordenadas
        if (xSupIzq > xInfDer || ySupIzq < yInfDer) {
            throw new IllegalArgumentException(
                "La primer coordenada no se encuentra arriba y a la izquierda de la segunda"
            );
        }
        superiorIzq = new Coordenada(xSupIzq, ySupIzq);
        inferiorDer = new Coordenada(xInfDer, yInfDer);        
    }

    // Constructor con objetos Coordenada (copias independientes y validación)
    public Rectangulo(Coordenada supIzq, Coordenada infDer) {
        if (supIzq.abcisa() > infDer.abcisa() || supIzq.ordenada() < infDer.ordenada()) {
            throw new IllegalArgumentException(
                "La primer coordenada no se encuentra arriba y a la izquierda de la segunda"
            );
        }
        this.superiorIzq = new Coordenada(supIzq.abcisa(), supIzq.ordenada());
        this.inferiorDer = new Coordenada(infDer.abcisa(), infDer.ordenada());
    }
    
    // Métodos getter
    public Coordenada superiorIzquierda() { return superiorIzq; }
    public Coordenada inferiorDerecha() { return inferiorDer; }
    
    // Sobrescritura de toString()
    @Override
    public String toString() {
        return "Esquina superior izquierda: " + superiorIzq +
               "\tEsquina inferior derecha: " + inferiorDer + "\n";
    }
}
