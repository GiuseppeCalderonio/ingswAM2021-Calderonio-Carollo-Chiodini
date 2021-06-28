package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseMarblesCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.NewWhiteMarble;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * this class represent the Choose Marbles Controller
 */
public class ChooseMarblesController extends TurnsController {


    public ChooseMarblesController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, boolean leaderAction) {
        super(model, nickname, clientNetworkUser, true , leaderAction);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setMainWindowSize();

        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        showMarbleMarket(getModel().getGame().getMarbleMarket(), getModel().getGame().getLonelyMarble());
        setMarbleMarketOpacity(1);
        createButtons();

        showRollbackButton();



    }

    /**
     * this method shows the buttons
     */
    private void createButtons(){

        double layoutXRow = getMainWindow().getPrefWidth() * 6 / 10;
        double layoutYRow = getMainWindow().getPrefHeight() * 45 / 100;

        double layoutXColumn = getMainWindow().getPrefWidth() * 46 / 100;
        double layoutYColumn = getMainWindow().getPrefHeight() * 63 / 100;


        double offsetX = getMainWindow().getPrefWidth() * 2 / 100;
        double offsetY = getMainWindow().getPrefHeight() * 5 / 100;

        Button firstRow = super.setButton("1", actionEvent -> selectRow(1));
        firstRow.setLayoutX(layoutXRow);
        firstRow.setLayoutY(layoutYRow);

        Button secondRow = super.setButton("2", actionEvent -> selectRow(2));
        secondRow.setLayoutX(layoutXRow);
        secondRow.setLayoutY(layoutYRow + offsetY);

        Button thirdRow = super.setButton("3", actionEvent -> selectRow(3));
        thirdRow.setLayoutX(layoutXRow);
        thirdRow.setLayoutY(layoutYRow + 2 * offsetY);

        Button firstColumn = super.setButton("1", actionEvent -> selectColumn(1));
        firstColumn.setLayoutX(layoutXColumn);
        firstColumn.setLayoutY(layoutYColumn);

        Button secondColumn = super.setButton("2", actionEvent -> selectColumn(2));
        secondColumn.setLayoutX(layoutXColumn + offsetX);
        secondColumn.setLayoutY(layoutYColumn);

        Button thirdColumn = super.setButton("3", actionEvent -> selectColumn(3));
        thirdColumn.setLayoutX(layoutXColumn + 2 * offsetX);
        thirdColumn.setLayoutY(layoutYColumn);

        Button fourthColumn = super.setButton("4", actionEvent -> selectColumn(4));
        fourthColumn.setLayoutX(layoutXColumn + 3 * offsetX);
        fourthColumn.setLayoutY(layoutYColumn);

        getMainWindow().getChildren().add(firstRow);
        getMainWindow().getChildren().add(secondRow);
        getMainWindow().getChildren().add(thirdRow);
        getMainWindow().getChildren().add(firstColumn);
        getMainWindow().getChildren().add(secondColumn);
        getMainWindow().getChildren().add(thirdColumn);
        getMainWindow().getChildren().add(fourthColumn);
    }

    /**
     * this method shows the rollback button
     */
    private void showRollbackButton(){
        Button rollback = setButton("rollback the action", (actionEvent -> rollBack()));
        rollback.setLayoutX(getMainWindow().getPrefWidth() / 5);
        rollback.setLayoutY( getMainWindow().getPrefHeight() / 3);

        getMainWindow().getChildren().add(rollback);
    }

    /**
     * this method select the row of the marble market
     * @param row this is the row of the marble market
     */
    public void selectRow(int row){
        sendNewCommand(new ChooseMarblesCommand("row", row));

    }

    /**
     * this method select the column of the marble market
     * @param column this is the column of the marble market
     */
    public void selectColumn(int column){
        sendNewCommand(new ChooseMarblesCommand("column", column));
    }


    @Override
    public void update() {
        if ( getModel().getGainedFromMarbleMarket().isEmpty() ){
            Gui.setRoot("/TurnsWindow", new TurnsController(getModel(), getNickname(), getClientNetworkUser(), false, getLeaderAction()));

        }
        else if (getModel().getGame().getMyself().getLeaderCards().size() == 2 &&
                        getModel().getGame().getMyself().getLeaderCards().stream().allMatch( leaderCard -> leaderCard instanceof NewWhiteMarble) &&
                        getModel().getGame().getMyself().getLeaderCards().stream().allMatch(LeaderCard::isActive) &&
                        getModel().getMarbles() > 0){

              Gui.setRoot("/ChooseLeaderCardsWindow", new ChooseLeaderCardsController(getModel(), getNickname(), getClientNetworkUser(), getLeaderAction()));
        }

        else {
            Gui.setRoot("/InsertInWarehouseWindow", new InsertInWarehouseController(getModel(), getNickname(), getClientNetworkUser(), getLeaderAction()));
        }
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
}
