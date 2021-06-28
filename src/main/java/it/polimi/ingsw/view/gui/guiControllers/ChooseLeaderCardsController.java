package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseLeaderCardsCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * this method represent the Choose Leader Cards Controller
 */
public class ChooseLeaderCardsController extends TurnsController{

    @FXML
    private HBox leaderWhiteMarbles;
    @FXML
    private Label advice;

    private int whiteMarble = 1;

    private final List<Integer> selectedMarbles = new ArrayList<>();

    public ChooseLeaderCardsController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, boolean leaderAction) {
        super(model, nickname, clientNetworkUser, true, leaderAction);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setMainWindowSize();
        drawPlayer(getNickname());
        setPlayerOpacity(0.5);

        leaderWhiteMarbles.setLayoutX(super.getMainWindow().getPrefWidth()/2 -getMainWindow().getPrefWidth()/7);

        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();

        String pngNameConstant = "/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-";
        ImageView leftLeaderCard = new ImageView();
        leftLeaderCard.setImage(new Image(pngNameConstant + getModel().getGame().getMyself().getLeaderCards().get(0).getId() + "-1.png"));
        leftLeaderCard.setFitHeight(280);
        leftLeaderCard.setFitWidth(200);

        ImageView rightLeaderCard = new ImageView();
        rightLeaderCard.setImage(new Image(pngNameConstant + getModel().getGame().getMyself().getLeaderCards().get(1).getId() + "-1.png"));
        rightLeaderCard.setFitHeight(280);
        rightLeaderCard.setFitWidth(200);

        Button leftButton = new Button();
        leftButton.setOnAction( actionEvent -> chooseFirstMarble());
        leftButton.setPrefWidth(leftLeaderCard.getFitWidth());

        Button rightButton = new Button();
        rightButton.setOnAction( actionEvent -> chooseSecondMarble());
        rightButton.setPrefWidth(leftLeaderCard.getFitWidth());

        leftVBox.getChildren().add(leftLeaderCard);
        leftVBox.setSpacing(10);
        leftVBox.getChildren().add(leftButton);

        rightVBox.getChildren().add(rightLeaderCard);
        rightVBox.setSpacing(10);
        rightVBox.getChildren().add(rightButton);

        leaderWhiteMarbles.getChildren().add(leftVBox);
        leaderWhiteMarbles.setSpacing(30);
        leaderWhiteMarbles.getChildren().add(rightVBox);




        advice.setText("You picked "+ getModel().getMarbles()+ " white marbles from the marble market. Choose the the power from one" +
                " leader card to substitute the white marble " + whiteMarble );
        advice.setPrefWidth(10000);
        advice.setStyle("-fx-font: 22 arial;");
        advice.setTextFill(Color.BLACK);
        advice.setLayoutY(super.getMainWindow().getPrefHeight()/2 + super.getMainWindow().getPrefHeight()/12);
    }

    /**
     * this method choose the second marble
     */
    public void chooseSecondMarble() {
        selectedMarbles.add(2);
        whiteMarble++;

        if (whiteMarble == (getModel().getMarbles() +1))
            getClientNetworkUser().send(new ChooseLeaderCardsCommand( selectedMarbles.stream().mapToInt(Integer::intValue).toArray()));
    }

    /**
     * this method choose the first marble
     */
    public void chooseFirstMarble() {
        selectedMarbles.add(1);
        whiteMarble++;

        if (whiteMarble == (getModel().getMarbles() +1))
            getClientNetworkUser().send(new ChooseLeaderCardsCommand( selectedMarbles.stream().mapToInt(Integer::intValue).toArray()));

    }

    @Override
    public void update() {
        Gui.setRoot("/InsertInWarehouseWindow",
                new InsertInWarehouseController(getModel(),
                        getNickname(),
                        getClientNetworkUser(),
                        getLeaderAction()));
    }
}

