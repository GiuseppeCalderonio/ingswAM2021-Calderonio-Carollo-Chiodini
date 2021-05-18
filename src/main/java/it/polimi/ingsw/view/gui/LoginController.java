package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.LoginCommand;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    public TextField nickname;

    @FXML
    private void switchToPrimary() throws IOException {
        Gui.setRoot("/InitialisingWindow");
    }

    @FXML
    public void sendNickname(ActionEvent actionEvent) {

        Gui.setCommand(new LoginCommand(nickname.getText()));

    }
}