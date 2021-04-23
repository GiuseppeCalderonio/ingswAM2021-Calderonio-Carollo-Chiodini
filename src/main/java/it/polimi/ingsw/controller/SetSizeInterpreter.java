package it.polimi.ingsw.controller;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class SetSizeInterpreter implements CommandInterpreter{


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
        if (!command.cmd.equals("set_players"))
            return "{ \"message\" : \"this command is not available in this phase of the game\", \"possibleCommands\" : " + new ArrayList<String>(Collections.singletonList("set_players")) + "}";
        if (command.size < 1 || command.size > 4)
            return "{ \"message\" : \"the size is not between 1 and 4\", \"possibleCommands\" : " + new ArrayList<String>(Collections.singletonList("set_players")) + "}";
        handler.setNumberOfPlayers(new AtomicInteger(command.size));
        return "{ \"message\" : \"ok, start with the login\", \"possibleCommands\" : " + new ArrayList<String>(Collections.singletonList("login")) + "}";
    }
}
