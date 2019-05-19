package part1.lesson10.task02;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Усовершенствовать задание 1:
 * a.      добавить возможность отправки личных сообщений (unicast).
 * b.      добавить возможность выхода из чата с помощью написанной в чате команды «quit»
 * см. task01
 */
public class Server {
    public static final Integer SERVER_PORT = 5000;

    private static Map<String, ClientsInfo> clientsInfo = new HashMap<>();
    private static Map<Integer, Integer> udpPorts = new HashMap<>();
    private static final Integer MAX_THREADS = 500;

    /**
     * При каждом установлении соединения с очередным клиентом создается новый поток для одного указанного соединения.
     * В данном случае создаются задачи в FixedThreadPool, и число задач не должно превысить MAX_THREADS.
     *
     * @see ClientThreadOnServer
     */
    public static void main(String[] args) {
        System.out.println("Сервер запущен");
        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                pool.execute(new ClientThreadOnServer(socket, clientsInfo, udpPorts));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
