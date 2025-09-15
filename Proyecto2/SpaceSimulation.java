package Proyecto2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.AffineTransform;

public class SpaceSimulation extends JFrame {
    private SpacePanel panel;
    private boolean gameRunning = true;
    private float progress = 0;
    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    
    public static void main(String[] args) {
        SpaceSimulation gui = new SpaceSimulation();
        gui.setVisible(true);
    }
    
    public SpaceSimulation() {
        setSize(WIDTH, HEIGHT);
        setTitle("Simulación de Nave Espacial - Solo Recorrido");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        panel = new SpacePanel();
        add(panel);
        
        // Timer para la animación
        Timer timer = new Timer(16, new ActionListener() { // ~60 FPS
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameRunning) {
                    panel.update();
                    panel.repaint();
                }
            }
        });
        timer.start();
    }
    
    private class SpacePanel extends JPanel {
        private Spaceship spaceship;
        private Random random;
        private ArrayList<Point> stars;
        private final int START_X = 50;
        private final int END_X = WIDTH + 1200;
        
        public SpacePanel() {
            random = new Random();
            spaceship = new Spaceship();
            
            // Crear estrellas de fondo
            stars = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
                stars.add(new Point(random.nextInt(WIDTH), random.nextInt(HEIGHT)));
            }
            
            setFocusable(true);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Fondo negro
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            
            // Dibujar estrellas de fondo (parpadeantes)
            g.setColor(Color.WHITE);
            for (Point star : stars) {
                if (random.nextFloat() > 0.1f) { // 90% de probabilidad de mostrarse
                    int brightness = 150 + random.nextInt(105);
                    g.setColor(new Color(brightness, brightness, brightness));
                    g.fillRect(star.x, star.y, 2, 2);
                }
            }
            
            // Dibujar nave y su estela
            spaceship.drawTrail(g);
            spaceship.draw(g);
            
            // Dibujar barra de progreso
            g.setColor(new Color(30, 30, 30));
            g.fillRect(40, 25, 520, 30);
            g.setColor(Color.GREEN);
            g.fillRect(45, 30, (int)(progress * 5.1), 20); // 5.1 para que 100% = 510px
            g.setColor(Color.WHITE);
            g.drawRect(45, 30, 510, 20);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("PROGRESO: " + String.format("%.1f", progress) + "%", 50, 20);
            
            // Dibujar meta (lado derecho)
            g.setColor(new Color(0, 255, 0, 100));
            g.fillRect(WIDTH - 10, 0, 10, HEIGHT);
            
            // Dibujar punto de inicio (lado izquierdo)
            g.setColor(new Color(0, 100, 255, 100));
            g.fillRect(0, 0, 10, HEIGHT);
            
            // Mostrar información de la nave
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 14));
            g.drawString("Velocidad: " + String.format("%.1f", spaceship.velocity) + " px/frame", 50, 70);
            g.drawString("Posición X: " + String.format("%.1f", spaceship.getX()) + " px", 50, 90);
            g.drawString("Posición Y: " + String.format("%.1f", spaceship.getY()) + " px", 50, 110);
        }
        
        public void update() {
            if (!gameRunning) return;
            
            // Actualizar nave
            spaceship.update();
            
            // Calcular progreso
            float totalDistance = END_X - START_X;
            float currentDistance = spaceship.getX() - START_X;
            progress = Math.min(100, Math.max(0, (currentDistance / totalDistance) * 100));
            
            // Si la nave llega al final
            if (spaceship.getX() > END_X) {
                gameRunning = false;
                progress = 100;
                System.out.println("¡Misión completada! 100% de avance");
            }
        }
        
        private class Spaceship {
            private float x, y;
            private float velocity;
            private float acceleration;
            private final float maxVelocity;
            private Polygon shape;
            private int size = 25;
            private ArrayList<Point> trail;
            private final int MAX_TRAIL = 30; // Estela más larga para mejor visualización
            private float targetY;
            
            public Spaceship() {
                // Posición inicial en el centro izquierdo
                x = START_X;
                y = HEIGHT / 2;
                targetY = HEIGHT / 2;
                
                velocity = 0.5f; // Pequeña velocidad inicial
                acceleration = 0.15f;
                maxVelocity = 5.0f;
                
                trail = new ArrayList<>();
                
                // Crear forma de nave
                shape = new Polygon();
                shape.addPoint(size, 0);
                shape.addPoint(-size/2, -size/2);
                shape.addPoint(-size/3, 0);
                shape.addPoint(-size/2, size/2);
            }
            
            public void update() {
                // Aplicar aceleración gradual
                if (velocity < maxVelocity) {
                    velocity += acceleration * (1 - velocity/maxVelocity);
                }
                
                // Mover nave en X
                x += velocity;
                
                // Movimiento suave con pequeñas variaciones en Y
                // Simula el movimiento natural de una nave en el espacio
                float wave = (float)Math.sin(x * 0.01f) * 20f;
                float randomVariation = (random.nextFloat() - 0.5f) * 4f;
                targetY = HEIGHT / 2 + wave + randomVariation;
                
                // Movimiento suave hacia la posición objetivo en Y
                float dy = targetY - y;
                y += dy * 0.03f;
                
                // Mantener dentro de límites razonables
                y = Math.max(size + 20, Math.min(HEIGHT - size - 20, y));
                
                // Añadir punto a la estela (más puntos para estela más densa)
                if (trail.size() >= MAX_TRAIL) {
                    trail.remove(0);
                }
                trail.add(new Point((int)x, (int)y));
            }
            
            public void draw(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar nave
                AffineTransform oldTransform = g2d.getTransform();
                g2d.translate(x, y);
                
                // Rotación ligera según el movimiento vertical
                float rotation = (targetY - y) * 0.01f;
                g2d.rotate(rotation);
                
                // Cuerpo de la nave
                g2d.setColor(new Color(200, 200, 255));
                g2d.fill(shape);
                
                // Detalles de la nave
                g2d.setColor(new Color(80, 80, 220));
                g2d.fillRect(size/3, -size/6, size/2, size/3);
                
                // Propulsores (más intensos según la velocidad)
                int alpha = 150 + (int)(velocity * 20);
                g2d.setColor(new Color(255, 165, 0, Math.min(255, alpha)));
                int flameLength = (int)(size * 0.9 * (velocity / maxVelocity));
                int[] flameX = {-size/2, -size/2 - flameLength, -size/2};
                int[] flameY = {-size/4, 0, size/4};
                g2d.fillPolygon(flameX, flameY, 3);
                
                // Restaurar transformación
                g2d.setTransform(oldTransform);
            }
            
            public void drawTrail(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar estela de la nave con degradado
                for (int i = 0; i < trail.size() - 1; i++) {
                    Point p1 = trail.get(i);
                    Point p2 = trail.get(i + 1);
                    
                    float alpha = (float)i / trail.size();
                    int width = (int)(size * 0.4f * alpha);
                    int colorAlpha = (int)(200 * alpha);
                    
                    // Color de la estela cambia de azul a cian
                    int blue = 255;
                    int green = 150 + (int)(105 * alpha);
                    
                    g2d.setColor(new Color(0, green, blue, colorAlpha));
                    g2d.setStroke(new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
                
                // Dibujar puntos de ruta en la estela
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
    }
}