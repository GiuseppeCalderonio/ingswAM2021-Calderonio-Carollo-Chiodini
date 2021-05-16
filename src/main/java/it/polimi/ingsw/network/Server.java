package it.polimi.ingsw.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this class represent the Server.
 * In particular, every client, once accepted the connection, will be
 * assigned to a thread that will run in the class waiting room, and from
 * there the communication will start
 */
public class Server {

    /**
     * this attribute represent the port of the server
     */
    private final int port;

    /**
     * this constructor create the server setting the port
     * @param port this is the port in which the server will be reachable
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * this method start the server, accepting connection and creating a pool of
     * threads for every player
     */
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
                executor.submit(new WaitingRoom(socket));

            } catch(IOException e) {

                break;
            }
        }
        System.out.println("Problem");
        executor.shutdown();
    }

    /**
     * this method is the main of the server, it create it and start it
     * @param args these are the arguments of the main, containing the port pf the server
     */
    public static void main(String[] args) {
        Server Server = new Server(Integer.parseInt(args[0]));
        Server.startServer();
    }
}