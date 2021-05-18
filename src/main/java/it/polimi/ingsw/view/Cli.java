package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.controller.commands.leaderCommands.LeaderCommand;
import it.polimi.ingsw.controller.commands.normalCommands.EndTurnCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.EndProductionCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.ProductionCommand;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.graphic.CharFigure;
import it.polimi.ingsw.view.graphic.GraphicalGame;
import it.polimi.ingsw.view.graphic.GraphicalInitializingLeaderCard;
import it.polimi.ingsw.view.thinModelComponents.ThinGame;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.List;

public class Cli implements View{

    private final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

    private final CliCommandCreator commandCreator = new CliCommandCreator();

    private final Client client;

    public Cli(Client client){
        this.client = client;
        showWelcomeMessage();

    }

    @Override
    public Command createCommand() throws IOException {

        String command;

        while ((command = stdIn.readLine()) != null) {

            System.out.println("Choose a command, or wait if you can't do anything");

            switch (command){

                case "help":
                    commandCreator.showLegend();
                    break;

                case "show":
                    client.show();
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
                    return commandCreator.initialiseResources(stdIn, client.getPosition());

                case "sr":
                    return commandCreator.shiftResources(stdIn);
                case "cm":
                    return commandCreator.chooseMarbles(stdIn);

                case "cl":
                    try {
                        return commandCreator.chooseLeaderCard(stdIn, client.getMarbles());
                    }catch (NullPointerException e){
                        throw new InvalidParameterException("you can't do this action");
                    }
                case "iiw":
                    return commandCreator.insertInWarehouse(stdIn, client.getGainedFromMarbleMarket());
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
    public void show(ThinGame game){
        try {
            int height = 53;
            if (game.getOpponents().size() <= 1)
                height = 41;
            CharStream console = new CharStream(200, height);
            for(int i=0; i<200; i++)
                for (int j=0; j<height; j++)
                    console.addColor(i,j, BackColor.ANSI_BRIGHT_BG_CYAN);
                GraphicalGame graphicalGame = new GraphicalGame(console, game);
                graphicalGame.draw();
                console.print(System.out);
                console.reset();
            } catch (NullPointerException e){

                System.err.println("error, this operation can be done only if the match is started");
            }

    }

    @Override
    public void showErrorMessage(Exception e) {
        System.err.println(e.getMessage());
    }

    @Override
    public void showInitialisingPhase(List<LeaderCard> leaderCards, int position) {
        // print the leader cards with ascii art
        CharStream console = new CharStream(200, 30);
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 60; j++) {
                console.addColor(i, j, BackColor.ANSI_BRIGHT_BG_BLACK);
            }
        }

        CharFigure graphicLeaderCards = new GraphicalInitializingLeaderCard(console, leaderCards);
        graphicLeaderCards.draw();
        console.print(System.out);
        console.reset();
    }

    @Override
    public void showContextAction(Command lastCommand, Status message) {

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

    @Override
    public void showWelcomeMessage() {
        CharStream console = new CharStream(200, 7);
        console.setMessage("MAESTRI DEL RINASCIMENTO",0,0, ForeColor.ANSI_BRIGHT_YELLOW, BackColor.ANSI_BG_BLACK);
        console.print(System.out);
        console.reset();

        System.out.println("Welcome to the game, decide the number of players[1,2,3,4]\n" +
                "if you are playing a local single game instead, start directly with the login");
    }
}
