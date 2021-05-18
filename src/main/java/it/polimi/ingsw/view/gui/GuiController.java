package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.CommandName;

import java.io.IOException;

public interface GuiController {


    public void update(CommandName name) throws IOException;

}
