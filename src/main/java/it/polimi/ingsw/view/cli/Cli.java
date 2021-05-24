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

public class Cli implements View {

    private final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    private final CliCommandCreator commandCreator = new CliCommandCreator();

    private NetworkUser<Command, ResponseToClient> clientNetwork;

    private Command lastCommand = new UnknownCommand();

    private final ThinModel model = new ThinModel();

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


    protected void start() throws IOException {

        //Thread networkReader = new Thread(this);
        //networkReader.start();
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
            if (message.equals(Status.YOUR_TURN))
                System.out.println("now is your turn! decide your action");
            if (message.equals(Status.QUIT)){
                System.out.println("quit the match");
                System.exit(1);
            }
        }
    }

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

    @Override
    public void showCompleteGame() {
        showCli();
    }

    @Override
    public void showErrorMessage(Exception e) {
        System.err.println(e.getMessage());
    }

    @Override
    public ThinModel getModel() {
        return model;
    }

    @Override
    public void quit() {
        System.err.println("Disconnection...");
        System.exit(1);
    }

    @Override
    public void updateTurn(String ownerTurnNickname) {
        System.out.println("Now is the turn of " + ownerTurnNickname);
    }

    @Override
    public void showWinner(String winner, int victoryPoints) {
        System.out.println("the winner is " + winner + ", you gained: " + victoryPoints);
    }
}
