package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.LeaderProductionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
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

public class LeaderProductionController extends TurnsController{

    private final ImageView selected;
    private final int productionToActivate;
    private Button yesButton = new Button();
    private Button noButton = new Button();
    private boolean toPayFromWarehouse;
    private Label contextAction = new Label();



    public LeaderProductionController(ThinModel model,
                                      String nickname,
                                      NetworkUser<Command, ResponseToClient> clientNetworkUser,
                                      ImageView selected,
                                      int productionToActivate,
                                      boolean normalActions,
                                      boolean leaderActions) {
        super(model, nickname, clientNetworkUser, normalActions, leaderActions);
        this.selected = selected;
        this.productionToActivate = productionToActivate;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setMainWindowSize();
        getMainWindow().getChildren().add(yesButton);
        getMainWindow().getChildren().add(noButton);
        getMainWindow().getChildren().add(contextAction);

        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        showCard();
        showButtons();

    }

    private void showCard(){
        ImageView card = selected;
        card.setFitWidth(getCardWidth());
        card.setFitHeight(getCardHeight());

        card.setLayoutX(getMainWindow().getPrefWidth() * 3 / 4);
        card.setLayoutY(getMainWindow().getPrefHeight() * 1 / 4);

        getMainWindow().getChildren().add(card);
    }

    private void showButtons(){

        double relativeXOffset = getMainWindow().getWidth() / 5;

        yesButton.setText("yes");
        yesButton.setOnAction(actionEvent -> {
            toPayFromWarehouse = true;
            showResourcesToPay();
        });

        yesButton.setLayoutX(getMainWindow().getPrefWidth() / 2 - relativeXOffset);
        yesButton.setLayoutY(getMainWindow().getPrefHeight() * 3 / 5);


        noButton.setText("yes");
        noButton.setOnAction(actionEvent -> {
            toPayFromWarehouse = false;
            showResourcesToPay();
        });

        noButton.setLayoutX(getMainWindow().getPrefWidth() / 2 - relativeXOffset);
        noButton.setLayoutY(getMainWindow().getPrefHeight() * 3 / 5);



        contextAction.setText("You want to pay the resource from the warehouse?");

        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( getMainWindow().getPrefWidth() - contextAction.getText().length(), getMainWindow().getPrefHeight() / 5 );
    }

    private void showResourcesToPay(){

        yesButton = new Button();
        noButton = new Button();

        double layoutX = getMainWindow().getPrefWidth() * 1 / 4;
        double layoutY = getMainWindow().getPrefHeight() / 2 - getMainWindow().getPrefHeight() / 7;

        double offsetX = getMainWindow().getPrefWidth() / 10;

        int i = 0;

        for (Resource resource : new ArrayList<>(Arrays.asList(new Coin(), new Stone(), new Shield(), new Servant()))){
            ImageView resourceToDraw = new ImageView(getResourceImage(resource));
            resourceToDraw.setLayoutX(layoutX + i * offsetX);
            resourceToDraw.setLayoutY(layoutY);
            resourceToDraw.setOnMouseClicked( mouseEvent -> {
                getClientNetworkUser().send(new LeaderProductionCommand(productionToActivate, toPayFromWarehouse, resource));
            });

            getMainWindow().getChildren().add(resourceToDraw);


            i++;
        }

        contextAction.setText("Click on the resource that you want to gain as output");

        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( getMainWindow().getPrefWidth() - contextAction.getText().length(), getMainWindow().getPrefHeight() / 5 );

    }

    @Override
    public void showErrorMessage() {
        super.showErrorMessage();
        rollBack();
    }

    @Override
    public void rollBack() {
        Gui.setRoot("/ProductionWindow",
                new ProductionController(getModel(),
                        getNickname(),
                        getClientNetworkUser(),
                        getNormalAction(),
                        getLeaderAction()));
    }

    @Override
    public void update() {
        setNormalAction(false);
        Gui.setRoot("/ProductionWindow",
                new ProductionController(getModel(),
                        getNickname(),
                        getClientNetworkUser(),
                        false,
                        getLeaderAction()));
    }


}
