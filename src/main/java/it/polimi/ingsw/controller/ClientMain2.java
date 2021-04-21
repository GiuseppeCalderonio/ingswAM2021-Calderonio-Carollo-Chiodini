package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Resources.ResourceType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * this class represent the main
 */
public class ClientMain2 {
    public static void main(String[] args) throws IOException {

        String hostName = "127.0.0.1";
        int portNumber = 1234;
        try (
                Socket echoSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(echoSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(echoSocket.getInputStream()));
                BufferedReader stdIn =
                        new BufferedReader(
                                new InputStreamReader(System.in))
        ) {

            while (true) {

                try {
                    String sentFromServer = in.readLine();
                    System.out.println(sentFromServer);

                    if (sentFromServer.contains("exit")){
                        break;
                    }
                }catch (IOException e) {
                    System.err.println("Error buttanazza di chidda buttanazza " +
                            hostName);
                    System.exit(1);
                }

                String command = stdIn.readLine();


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
                        int firstCard = convert(stdIn.readLine());
                        System.out.println("Scrivere la seconda leader card da voler scartare");
                        int secondCard = convert(stdIn.readLine());

                        json = "{\"cmd\" : \"initialise_leaderCards\", \"firstCard\" : " + firstCard + ", \"secondCard\" : " + secondCard + "}";
                        out.println(json);
                        out.flush();
                        break;

                    case "initialise_resources":
                        System.out.println("Scrivere la prima risorsa da voler ottenere[in caso non hai diritto ad alcuna risorsa, premi invio]");
                        String firstResource = stdIn.readLine();
                        System.out.println("Scrivere la seconda risorsa da voler ottenere[in caso non hai diritto ad alcuna risorsa, premi invio]");
                        String secondResource = stdIn.readLine();

                        json = "{\"cmd\" : \"initialise_resources\", \"firstResource\" : " + ResourceType.valueOf(ResourceType.class,firstResource.toUpperCase()).getResource() + ", \"secondResource\" : " + ResourceType.valueOf(ResourceType.class,secondResource.toUpperCase()).getResource() + "}";
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
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        }
    }

    private static int convert(String toConvert){
        switch (toConvert){
            case "1":
                return 1;
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            default:
                return 0;
        }
    }
}
