import java.io.IOException;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import java.util.ArrayList;


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
    private static ArrayList<Integer> vetorialClock;

    /** Construtor da classe que recebe o id.
     */
    public Conexao(String id) {
        Conexao.id = id;
        Conexao.vetorialClock = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Conexao.vetorialClock.add(0);
        }
    }

    /** Método que retorna o relógio vetorial.
     *
     * @return vetorialClock
     */
    public static ArrayList<Integer> getVetorialClock() {
        return Conexao.vetorialClock;
    }

    /** Método que retorna um valor do relógio vetorial.
     *
     * @return vetorialClock
     */
    public static Integer getVetorialClock(int index) {
        return Conexao.vetorialClock.get(index);
    }

    /** Método que altera o relógio vetorial.
     *
     * @param vetorialClock
     */
    public static void setVetorialClock(ArrayList<Integer> vetorialClock) {
        Conexao.vetorialClock = vetorialClock;
    }

    /** Método que altera um valor do relógio vetorial.
     */
    public static Integer setVetorialClock(int index, int vetorialClock) {
        return (Conexao.vetorialClock.set(index, vetorialClock));
    }

    /** Método que altera um valor do relógio vetorial.
     */
    public static Integer setVetorialClock(int index) {
        return (Conexao.vetorialClock.set(index, Conexao.vetorialClock.get(index) + 1));
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
     * @param idDestiny
     * @param idSource
     * @param time
     */
    public void enviar(String idDestiny,  String idSource, String time, String id, String time2) {
        byte message[] = ("0" + ";" + idDestiny + ";" + idSource + ";" + time + ";" + id + ";" + time2).getBytes();

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
                        Integer newId = Integer.parseInt(part[1].trim()) + 1;

                        if (id.equals(newId.toString())) {
                            Conexao.setVetorialClock(Integer.parseInt(part[1]));
                            Conexao.setVetorialClock(Integer.parseInt(part[2].trim()), Integer.parseInt(part[3].trim()));
                            Conexao.setVetorialClock(Integer.parseInt(part[4].trim()), Integer.parseInt(part[5].trim()));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}