package Proyecto2;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

public class SpaceSimulation extends JFrame {

    private final int WIDTH = 1280;
    private final int HEIGHT = 720;
    private SpacePanel panel;
    private boolean gameRunning = true;
    private int asteroidCount = 10; // Valor por defecto

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SpaceSimulation gui = new SpaceSimulation();
            gui.setVisible(true);
        });
    }

    public SpaceSimulation() {
        setSize(WIDTH, HEIGHT);
        setTitle("🚀 Simulación de Nave Espacial con Asteroides ☄️");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Con esta funcion mete el numero de asteroides
        askForAsteroidCount();

        panel = new SpacePanel(WIDTH, HEIGHT, asteroidCount);
        add(panel);

        Timer timer = new Timer(16, e -> { //funcion para mantener estable a 60 FPS
            if (gameRunning) {
                panel.update();
                checkCollisions();
                panel.repaint();
            }
        });
        timer.start();
    }
    
    private void askForAsteroidCount() {
        try {
            // Configuracion de  look and feel para diseño mas visual
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si fallo continuar con el look and feel por defecto
        }

        // Panel personalizado para la entrada de diseño
        JPanel inputPanel = new JPanel(new BorderLayout(15, 15));
        inputPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        inputPanel.setBackground(new Color(15, 25, 45)); // Fondo azul oscuro 

        // Título de la pantalla
        JLabel titleLabel = new JLabel("🌌 CONFIGURACIÓN DEL JUEGO 🌌", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(200, 220, 255));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        inputPanel.add(titleLabel, BorderLayout.NORTH);

        // Panel central con contenido
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBackground(new Color(15, 25, 45));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Icono de cometa/asteroide
        JLabel iconLabel = new JLabel("☄️", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setBorder(new EmptyBorder(10, 0, 20, 0));
        contentPanel.add(iconLabel, BorderLayout.NORTH);

        // Texto de instrucciones
        JLabel instructionLabel = new JLabel("<html><div style='text-align: center; color: #CCDDFF;'>"
                + "<b>Selecciona la cantidad de asteroides</b><br>"
                + "<small style='color: #8899AA;'>(Entre 5 y 30 asteroides)</small>"
                + "</div></html>", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        contentPanel.add(instructionLabel, BorderLayout.CENTER);

        // Panel para el spinner con Diseño
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        spinnerPanel.setBackground(new Color(15, 25, 45));
        
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(10, 5, 30, 1));
        spinner.setFont(new Font("Arial", Font.BOLD, 18));
        spinner.setPreferredSize(new Dimension(100, 40));
        
        // Personalizacion del Spinner
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
            textField.setHorizontalAlignment(JTextField.CENTER);
            textField.setBackground(new Color(30, 45, 70));
            textField.setForeground(new Color(200, 220, 255));
            textField.setCaretColor(new Color(100, 200, 255));
            textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
        }
        
        spinnerPanel.add(spinner);
        contentPanel.add(spinnerPanel, BorderLayout.SOUTH);

        inputPanel.add(contentPanel, BorderLayout.CENTER);

        // Panel de botones personalizados
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(15, 25, 45));
        
        // Botón Iniciar Juego
        JButton startButton = createStyledButton("🚀 Iniciar Juego", new Color(0, 200, 120), new Color(0, 230, 140));
        startButton.addActionListener(e -> {
            asteroidCount = (Integer) spinner.getValue();
            ((Window) SwingUtilities.getWindowAncestor((Component) e.getSource())).dispose();
        });
        
        // Botón Cancelar
        JButton cancelButton = createStyledButton("❌ Cancelar", new Color(220, 80, 80), new Color(255, 100, 100));
        cancelButton.addActionListener(e -> {
            System.exit(0);
        });
        
        buttonPanel.add(startButton);
        buttonPanel.add(cancelButton);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Crear diálogo
        JDialog dialog = new JDialog(this, "Configuración del Juego", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.getContentPane().add(inputPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        
        // Efecto de Cerrado con ESC
        inputPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke("ESCAPE"), "closeDialog");
        inputPanel.getActionMap().put("closeDialog", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        dialog.setVisible(true);
    }
    
    private JButton createStyledButton(String text, Color baseColor, Color hoverColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Fondo con gradiente
                GradientPaint gradient = new GradientPaint(
                    0, 0, baseColor, 
                    0, getHeight(), baseColor.darker()
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Borde con efecto de neón
                g2.setStroke(new BasicStroke(2.5f));
                g2.setColor(baseColor.brighter().brighter());
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 15, 15);
                
                // Sombra interior
                g2.setColor(new Color(255, 255, 255, 30));
                g2.drawRoundRect(3, 3, getWidth()-6, getHeight()-6, 12, 12);
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover mejorado
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Timer glowTimer;
            private float glowValue = 0.0f;
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (glowTimer != null && glowTimer.isRunning()) {
                    glowTimer.stop();
                }
                
                glowTimer = new Timer(10, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        glowValue = Math.min(1.0f, glowValue + 0.1f);
                        button.repaint();
                        if (glowValue >= 1.0f) {
                            glowTimer.stop();
                        }
                    }
                });
                glowTimer.start();
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (glowTimer != null && glowTimer.isRunning()) {
                    glowTimer.stop();
                }
                
                glowTimer = new Timer(10, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        glowValue = Math.max(0.0f, glowValue - 0.1f);
                        button.repaint();
                        if (glowValue <= 0.0f) {
                            glowTimer.stop();
                        }
                    }
                });
                glowTimer.start();
            }
            
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(255, 255, 255, 200));
            }
            
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    private void checkCollisions() {
        // Obtener el área de colisión de la nave
        Rectangle2D.Float shipBounds = panel.getSpaceship().getBounds();
        
        // Verificar colisiones con asteroides
        for (Asteroid asteroid : panel.getAsteroids()) {
            if (asteroid.isActive() && shipBounds.intersects(asteroid.getBounds())) {
                gameRunning = false;
                panel.setGameRunning(false);
                showGameOverDialog();
                break;
            }
        }
    }
    
    private void showGameOverDialog() {
        // Panel personalizado para el Game Over
        JPanel gameOverPanel = new JPanel(new BorderLayout(15, 15));
        gameOverPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        gameOverPanel.setBackground(new Color(25, 15, 30)); // Fondo púrpura oscuro

        // Icono de game over
        JLabel iconLabel = new JLabel("💥", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        gameOverPanel.add(iconLabel, BorderLayout.NORTH);

        // Mensaje con mejor formato
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; color: #FFCCDD;'>"
                + "<h2 style='color: #FF5555; margin: 10px 0;'>¡COLISIÓN! ☄️</h2>"
                + "<p style='font-size: 14px;'>Tu nave ha chocado con un asteroide</p>"
                + "<p style='font-size: 16px; margin-top: 15px;'><b>Progreso alcanzado: " 
                + String.format("%.1f", panel.getProgress()) + "%</b></p>"
                + "<p style='font-size: 14px; margin-top: 10px;'>Asteroides evitados: " 
                + panel.getAsteroidsAvoided() + "</p>"
                + "</div></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gameOverPanel.add(messageLabel, BorderLayout.CENTER);

        // Botón de aceptar con diseño mejorado
        JButton acceptButton = createStyledButton("👌 Aceptar", new Color(150, 90, 180), new Color(180, 110, 210));
        acceptButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
            window.dispose();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(25, 15, 30));
        buttonPanel.add(acceptButton);
        gameOverPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Mostrar diálogo personalizado
        JDialog dialog = new JDialog(this, "Misión Fallida", true);
        dialog.getContentPane().add(gameOverPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public void showVictoryDialog() {
        // Panel personalizado para la victoria
        JPanel victoryPanel = new JPanel(new BorderLayout(15, 15));
        victoryPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        victoryPanel.setBackground(new Color(15, 35, 25)); // Fondo verde oscuro

        // Icono de victoria
        JLabel iconLabel = new JLabel("🎉", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        victoryPanel.add(iconLabel, BorderLayout.NORTH);

        // Mensaje
        JLabel messageLabel = new JLabel("<html><div style='text-align: center; color: #DDFFDD;'>"
                + "<h2 style='color: #55FF55; margin: 10px 0;'>¡MISIÓN COMPLETADA! 🌟</h2>"
                + "<p style='font-size: 14px;'>Has llegado a tu destino evitando todos los asteroides</p>"
                + "<div style='background: rgba(0,100,0,0.3); padding: 10px; border-radius: 5px; margin: 15px 0;'>"
                + "<p style='font-size: 16px; margin: 5px 0;'><b>⏱️ Tiempo: " 
                + String.format("%.1f", panel.getGameTime() / 1000.0) + " segundos</b></p>"
                + "<p style='font-size: 16px; margin: 5px 0;'><b>☄️ Asteroides evitados: " 
                + panel.getAsteroidsAvoided() + "</b></p>"
                + "<p style='font-size: 16px; margin: 5px 0;'><b>🎯 Dificultad: " 
                + asteroidCount + " asteroides</b></p>"
                + "</div>"
                + "</div></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        victoryPanel.add(messageLabel, BorderLayout.CENTER);

        // Botón de aceptar con diseño mejorado
        JButton acceptButton = createStyledButton("⭐ ¡Excelente!", new Color(0, 180, 120), new Color(0, 210, 140));
        acceptButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
            window.dispose();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(15, 35, 25));
        buttonPanel.add(acceptButton);
        victoryPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Mostrar diálogo personalizado
        JDialog dialog = new JDialog(this, "¡Felicidades!", true);
        dialog.getContentPane().add(victoryPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}