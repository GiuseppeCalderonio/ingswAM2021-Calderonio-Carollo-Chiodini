package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.EndProductionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.NewProduction;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import it.polimi.ingsw.view.thinModelComponents.ThinProductionPower;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * this class represent the production controller
 */
public class ProductionController extends TurnsController{

    public ProductionController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, boolean normalAction,  boolean leaderAction) {
        super(model, nickname, clientNetworkUser, normalAction, leaderAction);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainWindowSize();
        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        showProductionsAvailable();

    }

    /**
     * this method shows the available productions
     */
    private void showProductionsAvailable(){
        // set the hBox in which place all the cards
        HBox productionsAvailable = new HBox();

        centerBox(productionsAvailable, getCardWidth() * 5 / 2 , getCardHeight() / 2 );

        addBasicProduction(productionsAvailable);

        addNormalProductions(productionsAvailable, getModel().getGame().getMyself().getProductionPower());

        addLeaderProductions(productionsAvailable, getModel().getGame().
                getMyself().getLeaderCards().stream().
                filter(leaderCard -> leaderCard.isActive() && leaderCard instanceof NewProduction).
                collect(Collectors.toList()));

        getMainWindow().getChildren().add(productionsAvailable);

        addEndProductionButton();

    }

    /**
     * this method adds the basic production to the available ones
     * @param graphicContainer this is the graphic container in which place the image
     */
    private void addBasicProduction(HBox graphicContainer){

        if (getModel().getGame().getMyself().isBasicProductionAffordable()){
            ImageView basicProduction = new ImageView(new Image("/board/BasicProduction.png"));
            basicProduction.setFitWidth(getCardWidth());
            basicProduction.setFitHeight(getCardHeight());
            basicProduction.setOnMouseClicked(mouseEvent -> Gui.setRoot("/BasicProductionWindow",
                    new BasicProductionController(getModel(),
                            getNickname(),
                            getClientNetworkUser(),
                            getNormalAction(),
                            getLeaderAction())));
            graphicContainer.getChildren().add(basicProduction);
        }


    }

    /**
     * this method adds the normal production to the available ones
     * @param graphicContainer this is the graphic container in which place the image
     * @param productionPower this is the thin production power of the player
     */
    private void addNormalProductions(HBox graphicContainer, ThinProductionPower productionPower){

        addCard(graphicContainer, productionPower.getProductionPower1(), 1);
        addCard(graphicContainer, productionPower.getProductionPower2(), 2);
        addCard(graphicContainer, productionPower.getProductionPower3(), 3);

    }

    /**
     * this method adds the available cards to show
     * @param graphicContainer this is the graphic container to show
     * @param deck this is the deck of cards to show
     * @param productionToActivate this is the production to activate
     */
    private void addCard(HBox graphicContainer, List<DevelopmentCard> deck, int productionToActivate){

        if (!deck.isEmpty() && getModel().getGame().getMyself().isNormalProductionAffordable(deck)){

            ImageView card = new ImageView(getCardImage(deck.get(deck.size() - 1)));
            card.setFitHeight(getCardHeight());
            card.setFitWidth(getCardWidth());
            card.setOnMouseClicked(mouseEvent -> Gui.setRoot("/NormalProductionWindow",
                    new NormalProductionController(getModel(),
                            getNickname(),
                            getClientNetworkUser(),
                            productionToActivate,
                            card,
                            getNormalAction(),
                            getLeaderAction())));

            graphicContainer.getChildren().add(card);
        }
    }

    /**
     * this method adds the leader production to the available ones
     * @param graphicContainer this is the graphic container in which place the image
     * @param leaderCards this is the list of leader cards
     */
    private void addLeaderProductions(HBox graphicContainer, List<LeaderCard> leaderCards){

        int i = 1;

        for (LeaderCard leaderCard : leaderCards){
            if (getModel().getGame().getMyself().isLeaderProductionAffordable(leaderCard)){
                ImageView card = new ImageView(getLeaderCardImage(leaderCard));

                card.setFitWidth(getCardWidth());
                card.setFitHeight(getCardHeight());
                int finalI = i;
                card.setOnMouseClicked(mouseEvent -> Gui.setRoot("/LeaderProductionWindow",
                        new LeaderProductionController(getModel(),
                                getNickname(),
                                getClientNetworkUser(),
                                card,
                                finalI,
                                getNormalAction(),
                                getLeaderAction())));

                graphicContainer.getChildren().add(card);
            }

            i++;
        }
    }

    /**
     * this method adds the end production button
     */
    private void addEndProductionButton(){

        Button endProduction = setButton("end production", actionEvent ->  {
            getClientNetworkUser().send(new EndProductionCommand());
            Gui.setRoot("/TurnsWindow",
                    new TurnsController(getModel(),
                            getNickname(),
                            getClientNetworkUser(),
                            getNormalAction(),
                            getLeaderAction()));
        } );
        endProduction.setLayoutX(getMainWindow().getPrefWidth() / 4);
        endProduction.setLayoutY(getMainWindow().getPrefHeight() / 3);

        getMainWindow().getChildren().add(endProduction);

    }

    @Override
    public void update() {

    }
}
