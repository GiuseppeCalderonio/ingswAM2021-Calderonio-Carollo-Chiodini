package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.LoginCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController implements GuiController {

    @FXML
    private Label errorMessage = new Label();

    @FXML
    private TextField nickname;


    private NetworkUser<Command, ResponseToClient> networkUser;

    public LoginController(NetworkUser<Command, ResponseToClient> networkUser){
        this.networkUser = networkUser;
    }


    @FXML
    public void sendNickname() {

        if (nickname.getText().equals("")){
            setEmptyNicknameErrorMessage();
            return;
        }

        if (nickname.getText().equals(new Player().getNickname())){
            setLorenzoStringErrorMessage();
            return;
        }
        sendNewCommand(new LoginCommand(nickname.getText()));

    }

    @Override
    public void update(){
        Gui.setRoot("/WaitingWindow", new WaitingController("Now wait for other players to join"));
    }

    @Override
    public void showErrorMessage() {
        this.errorMessage.setText("nickname already taken, choose another one");
    }

    @Override
    public void sendNewCommand(Command toSend) {
        networkUser.send(toSend);
    }


    private void setEmptyNicknameErrorMessage(){
        this.errorMessage.setText("choose a nickname!");
    }

    private void setLorenzoStringErrorMessage(){
        this.errorMessage.setText("you can't use this nickname!");
    }
}