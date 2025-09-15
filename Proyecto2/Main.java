// PROYECTO 2
// Profesor: M. en C. Ukranio Coronilla
// Elaborado por: Jimenez Velazquez Jose Bryan Omar
// 7CM4

package Proyecto2;

import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            JOptionPane.showMessageDialog(null, 
                "Debe ingresar el número de asteroides como argumento.\n" +
                "Ejemplo: java -jar proyecto2.jar 20", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int numeroAsteroides = 0;
        try {
            numeroAsteroides = Integer.parseInt(args[0]);
            if (numeroAsteroides < 0) {
                JOptionPane.showMessageDialog(null, 
                    "El número de asteroides debe ser positivo.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "El argumento debe ser un número entero válido.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear y mostrar la ventana de juego
        new VentanaJuego(numeroAsteroides);
    }
}