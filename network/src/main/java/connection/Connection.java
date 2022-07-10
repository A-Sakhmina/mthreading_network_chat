package connection;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {
    private final Socket socket;
    private final ConnectionListener listener;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final Thread connectionThread;

    public Connection(ConnectionListener listener, String ipAd, int port) throws IOException {
        this(listener, new Socket(ipAd, port));
    }

    public Connection(ConnectionListener listener, Socket socket) throws IOException {
        this.listener = listener;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        connectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listener.isConnectionReady(Connection.this);
                    while (!connectionThread.isInterrupted()) {
                        String msg;
                        msg = in.readLine();
                        if (msg != null) listener.receiveMessage(Connection.this, msg);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    listener.isConnectionDisconnected(Connection.this);
                }
            }
        });
        connectionThread.start();
    }

    public synchronized void sendMessage(String msg) {
        try {
            if ("/exit".equals(msg)) disconnect();
            else out.write(msg + "\r\n");
            out.flush();
        } catch (IOException e) {
            //e.printStackTrace();
            disconnect();
        }
    }

    //
    public synchronized void disconnect() {
        connectionThread.interrupt();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Connection: " + socket.getInetAddress() + " : " + socket.getPort();
    }
}
