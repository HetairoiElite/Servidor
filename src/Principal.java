import java.util.Scanner;

public class Principal {

    static public boolean perder = false;
    static public boolean ganar = false;
    static public int contganadores = 0;
    static Scanner sc = new Scanner(System.in);

    static int respuesta = 0;

    static public boolean reiniciar = false;
    static public boolean cerrar = false;
    static public Server server;

    public static void main(String[] args) {

        boolean continuar = true;

        int nclientes = 0;

        do {
            try {
                System.out.println("Digite cuantos clientes habrá: ");
                nclientes = sc.nextInt();
                continuar = false;
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Entrada incorrecta.");
            }
        } while (continuar);

        Thread hilo = new Thread(new Runnable() {

            @Override
            public void run() {

                while (true) {

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    System.out.println("perder: " + perder);
                    System.out.println("static perder: " + Principal.perder);

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (perder) {
                        System.out.println("Has perdido.");

                        do {
                            System.out.println("Quiere continuar jugando? 1 = si 2 = no ");
                            respuesta = sc.nextInt();

                            switch (respuesta) {
                                case 1:
                                    Principal.reiniciar = true;
                                    break;

                                case 2:
                                    Principal.cerrar = true;
                                    break;

                                default:
                                    System.out.println("Esa no es una opción");
                                    break;
                            }
                        } while (respuesta != 1 && respuesta != 2);
                    }

                    if (ganar) {
                        System.out.println("Has ganado.");
                        do {
                            System.out.println("Quiere continuar jugando? 1 = si 2 = no ");
                            respuesta = sc.nextInt();

                            switch (respuesta) {
                                case 1:
                                    Principal.reiniciar = true;
                                    break;

                                case 2:
                                    Principal.cerrar = true;
                                    break;

                                default:
                                    System.out.println("Esa no es una opción");
                                    break;
                            }
                        } while (respuesta != 1 && respuesta != 2);
                    }
                }
            }

        });

        server = new Server(nclientes);

        hilo.start();

        try {
            server.startToListen();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();

    }
}
