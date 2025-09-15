/**
 * PROYECTO 2 - Simulación de Nave Espacial con Asteroides
 * Elaborado por: Jimenez Velazquez Jose Bryan Omar
 * Profesor: M. en C. Ukranio Coronilla
 * GRUPO: 7CM4
 * 
 */

package Proyecto2;

import javax.swing.*;
import java.awt.*;
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
    private long startTime;
    private long gameTime = 0;
    private int asteroidsAvoided = 0;
    
    // Sistema de asteroides
    private ArrayList<Asteroid> asteroids;
    private int asteroidSpawnTimer = 0;
    private final int ASTEROID_SPAWN_RATE = 40; // Frames entre generación de asteroides
    
    public SpacePanel(int width, int height, int asteroidCount) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.START_X = 30;
        this.END_X = WIDTH -40;
        spaceship = new Spaceship(START_X, HEIGHT);
        
        // Inicializar sistema de asteroides con la cantidad especificada
        asteroids = new ArrayList<>();
        for (int i = 0; i < asteroidCount; i++) {
            asteroids.add(new Asteroid(WIDTH, HEIGHT));
        }
        
        // Generar estrellas de fondo
        for (int i = 0; i < 200; i++) {
            stars.add(new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT)));
        }
        
        startTime = System.currentTimeMillis();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo negro con estrellas
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        drawStars(g2d);
        
        // Dibujar asteroides
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(g2d);
        }
        
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
        // Barra de progreso con diseño mejorado
        int barWidth = 500;
        int barHeight = 25;
        int barX = (WIDTH - barWidth) / 2;
        int barY = 30;
        
        // Fondo de la barra con efecto de neón
        GradientPaint bgGradient = new GradientPaint(barX, barY, new Color(20, 20, 40), 
                                                    barX, barY + barHeight, new Color(10, 10, 20));
        g2d.setPaint(bgGradient);
        g2d.fillRoundRect(barX, barY, barWidth, barHeight, 15, 15);
        
        // Borde de la barra
        g2d.setColor(new Color(100, 100, 200, 150));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRoundRect(barX, barY, barWidth, barHeight, 15, 15);
        
        // Barra de progreso con gradiente
        int progressWidth = (int) (barWidth * (progress / 100));
        if (progressWidth > 0) {
            GradientPaint progressGradient = new GradientPaint(barX, barY, new Color(0, 200, 255), 
                                                             barX, barY + barHeight, new Color(0, 100, 200));
            g2d.setPaint(progressGradient);
            g2d.fillRoundRect(barX + 2, barY + 2, progressWidth - 4, barHeight - 4, 10, 10);
            
            // Efecto de brillo en la barra de progreso
            g2d.setColor(new Color(255, 255, 255, 50));
            g2d.fillRoundRect(barX + 2, barY + 2, progressWidth - 4, (barHeight - 4) / 2, 10, 10);
        }
        
        // Texto de progreso
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Sombra del texto
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString("PROGRESO: " + String.format("%.1f", progress) + "%", barX + 5, barY - 5);
        
        // Texto principal
        g2d.setColor(new Color(200, 230, 255));
        g2d.drawString("PROGRESO: " + String.format("%.1f", progress) + "%", barX + 3, barY - 7);
        
        // Información adicional
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(new Color(200, 200, 200));
        g2d.drawString("Asteroides: " + asteroids.size(), 20, HEIGHT - 60);
        g2d.drawString("Tiempo: " + String.format("%.1f", gameTime / 1000.0) + "s", 20, HEIGHT - 40);
        g2d.drawString("Evitados: " + asteroidsAvoided, 20, HEIGHT - 20);
        
        // Indicadores de inicio y fin, No se si dejarlo o no. 
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(new Color(150, 200, 255));
        g2d.drawString("INICIO", 15, barY + 15);
        g2d.drawString("META", WIDTH - 45, barY + 15);
        
        // Puntos de inicio y meta (mejorados)
        g2d.setColor(new Color(0, 100, 255, 200)); // Inicio
        g2d.fillRect(0, 0, 10, HEIGHT);
        
        g2d.setColor(new Color(0, 255, 100, 200)); // Meta
        g2d.fillRect(WIDTH - 10, 0, 10, HEIGHT);
        
        // Efecto de neón en los bordes
        g2d.setStroke(new BasicStroke(3f));
        g2d.setColor(new Color(0, 150, 255, 100));
        g2d.drawRect(0, 0, 10, HEIGHT);
        
        g2d.setColor(new Color(0, 255, 150, 100));
        g2d.drawRect(WIDTH - 10, 0, 10, HEIGHT);
        
        // Mensaje de victoria si se completó
        if (progress >= 100) {
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            g2d.setColor(new Color(0, 255, 0, 150));
            String victoryText = "¡MISIÓN COMPLETADA!";
            int textWidth = g2d.getFontMetrics().stringWidth(victoryText);
            g2d.drawString(victoryText, (WIDTH - textWidth) / 2, HEIGHT / 2);
        }
    }

    public void update() {
        if (!gameRunning) return;
        
        // Actualizar tiempo de juego
        gameTime = System.currentTimeMillis() - startTime;
        
        spaceship.update();
        
        // Actualizar asteroides y contar los evitados
        updateAsteroids();
        
        // Calcular progreso
        float totalDistance = END_X - START_X;
        float currentDistance = spaceship.getX() - START_X;
        progress = Math.min(100, Math.max(0, (currentDistance / totalDistance) * 100));
        
        if (spaceship.getX() > END_X) {
            gameRunning = false;
            progress = 100;
            ((SpaceSimulation) SwingUtilities.getWindowAncestor(this)).showVictoryDialog();
        }
    }
    
    private void updateAsteroids() {
        // Actualizar asteroides existentes y contar los que se salen de la pantalla para hacer recuento
        for (Asteroid asteroid : asteroids) {
            boolean wasActive = asteroid.isActive();
            asteroid.update();
            
            // Si el asteroide estaba activo y ahora no lo está, incrementar contador de evitados 
            if (wasActive && !asteroid.isActive()) {
                asteroidsAvoided++;
            }
        }
        
        // Generar nuevos asteroides
        asteroidSpawnTimer++;
        if (asteroidSpawnTimer >= ASTEROID_SPAWN_RATE) {
            asteroidSpawnTimer = 0;
            
            // Buscar un asteroide inactivo para reutilizar
            for (Asteroid asteroid : asteroids) {
                if (!asteroid.isActive()) {
                    asteroid.reset(WIDTH, HEIGHT);
                    break;
                }
            }
        }
    }
    
    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }
    
    public boolean isGameRunning() {
        return gameRunning;
    }
    
    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public Spaceship getSpaceship() {
        return spaceship;
    }
    
    public float getProgress() {
        return progress;
    }
    
    public long getGameTime() {
        return gameTime;
    }
    
    public int getAsteroidsAvoided() {
        return asteroidsAvoided;
    }
}