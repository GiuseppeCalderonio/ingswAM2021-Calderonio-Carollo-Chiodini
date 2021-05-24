package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.BuyCardAction;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class BuyCardController extends TurnsController{

    private DevelopmentCard selected;


    public BuyCardController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, boolean leaderAction) {
        super(model, nickname, clientNetworkUser, true, leaderAction);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainWindowSize();
        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        showCardsMarket(getModel().getGame().getCardsMarket());
        setCardsMarketOpacity(1);
        createScenario();
    }

    private void createScenario(){

        Label contextAction = new Label("Click in the card that you want to buy");

        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( getMainWindow().getPrefWidth() - contextAction.getText().length(), getMainWindow().getPrefHeight() / 5 );

        getMainWindow().getChildren().add(contextAction);

        Button rollback = setButton("rollback the action", (actionEvent -> rollBack()));
        rollback.setLayoutX(getMainWindow().getPrefWidth() / 5);
        rollback.setLayoutY( getMainWindow().getPrefHeight() / 3);

        getMainWindow().getChildren().add(rollback);

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
    public void showCardsMarket(DevelopmentCard[][] cardsMarket ){

        getCardsMarket().getChildren().clear();

        for (int j = 0; j < 4; j++) {

            VBox cardColumn = new VBox();

            for (int i = 0; i < 3; i++) {

                ImageView card = new ImageView();
                card.setFitHeight(150);
                card.setFitWidth(100);

                try {
                    card.setImage(getCardImage(cardsMarket[i][j]));
                    int finalI = i;
                    int finalJ = j;
                    card.setOnMouseClicked((mouseEvent) -> buyCard(cardsMarket[finalI][finalJ]));

                } catch (NullPointerException e){
                    card.imageProperty().setValue(null);
                }

                cardColumn.getChildren().add(card);
            }
            getCardsMarket().getChildren().add(cardColumn);
            centerBox(getCardsMarket());
            setCardsMarketOpacity(0);

        }
    }


    private void buyCard(DevelopmentCard selected){
        this.selected = selected;
        sendNewCommand(new BuyCardAction(selected.getColor(), selected.getLevel()));
    }

    @Override
    public void update() {
        Gui.setRoot("/SelectPositionWindow", new SelectPositionController(getModel(), getNickname(), getClientNetworkUser(), selected, getLeaderAction()));
    }

    @Override
    public void showErrorMessage() {
        super.showErrorMessage();
        rollBack();

    }
}
