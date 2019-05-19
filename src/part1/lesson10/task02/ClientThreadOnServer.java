package part1.lesson10.task02;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Класс потока,
 * при каждом установлении соединения с очередным клиентом создается новый поток ClientThreadOnServer для одного
 * указанного соединения.
 */
public class ClientThreadOnServer implements Runnable {

    private String name;
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private final Map<String, ClientsInfo> clientsInfo;
    private Map<Integer, Integer> udpPorts;
    private int udpPort;

    /**
     * Конструктор
     *
     * @param socket      сокет для клиента
     * @param clientsInfo коллекция имен клиентов в чате, также их IP адрес и порт, на котором
     *                    клиенты слушают UDP.
     * @param udpPorts    коллекция с количеством клиентов, слушающих порты, <порт, количество пользователей>
     *                    т.е. каждый клиент принимает на определенном порту UDP. Используется для возможности запуска клиентов
     *                    на одном устройстве.
     */
    public ClientThreadOnServer(Socket socket, Map<String, ClientsInfo> clientsInfo, Map<Integer, Integer> udpPorts) {
        this.socket = socket;
        this.clientsInfo = clientsInfo;
        this.udpPorts = udpPorts;
    }

    /**
     * метод для записи с использованием BufferedWriter. newLine(),flush().
     *
     * @param bufferedWriter BufferedWriter
     * @param str            строка для записи
     * @throws IOException
     */
    private void writeNFlush(BufferedWriter bufferedWriter, String str) throws IOException {
        bufferedWriter.write(str);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    /**
     * отправка по UDP. broadcast либо unicast (определяется параметром addr)
     *
     * @param sendStr строка для отправки
     * @param addr    IP адрес клиента либо "255.255.255.255" для broadcast
     * @param port    порт клиента, на котором он слушает UDP
     */
    private void datagramSend(String sendStr, InetAddress addr, int port) {
        try {
            byte[] data = sendStr.getBytes();
            DatagramPacket pack = new DatagramPacket(data, data.length, addr, port);
            DatagramSocket ds = new DatagramSocket();
            ds.send(pack);
            ds.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Есть подключение клиента Socket socket. Клиент передает значение порта, на котором он ожидает UDP.
     * Клиент передает имя (имя в чате), имя проверяется на несовпадение с уже зарегистрированными именами клиентов.
     * В случае успеха имя, IP клиента и порт прослушивателя UDP клиента добавляются в коллекцию clientsInfo, порт для
     * UDP также добавляется в коллекцию udpPorts (счетчик количества пользователей с данным портом увеличивается на 1).
     * При получении общих сообщений от клиентов сообщения подписываются именем клиента и рассылаются datagramSend всем
     * клиентам (broadcast). При получении личного сообщения, если получатель не найден, клиенту отправляется
     * соответствующее уведомление. Если получатель найден, ему c помощью datagramSend пересылается указанное личное
     * сообщение. При получении сообщения "quit" начинается завершение работы потока через return-finally. При этом
     * имя клиента, IP и порт удаляются из clientsInfo, а счетчик в udpPorts уменьшается на 1, а если счетчик стал
     * равен 0, то элемент удаляется из udpPorts. Клиенту отправляется сообщение о возможности завершить работу
     * самого клиента, socket закрывается.
     */
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            udpPort = Integer.parseInt(in.readLine());

            while (true) {
                writeNFlush(out, "Введите уникальное имя");
                name = in.readLine();
                if (name == null) {
                    return;
                }
                synchronized (clientsInfo) {
                    if (!name.isBlank() && !clientsInfo.containsKey(name)) {
                        clientsInfo.put(name, new ClientsInfo(socket.getInetAddress(), udpPort));
                        synchronized (udpPorts) {
                            if (!udpPorts.containsKey(udpPort)) {
                                udpPorts.put(udpPort, 1);
                            } else udpPorts.replace(udpPort, udpPorts.get(udpPort) + 1);
                        }
                        break;
                    }
                }
            }

            writeNFlush(out, "ACCEPTED");

            while (true) {
                String input = in.readLine();
                if (input.toLowerCase().startsWith("quit")) {
                    return;
                }
                if (input.startsWith("/")) {
                    int messageFirstPos = input.indexOf(":") + 1;
                    String str = input.substring(1, messageFirstPos - 1);
                    if (clientsInfo.containsKey(str)) {
                        ClientsInfo clientsInfoEntry = clientsInfo.get(str);
                        datagramSend(name + "(личное сообщение для " + str + "): " + input.substring(messageFirstPos), clientsInfoEntry.getAddress(), clientsInfoEntry.getPort());
                        writeNFlush(out, "FOUND");
                    } else {
                        writeNFlush(out, "NOTFOUND");
                    }
                } else {
                    InetAddress addr = InetAddress.getByName("255.255.255.255");
                    for (Map.Entry<Integer, Integer> entry : udpPorts.entrySet()) {
                        datagramSend(name + " : " + input, addr, entry.getKey());
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (name != null) {
                clientsInfo.remove(name);
                synchronized (udpPorts) {
                    udpPorts.replace(udpPort, udpPorts.get(udpPort) - 1);
                    if (udpPorts.get(udpPort) == 0) {
                        udpPorts.remove(udpPort);
                    }
                }
            }

            try {
                writeNFlush(out, "quit");
                socket.close();
            } catch (IOException e) {
            }
        }
    }
}
