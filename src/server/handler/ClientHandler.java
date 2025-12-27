package server.handler;

import server.manager.DirectoryManager;
import server.manager.SessionManager;
import server.model.FileMeta;
import server.protocol.CommandType;
import server.protocol.ServerResponse;
import server.exception.ProtocolException;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import server.handler.FileUploadHandler;
import server.handler.FileDownloadHandler;


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
            out.flush();
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
            if (username != null) SessionManager.logout(username);
            close();
        }
    }
    private void handleCommand(CommandType cmd) throws Exception {
        if (cmd == null) throw new ProtocolException("Null command");
        // Delegation happens here (UPLOAD, DOWNLOAD, REQUEST, etc.)
        switch (cmd) {
            case LIST_USERS: handleListUsers(); break;
            case LIST_FILES: handleListFiles(); break;
            case UPLOAD: handleUpload(); break;
            case DOWNLOAD: handleDownload(); break;
            case REQUEST: handleRequest(); break;
            case HISTORY: handleHistory(); break;
            case LOGOUT: handleLogout(); break;
            default: throw new ProtocolException("Unknown command: " + cmd);
        }
    }
    public void handleListUsers() throws IOException {
        var users = SessionManager.listUsers();
        out.writeObject(users);
        out.flush();
    }

    private void handleListFiles() throws IOException {
        Path userDir = DirectoryManager.getUserDir(username);
        List<String> files = Files.list(userDir)
                .filter(Files::isRegularFile)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
        out.writeObject(files);
        out.flush();
    }

    private void handleUpload() throws Exception {
        // Receive file metadata
        String fileName = (String) in.readObject();
        long size = (Long) in.readObject();
        boolean isPublic = (Boolean) in.readObject();

        FileMeta meta = FileUploadHandler.initiate(username, fileName, size, isPublic);
        if (meta == null) {
            out.writeObject(ServerResponse.error("Server buffer full, cannot upload now"));
            out.flush();
            return;
        }

        out.writeObject(ServerResponse.success(meta.fileId));
        out.flush();

        // Receive file in chunks
        while (meta.receivedSize < meta.originalSize) {
            byte[] chunk = (byte[]) in.readObject();
            FileUploadHandler.receiveChunk(meta.fileId, chunk);
        }

        boolean success = FileUploadHandler.complete(meta.fileId);
        out.writeObject(ServerResponse.success(success ? "Upload complete" : "Upload failed"));
        out.flush();
    }

    private void handleDownload() throws Exception {
        String fileName = (String) in.readObject();
        File file = DirectoryManager.getUserDir(username).resolve(fileName).toFile();

        if (!file.exists()) {
            out.writeObject(ServerResponse.error("File not found"));
            out.flush();
            return;
        }

        out.writeObject(ServerResponse.success("Starting download"));
        out.flush();
        FileDownloadHandler.send(file, socket.getOutputStream());
    }

    private void handleRequest() throws Exception {
        String owner = (String) in.readObject();
        String fileName = (String) in.readObject();

        Path publicFile = DirectoryManager.getPublicDir(owner).resolve(fileName);
        if (!Files.exists(publicFile)) {
            out.writeObject(ServerResponse.error("Requested file not found"));
            out.flush();
            return;
        }

        out.writeObject(ServerResponse.success("File ready for download"));
        out.flush();
        FileDownloadHandler.send(publicFile.toFile(), socket.getOutputStream());
    }

    private void handleHistory() throws IOException {
        Path historyFile = DirectoryManager.getUserDir(username).resolve("history.log");
        List<String> lines;
        if (!Files.exists(historyFile)) {
            lines = List.of(); // always send List<String> to avoid client errors
        } else {
            lines = Files.readAllLines(historyFile);
        }
        out.writeObject(lines);
        out.flush();
    }

    private void handleLogout() throws IOException {
        out.writeObject(ServerResponse.success("Logged out"));
        out.flush();
        SessionManager.logout(username);
        close();
    }

    private void close() {
        try { socket.close(); } catch (Exception ignored) {}
    }
}
