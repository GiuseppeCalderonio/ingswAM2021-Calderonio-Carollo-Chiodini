package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseLeaderCardsCommand;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseResourcesCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.ActivateCardCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.DiscardCardCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.LeaderCommand;
import it.polimi.ingsw.controller.commands.normalCommands.EndTurnCommand;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseMarblesCommand;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.InsertInWarehouseCommand;
import it.polimi.ingsw.controller.commands.normalCommands.ShiftResourcesCommand;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.BuyCardAction;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseLeaderCardsCommand;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.SelectPositionCommand;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.SelectResourcesFromWarehouseCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.*;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.*;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.controller.gsonManager.PersonalGsonBuilder.createPersonalGsonBuilder;

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
    private List<String> possibleCommands;
    private final CharStream console;


    public Client(String hostName, int portNumber) {
        this.hostName = hostName;
        try {
            this.echoSocket = new Socket(hostName, portNumber);
        }catch (IOException e){
            System.err.println("Error during the connection with the server");
            System.exit(1);
        }

        allLeaderCards = createLeaderCards();
        gson = createPersonalGsonBuilder();
        console = new CharStream(200, 7);
        console.setMessage("MAESTRI DEL RINASCIMENTO",0,0, ForeColor.ANSI_BRIGHT_YELLOW, BackColor.ANSI_BG_BLACK);
        console.print(System.out);
        console.reset();
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
                

                switch (command){

                    case "show":
                        show();
                        break;

                    case "set_players" :
                        System.out.println("Scrivere il numero di giocatori");
                        String numberOfPlayers = stdIn.readLine();
                        int numberOfPlayers1;
                        try {
                            numberOfPlayers1 = Integer.parseInt(numberOfPlayers);
                        }catch (NumberFormatException e){
                            System.err.println("Devi scrivere un numero valido");
                            break;
                        }
                        send(out, new SetSizeCommand(numberOfPlayers1));
                        break;
                    case "login":
                        System.out.println("Scrivere il nickname");
                        //jsonCommand.nickname = stdIn.readLine();
                        send(out, new LoginCommand(stdIn.readLine()));
                        break;
                    case "initialise_leaderCards":
                        initialiseLeaderCard(out, stdIn);
                        break;

                    case "initialise_resources":
                        initialiseResources(out, stdIn);
                        break;

                    case "shift_resources":
                        shiftResources(out, stdIn);
                        break;
                    case "choose_marbles":
                        chooseMarbles(out, stdIn);
                        break;
                    case "choose_leaderCards":
                        try {
                            chooseLeaderCard(out, stdIn);
                        }catch (NullPointerException e){
                            System.err.println("you can't do this action");
                        }

                        break;
                    case "insert_in_warehouse":
                        insertInWarehouse(out, stdIn);
                        break;
                    case "buy_card":
                        buyCard(out, stdIn);
                        break;
                    case "select_position":
                        selectPosition(out, stdIn);
                        break;
                    case "select_resources_from_warehouse":
                        selectResourcesFromWarehouse(out, stdIn);
                        break;
                    case "production":
                        send(out, new ProductionCommand());
                        break;
                    case "basic_production":
                        basicProduction(out, stdIn);
                        break;
                    case "normal_production":
                        normalProduction(out, stdIn);
                        break;
                    case "leader_production":
                        leaderProduction(out, stdIn);
                        break;
                    case "end_production":
                        send(out, new EndProductionCommand());
                        break;
                    case "leader_action":
                        send(out, new LeaderCommand());
                        break;
                    case "activate_card":
                        activateCard(out,stdIn );
                        break;
                    case "discard_card":
                        discardCard(out,stdIn );
                        break;
                    case "end_turn":
                        send(out, new EndTurnCommand());
                        break;
                    case "quit":
                        send(out, new QuitCommand());
                        break;
                    default:
                        System.out.println("Comando non riconosciuto");
                        System.out.println("possible commands" + possibleCommands);
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
                synchronized (this){
                    response = gson.fromJson(received, ResponseToClient.class);

                    response.updateClient(this);


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

    private void initialiseResources(PrintWriter out, BufferedReader stdIn) throws IOException {
        String firstResource;
        String secondResource;
        CollectionResources initialisingResources = new CollectionResources();
        //jsonCommand.initialisingResources = new CollectionResources();
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
                initialisingResources.add(convertResource(firstResource));
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
                initialisingResources.add(convertResource(firstResource));
                initialisingResources.add(convertResource(secondResource));
                break;
        }

        send(out, new InitialiseResourcesCommand(initialisingResources));
    }

    private void initialiseLeaderCard(PrintWriter out, BufferedReader stdIn) throws IOException {
        System.out.println("Scrivere la prima leader card da voler scartare");
        String firstCard = stdIn.readLine();
        int firstCard1;
        int secondCard1;
        try {
            firstCard1 = Integer.parseInt(firstCard);
        }catch (NumberFormatException e){
            System.err.println("Devi inserire un numero valido");
            return;
        }
        System.out.println("Scrivere la seconda leader card da voler scartare");
        String secondCard = stdIn.readLine();
        try {
            secondCard1 = Integer.parseInt(secondCard);
        }catch (NumberFormatException e){
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, new InitialiseLeaderCardsCommand(firstCard1, secondCard1));
    }

    private void shiftResources(PrintWriter out, BufferedReader stdIn) throws IOException {
        System.out.println("Scrivere il primo shelf in cui voler fare lo shift");
        String shelf = stdIn.readLine();
        int source, destination;
        try {
            source = Integer.parseInt(shelf);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        System.out.println("Scrivere il secondo shelf in cui voler fare lo shift");
        shelf = stdIn.readLine();
        try {
            destination = Integer.parseInt(shelf);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, new ShiftResourcesCommand(source, destination));
    }

    private void chooseMarbles(PrintWriter out, BufferedReader stdIn) throws IOException {
        System.out.println("Scrivere da dove comprare [row/column]");
        String dimension = stdIn.readLine();
        if (!dimension.equals("row") && !dimension.equals("column")){
            System.out.println("Non hai inserito valori validi");
            return;
        }


        System.out.println("Scrivere l'indice da cui comprare le biglie nel mercato");
        String index = stdIn.readLine();
        int index1;
        try {
            index1 = Integer.parseInt(index);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }

        send(out, new ChooseMarblesCommand(dimension, index1));
    }

    private void chooseLeaderCard(PrintWriter out, BufferedReader stdIn) throws IOException {
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

        send(out, new ChooseLeaderCardsCommand(indexes));
    }

    private void insertInWarehouse(PrintWriter out, BufferedReader stdIn) throws IOException {
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
        send(out, new InsertInWarehouseCommand(shelves));
    }

    private boolean isValidColor(String color){
        List<String> colors = new ArrayList<>(Arrays.asList("GREEN", "YELLOW", "PURPLE", "BLUE"));
        return colors.contains(color);
    }

    private CardColor convertColor(String color){
        return ResourceType.valueOf(CardColor.class, color);
    }

    private void buyCard(PrintWriter out, BufferedReader stdIn) throws IOException {
        System.out.println("Scrivere il colore della carta da comprare");
        String color = stdIn.readLine().toUpperCase();
        if (!isValidColor(color)){
            System.err.println("Colore scelto non valido");
            return;
        }
        System.out.println("Scrivere il livello della carta da comprare");
        String level = stdIn.readLine();
        int level1;
        try {
            level1 = Integer.parseInt(level);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, new BuyCardAction(convertColor(color), level1));
    }

    private void selectPosition(PrintWriter out, BufferedReader stdIn) throws IOException {
        System.out.println("Scrivere in quale spazio voler piazzare la carta [1, 2, 3]");
        String dashboardPosition = stdIn.readLine();
        int dashboardPosition1;
        try {
            dashboardPosition1 = Integer.parseInt(dashboardPosition);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, new SelectPositionCommand(dashboardPosition1));
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

    private void selectResourcesFromWarehouse(PrintWriter out, BufferedReader stdIn) throws IOException {
        CollectionResources toPayFromWarehouse = createCollectionResources(stdIn, "Scegliere risorsa da prendere dal warehouse");
        send(out, new SelectResourcesFromWarehouseCommand(toPayFromWarehouse));
    }

    private void basicProduction(PrintWriter out, BufferedReader stdIn) throws IOException {
        String output;
        CollectionResources toPayFromWarehouse = createCollectionResources(stdIn, "Scegliere risorsa da prendere dal warehouse");
        CollectionResources toPayFromStrongbox = createCollectionResources(stdIn, "Scegliere risorsa da prendere dallo strongbox");
        System.out.println("Scrivere la risorsa da voler ottenere come output della basic production[coin, stone, shield, servant]");
        output = stdIn.readLine().toUpperCase();
        if(invalidResource(output)){
            System.out.println("Risorsa non valida");
            return;
        }
        Resource output1 = convertResource(output);
        send(out, new BasicProductionCommand(toPayFromWarehouse, toPayFromStrongbox, output1));
    }

    private void normalProduction (PrintWriter out, BufferedReader stdIn) throws IOException {
        System.out.println("Scrivere quale tra le produzioni normali possibili scegliere [1, 2, 3]");
        String position = stdIn.readLine();
        int position1;
        try {
            position1 = Integer.parseInt(position);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        CollectionResources toPayFromWarehouse = createCollectionResources(stdIn, "Scegliere risorsa da prendere dal warehouse");
        send(out, new NormalProductionCommand(position1, toPayFromWarehouse));
    }

    private boolean isValidBoolean(String decision){
        return new ArrayList<>(Arrays.asList("yes", "no")).contains(decision);
    }

    private boolean convertBoolean(String decision){
        return decision.equals("yes");
    }

    private void leaderProduction (PrintWriter out, BufferedReader stdIn) throws IOException {
        System.out.println("Scrivere quale tra le produzioni leader possibili scegliere [1, 2]");
        String position = stdIn.readLine();
        int position1;
        try {
            position1 = Integer.parseInt(position);
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
        boolean fromWarehouse = convertBoolean(decision);

        System.out.println("Scrivere la risorsa da voler ottenere come output della basic production[coin, stone, shield, servant]");
        String output = stdIn.readLine().toUpperCase();
        if(invalidResource(output)){
            System.out.println("Risorsa non valida");
            return;
        }
        Resource output1 = convertResource(output);

        send(out, new LeaderProductionCommand(position1, fromWarehouse, output1));
    }

    private void activateCard(PrintWriter out, BufferedReader stdIn) throws IOException {
        System.out.println("Scrivere quale carta leader attivare [1, 2]: ");
        String toActivate = stdIn.readLine();
        int toActivate1;
        try {
            toActivate1 = Integer.parseInt(toActivate);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, new ActivateCardCommand(toActivate1));
    }

    private void discardCard(PrintWriter out, BufferedReader stdIn) throws IOException {
        System.out.println("Scrivere quale carta leader scartare [1, 2]: ");
        String toDiscard = stdIn.readLine();
        int toDiscard1;
        try {
            toDiscard1 = Integer.parseInt(toDiscard);
        }catch (NumberFormatException e) {
            System.err.println("Devi inserire un numero valido");
            return;
        }
        send(out, new DiscardCardCommand(toDiscard1));
    }

    public static List<LeaderCard> createLeaderCards(){
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

    /*
     * this method build the leader cards starting from the victory points and the resource associated
     * @param leaderCard this is the leader card to recreate
     * @return the card
     */

    public void show(){

        System.out.println(Arrays.deepToString(cardsMarket) + "\n");
        System.out.println(myself + "\n");
        System.out.println(opponents + "\n");
        System.out.println(Arrays.deepToString(marbleMarket) + "\n");
        System.out.println(lonelyMarble + "\n");
        System.out.println(solotoken + "\n");
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<LeaderCard> getAllLeaderCards() {
        return allLeaderCards;
    }

    public void setPossibleCommands(List<String> possibleCommands) {
        this.possibleCommands = possibleCommands;
    }

    public int getMarbles() {
        return marbles;
    }

    public void setMarbles(int marbles) {
        this.marbles = marbles;
    }

    public void setCardsMarket(DevelopmentCard[][] cardsMarket) {
        this.cardsMarket = cardsMarket;
    }

    public void setLonelyMarble(Marble lonelyMarble) {
        this.lonelyMarble = lonelyMarble;
    }

    public void setMarbleMarket(Marble[][] marbleMarket) {
        this.marbleMarket = marbleMarket;
    }

    public void setMyself(ThinPlayer myself) {
        this.myself = new ThinPlayer(myself);
    }

    public void setOpponents(List<ThinPlayer> opponents) {
        this.opponents = opponents;
    }

    public void setSoloToken(SoloToken solotoken) {
        this.solotoken = solotoken;
    }

    public ThinPlayer getMyself() {
        return myself;
    }

    public List<ThinPlayer> getOpponents() {
        return opponents;
    }

    public void setGainedFromMarbleMarket(List<Resource> gainedFromMarbleMarket) {
        this.gainedFromMarbleMarket = gainedFromMarbleMarket;
    }

    public DevelopmentCard[][] getCardsMarket() {
        return cardsMarket;
    }
}