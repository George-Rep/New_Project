package part1.lesson10.task01;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Класс клиента чата.
 *
 * @see Server
 */
public class Client {
    public static Integer UDP_PORT = 6001;
    static String serverAddress = "127.0.0.1";
    static int serverPort = 5000;
    static Scanner in;
    static BufferedWriter out;

    /**
     * метод для записи с использованием BufferedWriter. newLine(),flush().
     *
     * @param bufferedWriter BufferedWriter
     * @param str            строка для записи
     * @throws IOException
     */
    private static void writeNFlush(BufferedWriter bufferedWriter, String str) throws IOException {
        bufferedWriter.write(str);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    /**
     * После соединения с сервером, серверу передается порт, на котором клиент будет слушать UDP. Сервером запрашивается
     * имя клиента, до тех пор, пока оно не будет подтверждено сервером (имя должно быть уникальным по отношению к
     * именам ранее подключенных клиентов). При наборе в чате команды "quit", начинается подготовка к выходу из метода
     * и соответственно завершению работы клиента. Перед выходом происходит ожидание подтверждения выхода от сервера,
     * который также получает информацию о готовящемся отключении клиента, на стороне сервера
     * начинается завершение работы соответствующего потока и клиенту от сервера приходит указанное подтверждение.
     *
     * @see Server
     */
    private static void run() {
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            in = new Scanner(socket.getInputStream());
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            writeNFlush(out, UDP_PORT.toString());
            Scanner scanner = new Scanner(System.in);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.equals("ACCEPTED")) {
                    break;
                }
                System.out.println(line);
                writeNFlush(out, scanner.nextLine());
            }
            System.out.println("Чат: ");

            String message;
            message = scanner.nextLine();
            while (!message.equals("quit")) {
                writeNFlush(out, message);
                message = scanner.nextLine();
            }
            writeNFlush(out, message);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.equals("quit")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * метод определения возможности использования порта (для запуска слушателя UDP на данном порту).
     * true, если возможно использовать порт port.
     *
     * @param port порт клиента для оценки возможности использования.
     * @return boolean
     */
    public static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    /**
     * Определяется доступный порт, для использования на нем UDP слушателя.
     * Далее основной поток соединяется с сервером (см. метод run()) и
     * запускается второй поток (см. ClientListener), который будет прослушивать
     * UDP на указанном выше порту.
     */
    public static void main(String[] args) {

        for (int i = UDP_PORT; i < UDP_PORT + 1000; i++) {
            if (available(i)) {
                UDP_PORT = i;
                break;
            }
        }

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(UDP_PORT);
            ClientListener listenerThread = new ClientListener(ds);
            listenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        run();
        ds.close();
    }
}