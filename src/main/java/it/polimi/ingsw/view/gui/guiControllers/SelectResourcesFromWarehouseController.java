package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.SelectResourcesFromWarehouseCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SelectResourcesFromWarehouseController extends TurnsController{

    private final DevelopmentCard selected;
    private final CollectionResources toPayFromWarehouse = new CollectionResources();

    public SelectResourcesFromWarehouseController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, DevelopmentCard selected, boolean leaderAction) {
        super(model, nickname, clientNetworkUser, true, leaderAction);
        this.selected = selected;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setMainWindowSize();

        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        showCard();
        showResourcesToPay();

    }

    private void showCard(){
        ImageView card = new ImageView(getCardImage(selected));
        card.setFitWidth(getCardWidth());
        card.setFitHeight(getCardHeight());

        card.setLayoutX(getMainWindow().getPrefWidth() * 3 / 4);
        card.setLayoutY(getMainWindow().getPrefHeight() * 1 / 4);

        getMainWindow().getChildren().add(card);
    }

    private void showResourcesToPay(){

        double layoutX = getMainWindow().getPrefWidth() * 1 / 4;
        double layoutY = getMainWindow().getPrefHeight() / 2 - getMainWindow().getPrefHeight() / 7;

        double offsetX = getMainWindow().getPrefWidth() / 10;

        int i = 0;

        for (Resource resource : new ArrayList<>(Arrays.asList(new Coin(), new Stone(), new Shield(), new Servant()))){



            ImageView resourceToDraw = new ImageView(getResourceImage(resource));
            resourceToDraw.setLayoutX(layoutX + i * offsetX);
            resourceToDraw.setLayoutY(layoutY);

            Label resourcesSelected = new Label("x0");
            resourcesSelected.setLayoutX(resourceToDraw.getLayoutX());
            resourcesSelected.setLayoutY(resourceToDraw.getLayoutY() + resourceToDraw.getFitHeight());

            resourceToDraw.setOnMouseClicked( mouseEvent -> {
                toPayFromWarehouse.add(resource);
                resourcesSelected.setText("x" + toPayFromWarehouse.asList().stream().filter(resource::equals).count());
            });

            getMainWindow().getChildren().add(resourceToDraw);
            getMainWindow().getChildren().add(resourcesSelected);


            i++;
        }

        Button payButton = setButton("pay", actionEvent -> getClientNetworkUser().send(new SelectResourcesFromWarehouseCommand(toPayFromWarehouse)));

        payButton.setLayoutX(getMainWindow().getPrefWidth() / 2 );
        payButton.setLayoutY(getMainWindow().getPrefHeight() * 3 / 5);

        getMainWindow().getChildren().add(payButton);

        Label contextAction = new Label("Click in the resource that you want to pay from warehouse");

        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( getMainWindow().getPrefWidth() - contextAction.getText().length(), getMainWindow().getPrefHeight() / 5 );

        getMainWindow().getChildren().add(contextAction);
    }

    @Override
    public void showErrorMessage() {
        super.showErrorMessage();
        rollBack();
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
    public void update() {
        Gui.setRoot("/TurnsWindow",
                new TurnsController(getModel(),
                        getNickname(),
                        getClientNetworkUser(),
                        false,
                        getLeaderAction()));
    }
}
