package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.gsonManager.MarbleInterfaceAdapter;
import it.polimi.ingsw.controller.gsonManager.ResourceInterfaceAdapter;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.*;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.model.SingleGame.CardToken;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.model.SingleGame.TrackToken;
import it.polimi.ingsw.view.ThinLeaderCard;
import it.polimi.ingsw.view.ThinPlayer;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class represent the main
 */
public class Client implements Runnable {

    private final String hostName;
    private Socket echoSocket;
    private final Gson gson;
    // being able to show
    private int position = 0;
    private ThinPlayer myself;
    private List<ThinPlayer> opponents;
    private final List<LeaderCard> allLeaderCards;
    private DevelopmentCard[][] cardsMarket;
    private Marble[][] marbleMarket;
    private Marble lonelyMarble;
    private SoloToken solotoken;
    private List<Resource> gainedFromMarbleMarket;
    private int marbles;


    public Client(String hostName, int portNumber) {
        this.hostName = hostName;
        try {
            this.echoSocket = new Socket(hostName, portNumber);
        }catch (IOException e){
            System.err.println("Error during the connection with the server");
            System.exit(1);
        }

        allLeaderCards = createLeaderCards();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.
                        of(SoloToken.class, "type").
                        registerSubtype(TrackToken.class, "trackToken").
                        registerSubtype(CardToken.class, "cardToken"));
        builder.registerTypeAdapter(Resource.class, new ResourceInterfaceAdapter());
        builder.registerTypeAdapter(Marble.class, new MarbleInterfaceAdapter());
        gson = builder.create();
        start();
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

                Command jsonCommand = new Command();
                jsonCommand.cmd = command;

                switch (jsonCommand.cmd){

                    case "show":
                        show();
                        break;

                    case "set_players" :
                        System.out.println("Scrivere il numero di giocatori");
                        String numberOfPlayers = stdIn.readLine();
                        try {
                            jsonCommand.numberOfPlayers = Integer.parseInt(numberOfPlayers);
                        }catch (NumberFormatException e){
                            System.err.println("Devi scrivere un numero valido");
                            break;
                        }
                        send(out, jsonCommand);
                        break;
                    case "login":
                        System.out.println("Scrivere il nickname");
                        jsonCommand.nickname = stdIn.readLine();
                        send(out, jsonCommand);
                        break;
                    case "initialise_leaderCards":
                        initialiseLeaderCard(out, stdIn, jsonCommand);
                        break;

                    case "initialise_resources":
                        initialiseResources(out, stdIn, jsonCommand);
                        break;

                    case "shift_resources":
                        shiftResources(out, stdIn, jsonCommand);
                        break;
                    case "choose_marbles":
                        chooseMarbles(out, stdIn, jsonCommand);
                        break;
                    case "choose_leaderCards":
                        try {
                            chooseLeaderCard(out, stdIn, jsonCommand);
                        }catch (NullPointerException e){
                            System.err.println("you can't do this action");
                        }

                        break;
                    case "insert_in_warehouse":
                        insertInWarehouse(out, stdIn, jsonCommand);
                        break;
                    case "buy_card":
                        buyCard(out, stdIn, jsonCommand);
                        break;
                    case "select_position":
                        selectPosition(out, stdIn, jsonCommand);
                        break;
                    case "select_resources_from_warehouse":
                        selectResourcesFromWarehouse(out, stdIn, jsonCommand);
                        break;
                    case "production":
                        send(out, jsonCommand);
                        break;
                    case "basic_production":
                        basicProduction(out, stdIn, jsonCommand);
                        break;
                    case "normal_production":
                        normalProduction(out, stdIn, jsonCommand);
                        break;
                    case "leader_production":
                        leaderProduction(out, stdIn, jsonCommand);
                        break;
                    case "end_production":
                        send(out, jsonCommand);
                        break;
                    case "leader_action":
                        send(out, jsonCommand);
                        break;
                    case "activate_card":
                        activateCard(out,stdIn , jsonCommand);
                        break;
                    case "discard_card":
                        discardCard(out,stdIn , jsonCommand);
                        break;
                    case "end_turn":
                        send(out, jsonCommand);
                        break;
                    case "quit":
                        send(out, jsonCommand);
                        break;
                    default:
                        System.out.println("Comando non riconosciuto");
                        jsonCommand.cmd = "";
                        send(out, jsonCommand);
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

        ResponseToClient response;
        String received;

        try {
            while (true){
                assert in != null;
                if ((received = in.readLine()) == null) break;
                response = gson.fromJson(received, ResponseToClient.class);
                if (response.serialize){ // if the internal state of the game is changed
                    processObject(response.code, response);
                }

                synchronized (this){
                    if (response.message != null){
                        if (response.message.equals("ping"))
                            pong();
                        else{
                            if (response.ignorePossibleCommands)
                                System.out.println(response.message);
                            else{
                                response.possibleCommands.add("quit");
                                response.possibleCommands.add("show");
                                System.out.println(response.message + ", Possible commands:" + response.possibleCommands);
                            }

                            if (response.position > 0){
                                this.position = response.position;
                                System.out.println("You are the " + position + "° player");
                            }
                            if (response.marbles != 0)
                                marbles = response.marbles;
                        }

                    }
                }

            }
            in.close();
            System.err.println("Disconnessione in corso...");
            System.exit(1);
        } catch (IOException e){
            System.err.println("Qualcosa è andato storto con IOexception...");
            System.err.println(e.getMessage());
        }catch (NullPointerException e){
            System.err.println("Un client si è Disconnesso, errore di NullPointerException");
            System.err.println(e.getMessage());
            System.exit(1);
        }catch (JsonSyntaxException e){
            System.err.println("Disconnessione in corso per json errore...");
            System.err.println(e.getMessage());
        }
    }

