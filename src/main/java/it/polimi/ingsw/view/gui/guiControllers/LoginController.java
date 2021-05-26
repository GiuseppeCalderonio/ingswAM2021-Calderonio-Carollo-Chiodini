package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.LoginCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginController implements GuiController, Initializable {

    @FXML
    private Label errorMessage = new Label();

    @FXML
    private TextField nickname;


    private NetworkUser<Command, ResponseToClient> networkUser;

    @FXML
    private AnchorPane mainWindow = new AnchorPane();

    public LoginController(NetworkUser<Command, ResponseToClient> networkUser){
        this.networkUser = networkUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainWindowSize();

        showTextField();
        showContextAction();
        mainWindow.getChildren().add(errorMessage);
        errorMessage.setLayoutX(mainWindow.getPrefWidth() / 5);
        errorMessage.setLayoutY(mainWindow.getPrefHeight() / 5);
        errorMessage.setTextFill(Color.RED);
    }

    private void setMainWindowSize(){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        mainWindow.setPrefSize(width, height);
        drawBackGround();
    }

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

    private void showTextField(){
        nickname = new TextField("Player " + (char) ('a' + new Random().nextInt(26)));

        nickname.setLayoutX(mainWindow.getPrefWidth() / 2 - mainWindow.getPrefWidth() / 12);
        nickname.setLayoutY(mainWindow.getPrefHeight() / 2);
        mainWindow.getChildren().add(nickname);

        Button login = new Button("Login");
        login.setLayoutX(mainWindow.getPrefWidth() / 2 - mainWindow.getPrefWidth() / 13);
        login.setLayoutY(mainWindow.getPrefHeight() * 4 / 10);
        login.setOnAction(actionEvent -> sendNickname());

        mainWindow.getChildren().add(login);
    }

    private void showContextAction(){
        Label contextAction = new Label("Choose a Nickname!");
        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( mainWindow.getPrefWidth() - contextAction.getText().length(), mainWindow.getPrefHeight() / 5 );
        mainWindow.getChildren().add(contextAction);
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