package it.polimi.ingsw.controller;

public class LoginInterpreter implements CommandInterpreter {

    /**
     * this method execute the command given in input, returning a code that will
     * be sent to the client associated with the handler
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     * @param command this is the command to execute
     * @param handler this is the handler to notify in case of
     *                a internal state change
     * @return a code based on the type of action
     */
    @Override
    public String executeCommand(Command command, EchoServerClientHandler handler) {
        if (!command.cmd.equals("login")) return "this command is not available in this phase of the game";
        if (command.nickname.equals("")) return "the nickname can't be the empty string";
        if (handler.getNicknames().contains(command.nickname)) return "nickname selected is already taken";
        handler.addNickname(command.nickname);
        handler.setNickname(command.nickname);
        return "ok, wait for other players to join";
    }
}
