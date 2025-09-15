// PROYECTO 2
// Profesor: M. en C. Ukranio Coronilla
// Elaborado por: Jimenez Velazquez Jose Bryan Omar
// 7CM4

package Proyecto2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Nave {
    private double x, y;
    private double velocidadX, velocidadY;
    private final double ACELERACION = 0.08;
    private final double VELOCIDAD_MAX = 8.0;
    private final double FRENADO = 0.03;
    private Polygon poligono;
    private final int limiteX, limiteY;
    private double direccion; // Para efectos visuales
    private int tiempoSinAsteroides = 0;

    public Nave(int x, int y, int limiteX, int limiteY) {
        this.x = x;
        this.y = y;
        this.velocidadX = 0;
        this.velocidadY = 0;
        this.limiteX = limiteX;
        this.limiteY = limiteY;
        this.direccion = 0;
        crearPoligono();
    }

    private void crearPoligono() {
        // Crear forma de nave más detallada
        poligono = new Polygon();
        poligono.addPoint(0, -15);
        poligono.addPoint(-12, 10);
        poligono.addPoint(-5, 5);
        poligono.addPoint(0, 15);
        poligono.addPoint(5, 5);
        poligono.addPoint(12, 10);
    }

    public void actualizar(ArrayList<Asteroide> asteroides) {
        // Aplicar aceleración
        velocidadX += ACELERACION;
        if (velocidadX > VELOCIDAD_MAX) {
            velocidadX = VELOCIDAD_MAX;
        }
        
        // Movimiento inteligente para esquivar asteroides
        boolean asteroidesCercanos = false;
        double mejorDireccion = 0;
        double mayorUrgencia = 0;
        
        for (Asteroide asteroide : asteroides) {
            double distanciaX = asteroide.getX() - x;
            double distanciaY = asteroide.getY() - y;
            double distancia = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);
            
            // Solo considerar asteroides que están adelante y cerca
            if (distanciaX > 0 && distanciaX < 300 && Math.abs(distanciaY) < 200) {
                asteroidesCercanos = true;
                tiempoSinAsteroides = 0;
                
                // Calcular urgencia (más urgente si está más cerca y en rumbo de colisión)
                double urgencia = (300 - distanciaX) / 300 * (1 - Math.abs(distanciaY) / 200);
                
                if (urgencia > mayorUrgencia) {
                    mayorUrgencia = urgencia;
                    // Decidir dirección de evasión
                    if (distanciaY > 0) {
                        mejorDireccion = -1; // Arriba
                    } else {
                        mejorDireccion = 1;  // Abajo
                    }
                }
            }
        }
        
        if (!asteroidesCercanos) {
            tiempoSinAsteroides++;
        }
        
        // Comportamiento inteligente de la nave
        if (asteroidesCercanos && mayorUrgencia > 0.3) {
            // Evasión activa
            velocidadY += mejorDireccion * ACELERACION * 2 * mayorUrgencia;
        } else if (tiempoSinAsteroides > 60) {
            // Volver al centro cuando no hay asteroides cercanos
            double centroY = limiteY / 2;
            if (y < centroY - 20) {
                velocidadY += ACELERACION;
            } else if (y > centroY + 20) {
                velocidadY -= ACELERACION;
            }
        }
        
        // Aplicar inercia y límites de velocidad en Y
        if (velocidadY > VELOCIDAD_MAX / 2) velocidadY = VELOCIDAD_MAX / 2;
        if (velocidadY < -VELOCIDAD_MAX / 2) velocidadY = -VELOCIDAD_MAX / 2;
        
        // Aplicar frenado natural en Y
        if (velocidadY > 0) velocidadY -= FRENADO;
        else if (velocidadY < 0) velocidadY += FRENADO;
        
        // Actualizar posición
        x += velocidadX;
        y += velocidadY;
        
        // Actualizar dirección para efectos visuales
        direccion = velocidadY * 0.1;
        
        // Mantener dentro de límites verticales
        if (y < 20) {
            y = 20;
            velocidadY = 0;
        } else if (y > limiteY - 20) {
            y = limiteY - 20;
            velocidadY = 0;
        }
    }

    public boolean colisionaCon(Asteroide asteroide) {
        // Detección de colisión mejorada usando círculos de colisión
        double distanciaX = asteroide.getX() - x;
        double distanciaY = asteroide.getY() - y;
        double distancia = Math.sqrt(distanciaX * distanciaX + distanciaY * distanciaY);
        
        // Radio de colisión de la nave + radio del asteroide
        double radioColision = 15 + asteroide.getTamaño() / 2;
        
        return distancia < radioColision;
    }

    public void dibujar(Graphics2D g2d) {
        // Guardar transformación actual
        AffineTransform oldTransform = g2d.getTransform();
        
        // Aplicar transformaciones
        g2d.translate(x, y);
        g2d.rotate(direccion);
        
        // Dibujar nave
        g2d.setColor(new Color(0, 150, 255));
        g2d.fill(poligono);
        
        // Dibujar detalles
        g2d.setColor(Color.CYAN);
        g2d.fillPolygon(new int[]{-5, 0, 5}, new int[]{-10, -20, -10}, 3);
        
        // Dibujar motor
        int longitudMotor = (int) (velocidadX * 2);
        g2d.setColor(Color.ORANGE);
        g2d.fillRect(-8, -5, -longitudMotor, 5);
        g2d.fillRect(-8, 5, -longitudMotor, 5);
        g2d.setColor(Color.YELLOW);
        g2d.fillRect(-8, -3, -longitudMotor/2, 3);
        g2d.fillRect(-8, 7, -longitudMotor/2, 3);
        
        // Restaurar transformación
        g2d.setTransform(oldTransform);
    }

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
}