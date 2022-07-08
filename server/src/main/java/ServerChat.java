import connection.Connection;
import connection.ConnectionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class ServerChat implements ConnectionListener {
    public static void main(String[] args) {
        new ServerChat();
    }

    private Connection connection;
    //list of connections because we have several clients
    private final ArrayList<Connection> connections = new ArrayList<>();

    private ServerChat() {
        System.out.println("server starts running");
        try (ServerSocket serverSocket = new ServerSocket(8089)) {
            while (true) {
                //client connection
                try {
                    new Connection(this, serverSocket.accept());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void sendMsgToAllConnections(String msg) {
        System.out.println(msg);
        //if the string has been received then sent it to all clients(connections)
        for (Connection connect :
                connections) {
            connect.sendMessage(msg);
        }
    }

    @Override
    public synchronized void receiveMessage(Connection connection, String msg) {
        sendMsgToAllConnections(msg);
    }

    @Override
    public synchronized void isConnectionReady(Connection connection) {
        //if the connection is ok then add it to the list
        connections.add(connection);
        System.out.println("client added: " + connection);
    }

    @Override
    public synchronized void isConnectionDisconnected(Connection connection) {
        //if the connection is disconnected then remove it from the list
        connections.remove(connection);
        System.out.println("client disconnected :" + connection);
    }
}
