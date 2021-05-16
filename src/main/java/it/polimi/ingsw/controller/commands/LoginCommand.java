package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;

import java.util.List;

/**
 * this class represent the login command. Every player, once decided the
 * number of opponents, have to do a login, that consist into setting a nickname:
 * it must be different from "" and from every nickname of the players into the same lobby
 */
public class LoginCommand implements Command {

    /**
     * this attribute represent the nickname chosen from the player
     */
    private final String nickname;

    /**
     * this constructor create a login command, setting the cmd to "login" and
     * the attribute nickname
     * @param nickname this is the nickname to set
     */
    public LoginCommand(String nickname){
        this.nickname = nickname;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.LOGIN;
    }

    /**
     * this method return a string representing the error message
     * associated with the command
     *
     * @return a string representing the error message
     * associated with the command
     */
    @Override
    public String getErrorMessage() {
        return "login failed";
    }

    /**
     * this method return a string representing the confirm message
     * associated with the command
     *
     * @return a string representing the confirm message
     * associated with the command
     */
    @Override
    public String getConfirmMessage() {
        return "login completed, if you can see your leader cards, discard 2 of them, wait otherwise for other players to join";
    }

    /**
     * this method execute the command given in input,
     * returning a message that will be sent to the client.
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     * @param possibleCommands these are the possible commands to eventually modify
     * @param client this is the handler to notify in case of
     *                a internal state change
     * @param previousPossibleCommands these are the possible commands referred to the previous
     *                                 command, that eventually may be modified
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(List<CommandName> possibleCommands, ClientHandler client, List<CommandName> previousPossibleCommands){
        // if the nickname is the empty string
        if (nickname.equals(""))
            return errorMessage();
        // if exist another client in the game that have selected the same nickname
        if (client.getNicknames().contains(nickname))
            return errorMessage();
        // add the nickname to the list of nicknames
        client.addNickname(nickname);
        // set the nickname
        client.setNickname(nickname);
        // remove the login command from the possible ones
        possibleCommands.remove(CommandName.LOGIN);

        return acceptedMessage();

    }
}
