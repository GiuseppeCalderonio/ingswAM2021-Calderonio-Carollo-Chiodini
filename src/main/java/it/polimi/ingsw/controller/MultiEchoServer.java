package it.polimi.ingsw.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this class represent the controller
 */
public class MultiEchoServer {

    private final int port;

    public MultiEchoServer(int port) {
        this.port = port;
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // Port not available
            return;
        }
        System.out.println("Server ready");
        //Socket socket = null;

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new ClientHandler(socket));

            } catch(IOException e) {

                break;
            }
        }
        System.out.println("Problem");
        executor.shutdown();
    }
    public static void main(String[] args) {
        MultiEchoServer echoServer = new MultiEchoServer(Integer.parseInt(args[0]));
        echoServer.startServer();
    }
}