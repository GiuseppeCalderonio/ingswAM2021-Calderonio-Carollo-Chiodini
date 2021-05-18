package it.polimi.ingsw.network;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.UnknownCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.*;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.view.Cli;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinGame;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.controller.gsonManager.PersonalGsonBuilder.createPersonalGsonBuilder;
import static it.polimi.ingsw.view.gui.Gui.startGui;

/**
 * this class represent the main
 */
public class Client implements Runnable, NetworkUser<Command, ResponseToClient> {

    private Socket socket;
    private final Gson gson = createPersonalGsonBuilder();
    // being able to show
    private int position = 0;
    private ThinGame game;
    private final List<LeaderCard> allLeaderCards = createLeaderCards();
    private List<Resource> gainedFromMarbleMarket;
    private int marbles;
    private PrintWriter out;
    private BufferedReader in;
    private Command lastCommand = new UnknownCommand();
    private final View view;


    public Client(String hostName, int portNumber, boolean cli, View gui) {

        if (cli){
            view = new Cli(this);
        }
        else {
            view = new Gui();
            Gui.setClient(this);
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    startGui();
                }
            });
            t.start();

        }

        if (portNumber == 0 || hostName == null)
            return;

        try {

            this.socket = new Socket(hostName, portNumber);
            out = new PrintWriter(this.socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            start();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    protected void start() throws IOException {

        Thread networkReader = new Thread(this);
        networkReader.start();
        // this while true won't work forever because the thread associated with the network receiver
        // will close the program if necessary
        while (true) {
            try {
                lastCommand = view.createCommand();
                send(lastCommand);
            } catch (InvalidParameterException e){
                view.showErrorMessage(e);
            }
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

        ResponseToClient response;

        try {

            try {
                while (true) {

                    synchronized (this) {
                        response = receiveMessage();

                        response.updateClient(this);
                    }

                }
            } catch (NullPointerException e){

                in.close();
                System.err.println("Disconnection...");
                System.exit(1);
            }

        } catch (IOException e){
            System.err.println("Something wrong happened IOException..." + e.getMessage());
            System.exit(1);

        }
    }

    /**
     * this method send a message to the server, the message
     * have to be in the format class Command
     * @param command this is the socket to send
     */
    @Override
    public void send(Command command){
        out.println(gson.toJson(command, Command.class));
        out.flush();
    }

    /**
     * this method receive a message from the network
     * @return the message received
     * @throws IOException if a network error occurs
     */
    @Override
    public ResponseToClient receiveMessage() throws IOException {
        return gson.fromJson(in.readLine(), ResponseToClient.class);
    }

    public static List<LeaderCard> createLeaderCards(){

        List<LeaderCard> leaders = new ArrayList<>(); // create an arraylist of leaderCard
        LeaderCardRequirements requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.BLUE, CardColor.YELLOW)));
        LeaderCardRequirements requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.PURPLE, CardColor.GREEN)));
        LeaderCardRequirements requirements3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.GREEN, CardColor.PURPLE)));
        LeaderCardRequirements requirements4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.YELLOW, CardColor.BLUE)));

        //NewWhiteMarble leaderCards
        leaders.add(new NewWhiteMarble(requirements2,5,new Coin(), "60"));
        leaders.add(new NewWhiteMarble(requirements1 ,5,new Stone(), "59"));
        leaders.add(new NewWhiteMarble(requirements4,5,new Servant(), "57"));
        leaders.add(new NewWhiteMarble(requirements3,5,new Shield(), "58"));
        //NewShelf leaderCards
        leaders.add(new NewShelf(new ResourcesRequired(new Shield()),3,new Coin(), "56"));
        leaders.add(new NewShelf(new ResourcesRequired(new Servant()),3,new Shield(), "55"));
        leaders.add(new NewShelf(new ResourcesRequired(new Stone()),3,new Servant(), "54"));
        leaders.add(new NewShelf(new ResourcesRequired(new Coin()),3,new Stone(), "53"));
        //NewProduction leaderCards
        leaders.add(new NewProduction(new LevelRequired(CardColor.GREEN) ,4 , new Coin(), "64"));
        leaders.add(new NewProduction(new LevelRequired(CardColor.PURPLE) ,4 , new Stone(), "63"));
        leaders.add(new NewProduction(new LevelRequired(CardColor.BLUE) ,4 , new Servant(), "62"));
        leaders.add(new NewProduction(new LevelRequired(CardColor.YELLOW) ,4 , new Shield(), "61"));

        LeaderCardRequirements requirement1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.PURPLE)));
        LeaderCardRequirements requirement2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN , CardColor.BLUE)));
        LeaderCardRequirements requirement3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE , CardColor.PURPLE)));
        LeaderCardRequirements requirement4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.GREEN)));

        //NewDiscount leaderCards
        leaders.add(new NewDiscount(requirement1,2,new Coin(), "52"));
        leaders.add(new NewDiscount(requirement2,2,new Stone(), "51"));
        leaders.add(new NewDiscount(requirement3,2,new Shield(), "50"));
        leaders.add(new NewDiscount(requirement4,2,new Servant(), "49"));
        return leaders;
    }

    public void show(){
        view.show(game);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<LeaderCard> getAllLeaderCards() {
        return allLeaderCards;
    }

    public int getMarbles() {
        return marbles;
    }

    public void setMarbles(int marbles) {
        this.marbles = marbles;
    }

    public ThinGame getGame(){
        return game;
    }

    public void createGame(DevelopmentCard[][] cardsMarket,
                           Marble[][] marbleMarket,
                           Marble lonelyMarble,
                           SoloToken soloToken,
                           ThinPlayer actualPlayer,
                           List<ThinPlayer> opponents){
        game = new ThinGame(cardsMarket, marbleMarket, lonelyMarble, soloToken, actualPlayer, opponents);
    }

    public void setGainedFromMarbleMarket(List<Resource> gainedFromMarbleMarket) {
        this.gainedFromMarbleMarket = gainedFromMarbleMarket;
    }

    public int getPosition() {
        return position;
    }

    public List<Resource> getGainedFromMarbleMarket() {
        return gainedFromMarbleMarket;
    }

    public void showContextAction(Status message){
        view.showContextAction(lastCommand, message);
    }

    public View getView(){
        return view;
    }
}