import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientWorker implements Runnable {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private static int clientes = 0;
    private int id;
    private int Numganador;

    private boolean rascado;
    private boolean ganador;

    public ClientWorker(Socket socket, int Numganador, int[] ganadores, int nclientes) throws IOException {
        if (ClientWorker.clientes < nclientes) {
            ClientWorker.clientes++;
            this.id = ClientWorker.clientes;

            this.Numganador = Numganador;

            for (int i = 0; i < ganadores.length; i++) {
                if (ganadores[i] == this.id) {
                    this.ganador = true;
                    break;

                } else {
                    this.ganador = false;
                }
            }

            this.socket = socket;
            this.inputStream = new DataInputStream(this.socket.getInputStream());
            this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        } else {
            System.out.println("Se ha excedido el numero de clientes.");
        }
    }

    @Override
    public void run() {
        boolean isConected = true;
        while (isConected) {
            try {

                Thread.sleep(100);
                isConected = this.chooseOptions(isConected);
            } catch (IOException e) {
                // System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                // System.out.println(e.getMessage());
            }
        }
    }

    private synchronized boolean chooseOptions(boolean isConected) throws IOException {
        Options aux = Options.valueOf(this.inputStream.readUTF());

        if (rascado) {
            aux = Options.valueOf("MESSAGE");
        }

        // System.out.println(aux);
        switch (aux) {
            case MESSAGE:
                this.message();
                this.enviarNumero();
                if (!rascado) {
                    aux = Options.valueOf(this.inputStream.readUTF());
                    // System.out.println(aux);
                    switch (aux) {
                        case PERDER:
                            rascado = true;
                            Principal.perder = true;
                            System.out.println(Principal.perder);

                            break;
                        case GANAR:
                            rascado = true;
                            Principal.contganadores++;
                            if (Principal.contganadores == 3) {
                                Principal.ganar = true;
                            }
                            break;
                        default:
                            break;

                    }
                }
                break;
            case CLOSE:
                isConected = false;
                break;
            case RASCADO:

                break;

            default:
                break;

        }
        return isConected;
    }

    private void message() {
        try {

            this.outputStream.writeUTF("Se ha conectado al servidor CLIENTE: " + this.id);
        } catch (IOException e) {
            // System.out.println(e.getMessage());
        }
    }

    private void enviarNumero() {

        if (this.ganador) {
            try {
                this.outputStream.writeUTF(Integer.toString(this.Numganador));
            } catch (IOException e) {
                // System.out.println(e.getMessage());
            }
        } else {
            try {
                this.outputStream.writeUTF(Integer.toString(11));
            } catch (IOException e) {
                // System.out.println(e.getMessage());
            }
        }
    }

}
