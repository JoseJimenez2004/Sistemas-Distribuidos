package ProgramaClase10;

public class CondicionDeCarrera {

    public static int variable_compartida = 0;

    // Clase que implementa Runnable
    static class ModificadorDeVariable implements Runnable {
        private final int n;

        public ModificadorDeVariable(int n) {
            this.n = n;
        }

        @Override
        public void run() {
            for (int i = 0; i < n; i++) {
                modifica();
            }
        }

        // Método que modifica la variable compartida
        public static synchronized void modifica() {
            long id = Thread.currentThread().getId();
            if (Thread.currentThread().getName().equals("Hilo-Incrementador")) {
                variable_compartida++;
            } else if (Thread.currentThread().getName().equals("Hilo-Decrementador")) {
                variable_compartida--;
            }
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Por favor, ejecute el programa con el número de iteraciones como argumento.");
            return;
        }

        final int n = Integer.parseInt(args[0]);

        // Se crean los Runnables
        ModificadorDeVariable tarea = new ModificadorDeVariable(n);

        // Se crean los hilos con nombres distintos
        Thread hilo1 = new Thread(tarea, "Hilo-Incrementador");
        Thread hilo2 = new Thread(tarea, "Hilo-Decrementador");

        // Se inician
        hilo1.start();
        hilo2.start();

        try {
            hilo1.join();
            hilo2.join();
        } catch (InterruptedException e) {
            System.err.println("El hilo principal fue interrumpido.");
        }

        System.out.println("El valor final de la variable compartida es: " + variable_compartida);
    }
}
