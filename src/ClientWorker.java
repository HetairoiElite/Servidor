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
    private boolean reiniciar;

    private boolean rascado;
    private boolean ganador;

    public ClientWorker(
            Socket socket,
            int Numganador,
            int[] ganadores,
            int nclientes)
            throws IOException {
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

    public void reiniciar() {
        for (int i = 0; i < Principal.server.ganadores.length; i++) {
            if (Principal.server.ganadores[i] == this.id) {
                this.ganador = true;
                break;
            } else {
                this.ganador = false;
            }
        }

        System.out.println("");
        System.out.println("===========================================");

        System.out.println("Ganador: " + this.ganador);

        this.Numganador = Principal.server.Numganador;

        this.rascado = false;

        System.out.println("Rascado: " + this.rascado);

        System.out.println("Numero ganador: " + this.Numganador);
    }

    @Override
    public void run() {
        boolean isConected = true;

        reiniciar = false;
        while (isConected) {
            try {
                if (Principal.cerrar) {
                    outputStream.writeUTF(Options.CLOSE.toString());
                }

                if (Principal.reiniciar && !reiniciar) {
                    System.out.println("Reiniciando");
                    this.reiniciar();

                    System.out.println("===========================================");
                    System.out.println();
                    outputStream.writeUTF(Options.REINICIAR.toString());

                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                isConected = this.chooseOptions(isConected);
            } catch (IOException e) {
                // System.out.println(e.getMessage());
            }
        }
    }

    private synchronized boolean chooseOptions(boolean isConected)
            throws IOException {
        Options aux = Options.valueOf(this.inputStream.readUTF());
        // System.out.println("Primer aux: " + aux);

        // System.out.println("aux1: " + aux);
        if (rascado) {
            aux = Options.valueOf("MESSAGE");
        }

        // System.out.println("aux2: " + aux);
        switch (aux) {
            case MESSAGE:
                this.message();
                this.enviarNumero();
                if (!this.rascado) {
                    aux = Options.valueOf(this.inputStream.readUTF());
                    // System.out.println("Escribiendo:" + Principal.escribiendo);

                    if (Principal.escribiendo) {
                        // System.out.println("Escribiendo 1:" + Principal.escribiendo);
                        aux = Options.valueOf("MESSAGE");
                        Principal.perder = false;
                        this.rascado = false;
                    }

                    switch (aux) {
                        case PERDER:
                            this.rascado = true;
                            Principal.perder = true;
                            System.out.println(Principal.perder);

                            reiniciar = false;

                            if (Principal.escribiendo) {
                                Principal.perder = false;
                                this.rascado = false;

                                reiniciar = true;

                            }
                            System.out.println(Principal.perder);

                            break;
                        case GANAR:
                            this.rascado = true;
                            Principal.contganadores++;
                            if (Principal.contganadores == 3) {
                                Principal.ganar = true;
                            }
                            break;
                        default:
                            // System.out.println("REINICIAR");
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
            this.outputStream.writeUTF(
                    "Se ha conectado al servidor CLIENTE: " + this.id);
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
