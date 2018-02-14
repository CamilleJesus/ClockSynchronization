import java.util.Scanner;


/**
 * Classe principal CLI, responsável pela inicialização do relógio.
 *
 * @author Camille Jesus e Wanderson Silva
 */
public class CLI
{

    /**  Método que inicia o programa.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        String id;
        Scanner scanner = new Scanner(System.in);
        System.out.println("::.  Relógio de Lamport  .::");
        System.out.println("Informe Id:");
        id = scanner.nextLine();
        System.out.println("\nId: " + id);
        System.out.println("\nT: " + Conexao.getLogicalClock());

        Conexao c = new Conexao(id);
        c.conectar();

        do {
            System.out.println("\n [1] - Evento Interno\n [2] - Evento Externo\n [3] - Consultar Tempo\n [0] - Sair");
            int escolha = scanner.nextInt();//

            switch (escolha) {
                case 0:   //Opção Sair
                    System.exit(0);
                    break;
                case 1:
                    Conexao.setLogicalClock();
                    break;
                case 2:
                    System.out.println("\nPara qual processo?");
                    scanner.nextLine();
                    Integer escolha2 = scanner.nextInt();
                    Conexao.setLogicalClock();
                    c.enviar(escolha2.toString(), Conexao.getLogicalClock().toString());
                    break;
                case 3:
                    System.out.println("\nT" + id + ": " + Conexao.getLogicalClock());
                    continue;
            }
            System.out.println("\nT" + id + ": " + Conexao.getLogicalClock());
        } while (true);
    }

}