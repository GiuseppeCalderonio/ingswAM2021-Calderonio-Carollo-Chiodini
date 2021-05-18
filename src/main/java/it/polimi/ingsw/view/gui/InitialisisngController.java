package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseLeaderCardsCommand;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseResourcesCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.network.NetworkUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class InitialisisngController implements GuiController , Initializable {

    @FXML
    private final Pane paneView = new Pane();

    @FXML
    private Label errorMessage;

    @FXML
    private CheckBox fourth;

    @FXML
    private CheckBox third;

    @FXML
    private CheckBox second;

    @FXML
    private CheckBox first;

    @FXML
    private  ImageView firstLeaderCard = new ImageView();

    @FXML
    private  ImageView secondLeaderCard = new ImageView();

    @FXML
    private  ImageView thirdLeaderCard = new ImageView();

    @FXML
    private  ImageView fourthLeaderCard = new ImageView();

    private  List<LeaderCard> leaderCards;

    int position;

    private NetworkUser<Command, ResponseToClient> networkUser;


    public InitialisisngController(List<LeaderCard> leaderCards, int position, NetworkUser<Command, ResponseToClient> networkUser){

        this.position = position;
        this.leaderCards = leaderCards;
        this.networkUser = networkUser;
    }

    @Override
    public void update(CommandName name) throws IOException {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        firstLeaderCard.setImage(new Image(leaderCards.get(0).getPng()));

        secondLeaderCard.setImage(new Image(leaderCards.get(1).getPng()));

        thirdLeaderCard.setImage(new Image(leaderCards.get(2).getPng()));

        fourthLeaderCard.setImage(new Image(leaderCards.get(3).getPng()));
    }

    public void discardLeaderCards(ActionEvent actionEvent) {
        List<Integer> checkBoxes = new ArrayList<>();

        addIfSelected(checkBoxes, first.isSelected(), 1);
        addIfSelected(checkBoxes, second.isSelected(), 2);
        addIfSelected(checkBoxes, third.isSelected(), 3);
        addIfSelected(checkBoxes, fourth.isSelected(), 4);

        if (checkBoxes.size() == 2){
            networkUser.send(new InitialiseLeaderCardsCommand(checkBoxes.get(0), checkBoxes.get(1)));

            if (position == 1)
                networkUser.send(new InitialiseResourcesCommand(new CollectionResources()));

            return;
        }
            errorMessage.setOpacity(1);

    }

    private void addIfSelected(List<Integer> checkBoxes, boolean checkBox, int toAdd){
        if (checkBox)
            checkBoxes.add(toAdd);
    }

}
