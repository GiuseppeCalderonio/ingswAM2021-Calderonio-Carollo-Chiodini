package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.BasicProductionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * this class represent the basic production controller
 */
public class BasicProductionController extends TurnsController{

    private final CollectionResources toPayFromWarehouse = new CollectionResources();
    private final CollectionResources toPayFromStrongbox = new CollectionResources();
    private Label contextAction = new Label();
    private Button payButton = new Button();
    private Pane resourcesScenario = new Pane();


    public BasicProductionController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, boolean normalActions, boolean leaderActions) {
        super(model, nickname, clientNetworkUser, normalActions, leaderActions);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setMainWindowSize();

        drawPlayer(getNickname());
        setPlayerOpacity(0.5);

        getMainWindow().getChildren().add(resourcesScenario);

        getMainWindow().getChildren().add(payButton);
        getMainWindow().getChildren().add(contextAction);
        showCard();
        showResourcesToPayFromWarehouse();
        showRollbackButton();

    }

    /**
     * this method shows the cards
     */
    private void showCard(){
        ImageView card = new ImageView(getBasicProductionImage());
        card.setFitWidth(getCardWidth());
        card.setFitHeight(getCardHeight());

        card.setLayoutX(getMainWindow().getPrefWidth() * 3 / 4);
        card.setLayoutY(getMainWindow().getPrefHeight() * 1 / 4);

        getMainWindow().getChildren().add(card);
    }

    /**
     * this method shows the 4 resources to pay from warehouse
     */
    private void showResourcesToPayFromWarehouse(){

        setResourcesImages(toPayFromWarehouse, resourcesScenario);

        payButton.setText("pay");
        payButton.setOnAction(actionEvent -> showResourcesToPayFromStrongbox());

        payButton.setLayoutX(getMainWindow().getPrefWidth() / 2 );
        payButton.setLayoutY(getMainWindow().getPrefHeight() * 3 / 5);

        contextAction.setText("Click in the resources that you want to pay from Warehouse");

        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( getMainWindow().getPrefWidth() - contextAction.getText().length(), getMainWindow().getPrefHeight() / 5 );

    }

    /**
     * this method shows the 4 resources to pay from strongbox
     */
    private void showResourcesToPayFromStrongbox(){

        setResourcesImages(toPayFromStrongbox, resourcesScenario);


        payButton.setOnAction(actionEvent -> showResourcesToGainAsOutput());

        payButton.setLayoutX(getMainWindow().getPrefWidth() / 2 );
        payButton.setLayoutY(getMainWindow().getPrefHeight() * 3 / 5);

        contextAction.setText("Click in the resources that you want to pay from Strongbox");

        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( getMainWindow().getPrefWidth() - contextAction.getText().length(), getMainWindow().getPrefHeight() / 5 );


    }

    /**
     * this method set the resources image
     * @param toPay this is the collectionResources to pay
     * @param resourcesScenario this is the pane with the resources
     */
    private void setResourcesImages(CollectionResources toPay, Pane resourcesScenario) {

        resourcesScenario.getChildren().clear();

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
                toPay.add(resource);
                resourcesSelected.setText("x" + toPay.asList().stream().filter(resource::equals).count());
            });

            resourcesScenario.getChildren().add(resourceToDraw);
            resourcesScenario.getChildren().add(resourcesSelected);

            i++;
        }


    }

    /**
     * this method shows the 4 resources to gain as output
     */
    private void showResourcesToGainAsOutput(){

        resourcesScenario.getChildren().clear();

        double layoutX = getMainWindow().getPrefWidth() * 1 / 4;
        double layoutY = getMainWindow().getPrefHeight() / 2 - getMainWindow().getPrefHeight() / 7;

        double offsetX = getMainWindow().getPrefWidth() / 10;

        int i = 0;

        for (Resource resource : new ArrayList<>(Arrays.asList(new Coin(), new Stone(), new Shield(), new Servant()))){
            ImageView resourceToDraw = new ImageView(getResourceImage(resource));
            resourceToDraw.setLayoutX(layoutX + i * offsetX);
            resourceToDraw.setLayoutY(layoutY);
            resourceToDraw.setOnMouseClicked( mouseEvent -> getClientNetworkUser().send(new BasicProductionCommand(toPayFromWarehouse, toPayFromStrongbox, resource)));

            getMainWindow().getChildren().add(resourceToDraw);

            i++;
        }

        payButton = new Button();

        contextAction.setText("Click in the resources that you want to gain as output");

        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( getMainWindow().getPrefWidth() - contextAction.getText().length(), getMainWindow().getPrefHeight() / 5 );

    }

    /**
     * this method shows the rollback button
     */
    private void showRollbackButton(){
        Button rollback = setButton("rollback the action", (actionEvent -> rollBack()));
        rollback.setLayoutX(getMainWindow().getPrefWidth() / 7);
        rollback.setLayoutY( getMainWindow().getPrefHeight() / 3);

        getMainWindow().getChildren().add(rollback);
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
