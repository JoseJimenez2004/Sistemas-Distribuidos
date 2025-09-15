package Proyecto2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SpacePanel extends JPanel {
    private Spaceship spaceship;
    private final Random random = new Random();
    private final ArrayList<Point> stars = new ArrayList<>();
    private final int START_X;
    private final int END_X;
    private final int WIDTH;
    private final int HEIGHT;
    private float progress = 0;
    private boolean gameRunning = true;

    public SpacePanel(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.START_X = 30;
        this.END_X = WIDTH - 60;
        spaceship = new Spaceship(START_X, HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo negro
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Dibujar nave y su estela
        spaceship.drawTrail(g2d);
        spaceship.draw(g2d);

        // Dibujar interfaz de usuario
        drawUI(g2d);
    }
    
    private void drawStars(Graphics2D g2d) {
        for (Point star : stars) {
            if (random.nextFloat() > 0.1f) {
                int brightness = 150 + random.nextInt(105);
                g2d.setColor(new Color(brightness, brightness, brightness));
                g2d.fillRect(star.x, star.y, 2, 2);
            }
        }
    }
    
    private void drawUI(Graphics2D g2d) {
        // Barra de progreso
        g2d.setColor(new Color(30, 30, 30));
        g2d.fillRect(40, 25, 520, 30);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(45, 30, (int) (progress * 5.1), 20);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(45, 30, 510, 20);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("PROGRESO: " + String.format("%.1f", progress) + "%", 50, 20);
        
        // Puntos de inicio y meta
        g2d.setColor(new Color(0, 255, 0, 100)); // Meta
        g2d.fillRect(WIDTH - 10, 0, 10, HEIGHT);
        g2d.setColor(new Color(0, 100, 255, 100)); // Inicio
        g2d.fillRect(0, 0, 10, HEIGHT);
    }

    public void update() {
        if (!gameRunning) return;
        
        spaceship.update();
        
        // Calcular progreso
        float totalDistance = END_X - START_X;
        float currentDistance = spaceship.getX() - START_X;
        progress = Math.min(100, Math.max(0, (currentDistance / totalDistance) * 100));
        
        if (spaceship.getX() > END_X) {
            gameRunning = false;
            progress = 100;
            System.out.println("¡Misión completada! 100% de avance");
        }
    }
}