// PROYECTO 2
// Profesor: M. en C. Ukranio Coronilla
// Elaborado por: Jimenez Velazquez Jose Bryan Omar
// 7CM4

package Proyecto2;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;


public class VentanaJuego extends JFrame {
    private final int ANCHO = 1280;
    private final int ALTO = 720;
    private Nave nave;
    private ArrayList<Asteroide> asteroides;
    private Timer timer;
    private boolean juegoTerminado = false;
    private String mensajeFinal = "";
    private double porcentajeAvance = 0;
    private final int VELOCIDAD_JUEGO = 1000 / 60; // 60 FPS
    private Random rand = new Random();
    private ArrayList<Estrella> estrellas;

    public VentanaJuego(int n) {
        setTitle("Proyecto 2 - Simulación de Nave Espacial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Inicializar elementos del juego
        nave = new Nave(50, ALTO / 2, ANCHO, ALTO);
        asteroides = new ArrayList<>();
        estrellas = new ArrayList<>();
        
        // Crear estrellas de fondo
        for (int i = 0; i < 100; i++) {
            estrellas.add(new Estrella(rand.nextInt(ANCHO), rand.nextInt(ALTO), 
                                      rand.nextInt(3) + 1, rand.nextFloat()));
        }
        
        // Crear asteroides
        for (int i = 0; i < n; i++) {
            asteroides.add(new Asteroide(ANCHO + rand.nextInt(500), 
                                        rand.nextInt(ALTO), rand));
        }

        // Configurar panel de juego
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Dibujar fondo espacial
                dibujarFondo(g2d);
                
                // Dibujar elementos del juego
                for (Asteroide asteroide : asteroides) {
                    asteroide.dibujar(g2d);
                }
                nave.dibujar(g2d);
                
                // Dibujar interfaz de usuario
                dibujarUI(g2d);
                
                // Mostrar mensaje final si el juego terminó
                if (juegoTerminado) {
                    dibujarMensajeFinal(g2d);
                }
            }
        };
        
        panel.setPreferredSize(new Dimension(ANCHO, ALTO));
        add(panel);
        pack();
        setLocationRelativeTo(null);
        
        // Configurar timer para actualizar el juego
        timer = new Timer(VELOCIDAD_JUEGO, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!juegoTerminado) {
                    actualizarJuego();
                    panel.repaint();
                }
            }
        });
        timer.start();
    }

    private void dibujarFondo(Graphics2D g2d) {
        // Fondo negro
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, ANCHO, ALTO);
        
        // Dibujar estrellas
        for (Estrella estrella : estrellas) {
            estrella.dibujar(g2d);
        }
    }
    
    private void dibujarUI(Graphics2D g2d) {
        // Panel de información
        g2d.setColor(new Color(30, 30, 30, 200));
        g2d.fillRect(10, 10, 200, 80);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Nave Espacial - Proyecto 2", 20, 30);
        
        porcentajeAvance = Math.min(100, (nave.getX() / (double) ANCHO) * 100);
        g2d.drawString(String.format("Avance: %.2f%%", porcentajeAvance), 20, 55);
        g2d.drawString("Asteroides: " + asteroides.size(), 20, 75);
        
        // Barra de progreso
        g2d.setColor(Color.GRAY);
        g2d.fillRect(10, ALTO - 30, ANCHO - 20, 15);
        g2d.setColor(Color.GREEN);
        g2d.fillRect(10, ALTO - 30, (int) ((ANCHO - 20) * porcentajeAvance / 100), 15);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(10, ALTO - 30, ANCHO - 20, 15);
    }
    
    private void dibujarMensajeFinal(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(ANCHO/2 - 250, ALTO/2 - 60, 500, 120);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.drawString(mensajeFinal, ANCHO/2 - 240, ALTO/2 - 15);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString(String.format("Avance final: %.2f%%", porcentajeAvance), 
                      ANCHO/2 - 240, ALTO/2 + 25);
        g2d.drawString("Haga clic para salir", ANCHO/2 - 240, ALTO/2 + 55);
    }

    private void actualizarJuego() {
        // Actualizar nave
        nave.actualizar(asteroides);
        
        // Actualizar asteroides
        for (Asteroide asteroide : asteroides) {
            asteroide.actualizar();
            
            // Reaparecer asteroides que salen por la izquierda
            if (asteroide.getX() + asteroide.getTamaño() < 0) {
                asteroide.reiniciar(ANCHO, ALTO);
            }
        }
        
        // Verificar colisiones
        for (Asteroide asteroide : asteroides) {
            if (nave.colisionaCon(asteroide)) {
                juegoTerminado = true;
                mensajeFinal = "¡NAVE DESTRUIDA! Misión fallida.";
                timer.stop();
                return;
            }
        }
        
        // Verificar victoria
        if (nave.getX() >= ANCHO) {
            juegoTerminado = true;
            porcentajeAvance = 100;
            mensajeFinal = "¡MISIÓN CUMPLIDA! Nave a salvo.";
            timer.stop();
        }
    }
    
    // Clase interna para estrellas de fondo
    private class Estrella {
        private int x, y;
        private int tamaño;
        private float brillo;
        
        public Estrella(int x, int y, int tamaño, float brillo) {
            this.x = x;
            this.y = y;
            this.tamaño = tamaño;
            this.brillo = brillo;
        }
        
        public void dibujar(Graphics2D g2d) {
            int alpha = (int) (brillo * 255);
            g2d.setColor(new Color(255, 255, 255, alpha));
            g2d.fillOval(x, y, tamaño, tamaño);
        }
    }
}