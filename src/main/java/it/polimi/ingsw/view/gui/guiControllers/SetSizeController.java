package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.SetSizeCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class represent the set size controller
 */
public class SetSizeController implements GuiController, Initializable {

    private final NetworkUser<Command, ResponseToClient> networkUser;

    @FXML
    private AnchorPane mainWindow = new AnchorPane();



    public SetSizeController(NetworkUser<Command, ResponseToClient> networkUser){
        this.networkUser = networkUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainWindowSize();

        showButtons();
        showContextAction();

    }

    /**
     * this method set the main window size
     */
    private void setMainWindowSize(){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        mainWindow.setPrefSize(width, height);
        drawBackGround();
    }

    /**
     * this method draw the background
     */
    private void drawBackGround(){
        ImageView playerBoard = new ImageView(new Image("/board/Masters of Renaissance_PlayerBoard.png"));
        playerBoard.setFitWidth(mainWindow.getPrefWidth());
        playerBoard.setFitHeight(mainWindow.getPrefHeight() - 60);
        playerBoard.setPreserveRatio(false);
        playerBoard.setLayoutX(0);
        playerBoard.setLayoutY(mainWindow.getHeight() + 20);
        playerBoard.setOpacity(0.2);
        mainWindow.getChildren().add(playerBoard);
    }

    /**
     * this method shpws the useful buttons
     */
    private void showButtons(){

        double layoutX = mainWindow.getPrefWidth() / 3;
        double layoutY = mainWindow.getPrefHeight() * 40 / 100;

        double offsetX = mainWindow.getPrefWidth() / 5;
        double offsetY = mainWindow.getPrefHeight() / 5;

        Button firstButton = new Button("1");
        firstButton.setOnAction(actionEvent -> setSize(1));
        firstButton.setLayoutX(layoutX);
        firstButton.setLayoutY(layoutY);

        mainWindow.getChildren().add(firstButton);

        Button secondButton = new Button("2");
        secondButton.setOnAction(actionEvent -> setSize(2));
        secondButton.setLayoutX(layoutX + offsetX);
        secondButton.setLayoutY(layoutY);

        mainWindow.getChildren().add(secondButton);

        Button thirdButton = new Button("3");
        thirdButton.setOnAction(actionEvent -> setSize(3));
        thirdButton.setLayoutX(layoutX);
        thirdButton.setLayoutY(layoutY + offsetY);

        mainWindow.getChildren().add(thirdButton);

        Button fourthButton = new Button("4");
        fourthButton.setOnAction(actionEvent -> setSize(4));
        fourthButton.setLayoutX(layoutX + offsetX);
        fourthButton.setLayoutY(layoutY + offsetY);

        mainWindow.getChildren().add(fourthButton);


    }

    /**
     * this method shows the context action
     */
    private void showContextAction(){
        Label contextAction = new Label("Welcome! Decide the number of players for your game!");
        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( mainWindow.getPrefWidth() - contextAction.getText().length(), mainWindow.getPrefHeight() / 5 );
        mainWindow.getChildren().add(contextAction);
    }



    @FXML
    private void switchToLogin()  {
        Gui.setRoot("/LoginWindow", new LoginController(networkUser));
    }

    @FXML
    public void setSize(int size) {

        sendNewCommand(new SetSizeCommand(size));
        System.out.println("1");
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
