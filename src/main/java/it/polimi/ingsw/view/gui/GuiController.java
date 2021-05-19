package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;

public interface GuiController {


    void update(CommandName name);

    default void showErrorMessage(String errorMessage){

    };

    void sendNewCommand(Command toSend);

}