    private void pong() throws IOException {
        Command pong = new Command();
        pong.cmd = "pong";
        send(new PrintWriter(echoSocket.getOutputStream()), pong);
    }

    /**
     * this method send a message to the server, the message
     * have to be in the format class Command
     * @param out this is the printWriter associated with the socket
     * @param command this is the socket to send
     */
    private void send(PrintWriter out, Command command){
        out.println(gson.toJson(command, Command.class));
        out.flush();
    }

    /**
     * this method verify that a string is associated with a resource,
     * that happen when the string toVerify is not STONE, SHIELD, SERVANT or COIN
     * @param toVerify this is the string to verify
     * @return true if the string is associated with a resource, false otherwise
     */
    private boolean invalidResource(String toVerify){
        List<String> resources = new ArrayList<>(Arrays.asList("COIN", "STONE", "SHIELD", "SERVANT"));
        return !resources.contains(toVerify);
    }

    /**
     * this method convert the string passed in input, it requires that
     * isValidResource(resource) == true
     * it returns coin, stone, servant or shield depending on the string in input
     * @param resource this is the string to convert
     * @return the resource associated with the string
     */
    private Resource convertResource(String resource){
        return ResourceType.valueOf(ResourceType.class, resource).getResource();
    }

    private synchronized void initialiseResources(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        String firstResource;
        String secondResource;
        switch (position){
            case 1:
                break;
            case 2:
            case 3:
                System.out.println("Scrivere la risorsa da voler ottenere[coin, stone, shield, servant]");
                firstResource = stdIn.readLine().toUpperCase();
                if(invalidResource(firstResource)){
                    System.out.println("Risorsa non valida");
                    return;
                }
                jsonCommand.firstResource = convertResource(firstResource);
                break;
            case 4:
                System.out.println("Scrivere la prima risorsa da voler ottenere[coin, stone, shield, servant]");
                firstResource = stdIn.readLine().toUpperCase();
                if(invalidResource(firstResource)){
                    System.out.println("Risorsa non valida");
                    return;
                }
                System.out.println("Scrivere la seconda risorsa da voler ottenere[coin, stone, shield, servant]");
                secondResource = stdIn.readLine().toUpperCase();
                if(invalidResource(secondResource)){
                    System.out.println("Risorsa non valida");
                    return;
                }
                jsonCommand.firstResource = convertResource(firstResource);
                jsonCommand.secondResource = convertResource(secondResource);
                break;
        }

        send(out, jsonCommand);
    }

