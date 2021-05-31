package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.controller.commands.leaderCommands.LeaderCommand;
import it.polimi.ingsw.controller.commands.normalCommands.EndTurnCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.EndProductionCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.ProductionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.network.ClientNetwork;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.network.localGame.LocalClient;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.graphic.CharFigure;
import it.polimi.ingsw.view.cli.graphic.GraphicalGame;
import it.polimi.ingsw.view.cli.graphic.GraphicalInitializingLeaderCard;
import it.polimi.ingsw.view.cli.graphic.utilities.CharStream;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.ForeColor;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * this class represent the cli version of the gui, in fact
 * it implements it
 */
public class Cli implements View {

    /**
     * this attribute represent the buffer reader used to read strings from the input
     */
    private final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    /**
     * this attribute represent the cli command creator, to create commands
     * @see CliCommandCreator
     */
    private final CliCommandCreator commandCreator = new CliCommandCreator();

    /**
     * this attribute represent the network user of the game to handle network message
     * @see NetworkUser
     */
    private NetworkUser<Command, ResponseToClient> clientNetwork;

    /**
     * this attribute represent the last command sent from the client to the server
     * @see Command
     */
    private Command lastCommand = new UnknownCommand();

    /**
     * this attribute represent the thin model of the game
     * @see ThinModel
     */
    private final ThinModel model = new ThinModel();

