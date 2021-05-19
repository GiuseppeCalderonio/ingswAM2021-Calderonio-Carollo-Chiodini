package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseLeaderCardsCommand;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseResourcesCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.network.NetworkUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InitialisisngController implements GuiController , Initializable {

    @FXML
    private Button choiceButton;

    @FXML
    private Label contextAction = new Label();

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
    private  ImageView firstChoice = new ImageView();

    @FXML
    private  ImageView secondChoice = new ImageView();

    @FXML
    private  ImageView thirdChoice = new ImageView();

    @FXML
    private  ImageView fourthChoice = new ImageView();

    private  List<LeaderCard> leaderCards;

    int position;

    private NetworkUser<Command, ResponseToClient> networkUser;


    public InitialisisngController(List<LeaderCard> leaderCards, int position, NetworkUser<Command, ResponseToClient> networkUser){

        this.position = position;
        this.leaderCards = leaderCards;
        this.networkUser = networkUser;
    }

    @Override
    public void update(CommandName name) {

    }

    @Override
    public void sendNewCommand(Command toSend) {
        Gui.setLastCommand(toSend);
        networkUser.send(toSend);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        firstChoice.setImage(new Image(leaderCards.get(0).getPng()));

        secondChoice.setImage(new Image(leaderCards.get(1).getPng()));

        thirdChoice.setImage(new Image(leaderCards.get(2).getPng()));

        fourthChoice.setImage(new Image(leaderCards.get(3).getPng()));

        contextAction.setText("these are your leader cards! Choose two of them to discard");
    }

    public void discardLeaderCards(ActionEvent actionEvent) {

        List<Integer> discarded = new ArrayList<>();

        addIfSelected(discarded, first.isSelected(), 1);
        addIfSelected(discarded, second.isSelected(), 2);
        addIfSelected(discarded, third.isSelected(), 3);
        addIfSelected(discarded, fourth.isSelected(), 4);

        if (discarded.size() == 2){
            sendNewCommand(new InitialiseLeaderCardsCommand(discarded.get(0), discarded.get(1)));

            if (position == 1){
                sendNewCommand(new InitialiseResourcesCommand(new CollectionResources()));
                Gui.setRoot("/WaitingWindow", new WaitingController("Wait for other player to initialize..."));
            }else {
                switchScenario();
            }

        }else {
            errorMessage.setOpacity(1);
        }


    }

    private void addIfSelected(List<Integer> checkBoxes, boolean checkBox, int toAdd){
        if (checkBox)
            checkBoxes.add(toAdd);
    }

    private void addIfSelected(CollectionResources checkBoxes, boolean checkBox, Resource toAdd){
        if (checkBox)
            checkBoxes.add(toAdd);
    }

    private void switchScenario(){
        firstChoice.setImage(new Image(new Coin().getPng()));
        secondChoice.setImage(new Image(new Stone().getPng()));
        thirdChoice.setImage(new Image(new Shield().getPng()));
        fourthChoice.setImage(new Image(new Servant().getPng()));
        contextAction.setText("choose " + resourcesToChoose() + " resources");
        errorMessage.setOpacity(0);
        errorMessage.setText("You can choose only " + resourcesToChoose() + " resources" );
        choiceButton.setText("gain");
        choiceButton.setOnAction(actionEvent -> chooseResources());
    }

    private int resourcesToChoose(){
        switch (position){
            case 2:
                return 1;
            case 3:
            case 4:
                return 2;
        }
        return 0;
    }

    void chooseResources(){

        CollectionResources resourcesGained = new CollectionResources();

        addIfSelected(resourcesGained, first.isSelected(), new Coin());
        addIfSelected(resourcesGained, second.isSelected(), new Stone());
        addIfSelected(resourcesGained, third.isSelected(), new Shield());
        addIfSelected(resourcesGained, fourth.isSelected(), new Servant());

        if (resourcesGained.getSize() == resourcesToChoose()){
            sendNewCommand(new InitialiseResourcesCommand(resourcesGained));
        }else {
            errorMessage.setOpacity(1);
        }
    }

}