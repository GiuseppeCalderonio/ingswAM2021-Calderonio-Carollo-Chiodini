package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.SetSizeCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.guiControllers.GuiController;
import it.polimi.ingsw.view.gui.guiControllers.LoginController;
import javafx.fxml.FXML;

public class SetSizeController implements GuiController {

    private NetworkUser<Command, ResponseToClient> networkUser;

    public SetSizeController(NetworkUser<Command, ResponseToClient> networkUser){
        this.networkUser = networkUser;
    }

    @FXML
    private void switchToLogin()  {
        Gui.setRoot("/LoginWindow", new LoginController(networkUser));
    }

    @FXML
    public void setSizeOne() {

        sendNewCommand(new SetSizeCommand(1));
        System.out.println("1");
    }

    @FXML
    public void SetSizeTwo() {
        sendNewCommand(new SetSizeCommand(2));
        System.out.println("2");
    }

    @FXML
    public void SetSizeThree() {
        sendNewCommand(new SetSizeCommand(3));
        System.out.println("3");
    }

    @FXML
    public void SetSizeFour() {
        sendNewCommand(new SetSizeCommand(4));
        System.out.println("4");
    }

    @Override
    public void update()  {
            switchToLogin();

    }

    @Override
    public void sendNewCommand(Command toSend) {
        networkUser.send(toSend);
    }
}
