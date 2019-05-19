package part1.lesson10.task01;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Map;
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
    private final Set<String> names;
    private final Map<Integer, Integer> udpPorts;
    private int udpPort;

    /**
     * Конструктор
     *
     * @param socket   сокет для клиента
     * @param names    коллекция имен пользователей
     * @param udpPorts коллекция с количеством клиентов, слушающих порты, <порт, количество пользователей>
     *                 т.е. каждый клиент принимает на определенном порту UDP. Используется для возможности запуска клиентов
     *                 на одном устройстве.
     */
    ClientThreadOnServer(Socket socket, Set<String> names, Map<Integer, Integer> udpPorts) {
        this.socket = socket;
        this.names = names;
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
     * отправка по UDP, broadcast
     *
     * @param sendStr строка для отправки
     * @param port    порт клиентов
     */
    private void datagramSend(String sendStr, int port) {
        try {
            byte[] data = sendStr.getBytes();
            InetAddress addr = InetAddress.getByName("255.255.255.255");
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
     * В случае успеха имя добавляется в коллекцию names, а порт для UDP в коллекцию udpPorts (счетчик количества
     * пользователей с данным портом увеличивается на 1). При получении сообщений от клиентов сообщения подписываются
     * именем клиента и рассылаются datagramSend всем клиентам (broadcast). При получении сообщения "quit" начинается
     * завершение работы потока через return-finally. При этом имя клиента удаляется из names, а счетчик в udpPorts
     * уменьшается на 1, а если счетчик стал равен 0, то элемент удаляется из udpPorts. Клиенту отправляется
     * сообщение о возможности завершить работу самого клиента, socket закрывается.
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
                synchronized (names) {
                    if (!name.isBlank() && !names.contains(name)) {
                        names.add(name);
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
                if (input.equals("quit")) {
                    return;
                }

                for (Map.Entry<Integer, Integer> entry : udpPorts.entrySet()) {
                    datagramSend(name + " : " + input, entry.getKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (name != null) {
                names.remove(name);
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
