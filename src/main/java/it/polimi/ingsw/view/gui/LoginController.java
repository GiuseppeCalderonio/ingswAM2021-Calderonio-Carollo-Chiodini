package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.LoginCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.NetworkUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController implements GuiController{

    @FXML
    private TextField nickname;

    private NetworkUser<Command, ResponseToClient> networkUser;

    public LoginController(NetworkUser<Command, ResponseToClient> networkUser){
        this.networkUser = networkUser;
    }


    @FXML
    public void sendNickname(ActionEvent actionEvent) {

        networkUser.send(new LoginCommand(nickname.getText()));
        try {
            Gui.setRoot("/WaitingWindow", new WaitingController("Now wait for other players to join"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void update(CommandName name) throws IOException {

    }
}