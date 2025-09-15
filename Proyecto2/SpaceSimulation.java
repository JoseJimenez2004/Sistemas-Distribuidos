package Proyecto2;

import javax.swing.*;

public class SpaceSimulation extends JFrame {

    private final int WIDTH = 1240;
    private final int HEIGHT = 720;
    private SpacePanel panel;
    private boolean gameRunning = true;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SpaceSimulation gui = new SpaceSimulation();
            gui.setVisible(true);
        });
    }

    public SpaceSimulation() {
        setSize(WIDTH, HEIGHT);
        setTitle("Simulación de Nave Espacial - Recorrido Optimizada");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new SpacePanel(WIDTH, HEIGHT);
        add(panel);

        Timer timer = new Timer(16, e -> { // ~60 FPS
            if (gameRunning) {
                panel.update();
                panel.repaint();
            }
        });
        timer.start();
    }
}