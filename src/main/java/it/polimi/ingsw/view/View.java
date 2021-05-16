package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.view.thinModelComponents.ThinGame;

import java.io.IOException;

public interface View {

    Command createCommand() throws IOException;

    void showContextAction(Command command, Status message);

    void showWelcomeMessage();

    void show(ThinGame game);

    void showErrorMessage(Exception e);

}
