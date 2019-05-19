package part1.lesson10.task02;


import java.io.IOException;
import java.net.*;

/**
 * Поток соответствующий листенеру клиента, ожидается UDP от сервера.
 */
public class ClientListener extends Thread {
    private DatagramSocket ds;

    /**
     * Конструктор
     *
     * @param ds сокет для получения UDP сообщений.
     */
    public ClientListener(DatagramSocket ds) {
        this.ds = ds;
    }

    /**
     * Принимаемые датаграммы выводятся на консоль.
     * DatagramSocket закрывается при завершении основной программы.
     * На данный момент это приводит к отработке SocketException
     * в данном методе.
     */
    @Override
    public void run() {

        try {
            byte[] buf = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buf, 1024);

            while (true) {
                ds.receive(dp);
                String str = new String(dp.getData(), 0, dp.getLength());
                System.out.println(str);
            }
        } catch (SocketException e) {
            System.out.println("Выход");
            //e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
