package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.SetSizeCommand;
import javafx.fxml.FXML;

import java.io.IOException;

public class SetSizeController {

    @FXML
    private void switchToSecondary() throws IOException {
        Gui.setRoot("/LoginWindow");
    }

    @FXML
    public void setSizeOne() {

        Gui.setCommand(new SetSizeCommand(1));
        System.out.println("1");
    }

    @FXML
    public void SetSizeTwo() {
        Gui.setCommand(new SetSizeCommand(2));

    }

    @FXML
    public void SetSizeThree() {
        Gui.setCommand(new SetSizeCommand(3));

    }

    @FXML
    public void SetSizeFour() {
        Gui.setCommand(new SetSizeCommand(4));

    }
}