    private void initialiseLeaderCard(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        System.out.println("Scrivere la prima leader card da voler scartare");
        String firstCard = stdIn.readLine();
        try {
            jsonCommand.firstCard = Integer.parseInt(firstCard);
        }catch (NumberFormatException e){
            System.err.println("Devi inserire un numero valido");
            return;
        }
        System.out.println("Scrivere la seconda leader card da voler scartare");
        String secondCard = stdIn.readLine();
        try {
            jsonCommand.secondCard = Integer.parseInt(secondCard);
        }catch (NumberFormatException e){
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, jsonCommand);
    }

    private void shiftResources(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        System.out.println("Scrivere il primo shelf in cui voler fare lo shift");
        String shelf = stdIn.readLine();
        try {
            jsonCommand.source = Integer.parseInt(shelf);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        System.out.println("Scrivere il secondo shelf in cui voler fare lo shift");
        shelf = stdIn.readLine();
        try {
            jsonCommand.destination = Integer.parseInt(shelf);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, jsonCommand);
    }

    private void chooseMarbles(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        System.out.println("Scrivere da dove comprare [row/column]");
        String dimension = stdIn.readLine();
        if (!dimension.equals("row") && !dimension.equals("column")){
            System.out.println("Non hai inserito valori validi");
            return;
        }
        jsonCommand.dimension = dimension;

        System.out.println("Scrivere l'indice da cui comprare le biglie nel mercato");
        String index = stdIn.readLine();
        try {
            jsonCommand.index = Integer.parseInt(index);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }

        send(out, jsonCommand);
    }

    private void chooseLeaderCard(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        int[] indexes = new int[marbles];
        for (int i = 0; i < marbles; i++) {
            System.out.println("Scrivere l'indice della carta leader con cui convertire la " + (i+1) + "° biglia bianca");
            String index = stdIn.readLine();
            try {
                 indexes[i] = Integer.parseInt(index);
            }catch (NumberFormatException e) {
                System.err.println("Devi inserire un numero valido");
                return;
            }
        }
        jsonCommand.indexes = indexes;
        send(out, jsonCommand);
    }

    private void insertInWarehouse(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        int[] shelves = new int[gainedFromMarbleMarket.size()];
        int i = 0;
        for (Resource gained : gainedFromMarbleMarket){
            System.out.println("Scrivere in quale scaffale inserire "+ gained);
            String index = stdIn.readLine();
            try {
                shelves[i] = Integer.parseInt(index);
            }catch (NumberFormatException e) {
                System.err.println("Devi inserire un numero valido");
                return;
            }
            i++;
        }
        jsonCommand.shelves = shelves;
        send(out, jsonCommand);
    }

    private boolean isValidColor(String color){
        List<String> colors = new ArrayList<>(Arrays.asList("GREEN", "YELLOW", "PURPLE", "BLUE"));
        return colors.contains(color);
    }

    private CardColor convertColor(String color){
        return ResourceType.valueOf(CardColor.class, color);
    }

    private void buyCard(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        System.out.println("Scrivere il colore della carta da comprare");
        String color = stdIn.readLine().toUpperCase();
        if (!isValidColor(color)){
            System.err.println("Colore scelto non valido");
            return;
        }
        jsonCommand.color = convertColor(color);
        System.out.println("Scrivere il livello della carta da comprare");
        String level = stdIn.readLine();
        try {
            jsonCommand.level = Integer.parseInt(level);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, jsonCommand);
    }

    private void selectPosition(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        System.out.println("Scrivere in quale spazio voler piazzare la carta [1, 2, 3]");
        String dashboardPosition = stdIn.readLine();
        try {
            jsonCommand.dashboardPosition = Integer.parseInt(dashboardPosition);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, jsonCommand);
    }

    private CollectionResources createCollectionResources(BufferedReader stdIn, String message) throws IOException {
        String resource;
        CollectionResources resources = new CollectionResources();
        while (true){
            System.out.println(message + ": [coin, servant, stone, shield] \n scrivere [stop] se non si vogliono prelevare altre risorse");
            resource = stdIn.readLine().toUpperCase();
            if (resource.equals("STOP")) break;

            if (invalidResource(resource)){
                System.out.println("Risorsa non valida");
            }else {
                resources.add(convertResource(resource));
            }
            System.out.println("Risorsa da pagare attualmente scelte :" + resources);
        }
        return resources;
    }

    private void selectResourcesFromWarehouse(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        jsonCommand.toPayFromWarehouse = createCollectionResources(stdIn, "Scegliere risorsa da prendere dal warehouse");
        send(out, jsonCommand);
    }

    private void basicProduction(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        String output;
        jsonCommand.toPayFromWarehouse = createCollectionResources(stdIn, "Scegliere risorsa da prendere dal warehouse");
        jsonCommand.toPayFromStrongbox = createCollectionResources(stdIn, "Scegliere risorsa da prendere dallo strongbox");
        System.out.println("Scrivere la risorsa da voler ottenere come output della basic production[coin, stone, shield, servant]");
        output = stdIn.readLine().toUpperCase();
        if(invalidResource(output)){
            System.out.println("Risorsa non valida");
            return;
        }
        jsonCommand.output = convertResource(output);
        send(out, jsonCommand);
    }

    private void normalProduction (PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        System.out.println("Scrivere quale tra le produzioni normali possibili scegliere [1, 2, 3]");
        String position = stdIn.readLine();
        try {
            jsonCommand.position = Integer.parseInt(position);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        jsonCommand.toPayFromWarehouse = createCollectionResources(stdIn, "Scegliere risorsa da prendere dal warehouse");
        send(out, jsonCommand);
    }

    private boolean isValidBoolean(String decision){
        return new ArrayList<>(Arrays.asList("yes", "no")).contains(decision);
    }

    private boolean convertBoolean(String decision){
        return decision.equals("yes");
    }

    private void leaderProduction (PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        System.out.println("Scrivere quale tra le produzioni leader possibili scegliere [1, 2]");
        String position = stdIn.readLine();
        try {
            jsonCommand.position = Integer.parseInt(position);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        System.out.println("Vuoi pagare la risorsa scelta dal warehouse? [yes/no]" );
        String decision = stdIn.readLine().toLowerCase();
        if (!isValidBoolean(decision)){
            System.err.println("Devi inserire un valore valido");
            return;
        }
        jsonCommand.fromWarehouse = convertBoolean(decision);
        send(out, jsonCommand);
    }

    private void activateCard(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        System.out.println("Scrivere quale carta leader attivare [1, 2]: ");
        String toActivate = stdIn.readLine();
        try {
            jsonCommand.toActivate = Integer.parseInt(toActivate);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, jsonCommand);
    }

    private void discardCard(PrintWriter out, BufferedReader stdIn, Command jsonCommand) throws IOException {
        System.out.println("Scrivere quale carta leader scartare [1, 2]: ");
        String toDiscard = stdIn.readLine();
        try {
            jsonCommand.toDiscard = Integer.parseInt(toDiscard);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, jsonCommand);
    }


    private synchronized void processObject( int code, ResponseToClient response){
        switch (code){
            case 1:
                List<LeaderCard> leaderCards = response.leaderCards.stream().
                        map(this::recreate).
                        collect(Collectors.toList());
                System.out.println(leaderCards);
                return;
            case 2:
                cardsMarket = response.cardsMarket;
                this.marbleMarket = response.marbleMarket;
                lonelyMarble = response.lonelyMarble;
                solotoken = response.soloToken;
                myself = new ThinPlayer(response.actualPlayer, allLeaderCards);
                opponents = response.opponents.stream().map(opponent -> new ThinPlayer(opponent, allLeaderCards)).collect(Collectors.toList());
                break;
            case 3:
                myself = new ThinPlayer(response.actualPlayer, allLeaderCards);
                opponents = response.opponents.stream().map(opponent -> new ThinPlayer(opponent, allLeaderCards)).collect(Collectors.toList());
                break;
            case 4:
                gainedFromMarbleMarket = response.resourcesSet;
                return;
            case 5:
                myself = new ThinPlayer(response.actualPlayer, allLeaderCards);
                opponents = response.opponents.stream().map(opponent -> new ThinPlayer(opponent, allLeaderCards)).collect(Collectors.toList());
                marbleMarket = response.marbleMarket;
                lonelyMarble = response.lonelyMarble;
                break;
            case 6:
                myself = new ThinPlayer(response.actualPlayer, allLeaderCards);
                opponents = response.opponents.stream().map(opponent -> new ThinPlayer(opponent, allLeaderCards)).collect(Collectors.toList());
                cardsMarket = response.cardsMarket;
                break;
            case 7:
                myself = new ThinPlayer(response.actualPlayer, allLeaderCards);
                opponents = response.opponents.stream().map(opponent -> new ThinPlayer(opponent, allLeaderCards)).collect(Collectors.toList());
                cardsMarket = response.cardsMarket;
                solotoken = response.soloToken;
                break;
        }
        show();
    }

    private List<LeaderCard> createLeaderCards(){
        List<LeaderCard> leaders = new ArrayList<>(); // create an arraylist of leaderCard
        LeaderCardRequirements requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.BLUE, CardColor.YELLOW)));
        LeaderCardRequirements requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.PURPLE, CardColor.GREEN)));
        LeaderCardRequirements requirements3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.GREEN, CardColor.PURPLE)));
        LeaderCardRequirements requirements4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.YELLOW, CardColor.BLUE)));

        //NewWhiteMarble leaderCards
        leaders.add(new NewWhiteMarble(requirements2,5,new Coin())); leaders.add(new NewWhiteMarble(requirements1 ,5,new Stone())); leaders.add(new NewWhiteMarble(requirements4,5,new Servant())); leaders.add(new NewWhiteMarble(requirements3,5,new Shield())); //NewWhiteMarble leaderCard
        //NewShelf leaderCards
        leaders.add(new NewShelf(new ResourcesRequired(new Shield()),3,new Coin())); leaders.add(new NewShelf(new ResourcesRequired(new Servant()),3,new Shield())); leaders.add(new NewShelf(new ResourcesRequired(new Stone()),3,new Servant())); leaders.add(new NewShelf(new ResourcesRequired(new Coin()),3,new Stone())); //NewShelf leaderCard
        //NewProduction leaderCards
        leaders.add(new NewProduction(new LevelRequired(CardColor.GREEN) ,4 , new Coin())); leaders.add(new NewProduction(new LevelRequired(CardColor.PURPLE) ,4 , new Stone())); leaders.add(new NewProduction(new LevelRequired(CardColor.BLUE) ,4 , new Servant())); leaders.add(new NewProduction(new LevelRequired(CardColor.YELLOW) ,4 , new Shield())); //NewProduction leaderCard

        LeaderCardRequirements requirement1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.PURPLE)));
        LeaderCardRequirements requirement2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN , CardColor.BLUE)));
        LeaderCardRequirements requirement3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE , CardColor.PURPLE)));
        LeaderCardRequirements requirement4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.GREEN)));

        //NewDiscount leaderCards
        leaders.add(new NewDiscount(requirement1,2,new Coin())); leaders.add(new NewDiscount(requirement2,2,new Stone())); leaders.add(new NewDiscount(requirement3,2,new Shield())); leaders.add(new NewDiscount(requirement4,2,new Servant())); //NewDiscount leaderCard
        return leaders;
    }

    /**
     * this method build the leader cards starting from the victory points and the resource associated
     * @param leaderCard this is the leader card to recreate
     * @return the card
     */
    private LeaderCard recreate(ThinLeaderCard leaderCard){
        Resource resource = leaderCard.getResource();
        int victoryPoints = leaderCard.getVictoryPoints();
        for (LeaderCard card : allLeaderCards){
            if (card.getResource().equals(resource) && card.getVictoryPoints() == victoryPoints){
                if (leaderCard.isActive())
                    card.setActive();
                return card;
            }
        }
        return null;
    }

    private void show(){
        System.out.println(Arrays.deepToString(cardsMarket) + "\n");
        System.out.println(myself + "\n");
        System.out.println(opponents + "\n");
        System.out.println(Arrays.deepToString(marbleMarket) + "\n");
        System.out.println(lonelyMarble + "\n");
        System.out.println(solotoken + "\n");
    }
}