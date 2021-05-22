package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseMarblesCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.LeaderCard.NewWhiteMarble;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ChooseMarblesController extends TurnsController{





    public ChooseMarblesController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser) {
        super(model, nickname, clientNetworkUser);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainWindowSize();
        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        showMarbleMarket(getModel().getGame().getMarbleMarket(), getModel().getGame().getLonelyMarble());
        setMarbleMarketOpacity(1);
    }

    public void firstRowSelection(ActionEvent actionEvent) {
        getClientNetworkUser().send(new ChooseMarblesCommand("row", 1));
    }

    public void secondRowSelection(ActionEvent actionEvent) {
        getClientNetworkUser().send(new ChooseMarblesCommand("row", 2));

    }

    public void thirdRowSelection(ActionEvent actionEvent) {
        getClientNetworkUser().send(new ChooseMarblesCommand("row", 3));
    }

    public void thirdColumnSelection(ActionEvent actionEvent) {
        getClientNetworkUser().send(new ChooseMarblesCommand("column", 3));
    }

    public void fourthColumnSelection(ActionEvent actionEvent) {
        getClientNetworkUser().send(new ChooseMarblesCommand("column", 4));
    }

    public void secondColumnSelection(ActionEvent actionEvent) {
        getClientNetworkUser().send(new ChooseMarblesCommand("column", 2));
    }

    public void firstColumnSelection(ActionEvent actionEvent) {
        getClientNetworkUser().send(new ChooseMarblesCommand("column", 1));
    }

    @Override
    public void update(CommandName name) {
        if ( getModel().getGame().getMyself().getLeaderCards().size() == 2 &&
                getModel().getGame().getMyself().getLeaderCards().stream().allMatch( leaderCard -> leaderCard instanceof NewWhiteMarble) ){
            // set scenario choose leader cards
        }

        else {
            // set scenario insert in warehouse
        }
    }
}
