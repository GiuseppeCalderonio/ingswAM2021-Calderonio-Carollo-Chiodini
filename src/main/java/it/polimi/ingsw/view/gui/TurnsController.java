package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseMarblesCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TurnsController implements GuiController, Initializable {

    @FXML
    private Circle lonelyMarble;

    @FXML
    private VBox marbleMarket;

    @FXML
    private ToolBar actions;

    @FXML
    private ImageView marbleMarketBig;

    @FXML
    private ImageView marbleMarketThin;

    @FXML
    private TabPane playersTab;

    @FXML
    private VBox leaderCards;

    @FXML
    private HBox cardsMarket;

    @FXML
    private Label strongboxCoins;

    @FXML
    private Label strongboxStones;

    @FXML
    private Label strongboxShields;

    @FXML
    private Label strongboxServants;

    @FXML
    private ImageView firstShelfFirstResource = new ImageView();

    @FXML
    private ImageView secondShelfFirstResource = new ImageView();

    @FXML
    private ImageView secondShelfSecondResource = new ImageView();

    @FXML
    private ImageView thirdShelfFirstResource = new ImageView();

    @FXML
    private ImageView thirdShelfSecondResource = new ImageView();

    @FXML
    private ImageView thirdShelfThirdResource = new ImageView();

    @FXML
    private ImageView soloToken = new ImageView();

    @FXML
    private ImageView playerBoard = new ImageView();

    @FXML
    private Button firstRowButton;

    private ThinModel model;

    private String nickname;

    private NetworkUser<Command, ResponseToClient> clientNetworkUser;


    public TurnsController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser){
        this.clientNetworkUser = clientNetworkUser;
        this.model = model;
        this.nickname = nickname;
    }

    @Override
    public void update(CommandName name){

    }

    @Override
    public void sendNewCommand(Command toSend) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        playersTab.getTabs().clear();
        for (ThinPlayer player : getRealPlayers()){
            Tab tab = new Tab(player.getNickname());
            tab.setClosable(false);

            playersTab.getTabs().add(tab);

            tab.setOnSelectionChanged( (actionEvent) -> drawPlayer(tab.getText()));
        }
        playerBoard.setImage(new Image("/board/Masters of Renaissance_PlayerBoard.png"));
        showMarbleMarket(model.getGame().getMarbleMarket(), model.getGame().getLonelyMarble() , 508, 86);
        showCardsMarket(model.getGame().getCardsMarket());
        showSoloToken(model.getGame().getSoloToken());
        // draw the player
        drawPlayer(nickname);
        initializeActions(actions);

    }

    private List<ThinPlayer> getRealPlayers(){
        List< ThinPlayer> players = new ArrayList<>();
        players.add(model.getGame().getMyself());
        if (model.getGame().getOpponents().stream().
                anyMatch(player -> player.getNickname().equals(new Player().getNickname())))
            return players;

        players.addAll(model.getGame().getOpponents());

        return players;

    }

    private void initializeActions(ToolBar actions){
        actions.getItems().clear();
        List<Button> buttonActions = new ArrayList<>();


        buttonActions.add(setButton("choose marbles", (actionEvent) -> chooseMarbles()));
        actions.getItems().addAll(buttonActions);
    }

    private Button setButton(String buttonName, EventHandler<ActionEvent> eventHandler){
        Button button = new Button();
        button.setText(buttonName);
        button.setOnAction(eventHandler);
        return button;
    }

    private Button setButton(String buttonName, EventHandler<ActionEvent> eventHandler, int layoutX, int layoutY){
        Button button = setButton(buttonName, eventHandler);
        button.setLayoutX(layoutX);
        button.setLayoutY(layoutY);
        return button;
    }

    public void drawPlayer(String nickname){
        showLeaderCards(model.getGame().getPlayer(nickname).getLeaderCards());
        showWarehouse(model.getGame().getPlayer(nickname).getWarehouse());
        showStrongbox(model.getGame().getPlayer(nickname).getStrongbox());
    }

    @FXML
    public void chooseMarbles(){

        int layoutX = 650;
        int layoutY = 250;

        showMarbleMarket(model.getGame().getMarbleMarket(),model.getGame().getLonelyMarble() , layoutX, layoutY);

        firstRowButton = setButton("",
                (actionEvent) -> {
            sendNewCommand(new ChooseMarblesCommand("row", 1));
            //createMarblesScenario();
        },
                 layoutX + (int) marbleMarketBig.getLayoutX() + 5,
                layoutY + 5);

    }

    public void showCardsMarket(DevelopmentCard[][] cardsMarket){

        this.cardsMarket.getChildren().clear();

        String pngNameConstant = "/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-";

        for (int j = 0; j < 4; j++) {

            VBox cardColumn = new VBox();

            for (int i = 0; i < 3; i++) {

                ImageView card = new ImageView();
                card.setFitHeight(150);
                card.setFitWidth(100);

                try {
                    card.setImage(new Image(pngNameConstant + cardsMarket[i][j].getId() + "-1.png"));

                } catch (NullPointerException e){
                    card.imageProperty().setValue(null);
                }


                cardColumn.getChildren().add(card);
            }
            this.cardsMarket.getChildren().add(cardColumn);

        }

    }

    public void showLeaderCards(List<LeaderCard> leaderCards){

        this.leaderCards.getChildren().clear();



        for (int i = 0; i < 2; i++) {

            String pngNameConstant = "/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-";

            ImageView leaderCard = new ImageView();
            leaderCard.setFitHeight(150);
            leaderCard.setFitWidth(100);

            try {

                if (leaderCards.get(i).getId() == 49)
                    pngNameConstant = "/back/Masters of Renaissance__Cards_BACK_3mmBleed-";

                leaderCard.setImage(new Image(pngNameConstant + leaderCards.get(i).getId() + "-1.png"));
            } catch (IndexOutOfBoundsException e){
                leaderCard.imageProperty().setValue(null);
            }

            this.leaderCards.getChildren().add(leaderCard);
        }

    }

    public void showSoloToken(SoloToken token){
        try {
            soloToken.setImage(new Image(token.getPng()));
        } catch (NullPointerException ignored){ }
    }

    public void showMarbleMarket(Marble[][] marbleMarket, Marble lonelyMarble ,  int layoutX, int layoutY){

        int relativeLayoutMarbleMarketX = 29;
        int relativeLayoutMarbleMarketY = 28;

        Image marbleMarketBig = new Image("/punchboard/plancia portabiglie.png");
        Image marbleMarketThin = new Image("/board/ContenitoreBiglie.png");

        this.marbleMarketBig.setImage(marbleMarketBig);
        this.marbleMarketThin.setImage(marbleMarketThin);

        this.marbleMarketBig.setLayoutX(layoutX);
        this.marbleMarketBig.setLayoutY(layoutY);

        this.marbleMarketThin.setLayoutX(layoutX + relativeLayoutMarbleMarketX);
        this.marbleMarketThin.setLayoutY(layoutY + relativeLayoutMarbleMarketY);

        this.marbleMarket.getChildren().clear();

        int relativeLayoutMarblesX = 55;
        int relativeLayoutMarblesY = 55;

        this.marbleMarket.setLayoutX(layoutX + relativeLayoutMarblesX);
        this.marbleMarket.setLayoutY(layoutY + relativeLayoutMarblesY);


        for (int i = 0; i < 3; i++) {

            HBox marbleRow = new HBox();

            for (int j = 0; j < 4; j++) {

                Circle marble = new Circle();
                marble.setFill(marbleMarket[i][j].getColor());
                marble.setRadius(11);
                marble.setStroke(Color.BLACK);

                marbleRow.getChildren().add(marble);
            }
            this.marbleMarket.getChildren().add(marbleRow);

        }

        int relativeLayoutLonelyMarbleX = 150;
        int relativeLayoutLonelyMarbleY = 45;

        this.lonelyMarble.setLayoutX(layoutX + relativeLayoutLonelyMarbleX);
        this.lonelyMarble.setLayoutY(layoutY + relativeLayoutLonelyMarbleY);

        this.lonelyMarble.setFill(lonelyMarble.getColor());
        this.lonelyMarble.setStroke(Color.BLACK);


    }

    public void showWarehouse(ThinWarehouse warehouse){

        String pngNameConstant = "/punchboard/Resource-";

        showFirstShelf(warehouse.getFirstShelf(), pngNameConstant);
        showSecondShelf(warehouse.getSecondShelf(), pngNameConstant);
        showThirdShelf(warehouse.getThirdShelf(), pngNameConstant);
    }

    private void showFirstShelf(CollectionResources firstShelf, String pngNameConstant){



        try {
            firstShelfFirstResource.setImage(new Image(pngNameConstant + firstShelf.asList().get(0).getId() + ".png"));
        } catch (IndexOutOfBoundsException e){
            firstShelfFirstResource.imageProperty().setValue(null);
        }
    }

    private void showSecondShelf(CollectionResources secondShelf, String pngNameConstant){
        try {
            secondShelfFirstResource.setImage(new Image(pngNameConstant + secondShelf.asList().get(0).getId() + ".png"));
        } catch (IndexOutOfBoundsException e){
            secondShelfFirstResource.imageProperty().setValue(null);
        }

        try {
            secondShelfSecondResource.setImage(new Image(pngNameConstant + secondShelf.asList().get(1).getId() + ".png"));
        } catch (IndexOutOfBoundsException e){
            secondShelfSecondResource.imageProperty().setValue(null);
        }

    }

    private void showThirdShelf(CollectionResources thirdShelf, String pngNameConstant){
        try {
            thirdShelfFirstResource.setImage(new Image(pngNameConstant + thirdShelf.asList().get(0).getId() + ".png"));
        } catch (IndexOutOfBoundsException e){
            thirdShelfFirstResource.imageProperty().setValue(null);
        }

        try {
            thirdShelfFirstResource.setImage(new Image(pngNameConstant + thirdShelf.asList().get(1).getId() + ".png"));
        } catch (IndexOutOfBoundsException e){
            thirdShelfSecondResource.imageProperty().setValue(null);
        }

        try {
            thirdShelfFirstResource.setImage(new Image(pngNameConstant + thirdShelf.asList().get(2).getId() + ".png"));
        } catch (IndexOutOfBoundsException e){
            thirdShelfThirdResource.imageProperty().setValue(null);
        }
    }

    public void showStrongbox(CollectionResources strongbox){

        strongboxCoins.setText(String.valueOf(strongbox.asList().stream().filter(resource -> resource.equals(new Coin())).count()));
        strongboxStones.setText(String.valueOf(strongbox.asList().stream().filter(resource -> resource.equals(new Stone())).count()));
        strongboxShields.setText(String.valueOf(strongbox.asList().stream().filter(resource -> resource.equals(new Shield())).count()));
        strongboxServants.setText(String.valueOf(strongbox.asList().stream().filter(resource -> resource.equals(new Servant())).count()));
    }


}
