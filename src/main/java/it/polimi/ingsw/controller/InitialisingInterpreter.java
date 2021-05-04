package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitialisingInterpreter implements CommandInterpreter{

    private List<String> possibleCommands;

    private int firstCard, secondCard;

    public InitialisingInterpreter(){
        possibleCommands = new ArrayList<>(Collections.singletonList("initialise_leaderCards"));

    }


    /**
     * this method execute the command given in input, returning a code that will
     * be sent to the client associated with the handler
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     *
     * @param command this is the command to execute
     * @param client this is the handler to notify in case of
     *                a internal state change
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(Command command, ClientHandler client) {
        if (command.getCmd().equals("quit"))
            throw new QuitException();
        if (!possibleCommands.contains(command.getCmd()))
            return buildResponse("this command is not available in this phase of the game");

        return command.executeCommand(possibleCommands,client, new ArrayList<>());
    }

    public List<String> getPossibleCommands() {
        return possibleCommands;
    }

    /**
     * this method set the possible commands to the value passed as parameter
     *
     * @param possibleCommands this is the new list to set
     */
    @Override
    public void setPossibleCommands(List<String> possibleCommands) {
        this.possibleCommands = possibleCommands;
    }

    /**
     * this method return the enum associated with the phase of the game
     * for loginInterpreter, it returns LOGIN, for initialisingInterpreter, it returns INITIALISING,
     * for turnsInterpreter, it returns TURNS
     *
     * @return the enum associated with the phase of the game
     */
    @Override
    public GamePhase getGamePhase() {
        return GamePhase.INITIALISING;
    }

    private ResponseToClient buildResponse(String message){
        ResponseToClient response = new ResponseToClient();
        response.message = message;
        response.possibleCommands = possibleCommands;
        return response;
    }

    /**
     * this method get the index relative to the first card that the player want to discard during
     * the initialising game phase.
     * in particular, return the right index when the interface is implemented by the class InitialisingInterpreter,
     * return 0 otherwise
     * @return the index relative to the first card that the player want to discard
     */
    @Override
    public int getFirstLeaderCard() {
        return firstCard;
    }

    /**
     * this method set the index relative to the first card that the player want to discard during
     * the initialising game phase.
     * in particular, set the right index when the interface is implemented by the class InitialisingInterpreter,
     * do nothing otherwise
     *
     * @param firstCard this is the index relative to the first card that the player want to discard
     */
    @Override
    public void setFirstLeaderCard(int firstCard) {
        this.firstCard = firstCard;
    }

    /**
     * this method get the index relative to the second card that the player want to discard during
     * the initialising game phase.
     * in particular, return the right index when the interface is implemented by the class InitialisingInterpreter,
     * return 0 otherwise
     * @return the index relative to the second card that the player want to discard
     */
    @Override
    public int getSecondLeaderCard() {
        return secondCard;
    }

    /**
     * this method set the index relative to the second card that the player want to discard during
     * the initialising game phase.
     * in particular, set the right index when the interface is implemented by the class InitialisingInterpreter,
     * do nothing otherwise
     *
     * @param secondCard this is the index relative to the second card that the player want to discard
     */
    @Override
    public void setSecondLeaderCard(int secondCard) {
        this.secondCard = secondCard;
    }
}
