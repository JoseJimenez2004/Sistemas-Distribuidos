package Proyecto2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Random;

public class Spaceship {
    private float x, y;
    private float velocity;
    private final float acceleration;
    private final float maxVelocity;
    private final int size = 25;
    private final Polygon shape;
    private final ArrayList<Point> trail = new ArrayList<>();
    private final int MAX_TRAIL = 40;
    private float targetY;
    private final Random random = new Random();
    private final int HEIGHT;
    private final int START_X;
    
    public Spaceship(int startX, int height) {
        this.START_X = startX;
        this.HEIGHT = height;
        
        // Posición inicial en el centro izquierdo
        x = START_X;
        y = HEIGHT / 4f; // Usar float para mayor precisión
        targetY = y;
        
        velocity = 0.5f;
        acceleration = 0.15f;
        maxVelocity = 5.0f;
        
        // Crear forma de nave
        shape = new Polygon();
        shape.addPoint(size, 0);
        shape.addPoint(-size / 2, -size / 2);
        shape.addPoint(-size / 3, 0);
        shape.addPoint(-size / 2, size / 2);
    }

    public void update() {
        // Aceleración gradual y movimiento en X
        velocity = Math.min(maxVelocity, velocity + acceleration * (1 - velocity / maxVelocity));
        x += velocity;
        
        // Movimiento vertical suave
        float wave = (float) Math.sin(x * 0.01f) * 20f;
        float randomVariation = (random.nextFloat() - 0.5f) * 4f;
        targetY = HEIGHT / 2f + wave + randomVariation;
        y += (targetY - y) * 0.03f;
        
        // Limitar la nave a la ventana
        y = Math.max(size + 20, Math.min(HEIGHT - size - 20, y));

        // Actualizar estela
        if (trail.size() >= MAX_TRAIL) {
            trail.remove(0);
        }
        trail.add(new Point((int) x, (int) y));
    }

    public void draw(Graphics2D g2d) {
        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(x, y);

        // Rotación ligera
        float rotation = (targetY - y) * 0.01f;
        g2d.rotate(rotation);

        // Dibujar cuerpo de la nave
        g2d.setColor(new Color(200, 200, 255));
        g2d.fill(shape);

        // Dibujar propulsores
        int alpha = 150 + (int) (velocity * 20);
        g2d.setColor(new Color(255, 165, 0, Math.min(255, alpha)));
        int flameLength = (int) (size * 0.9 * (velocity / maxVelocity));
        int[] flameX = {-size / 2, -size / 2 - flameLength, -size / 2};
        int[] flameY = {-size / 4, 0, size / 4};
        g2d.fillPolygon(flameX, flameY, 3);
        
        g2d.setTransform(oldTransform);
    }

    public void drawTrail(Graphics2D g2d) {
        for (int i = 0; i < trail.size() - 1; i++) {
            Point p1 = trail.get(i);
            Point p2 = trail.get(i + 1);
            
            float alpha = (float) i / trail.size();
            int width = (int) (size * 0.4f * alpha);
            int colorAlpha = (int) (200 * alpha);
            
            int blue = 255;
            int green = 150 + (int) (105 * alpha);
            
            g2d.setColor(new Color(0, green, blue, colorAlpha));
            g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        
        // Dibujar puntos de ruta
        g2d.setColor(new Color(0, 200, 255, 100));
        for (Point point : trail) {
            g2d.fillOval(point.x - 2, point.y - 2, 4, 4);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}