package server;

import server.config.ServerConfig;
import server.handler.ClientHandler;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.ECField;

public class MainServer {
    public static void main(String[] args) {
        System.out.println("File Server is Starting.....");

        try(ServerSocket serverSocket = new ServerSocket(ServerConfig.SERVER_PORT)){
            while(true)
            {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        }catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
}