import connection.Connection;
import connection.ConnectionListener;

import java.io.IOException;
import java.util.Scanner;

public class ClientChat implements ConnectionListener {

    private static final String IP = "192.168.0.10";
    private static final int PORT = 8089;


    public static void main(String[] args) {

        new ClientChat();
    }

    private Connection connection;
    private final String nickname;
    //private final Logger logger;

    private ClientChat() {
        System.out.println("Введите своё имя");
        Scanner scanner = new Scanner(System.in);
        this.nickname = scanner.nextLine();
        try {
            connection = new Connection(this, IP, PORT);
        } catch (IOException e) {
            printMsg("Connection exception");
        }
    }

    private synchronized void printMsg(String message) {
        System.out.println(nickname + ": " + message);
        connection.sendMessage(nickname + ": " + message);
    }

    @Override
    public void isConnectionReady(Connection connection) {
        printMsg("Connection ready");
    }

    @Override
    public void isConnectionDisconnected(Connection connection) {
        printMsg("Connection closed");
    }

    @Override
    public void receiveMessage(Connection connection, String msg) {
        printMsg(msg);
    }
}
