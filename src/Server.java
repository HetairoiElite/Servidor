import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Server
 */
public class Server {

    private final int PORT = 3000;
    private ServerSocket serverSockect;
    protected int Numganador;
    protected int clientes;

    public int ganadores[];

    public boolean perder = false;

    public Server(int clientes) {
        this.clientes = clientes;

        Numganador = getNumGanador();
        this.ganadores = getGanadores();

        System.out.println("Numero ganador: " + Numganador);

        System.out.println("Ganadores: ");

        for (int i = 0; i < ganadores.length; i++) {
            System.out.println("Ganador " + (i + 1) + ": Computadora " + ganadores[i]);
        }

        try {
            this.serverSockect = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startToListen() {
        while (true) {
            try {
                Socket cliente = this.serverSockect.accept();

                new Thread(new ClientWorker(cliente, this.Numganador, this.ganadores, this.clientes)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGanadores() {
        this.ganadores = getGanadores();
    }

    public void setNumGanador() {
        this.Numganador = getNumGanador();
    }

    public int getNumGanadorServer() {
        return this.Numganador;
    }

    public int getNumGanador() {

        int min = 0;
        int max = 9;

        Random random = new Random();

        int ganador = random.nextInt(max + min) + min;

        return ganador;
    }

    public int[] getGanadores() {

        int ganadores[] = new int[3];
        int min = 1;
        int max = clientes - 1;

        boolean continuar = false;

        Random random = new Random();

        ganadores[0] = 0;
        ganadores[1] = 0;
        ganadores[2] = 0;

        for (int i = 0; i < 3; i++) {

            do {
                switch (i) {
                    case 0:

                        int ganador0 = random.nextInt(max + min) + min;

                        ganadores[i] = ganador0;
                        break;
                    case 1:

                        int ganador1 = random.nextInt(max + min) + min;

                        ganadores[i] = ganador1;

                        if (ganadores[i - 1] == ganadores[i]) {
                            continuar = true;
                        } else {
                            continuar = false;
                        }

                        break;
                    case 2:
                        int ganador2 = random.nextInt(max + min) + min;

                        ganadores[i] = ganador2;

                        if (ganadores[i - 2] == ganadores[i] || ganadores[i - 1] == ganadores[i]) {
                            continuar = true;
                        } else {
                            continuar = false;
                        }

                    default:
                        break;
                }
            } while (continuar);

        }

        return ganadores;
    }
}