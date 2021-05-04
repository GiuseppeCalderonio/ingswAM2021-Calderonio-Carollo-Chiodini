package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;

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
        //super("login");
        this.nickname = nickname;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "login";
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
    public ResponseToClient executeCommand(List<String> possibleCommands, ClientHandler client, List<String> previousPossibleCommands){
        if (!possibleCommands.contains(getCmd()))
            return buildResponse("this command is not available in this phase of the game", possibleCommands);

        if (nickname.equals(""))
            return buildResponse("the nickname can't be the empty string", possibleCommands);

        if (client.getNicknames().contains(nickname))
            return buildResponse("nickname selected is already taken", possibleCommands);

        client.addNickname(nickname);
        client.setNickname(nickname);
        possibleCommands.remove("login");

        if (client.getNumberOfPlayers() == 1){
            return buildResponseIgnoringCommands("login completed successfully");
        }


        return buildResponse("login completed successfully, wait for other players to join", possibleCommands);
    }
}
