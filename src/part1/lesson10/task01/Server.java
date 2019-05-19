package part1.lesson10.task01;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Разработать приложение - многопользовательский чат, в котором участвует произвольное количество клиентов.
 * Каждый клиент после запуска отправляет свое имя серверу. После чего начинает отправлять ему сообщения.
 * Каждое сообщение сервер подписывает именем клиента и рассылает всем клиентам (broadcast).
 */
public class Server {
    private static final Integer SERVER_PORT = 5000;
    private static final Integer MAX_THREADS = 500;
    private static Set<String> names = new HashSet<>();
    private static Map<Integer, Integer> udpPorts = new HashMap<>();


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
                pool.execute(new ClientThreadOnServer(socket, names, udpPorts));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
