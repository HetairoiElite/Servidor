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

    private boolean chooseOptions(boolean isConected) throws IOException {
        Options aux = Options.valueOf(this.inputStream.readUTF());

        switch (aux) {
            case MESSAGE:
                this.message();
                this.enviarNumero();
                break;
            case CLOSE:
                isConected = false;
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
