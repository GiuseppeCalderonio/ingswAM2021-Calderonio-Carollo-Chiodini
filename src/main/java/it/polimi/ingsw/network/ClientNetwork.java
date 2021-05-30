package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.PongCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.PingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static it.polimi.ingsw.controller.gsonManager.PersonalGsonBuilder.createPersonalGsonBuilder;

/**
 * this class represent the client network.
 * in particular, a client network have to implement network user interface
 * because is a class used to interact and exchange messages throughout the network,
 * and have to implement runnable because it needs a thread to wait messages from the network
 */
public class ClientNetwork implements NetworkUser<Command, ResponseToClient>, Runnable {

    /**
     * this attribute represent the print writer associated with the socket
     */
    private PrintWriter out = null;

    /**
     * this attribute represent the buffer reader associated with the socket
     */
    private BufferedReader in = null;

    /**
     * this attribute represent the socket associated with the client network.
     * it is used to send messages
     */
    private Socket socket;

    /**
     * this attribute represent the gson used to parse messages
     */
    private final Gson gson = createPersonalGsonBuilder();

    /**
     * this attribute represent the view to eventually update
     */
    private View view;

    /**
     * this constructor create the object creating the socket and the buffer reader and
     * the scanner associating them with the port and ip passed as input, and
     * set the view to eventually update after a message received.
     * it also start the thread that read messages from th network
     * @param hostName this is the host name in which connect the socket
     * @param portNumber this is the port number in which connect the socket
     * @param view this is the view to eventually update after a message received
     * @throws IOException if a network error occurs
     */
    public ClientNetwork(String hostName, int portNumber, View view) throws IOException {

        this.socket = new Socket(hostName, portNumber);
        out = new PrintWriter(this.socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.view = view;

        Thread networkReader = new Thread(this);
        networkReader.start();
    }

    /**
     * this constructor is used only for the sub class localClient
     * @see it.polimi.ingsw.network.localGame.LocalClient
     */
    protected ClientNetwork() {
    }


    /**
     * this method send a message to the network
     *
     * @param networkMessage this is the message to send
     */
    @Override
    public void send(Command networkMessage) {
        out.println(gson.toJson(networkMessage, Command.class));
        out.flush();
    }

    /**
     * this method receive a message from the network
     *
     * @return the message received
     * @throws IOException if a network error occurs
     */
    @Override
    public ResponseToClient receiveMessage() throws IOException {
        return gson.fromJson(in.readLine(), ResponseToClient.class);
    }


    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */

    @Override
    public void run() {

        ResponseToClient response;

        try {

            try {
                while (true) {

                    response = receiveMessage();
                    synchronized (this) {

                        try {
                            response.updateClient(view);

                        } catch (PingException e){
                            send(new PongCommand());
                        }

                    }

                }
            } catch (NullPointerException e){
                //e.printStackTrace();
                in.close();
                out.close();
                socket.close();
                view.quit();
            }

        } catch (IOException e){
            System.err.println("Something wrong happened IOException..." + e.getMessage());
            System.exit(1);

        }
    }
}
