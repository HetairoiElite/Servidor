import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
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

        try {
            new Server(nclientes).startToListen();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();

    }
}
