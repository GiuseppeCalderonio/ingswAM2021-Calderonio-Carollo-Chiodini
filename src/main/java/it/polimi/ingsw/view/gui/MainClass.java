package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.network.WaitingRoom;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this is the main class of the project. This class can be the server, a client or a local client. When we run the jar from wsl
 * we insert some parameters and through this parameter we can decide in which mode this class have to start.
 */
public class MainClass {

    /**
     * this attribute represent the port of the server
     */
    private final int port;

    /**
     * this constructor create the server setting the port
     * @param port this is the port in which the server will be reachable
     */
    public MainClass(int port) {
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

    public static void main(String[] args) {
        switch (args[0]) {
            //server case
            case "server":
                MainClass Server = new MainClass(Integer.parseInt(args[1]));
                Server.startServer();
                break;

            //local client case
            case "localClient":
                String view = args[3];
                new Client(null, 0, view);

            //normal client case
            case "client":
                String View = args[3];
                int portNumber = Integer.parseInt(args[1]);
                String hostname = args[2];
                new Client(hostname, portNumber, View);

            default:
                System.out.println("wrong initialisation, the correct initialisation is:\n" +
                        "First parameter: server / localClient / client\n" +
                        "Second parameter: port number\n" +
                        "Third parameter: ip address\n" +
                        "Fourth parameter: -CLI / -GUI");
        }
    }
}
