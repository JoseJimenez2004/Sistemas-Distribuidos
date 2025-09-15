package ProgramaClase7;

import javax.swing.*;
import java.awt.*;

public class DibujoFiguras extends JPanel {
    private TrianguloEq trianguloOriginal, trianguloMovido;
    private Rectangulo rectOriginal, rectMovido;

    // Desplazamientos
    private double dxTri, dyTri;
    private double dxRect, dyRect;

    public DibujoFiguras() {
        // Figuras originales
        trianguloOriginal = new TrianguloEq(new Coordenada(0, 0), 80);
        rectOriginal = new Rectangulo(new Coordenada(150, -50), 120, 60);

        // Copias desplazadas
        trianguloMovido = new TrianguloEq(new Coordenada(0, 0), 80);
        rectMovido = new Rectangulo(new Coordenada(150, -50), 120, 60);

        // Valores de desplazamiento
        dxTri = 120;
        dyTri = 100;
        dxRect = -100;
        dyRect = 80;

        // Aplicar desplazamiento a las copias
        trianguloMovido.desplazar(dxTri, dyTri);
        rectMovido.desplazar(dxRect, dyRect);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        // Dibujar eje cartesiano
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(0, getHeight()/2, getWidth(), getHeight()/2); // eje X
        g2.drawLine(getWidth()/2, 0, getWidth()/2, getHeight()); // eje Y

        // -------- Triángulo --------
        g2.setColor(Color.GRAY);
        dibujarFigura(g2, trianguloOriginal.getVertices()); // original
        g2.setColor(new Color(0, 128, 255));
        dibujarFigura(g2, trianguloMovido.getVertices());   // movido

        // Texto triángulo
        g2.setColor(Color.BLACK);
        g2.drawString("Triángulo original (gris)", 20, 20);
        g2.drawString("Triángulo desplazado (azul)", 20, 40);
        g2.drawString("Δx = " + dxTri + ", Δy = " + dyTri, 20, 60);

        // -------- Rectángulo --------
        g2.setColor(Color.GRAY);
        dibujarFigura(g2, rectOriginal.getVertices()); // original
        g2.setColor(new Color(220, 50, 47)); 
        dibujarFigura(g2, rectMovido.getVertices());   // movido

        // Texto rectángulo
        g2.setColor(Color.BLACK);
        g2.drawString("Rectángulo original (gris)", 20, 100);
        g2.drawString("Rectángulo desplazado (rojo)", 20, 120);
        g2.drawString("Δx = " + dxRect + ", Δy = " + dyRect, 20, 140);
    }

    private void dibujarFigura(Graphics2D g2, Coordenada[] vertices) {
        int n = vertices.length;
        int[] x = new int[n];
        int[] y = new int[n];

        for (int i = 0; i < n; i++) {
            x[i] = convertirX(vertices[i].abcisa());
            y[i] = convertirY(vertices[i].ordenada());
        }

        g2.drawPolygon(x, y, n);
        g2.fillOval(x[0]-3, y[0]-3, 6, 6); // marca vértice inicial
    }

    // Convertir coordenadas matemáticas al lienzo
    private int convertirX(double x) {
        return (int)(getWidth()/2 + x);
    }

    private int convertirY(double y) {
        return (int)(getHeight()/2 - y);
    }

    public static void main(String[] args) {
        JFrame ventana = new JFrame("Figuras en eje cartesiano");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(700, 700);
        ventana.setLocationRelativeTo(null);
        ventana.add(new DibujoFiguras());
        ventana.setVisible(true);
    }
}
