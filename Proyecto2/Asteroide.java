// PROYECTO 2
// Profesor: M. en C. Ukranio Coronilla
// Elaborado por: Jimenez Velazquez Jose Bryan Omar
// 7CM4

package Proyecto2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;
import java.awt.geom.AffineTransform;

public class Asteroide {
    private double x, y;
    private double velocidadX, velocidadY;
    private double velocidadRotacion;
    private double angulo;
    private int tamaño;
    private Polygon poligono;
    private Random rand;
    private Color color;

    public Asteroide(int x, int y, Random rand) {
        this.x = x;
        this.y = y;
        this.rand = rand;
        this.tamaño = 15 + rand.nextInt(35); // Tamaños entre 15 y 50
        this.velocidadX = -1.5 - rand.nextDouble() * 3; // Velocidad aleatoria
        this.velocidadY = -1 + rand.nextDouble() * 2;
        this.velocidadRotacion = rand.nextDouble() * 0.03 - 0.015;
        this.color = new Color(
            100 + rand.nextInt(100), 
            100 + rand.nextInt(100), 
            100 + rand.nextInt(100)
        );
        crearPoligono();
    }

    private void crearPoligono() {
        poligono = new Polygon();
        int puntos = 7 + rand.nextInt(6); // Entre 7 y 12 puntos
        double anguloSegmento = 2 * Math.PI / puntos;
        
        for (int i = 0; i < puntos; i++) {
            double variacion = 0.7 + rand.nextDouble() * 0.6; // Forma irregular
            int px = (int) (tamaño * variacion * Math.cos(i * anguloSegmento));
            int py = (int) (tamaño * variacion * Math.sin(i * anguloSegmento));
            poligono.addPoint(px, py);
        }
    }

    public void actualizar() {
        x += velocidadX;
        y += velocidadY;
        angulo += velocidadRotacion;
        
        // Rebote suave en bordes superior e inferior
        if (y < tamaño) {
            y = tamaño;
            velocidadY = Math.abs(velocidadY) * 0.8;
        } else if (y > 720 - tamaño) {
            y = 720 - tamaño;
            velocidadY = -Math.abs(velocidadY) * 0.8;
        }
    }

    public void reiniciar(int anchoVentana, int altoVentana) {
        this.x = anchoVentana + rand.nextInt(200);
        this.y = rand.nextInt(altoVentana);
        this.velocidadX = -1.5 - rand.nextDouble() * 3;
        this.velocidadY = -1 + rand.nextDouble() * 2;
        this.velocidadRotacion = rand.nextDouble() * 0.03 - 0.015;
        this.tamaño = 15 + rand.nextInt(35);
        crearPoligono();
    }

    public void dibujar(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();
        
        g2d.translate(x, y);
        g2d.rotate(angulo);
        
        // Dibujar asteroide con gradiente de color
        g2d.setColor(color);
        g2d.fill(poligono);
        
        // Dibujar cráteres
        g2d.setColor(new Color(70, 70, 70));
        for (int i = 0; i < 3; i++) {
            int craterSize = tamaño / 5;
            int craterX = (int) (poligono.xpoints[i] * 0.5);
            int craterY = (int) (poligono.ypoints[i] * 0.5);
            g2d.fillOval(craterX - craterSize/2, craterY - craterSize/2, craterSize, craterSize);
        }
        
        g2d.setTransform(oldTransform);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTamaño() {
        return tamaño;
    }
}