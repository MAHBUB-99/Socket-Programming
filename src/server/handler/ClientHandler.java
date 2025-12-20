package server.handler;

import server.model.SessionManager;
import server.protocol.CommandType;
import server.protocol.ServerResponse;
import server.exception.ProtocolException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private final Socket socket;
    private String username;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    public ClientHandler(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try{
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            username = (String) in.readObject();

            if (!SessionManager.login(username, socket)) {
                out.writeObject(ServerResponse.error("Username already in use"));
                socket.close();
                return;
            }

            out.writeObject(ServerResponse.success("Connected"));

            while (true) {
                CommandType cmd = (CommandType) in.readObject();
                handleCommand(cmd);
            }
        }catch (Exception exception)
        {
            exception.printStackTrace();
        }finally {
            SessionManager.logout(username);
            close();
        }
    }
    private void handleCommand(CommandType cmd) throws ProtocolException {
        if (cmd == null) throw new ProtocolException("Null command");
        // Delegation happens here (UPLOAD, DOWNLOAD, REQUEST, etc.)
    }

    private void close() {
        try { socket.close(); } catch (Exception ignored) {}
    }

}
