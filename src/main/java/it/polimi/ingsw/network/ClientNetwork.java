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

public class ClientNetwork implements NetworkUser<Command, ResponseToClient>, Runnable {


    private PrintWriter out = null;
    private BufferedReader in = null;
    private Socket socket;
    private final Gson gson = createPersonalGsonBuilder();
    private View view;


    public ClientNetwork(String hostName, int portNumber, View view) throws IOException {

        this.socket = new Socket(hostName, portNumber);
        out = new PrintWriter(this.socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.view = view;

        Thread networkReader = new Thread(this);
        networkReader.start();
    }

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

                    synchronized (this) {
                        response = receiveMessage();

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
