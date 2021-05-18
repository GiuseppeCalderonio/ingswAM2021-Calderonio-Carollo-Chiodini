package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.SetSizeCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.NetworkUser;
import javafx.fxml.FXML;

import java.io.IOException;

public class SetSizeController implements GuiController{

    private NetworkUser<Command, ResponseToClient> networkUser;

    public SetSizeController(NetworkUser<Command, ResponseToClient> networkUser){
        this.networkUser = networkUser;
    }

    @FXML
    private void switchToLogin() throws IOException {
        Gui.setRoot("/LoginWindow", new LoginController(networkUser));
    }

    @FXML
    public void setSizeOne() {

        networkUser.send(new SetSizeCommand(1));
        System.out.println("1");
    }

    @FXML
    public void SetSizeTwo() {
        networkUser.send(new SetSizeCommand(2));
        System.out.println("2");
    }

    @FXML
    public void SetSizeThree() {
        networkUser.send(new SetSizeCommand(3));
        System.out.println("3");
    }

    @FXML
    public void SetSizeFour() {
        networkUser.send(new SetSizeCommand(4));
        System.out.println("4");
    }

    @Override
    public void update(CommandName name) throws IOException {
            switchToLogin();

    }
}
