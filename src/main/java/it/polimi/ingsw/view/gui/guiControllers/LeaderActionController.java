package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.leaderCommands.ActivateCardCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.DiscardCardCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.LeaderCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaderActionController extends TurnsController {

    @FXML
    private HBox leaderCardsHBox;

    private boolean firstUpdate = false;

    public LeaderActionController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, boolean normalActions) {
        super(model, nickname, clientNetworkUser, normalActions, true);
        getClientNetworkUser().send(new LeaderCommand());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setMainWindowSize();
        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();
        leaderCardsHBox.setLayoutX(super.getMainWindow().getPrefWidth()/2 -getMainWindow().getPrefWidth()/7);
        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        setLeaderCardsOpacity(1);

        if (!super.getLeaderCards().getChildren().isEmpty()){
            leftVBox.getChildren().add(super.getLeaderCards().getChildren().get(0));
            // if (super.getLeaderCards().getChildren().size()==1){
            rightVBox.getChildren().add(super.getLeaderCards().getChildren().get(0));
            // }
        }

        if (!getModel().getGame().getMyself().getLeaderCards().get(0).isActive()) {
            VBox leftButtons = new VBox();
            Button leftButtonDiscard = new Button();
            leftButtonDiscard.setOnAction(actionEvent -> leftDiscard());
            leftButtonDiscard.setText("discard");
            leftButtons.getChildren().add(leftButtonDiscard);
            leftButtons.setSpacing(5);
            Button leftButtonActive = new Button();
            leftButtonActive.setOnAction(actionEvent -> leftActive() );
            leftButtonActive.setText("active");
            leftButtons.getChildren().add(leftButtonActive);
            leftVBox.setSpacing(5);
            leftVBox.getChildren().add(leftButtons);
        }

        if(getModel().getGame().getMyself().getLeaderCards().size()==2) {
            if (!getModel().getGame().getMyself().getLeaderCards().get(1).isActive()) {
                VBox rightButtons = new VBox();
                Button rightButtonDiscard = new Button();
                rightButtonDiscard.setOnAction(actionEvent -> rightDiscard());
                rightButtonDiscard.setText("discard");
                rightButtons.getChildren().add(rightButtonDiscard);
                rightButtons.setSpacing(5);
                Button rightButtonActive = new Button();
                rightButtonActive.setOnAction(actionEvent -> rightActive());
                rightButtonActive.setText("active");
                rightButtons.getChildren().add(rightButtonActive);

                rightVBox.setSpacing(5);
                rightVBox.getChildren().add(rightButtons);
            }
        }
        leaderCardsHBox.getChildren().add(leftVBox);
        leaderCardsHBox.setSpacing(getMainWindow().getPrefWidth()/7);
        leaderCardsHBox.getChildren().add(rightVBox);
    }

    public void rightDiscard() {
        getClientNetworkUser().send(new DiscardCardCommand(2));
    }

    public void leftDiscard() {
        getClientNetworkUser().send(new DiscardCardCommand(1));
    }

    public void rightActive() {
        getClientNetworkUser().send(new ActivateCardCommand(2));
    }

    public void leftActive() {
        getClientNetworkUser().send(new ActivateCardCommand(1));
    }

    @Override
    public void update() {
        if (!firstUpdate){
            firstUpdate = true;
            return;
        }

        Gui.setRoot("/TurnsWindow",
                new TurnsController(getModel(),
                        getNickname(),
                        getClientNetworkUser(),
                        getNormalAction(),
                        false));
    }

    @Override
    public void showErrorMessage() {
        super.showErrorMessage();
        Gui.setRoot("/TurnsWindow",
                new TurnsController(getModel(),
                        getNickname(),
                        getClientNetworkUser(),
                        getNormalAction(),
                        true));
    }
}

