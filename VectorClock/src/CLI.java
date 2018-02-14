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
        System.out.println("::.  Relógio Vetorial  .::");
        System.out.println("Informe Id:");
        id = scanner.nextLine();
        System.out.println("\nId: " + id);
        System.out.println("\nT"+ id + ":" + Conexao.getVetorialClock());

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
                    Conexao.setVetorialClock(Integer.parseInt(id) - 1);
                    break;
                case 2:
                    System.out.println("\nPara qual processo?");
                    scanner.nextLine();
                    Integer escolha2 = scanner.nextInt();
                    Integer idDestiny = escolha2 - 1;
                    Integer idSource = Integer.parseInt(id) - 1;
                    Conexao.setVetorialClock(idSource);
                    Integer i;

                    for (i = 0; i < Conexao.getVetorialClock().size(); i++) {

                        if ((i != idDestiny) && (i != idSource)) {
                            break;
                        }
                    }
                    c.enviar(idDestiny.toString(), idSource.toString(), Conexao.getVetorialClock(idSource).toString(),
                            i.toString(), Conexao.getVetorialClock(i).toString());
                    break;
                case 3:
                    System.out.println("\nT" + id + ": " + Conexao.getVetorialClock().toString());
                    continue;
            }
            System.out.println("\nT" + id + ": " + Conexao.getVetorialClock().toString());
        } while (true);
    }

}