package server.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientSession {
    private final String username;
    private final Socket socket;

    private final BufferedInputStream inputStream;
    private final BufferedOutputStream outputStream;

    private final LocalDateTime connectedAt;
    private final AtomicBoolean online;

    public ClientSession(String username, Socket socket) throws IOException {
        this.username = username;
        this.socket = socket;
        this.inputStream = new BufferedInputStream(socket.getInputStream());
        this.outputStream = new BufferedOutputStream(socket.getOutputStream());
        this.connectedAt = LocalDateTime.now();
        this.online = new AtomicBoolean(true);
    }

    // ------------------- Getters -------------------

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedInputStream getInputStream() {
        return inputStream;
    }

    public BufferedOutputStream getOutputStream() {
        return outputStream;
    }

    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }

    public boolean isOnline() {
        return online.get();
    }

    // ------------------- Session Control -------------------

    /**
     * Marks the session as offline and safely closes resources.
     */
    public void disconnect() {
        if (online.compareAndSet(true, false)) {
            try {
                inputStream.close();
            } catch (IOException ignored) {}

            try {
                outputStream.close();
            } catch (IOException ignored) {}

            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }

    /**
     * Send raw bytes to client.
     */
    public synchronized void send(byte[] data) throws IOException {
        outputStream.write(data);
        outputStream.flush();
    }

    @Override
    public String toString() {
        return "ClientSession{" +
                "username='" + username + '\'' +
                ", online=" + online +
                ", connectedAt=" + connectedAt +
                '}';
    }
}
