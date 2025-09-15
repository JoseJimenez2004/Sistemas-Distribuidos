package Proyecto2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Random;

public class Asteroid {
    private float x, y;
    private float velocityX, velocityY;
    private float rotation;
    private float rotationSpeed;
    private int size;
    private int[] xPoints, yPoints;
    private int nPoints;
    private Color color;
    private Random random;
    private boolean active;
    
    public Asteroid(int screenWidth, int screenHeight) {
        random = new Random();
        reset(screenWidth, screenHeight);
    }
    
    public void reset(int screenWidth, int screenHeight) {
        // Generador de tamaño entre 15 y 40 píxeles
        size = 15 + random.nextInt(26);
        
        // Posición inicial fuera de la pantalla a la derecha
        x = screenWidth + random.nextInt(100);
        y = random.nextInt(screenHeight);
        
        // Velocidad aleatoria (cuando el asteroides es mas grandes, es más lento)
        float speed = 1.5f + random.nextFloat() * 2.5f;
        velocityX = -speed * (40f / size);
        velocityY = (random.nextFloat() - 0.5f) * 1.5f;
        
        // Rotación aleatoria
        rotation = random.nextFloat() * (float) Math.PI * 2;
        rotationSpeed = (random.nextFloat() - 0.5f) * 0.05f;
        
        // Crear forma irregular de asteroide
        nPoints = 8 + random.nextInt(5);
        xPoints = new int[nPoints];
        yPoints = new int[nPoints];
        
        for (int i = 0; i < nPoints; i++) {
            double angle = 2 * Math.PI * i / nPoints;
            double distance = size * (0.7 + random.nextDouble() * 0.3);
            xPoints[i] = (int) (Math.cos(angle) * distance);
            yPoints[i] = (int) (Math.sin(angle) * distance);
        }
        
        // Color grisáceo con variaciones
        int gray = 100 + random.nextInt(80);
        int variation = random.nextInt(30) - 15;
        color = new Color(
            Math.max(0, Math.min(255, gray + variation)),
            Math.max(0, Math.min(255, gray + variation)),
            Math.max(0, Math.min(255, gray + variation))
        );
        
        active = true;
    }
    
    public void update() {
        if (!active) return;
        
        x += velocityX;
        y += velocityY;
        rotation += rotationSpeed;
        
        // Si el asteroide sale completamente de la pantalla por la izquierda,
        // Se marca como inactivo para ser reutilizado
        if (x < -size * 2) {
            active = false;
        }
    }
    
    public void draw(Graphics2D g2d) {
        if (!active) return;
        
        AffineTransform oldTransform = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(rotation);
        
        // Dibujar asteroide
        g2d.setColor(color);
        g2d.fillPolygon(xPoints, yPoints, nPoints);
        
        // Añadir detalles/cráteres
        g2d.setColor(color.darker());
        for (int i = 0; i < 2; i++) {
            int craterSize = size / 4 + random.nextInt(size / 4);
            int craterX = random.nextInt(size) - size / 2;
            int craterY = random.nextInt(size) - size / 2;
            g2d.fillOval(craterX, craterY, craterSize, craterSize);
        }
        
        g2d.setTransform(oldTransform);
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int) x - size, (int) y - size, size * 2, size * 2);
    }
    
    public float getX() {
        return x;
    }
    
    public float getY() {
        return y;
    }
    
    public int getSize() {
        return size;
    }
}