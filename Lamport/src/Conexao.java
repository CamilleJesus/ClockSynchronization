import java.io.IOException;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


/**
 * Classe que conecta os usuários ao grupo multicast e gerencia os métodos de comunicação.
 *
 * @author Camille Jesus e Wanderson Silva
 */
public class Conexao
{

    private static InetAddress IPMulticast;
    private static MulticastSocket multicast;
    private static final int port = 7777;
    private static String id;
    private static Integer logicalClock = 0;

    /** Construtor da classe que recebe o id.
     */
    public Conexao(String id) {
        Conexao.id = id;
    }

    /** Método que retorna o valor do relógio lógico.
     *
     * @return logicalClock
     */
    public static Integer getLogicalClock() {
        return Conexao.logicalClock;
    }

    /** Método que altera o valor do relógio lógico de acordo com o tempo recebido.
     *
     * @param logicalClock
     */
    public static void setLogicalClock(int logicalClock) {
        Conexao.logicalClock = logicalClock;
    }

    /** Método que incrementa o valor do relógio lógico.
     *
     * @return logicalClock
     */
    public static Integer setLogicalClock() {
        return (++Conexao.logicalClock);
    }

    /** Método que conecta ao grupo.
     *
     * @throws IOException
     */
    public void conectar() {

        try {
            Conexao.IPMulticast = InetAddress.getByName("235.0.0.1");
            Conexao.multicast = new MulticastSocket(Conexao.port);
            Conexao.multicast.joinGroup(Conexao.IPMulticast);
            new ThreadCliente(Conexao.multicast).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Método que envia uma string para o grupo.
     *
     * @param id
     * @param time
     */
    public void enviar(String id, String time) {
        byte message[] = ("0" + ";" + id + ";" + time).getBytes();

        try {
            Conexao. multicast.send(new DatagramPacket(message, message.length, Conexao.IPMulticast, Conexao.port));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Classe que recebe os envios do grupo multicast e os processa.
     *
     * @author Camille Jesus e Wanderson Silva
     */
    private static class ThreadCliente extends Thread
    {

        private final MulticastSocket socketMulticast;

        public ThreadCliente(MulticastSocket socketMulticast) {
            this.socketMulticast = socketMulticast;
        }

        /** Método que recebe as solicitações, as verifica e processa.
         */
        @Override
        public void run() {

            try {

                while (true) {
                    byte message[] = new byte[1024];
                    DatagramPacket pack = new DatagramPacket(message, message.length);
                    this.socketMulticast.receive(pack);
                    String s = new String(pack.getData());

                    if (s.startsWith("0")) {
                        String[] part = s.split(";");

                        if (id.equals(part[1].trim())) {
                            setLogicalClock(Integer.parseInt(part[2].trim()) + 1);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}