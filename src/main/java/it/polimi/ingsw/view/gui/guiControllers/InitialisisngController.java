package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseLeaderCardsCommand;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseResourcesCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * this class represent the initialising controller
 */
public class InitialisisngController implements GuiController, Initializable {

    @FXML
    private Pane paneView;

    @FXML
    private AnchorPane mainWindow;

    @FXML
    private ImageView backGround = new ImageView();

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

    /**
     * this attribute represent the 4 initial leader cards
     */
    private final List<LeaderCard> leaderCards;

    /**
     * this attribute represent the position
     */
    int position;

    /**
     * this attribute represent the network user
     */
    private final NetworkUser<Command, ResponseToClient> networkUser;


    public InitialisisngController(List<LeaderCard> leaderCards, int position, NetworkUser<Command, ResponseToClient> networkUser){

        this.position = position;
        this.leaderCards = leaderCards;
        this.networkUser = networkUser;
    }

    @Override
    public void update() {

    }

    @Override
    public void sendNewCommand(Command toSend) {
        networkUser.send(toSend);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setMainWindowSize();

        String pngNameConstant = "/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-";

        firstChoice.setImage(new Image(pngNameConstant + leaderCards.get(0).getId() + "-1.png"));

        secondChoice.setImage(new Image(pngNameConstant + leaderCards.get(1).getId() + "-1.png"));

        thirdChoice.setImage(new Image(pngNameConstant + leaderCards.get(2).getId() + "-1.png"));

        fourthChoice.setImage(new Image(pngNameConstant + leaderCards.get(3).getId() + "-1.png"));

        contextAction.setText("these are your leader cards! Choose two of them to discard");

    }

    /**
     * this method set the main window size
     */
    private void setMainWindowSize(){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        mainWindow.setPrefSize(width, height);
        drawBackGround();
    }

    /**
     * this method draw the background
     */
    private void drawBackGround(){
        backGround.setImage(new Image("/board/Masters of Renaissance_PlayerBoard.png"));
        backGround.setFitWidth(mainWindow.getPrefWidth());
        backGround.setFitHeight(mainWindow.getPrefHeight() - 60);
        backGround.setPreserveRatio(false);
        backGround.setLayoutX(0);
        backGround.setLayoutY(mainWindow.getHeight() + 20);
        backGround.setOpacity(0.5);
    }

    /**
     * this method discard the initial leader cards
     * @param actionEvent this is the action event to handle
     */
    public void discardLeaderCards(ActionEvent actionEvent) {

        List<Integer> discarded = new ArrayList<>();

        addIfSelected(discarded, first.isSelected(), 1);
        addIfSelected(discarded, second.isSelected(), 2);
        addIfSelected(discarded, third.isSelected(), 3);
        addIfSelected(discarded, fourth.isSelected(), 4);

        if (discarded.size() == 2){
            sendNewCommand(new InitialiseLeaderCardsCommand(discarded.get(0), discarded.get(1)));

            if (position == 1){
                Gui.setRoot("/WaitingWindow", new WaitingController("Wait for other player to initialize..."));
                sendNewCommand(new InitialiseResourcesCommand(new CollectionResources()));

            }else {
                switchScenario();
            }

        }else {
            errorMessage.setOpacity(1);
        }


    }

    /**
     * this method add the check boxes if selected
     * @param checkBoxes this is the list of integer representing the check boxes
     * @param checkBox this is the boolean representing if a check box got selected
     * @param toAdd this is the integer to add
     */
    private void addIfSelected(List<Integer> checkBoxes, boolean checkBox, int toAdd){
        if (checkBox)
            checkBoxes.add(toAdd);
    }

    /**
     * this method add the check boxes if selected
     * @param checkBoxes this is the collection resources representing the check boxes
     * @param checkBox this is the boolean representing if a check box got selected
     * @param toAdd this is the integer to add
     */
    private void addIfSelected(CollectionResources checkBoxes, boolean checkBox, Resource toAdd){
        if (checkBox)
            checkBoxes.add(toAdd);
    }

    /**
     * this method switch the scenario to the choose resources one
     */
    private void switchScenario(){

        firstChoice.setImage(getCoinImage());
        secondChoice.setImage(getStoneImage());
        thirdChoice.setImage(getShieldImage());
        fourthChoice.setImage(getServantImage());
        contextAction.setText("choose " + resourcesToChoose() + " resources");
        errorMessage.setOpacity(0);
        errorMessage.setText("You can choose only " + resourcesToChoose() + " resources" );
        choiceButton.setText("gain");
        choiceButton.setOnAction(actionEvent -> chooseResources());
    }

    /**
     * this method decide how many resources the host player can chose based on
     * his position
     * @return 0, 1 or 2 depending on the position
     */
    private int resourcesToChoose(){
        switch (position){
            case 2:
            case 3:
                return 1;
            case 4:
                return 2;
        }
        return 0;
    }

    /**
     * this method choose the resources and set the new root to the waiting window
     */
    void chooseResources(){

        CollectionResources resourcesGained = new CollectionResources();

        addIfSelected(resourcesGained, first.isSelected(), new Coin());
        addIfSelected(resourcesGained, second.isSelected(), new Stone());
        addIfSelected(resourcesGained, third.isSelected(), new Shield());
        addIfSelected(resourcesGained, fourth.isSelected(), new Servant());

        if (resourcesGained.getSize() == resourcesToChoose()){
            Gui.setRoot("/WaitingWindow", new WaitingController("Wait for other player to initialize..."));
            sendNewCommand(new InitialiseResourcesCommand(resourcesGained));
        }else {
            errorMessage.setOpacity(1);
        }
    }

}
