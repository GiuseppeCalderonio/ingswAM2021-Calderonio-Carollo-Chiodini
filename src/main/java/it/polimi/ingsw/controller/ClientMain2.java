package it.polimi.ingsw.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * this class represent the main
 */
public class ClientMain2 implements Runnable {

    private static boolean end = true;
    private String hostName;
    private int portNumber;
    private Socket echoSocket;
    // warehouse
    // strongbox
    // track
    // tessere papali giocatore 1
    // tessere papali giocatore 2
    // tessere papali giocatore 3
    // tessere papali giocatore 4
    // cardsMarket
    // leaderCards
    // marbleMarket
    // powerProduction
    // eventuali leader white marbles
    // eventuali sconti
    // eventuali power leader production

    public static void main(String[] args) throws IOException{
        ClientMain2 client = new ClientMain2("127.0.0.1", 1234);
        client.start();
    }

    public ClientMain2(String hostName, int portNumber) throws IOException {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.echoSocket = new Socket(hostName, portNumber);
    }


    private void start() {

        Thread t = new Thread(this);
        t.start();

        try (
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))
        ) {

            String command;
            while ((command = stdIn.readLine()) != null) {
                switch (command){

                    case "set_players" :
                        System.out.println("Scrivere il numero di giocatori");

                        String numberOfPlayers = stdIn.readLine();
                        String json = "{\"cmd\" : \"set_players\", \"size\" : " + numberOfPlayers + "}";
                        out.println(json);
                        out.flush();
                        break;

                    case "login":
                        System.out.println("Scrivere il nickname");

                        String nickname = stdIn.readLine();
                        json = "{\"cmd\" : \"login\", \"nickname\" : " + nickname + "}";
                        out.println(json);
                        out.flush();
                        break;

                    case "initialise_leaderCards":
                        System.out.println("Scrivere la prima leader card da voler scartare");
                        String firstCard = stdIn.readLine();
                        System.out.println("Scrivere la seconda leader card da voler scartare");
                        String secondCard = stdIn.readLine();

                        json = "{\"cmd\" : \"initialise_leaderCards\", \"firstCard\" : " + firstCard + ", \"secondCard\" : " + secondCard + "}";
                        out.println(json);
                        out.flush();
                        break;

                    case "initialise_resources":
                        System.out.println("Scrivere la prima risorsa da voler ottenere[in caso non hai diritto ad alcuna risorsa, premi invio]");
                        String firstResource = stdIn.readLine().toUpperCase();
                        if(!isValidResource(firstResource)){
                            System.out.println("Risorsa non valida");
                            break;
                        }
                        System.out.println("Scrivere la seconda risorsa da voler ottenere[in caso non hai diritto ad alcuna risorsa, premi invio]");
                        String secondResource = stdIn.readLine().toUpperCase();
                        if(!isValidResource(secondResource)){
                            System.out.println("Risorsa non valida");
                            break;
                        }
                        json = "{\"cmd\" : \"initialise_resources\", \"firstResource\" : " + firstResource + ", \"secondResource\" : " + secondResource + "}";
                        out.println(json);
                        out.flush();
                        break;

                    case "quit":

                        json = "{\"cmd\" : \"quit\"}";
                        out.println(json);
                        out.flush();
                        break;

                    default:
                        System.out.println("Comando non riconosciuto");
                        json = "{\"cmd\" : \"sorcio\"}";
                        out.println(json);
                        out.flush();
                        break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Someone left, the game finish, we are sorry...");
            System.exit(1);
        }
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
        BufferedReader in =
                null;
        try {
            in = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String recived;

        try {
            while ((recived = in.readLine()) != null){
                System.out.println(recived);
            }
            System.err.println("Disconnessione in corso...");
            System.exit(1);
        } catch (IOException e){
            System.err.println(e.getMessage());
        }catch (NullPointerException e){
            System.err.println("Un client si è nullDisconnesso");
            System.exit(1);
        }
    }

    private boolean isValidResource(String toVerify){
        List<String> resources = new ArrayList<>();
        resources.add("COIN");
        resources.add("STONE");
        resources.add("SHIELD");
        resources.add("SERVANT");
        resources.add("");
        return resources.contains(toVerify);
    }
}
