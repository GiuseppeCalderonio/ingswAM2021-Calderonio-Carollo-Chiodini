package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.ShiftResourcesCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * this class represent the Shift Resources Controller
 */
public class ShiftResourcesController extends TurnsController{

    /**
     * this attribute represent the list of useful buttons to use
     */
    private List<Node> buttons = new ArrayList<>();

    /**
     * this attribute represent the index of the first shelf selected
     */
    private int firstShelfSelected;

    /**
     * this attribute represent the context action to show
     */
    private Label contextAction = new Label();


    public ShiftResourcesController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, boolean normalAction, boolean leaderAction) {
        super(model, nickname, clientNetworkUser, normalAction, leaderAction);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainWindowSize();

        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        setWarehouseOpacity(1);
        drawScenario();
        showRollbackButton();
    }

    /**
     * this method draw the scenario
     */
    public void drawScenario(){


        showButtons();
        getMainWindow().getChildren().addAll(buttons);
        getMainWindow().getChildren().add(contextAction);
        showContextAction("Decide the first shelf for the shift");

    }

    /**
     * this method shows the context action
     * @param message this is the message to show
     */
    private void showContextAction(String message){
        contextAction.setText(message);
        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( getMainWindow().getPrefWidth() - contextAction.getText().length(), getMainWindow().getPrefHeight() / 5 );
    }

    /**
     * this method shows the buttons
     */
    private void showButtons(){

        buttons = new ArrayList<>();

        Button firstShelf = setButton("1", actionEvent -> selectedShelf(1));

        firstShelf.setLayoutX(getFirstShelf().getLayoutX() * 4 / 6);
        firstShelf.setLayoutY(getFirstShelf().getLayoutY());

        buttons.add(firstShelf);

        Button secondShelf = setButton("2", actionEvent -> selectedShelf(2));

        secondShelf.setLayoutX(getSecondShelf().getLayoutX() * 4 / 6);
        secondShelf.setLayoutY(getSecondShelf().getLayoutY());

        buttons.add(secondShelf);

        Button thirdShelf = setButton("3" , actionEvent -> selectedShelf(3));

        thirdShelf.setLayoutX(getThirdShelf().getLayoutX() * 4 / 6);
        thirdShelf.setLayoutY(getThirdShelf().getLayoutY());

        buttons.add(thirdShelf);

        if (getModel().getGame().getMyself().getWarehouse().getFourthShelf() == null){
            return;
        }

        Button fourthShelf = setButton("4", actionEvent -> selectedShelf(4));

        fourthShelf.setLayoutX(getFourthDepot().getLayoutX() - getFourthDepot().getLayoutX() * 1 / 10);
        fourthShelf.setLayoutY(getFourthDepot().getLayoutY());

        buttons.add(fourthShelf);

        if (getModel().getGame().getMyself().getWarehouse().getFifthShelf() == null){
            return;
        }
        Button fifthShelf = setButton("5", actionEvent -> selectedShelf(5));

        fifthShelf.setLayoutX(getFifthDepot().getLayoutX() - getFourthDepot().getLayoutX() * 1 / 10);
        fifthShelf.setLayoutY(getFifthDepot().getLayoutY());


        buttons.add(fifthShelf);
    }

    /**
     * this method handle the shelves selection
     * @param shelf this is the shelf selected
     */
    public void selectedShelf(int shelf){
        firstShelfSelected = shelf;
        showButtonsSecondTime();
        getMainWindow().getChildren().addAll(buttons);
        showContextAction("Decide the second shelf for the shift");


    }

    /**
     * this method shows the buttons for the second time
     */
    private void showButtonsSecondTime(){

        buttons = new ArrayList<>();

        Button firstShelf = setButton("1", actionEvent -> getClientNetworkUser().send(new ShiftResourcesCommand(firstShelfSelected, 1)));

        firstShelf.setLayoutX(getFirstShelf().getLayoutX() * 4 / 6);
        firstShelf.setLayoutY(getFirstShelf().getLayoutY());

        buttons.add(firstShelf);

        Button secondShelf = setButton("2", actionEvent -> getClientNetworkUser().send(new ShiftResourcesCommand(firstShelfSelected, 2)));

        secondShelf.setLayoutX(getSecondShelf().getLayoutX() * 4 / 6);
        secondShelf.setLayoutY(getSecondShelf().getLayoutY());

        buttons.add(secondShelf);

        Button thirdShelf = setButton("3" , actionEvent -> getClientNetworkUser().send(new ShiftResourcesCommand(firstShelfSelected, 3)));

        thirdShelf.setLayoutX(getThirdShelf().getLayoutX() * 4 / 6);
        thirdShelf.setLayoutY(getThirdShelf().getLayoutY());


        buttons.add(thirdShelf);

        if (getModel().getGame().getMyself().getWarehouse().getFourthShelf() == null){
            return;
        }

        Button fourthShelf = setButton("4", actionEvent -> getClientNetworkUser().send(new ShiftResourcesCommand(firstShelfSelected, 4)));

        fourthShelf.setLayoutX(getFourthDepot().getLayoutX() - getFourthDepot().getLayoutX() * 1 / 10);
        fourthShelf.setLayoutY(getFourthDepot().getLayoutY());

        buttons.add(fourthShelf);

        if (getModel().getGame().getMyself().getWarehouse().getFifthShelf() == null){
            return;
        }

        Button fifthShelf = setButton("5", actionEvent -> getClientNetworkUser().send(new ShiftResourcesCommand(firstShelfSelected, 5)));

        fifthShelf.setLayoutX(getFifthDepot().getLayoutX() - getFourthDepot().getLayoutX() * 1 / 10);
        fifthShelf.setLayoutY(getFifthDepot().getLayoutY());

        buttons.add(fifthShelf);

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

    @Override
    public void update() {

        Gui.setRoot("/TurnsWindow",
                new TurnsController(getModel(), getNickname(), getClientNetworkUser(), getNormalAction(), getLeaderAction()));
    }

    @Override
    public void showErrorMessage() {
        super.showErrorMessage();
        update();
    }

    @Override
    public void rollBack() {
        update();
    }
}
