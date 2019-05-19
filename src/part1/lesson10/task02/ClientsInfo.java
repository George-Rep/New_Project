package part1.lesson10.task02;

import java.net.InetAddress;

/**
 * класс для хранения IP клиента и порта,
 * на котором клиент слушает UDP
 */
public class ClientsInfo {

    InetAddress address;
    int port;

    public ClientsInfo(InetAddress address, int port) {

        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
