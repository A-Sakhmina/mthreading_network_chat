package connection;

public interface ConnectionListener {
    void isConnectionReady(Connection connection);
    void isConnectionDisconnected(Connection connection);
    void receiveMessage(Connection connection, String msg);
}
