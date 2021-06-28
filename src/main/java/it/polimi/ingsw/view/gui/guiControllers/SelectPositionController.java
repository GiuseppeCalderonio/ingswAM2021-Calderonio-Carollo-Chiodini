package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.SelectPositionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class represent the select position controller
 */
public class SelectPositionController extends TurnsController{

    private final DevelopmentCard selected;

    public SelectPositionController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, DevelopmentCard selected, boolean leaderAction) {
        super(model, nickname, clientNetworkUser, true, leaderAction);
        this.selected = selected;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainWindowSize();

        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        showCard();
        createButtons();
    }

    /**
     * this method shows the card
     */
    private void showCard(){
        ImageView cardSelected = new ImageView(getCardImage(selected));
        cardSelected.setFitWidth(getCardWidth());
        cardSelected.setFitHeight(getCardHeight());
        centerImageView(cardSelected);


        getMainWindow().getChildren().add(cardSelected);
    }

    /**
     * this method create the useful buttons
     */
    private void createButtons(){

        double layoutX = getMainWindow().getPrefWidth() * 45 / 100;
        double layoutY = getMainWindow().getPrefHeight() * 80 / 100;

        double offsetX = getMainWindow().getPrefWidth() / 5;

        Button firstDeck = setButton("first deck", actionEvent -> getClientNetworkUser().send(new SelectPositionCommand(1)));
        firstDeck.setLayoutX(layoutX + 0 * offsetX);
        firstDeck.setLayoutY(layoutY);

        getMainWindow().getChildren().add(firstDeck);

        Button secondDeck = setButton("second deck", actionEvent -> getClientNetworkUser().send(new SelectPositionCommand(2)));
        secondDeck.setLayoutX(layoutX + 1 * offsetX);
        secondDeck.setLayoutY(layoutY);

        getMainWindow().getChildren().add(secondDeck);

        Button thirdDeck = setButton("third deck", actionEvent -> getClientNetworkUser().send(new SelectPositionCommand(3)));
        thirdDeck.setLayoutX(layoutX + 2 * offsetX);
        thirdDeck.setLayoutY(layoutY);

        getMainWindow().getChildren().add(thirdDeck);


    }

    @Override
    public void update() {
        Gui.setRoot("/SelectResourcesFromWarehouseWindow",
                new SelectResourcesFromWarehouseController(getModel(),
                        getNickname(),
                        getClientNetworkUser(),
                        selected,
                        getLeaderAction()));
    }

    @Override
    public void rollBack() {

        Gui.setRoot("/TurnsWindow",
                new TurnsController(getModel(),
                        getNickname(),
                        getClientNetworkUser(),
                        getNormalAction(),
                        getLeaderAction()));
    }

    @Override
    public void showErrorMessage() {
        super.showErrorMessage();
        rollBack();
    }
}
