package Proyecto2;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class Spaceship {
    private float x, y;
    private float velocity;
    private final float acceleration;
    private final float maxVelocity;
    private final int size = 30;
    private final ArrayList<Point> trail = new ArrayList<>();
    private final int MAX_TRAIL = 50;
    private float targetY;
    private final Random random = new Random();
    private final int HEIGHT;
    private final int START_X;
    
    // Colores de la nave
    private final Color PRIMARY_COLOR = new Color(100, 150, 255);
    private final Color SECONDARY_COLOR = new Color(70, 130, 230);
    private final Color ACCENT_COLOR = new Color(200, 220, 255);
    private final Color WINDOW_COLOR = new Color(160, 220, 255);
    private final Color ENGINE_COLOR = new Color(255, 100, 0);
    
    public Spaceship(int startX, int height) {
        this.START_X = startX;
        this.HEIGHT = height;
        
        // Posición inicial en el centro izquierdo
        x = START_X;
        y = HEIGHT / 4f;
        targetY = y;
        
        velocity = 0.5f;
        acceleration = 0.15f;
        maxVelocity = 5.0f;
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

        // Rotación ligera según el movimiento
        float rotation = (targetY - y) * 0.01f;
        g2d.rotate(rotation);

        // Dibujar la nave mejorada
        drawSpaceship(g2d);
        
        // Dibujar propulsores
        drawEngines(g2d);
        
        g2d.setTransform(oldTransform);
    }

    private void drawSpaceship(Graphics2D g2d) {
        // Cuerpo principal de la nave
        int[] bodyX = {size, -size/2, -size/3, -size/2};
        int[] bodyY = {0, -size/2, 0, size/2};
        
        // Cuerpo principal con gradiente
        GradientPaint bodyGradient = new GradientPaint(
            -size/2, -size/2, PRIMARY_COLOR, 
            -size/2, size/2, SECONDARY_COLOR
        );
        g2d.setPaint(bodyGradient);
        g2d.fillPolygon(bodyX, bodyY, 4);
        
        // Detalles de la nave
        g2d.setColor(ACCENT_COLOR);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawPolygon(bodyX, bodyY, 4);
        
        // Ventana de la cabina
        g2d.setColor(WINDOW_COLOR);
        Ellipse2D window = new Ellipse2D.Float(-size/4, -size/6, size/3, size/3);
        g2d.fill(window);
        
        g2d.setColor(ACCENT_COLOR);
        g2d.draw(window);
        
        // Detalles adicionales en el cuerpo
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(size/2, -size/4, size/2, size/4);
        g2d.drawLine(size/3, -size/3, size/3, size/3);
    }

    private void drawEngines(Graphics2D g2d) {
        // Intensidad de las llamas basada en la velocidad
        float engineIntensity = velocity / maxVelocity;
        int flameLength = (int) (size * 1.2 * engineIntensity);
        int flameWidth = (int) (size * 0.4 * engineIntensity);
        
        // Llama principal
        int alpha = 180 + (int) (75 * engineIntensity);
        Color flameColor = new Color(255, 165, 0, Math.min(255, alpha));
        Color innerFlameColor = new Color(255, 255, 200, Math.min(200, alpha));
        
        // Llama del propulsor principal
        int[] flameX = {-size/2, -size/2 - flameLength, -size/2};
        int[] flameY = {-size/4, 0, size/4};
        
        // Gradiente para la llama
        GradientPaint flameGradient = new GradientPaint(
            -size/2, -size/4, innerFlameColor,
            -size/2 - flameLength/2, 0, flameColor
        );
        
        g2d.setPaint(flameGradient);
        g2d.fillPolygon(flameX, flameY, 3);
        
        // Propulsores laterales más pequeños
        if (velocity > maxVelocity * 0.3) {
            int smallFlameLength = (int) (flameLength * 0.6);
            
            // Propulsor superior
            int[] topFlameX = {-size/3, -size/3 - smallFlameLength, -size/3};
            int[] topFlameY = {-size/3, -size/3, -size/3 + flameWidth/2};
            
            // Propulsor inferior
            int[] bottomFlameX = {-size/3, -size/3 - smallFlameLength, -size/3};
            int[] bottomFlameY = {size/3, size/3, size/3 - flameWidth/2};
            
            g2d.fillPolygon(topFlameX, topFlameY, 3);
            g2d.fillPolygon(bottomFlameX, bottomFlameY, 3);
        }
        
        // Base de los propulsores
        g2d.setColor(new Color(60, 60, 80));
        g2d.fillRect(-size/2 - 2, -size/4, 4, size/2);
        g2d.fillRect(-size/3 - 2, -size/3, 4, size/6);
        g2d.fillRect(-size/3 - 2, size/3 - size/6, 4, size/6);
    }

    public void drawTrail(Graphics2D g2d) {
        for (int i = 0; i < trail.size() - 1; i++) {
            Point p1 = trail.get(i);
            Point p2 = trail.get(i + 1);
            
            float alpha = (float) i / trail.size();
            int width = (int) (size * 0.3f * alpha);
            
            // Gradiente de color en la estela (azul a cian) con transparencia
            int blue = 255;
            int green = 150 + (int) (105 * alpha);
            int colorAlpha = (int) (200 * alpha);
            
            g2d.setColor(new Color(0, green, blue, colorAlpha));
            g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
        
        // Destellos en la estela
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < trail.size(); i += 5) {
            Point point = trail.get(i);
            float alpha = (float) i / trail.size();
            int glowSize = 3 + (int) (5 * alpha);
            
            g2d.setColor(new Color(100, 200, 255, (int) (150 * alpha)));
            g2d.fillOval(point.x - glowSize/2, point.y - glowSize/2, glowSize, glowSize);
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(x - size, y - size, size * 2, size * 2);
    }
}