    /**
     * this constructor create the object starting from the host name
     * and the port of the server to connect with.
     * in particular, it verify if the game is a single one or not,
     * then create the client network handler, and calls the method start
     * that is the only one used to handle the game, so this is the
     * only method to call to start a game
     * @param hostName this is the host name of the server to connect with
     * @param portNumber this is the port of the server to connect with
     */
    public Cli(String hostName, int portNumber){

        try {
            if (hostName != null || portNumber != 0){
                clientNetwork = new ClientNetwork(hostName, portNumber, this);
                //client.startNetwork(hostName, portNumber);
            }
            else {
                clientNetwork = new LocalClient(this);
            }
        } catch (IOException e){
            System.exit(1);
        }
        //this.client = client;
        showWelcomeMessage();

        try {
            start();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * this method is used to handle the cli client.
     * in particular, is a while true in which got stored the command sent to the server,
     * and send it to the server.
     * will be the network thread that will update the view
     * @throws IOException if a file/network error occurs
     */
    protected void start() throws IOException {
        // this while true won't work forever because the thread associated with the network receiver
        // will close the program if necessary
        while (true) {
            try {
                lastCommand = createCommand();
                clientNetwork.send(lastCommand);
            } catch (InvalidParameterException e){
                showErrorMessage(e);
            }
        }
    }

    /**
     * this method is used to generate a command.
     * in particular, the cli client have to write a command with the
     * buffer reader, and then the command will be sent to the server
     * @return the command to send to the server
     * @throws IOException if a file error occurs
     */
    private Command createCommand() throws IOException {

        String command;

        while ((command = stdIn.readLine()) != null) {

            switch (command){

                case "help":
                    commandCreator.showLegend();
                    break;

                case "show":
                    showCli();
                    break;
                case "setp" :
                    System.out.println("Write the number of players");
                    String numberOfPlayers = stdIn.readLine();
                    int numberOfPlayers1;
                    try {
                        numberOfPlayers1 = Integer.parseInt(numberOfPlayers);
                    }catch (NumberFormatException e){
                        System.err.println("number not valid");
                        break;
                    }
                    return new SetSizeCommand(numberOfPlayers1);
                case "l":
                    System.out.println("write your nickname");
                    return (new LoginCommand(stdIn.readLine()));
                case "il":
                    return commandCreator.initialiseLeaderCard(stdIn);

                case "ir":
                    return commandCreator.initialiseResources(stdIn, model.getPosition());

                case "sr":
                    return commandCreator.shiftResources(stdIn);
                case "cm":
                    return commandCreator.chooseMarbles(stdIn);

                case "cl":
                    try {
                        return commandCreator.chooseLeaderCard(stdIn, model.getMarbles());
                    }catch (NullPointerException e){
                        throw new InvalidParameterException("you can't do this action");
                    }
                case "iiw":
                    return commandCreator.insertInWarehouse(stdIn, model.getGainedFromMarbleMarket());
                case "bc":
                    return commandCreator.buyCard(stdIn);
                case "sp":
                    return commandCreator.selectPosition(stdIn);
                case "srfw":
                    return commandCreator.selectResourcesFromWarehouse(stdIn);
                case "p":
                    return new ProductionCommand();
                case "bp":
                    return commandCreator.basicProduction(stdIn);
                case "np":
                    return commandCreator.normalProduction(stdIn);
                case "lp":
                    return commandCreator.leaderProduction(stdIn);
                case "ep":
                    return new EndProductionCommand();
                case "la":
                    return new LeaderCommand();
                case "ac":
                    return commandCreator.activateCard( stdIn);
                case "dc":
                    return commandCreator.discardCard( stdIn);
                case "et":
                    return new EndTurnCommand();
                case "q":
                    return new QuitCommand();
                default:
                    return new UnknownCommand();
            }
        }

        return new UnknownCommand();
    }

    /**
     * this method is used only in the cli version of the gui,
     * it print all the game state.in particular, it is used
     * after some changes
     */
    @Override
    public void showCli(){
        try {
            int height = 53;
            if (model.getGame().getOpponents().size() <= 1)
                height = 41;

            int width = 155;
            CharStream console = new CharStream(width, height);
            for(int i=0; i<width; i++)
                for (int j=0; j<height; j++)
                    console.addColor(i,j, BackColor.ANSI_BRIGHT_BG_CYAN);
                GraphicalGame graphicalGame = new GraphicalGame(console, model.getGame());
                graphicalGame.draw();
                console.print(System.out);
                console.reset();
            } catch (NullPointerException e){

                System.err.println("error, this operation can be done only if the match is started");
            }

    }

    /**
     * this method is used to show the initialising phase.
     * in particular, after that every player do the login, he have
     * to select the leader cards and the initial resources
     * @param leaderCards these are the initial leader cards
     * @param position this is the position of the player, used
     *                 to manage the choice of the resources
     */
    @Override
    public void showInitialisingPhase(List<LeaderCard> leaderCards, int position) {

        int width = 180;
        int height = 30;

        // print the leader cards with ascii art
        CharStream console = new CharStream(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                console.addColor(i, j, BackColor.ANSI_BRIGHT_BG_BLACK);
            }
        }

        CharFigure graphicLeaderCards = new GraphicalInitializingLeaderCard(console, leaderCards);
        graphicLeaderCards.draw();
        console.print(System.out);
        console.reset();
    }

    /**
     * this method show the action associated with a specific
     * state of the game, as a suggestion or an error message
     * @param message this is the message to show
     */
    @Override
    public void showContextAction( Status message) {

        if (message != null){
            if(message.equals(Status.ACCEPTED))
                System.out.println(lastCommand.getConfirmMessage());
            if (message.equals(Status.REFUSED))
                System.out.println(lastCommand.getErrorMessage());
            if (message.equals(Status.ERROR))
                System.out.println("the command created an error on the server");
            if (message.equals(Status.WRONG_TURN))
                System.out.println("is not your turn!");
            if (message.equals(Status.QUIT)){
                System.out.println("quit the match");
                System.exit(1);
            }
        }
    }

    /**
     * this method show the welcome message of the game.
     * in particular, it print with the ascii art the
     * string "maestri del rinascimento"
     */
    private void showWelcomeMessage() {

        int width = 95;
        int height = 5;

        CharStream console = new CharStream(width, height);
        console.setMessage("MAESTRI DEL",0,0, ForeColor.ANSI_BRIGHT_YELLOW, BackColor.ANSI_BG_BLACK);
        console.print(System.out);
        console.reset();
        console = new CharStream(width, height);
        console.setMessage("RINASCIMENTO",0,0, ForeColor.ANSI_BRIGHT_YELLOW, BackColor.ANSI_BG_BLACK);
        console.print(System.out);
        console.reset();


        System.out.println("Welcome to the game, decide the number of players[1,2,3,4]\n" +
                "if you are playing a local single game instead, start directly with the login");
    }

    /**
     * this method show the game after that every player complete the initialization phase
     */
    @Override
    public void showCompleteGame() {
        showCli();
    }

    /**
     * this method show the error message whenever is present
     * @param e this is the exception trowed
     */
    @Override
    public void showErrorMessage(Exception e) {
        System.err.println(e.getMessage());
    }

    /**
     * this method get the thin model associated with the game
     * @return the thin model associated with the game
     */
    @Override
    public ThinModel getModel() {
        return model;
    }

    @Override
    public void quit() {
        System.err.println("Disconnection...");
        System.exit(1);
    }

    /**
     * this method is used to change the turn, after that a player finish it.
     * in particular, if is the turn of the player specified by the nickname,
     * the view will notify the player, do nothing otherwise (or eventually show the
     * nickname of the player that own the turn)
     * @param ownerTurnNickname this is the nickname of the player that now own the turn
     */
    @Override
    public void updateTurn(String ownerTurnNickname) {
        System.out.println("Now is the turn of " + ownerTurnNickname);
    }

    /**
     * this method show the winner of the game.
     * @param winner this is the nickname of the winner
     * @param victoryPoints these are the victory points of the winner
     */
    @Override
    public void showWinner(String winner, int victoryPoints) {
        System.out.println("the winner is " + winner + ", you gained: " + victoryPoints);
    }
}
