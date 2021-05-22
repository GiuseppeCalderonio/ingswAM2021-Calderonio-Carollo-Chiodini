package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TurnsController implements GuiController, Initializable {

    @FXML
    private HBox secondShelf = new HBox();

    @FXML
    private HBox thirdShelf = new HBox();

    @FXML
    private HBox firstShelf = new HBox();

    @FXML
    private ToggleButton cardsMarketButton;

    @FXML
    private ToggleButton marbleMarketButton;

    @FXML
    private AnchorPane mainWindow;

    @FXML
    private HBox strongbox = new HBox();

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

    protected ThinModel getModel() {
        return model;
    }

    protected String getNickname(){
        return nickname;
    }

    protected NetworkUser<Command, ResponseToClient> getClientNetworkUser(){
        return clientNetworkUser;
    }

    @Override
    public void update(CommandName name){

    }

    @Override
    public void sendNewCommand(Command toSend) {

    }

    private void toDelete(){
        model.getGame().getMyself().getWarehouse().getFirstShelf().add(new Coin());
        model.getGame().getMyself().getWarehouse().getSecondShelf().add(new Coin());
        model.getGame().getMyself().getWarehouse().getSecondShelf().add(new Coin());
        model.getGame().getMyself().getWarehouse().getThirdShelf().add(new Coin());
        model.getGame().getMyself().getWarehouse().getThirdShelf().add(new Coin());
        model.getGame().getMyself().getWarehouse().getThirdShelf().add(new Coin());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        toDelete();

        setMainWindowSize();
        playersTab.getTabs().clear();
        for (ThinPlayer player : getRealPlayers()){
            Tab tab = new Tab(player.getNickname());
            tab.setClosable(false);

            playersTab.getTabs().add(tab);

            tab.setOnSelectionChanged( (actionEvent) -> drawPlayer(tab.getText()));
        }


        // draw the player
        drawPlayer(nickname);

        showCardsMarket(model.getGame().getCardsMarket());
        showSoloToken(model.getGame().getSoloToken());
        showLeaderCards(model.getGame().getPlayer(nickname).getLeaderCards());
        showMarbleMarket(model.getGame().getMarbleMarket(), model.getGame().getLonelyMarble());
        initializeActions(actions);

    }

    protected void setMainWindowSize(){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        mainWindow.setPrefSize(width, height);
    }

    protected List<ThinPlayer> getRealPlayers(){
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

    protected Button setButton(String buttonName, EventHandler<ActionEvent> eventHandler){
        Button button = new Button();
        button.setText(buttonName);
        button.setOnAction(eventHandler);
        return button;
    }

    public void drawPlayer(String nickname){
        playerBoard.setImage(new Image("/board/Masters of Renaissance_PlayerBoard.png"));
        playerBoard.setFitWidth(mainWindow.getPrefWidth());
        playerBoard.setFitHeight(mainWindow.getPrefHeight() - 60);
        playerBoard.setPreserveRatio(false);
        playerBoard.setLayoutX(0);
        playerBoard.setLayoutY(mainWindow.getHeight() + 20);
        showWarehouse(model.getGame().getPlayer(nickname).getWarehouse());
        showStrongbox(model.getGame().getPlayer(nickname).getStrongbox());
        showLeaderCards(model.getGame().getPlayer(nickname).getLeaderCards());
    }

    protected void setPlayerOpacity(double opacity){
        playerBoard.setOpacity(opacity);
        setWarehouseOpacity(opacity);
        setStrongboxOpacity(opacity);
        setLeaderCardsOpacity(opacity);
        setSoloTokenOpacity(opacity);
    }

    @FXML
    public void chooseMarbles(){

        Gui.setRoot("/ChooseMarblesWindow", new ChooseMarblesController(model, nickname, clientNetworkUser));

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
            centerBox(this.cardsMarket);
            setCardsMarketOpacity(0);

        }
    }

    protected void setCardsMarketOpacity(double opacity){
        this.cardsMarket.setOpacity(opacity);

    }

    public void showLeaderCards(List<LeaderCard> leaderCards){

        this.leaderCards.getChildren().clear();

        double cardHeight = 150;
        double cardWidth = 100;


        for (int i = 0; i < 2; i++) {

            String pngNameConstant = "/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-";

            ImageView leaderCard = new ImageView();
            leaderCard.setFitHeight(cardHeight);
            leaderCard.setFitWidth(cardWidth);

            try {

                if (leaderCards.get(i).getId() == 0)
                    pngNameConstant = "/back/Masters of Renaissance__Cards_BACK_3mmBleed-";

                leaderCard.setImage(new Image(pngNameConstant + leaderCards.get(i).getId() + "-1.png"));
            } catch (IndexOutOfBoundsException e){
                leaderCard.imageProperty().setValue(null);
            }

            this.leaderCards.getChildren().add(leaderCard);
        }

        this.leaderCards.setLayoutX(mainWindow.getPrefWidth() - cardWidth);
        this.leaderCards.setLayoutY(mainWindow.getPrefHeight()/2 - cardHeight);

    }

    protected void setLeaderCardsOpacity(double opacity){
        this.leaderCards.setOpacity(opacity);
    }

    public void showSoloToken(SoloToken token){
        try {
            soloToken.setImage(new Image(token.getPng()));
            soloToken.setLayoutX(mainWindow.getPrefWidth() / 16);
            soloToken.setLayoutY(mainWindow.getPrefHeight() / 8);
        } catch (NullPointerException ignored){ }
    }

    protected void setSoloTokenOpacity(double opacity){
        soloToken.setOpacity(opacity);
    }

    public void showMarbleMarket(Marble[][] marbleMarket, Marble lonelyMarble){

        int relativeLayoutMarbleMarketX = 29;
        int relativeLayoutMarbleMarketY = 28;

        Image marbleMarketBig = new Image("/punchboard/plancia portabiglie.png");
        Image marbleMarketThin = new Image("/board/ContenitoreBiglie.png");

        this.marbleMarketBig.setImage(marbleMarketBig);
        this.marbleMarketThin.setImage(marbleMarketThin);

        centerImageView(this.marbleMarketBig);

        double layoutX = this.marbleMarketBig.getLayoutX();
        double layoutY = this.marbleMarketBig.getLayoutY();

        this.marbleMarketBig.setFitWidth(200);
        this.marbleMarketBig.setFitHeight(264);

        this.marbleMarketThin.setLayoutX(layoutX + relativeLayoutMarbleMarketX);
        this.marbleMarketThin.setLayoutY(layoutY + relativeLayoutMarbleMarketY);
        this.marbleMarketThin.setFitHeight(121);
        this.marbleMarketThin.setFitWidth(143);

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
        this.lonelyMarble.setRadius(11);

        setMarbleMarketOpacity(0);

    }

    protected void setMarbleMarketOpacity(double opacity){
        marbleMarket.setOpacity(opacity);
        marbleMarketThin.setOpacity(opacity);
        marbleMarketBig.setOpacity(opacity);
        lonelyMarble.setOpacity(opacity);
    }

    public void showWarehouse(ThinWarehouse warehouse){

        String pngNameConstant = "/punchboard/Resource-";

        showShelf(firstShelf,
                warehouse.getFirstShelf(),
                mainWindow.getPrefWidth() * 2 / 17,
                mainWindow.getPrefHeight() * 3 / 7);

        showShelf(secondShelf,
                warehouse.getSecondShelf(),
                mainWindow.getPrefWidth() * 2 / 19,
                mainWindow.getPrefHeight() * 43 / 84);

        showShelf(thirdShelf,
                warehouse.getThirdShelf(),
                mainWindow.getPrefWidth() * 2 / 23,
                mainWindow.getPrefHeight() * 50 / 84);
    }

    private void showShelf(HBox shelf, CollectionResources firstShelf, double layoutX, double layoutY){

        for (Resource resource : firstShelf){

            ImageView resourceToDraw = new ImageView(getResourceImage(resource));
            resourceToDraw.setFitWidth(mainWindow.getPrefWidth()/ 35);
            resourceToDraw.setFitHeight(mainWindow.getPrefWidth()/ 35);

            shelf.getChildren().add(resourceToDraw);
        }

        shelf.setLayoutX(layoutX);
        shelf.setLayoutY(layoutY);

    }

    protected void setWarehouseOpacity(double opacity){
        firstShelf.setOpacity(opacity);
        secondShelf.setOpacity(opacity);
        thirdShelf.setOpacity(opacity);
    }

    public void showStrongbox(CollectionResources strongbox){

        this.strongbox.getChildren().clear();

        this.strongbox.setLayoutX(this.playerBoard.getFitWidth() /20);
        this.strongbox.setLayoutY(this.playerBoard.getFitHeight() * 13 / 16);

        for (Resource resource : new ArrayList<>(Arrays.asList(new Coin(), new Stone(), new Shield(), new Servant()))){
            VBox resourcesSet = new VBox();
            //resourcesSet.setLayoutX(20);
            //resourcesSet.setLayoutY(48);

            ImageView resourceToDraw = new ImageView(getResourceImage(resource));
            resourceToDraw.setFitWidth(mainWindow.getPrefWidth()/ 35);
            resourceToDraw.setFitHeight(mainWindow.getPrefWidth() / 35);

            Label resourceCount = new Label("x" + strongbox.asList().stream().
                    filter(StrongboxResource -> StrongboxResource.equals(resource)).count());

            resourceCount.setTextFill(Color.WHITE);
            resourceCount.setPrefSize(mainWindow.getPrefWidth() / 40, mainWindow.getPrefWidth() / 40);

            resourcesSet.getChildren().add(resourceToDraw);
            resourcesSet.getChildren().add(resourceCount);

            this.strongbox.getChildren().add(resourcesSet);

        }
    }

    protected void setStrongboxOpacity(double opacity){
        strongbox.setOpacity(opacity);
    }

    @FXML
    public void showMarbleMarket() {

        //showMarbleMarket(model.getGame().getMarbleMarket(), model.getGame().getLonelyMarble(),  mainWindow.getWidth()/2, mainWindow.getHeight()/2 );
        setMarbleMarketOpacity(1);
        setPlayerOpacity(0.5);
        soloToken.setOpacity(0.5);
        cardsMarketButton.setOnAction( actionEvent -> {});
        marbleMarketButton.setOnAction(action -> {
            setMarbleMarketOpacity(0);
            setPlayerOpacity(1);
            setSoloTokenOpacity(1);
            marbleMarketButton.setOnAction(actionEvent -> showMarbleMarket());
            cardsMarketButton.setOnAction( actionEvent -> showCardsMarket());
        });

    }

    @FXML
    public void showCardsMarket() {

        setCardsMarketOpacity(1);
        setSoloTokenOpacity(0.5);
        marbleMarketButton.setOnAction(actionEvent -> {});
        setPlayerOpacity(0.5);
        cardsMarketButton.setOnAction(action -> {
            setCardsMarketOpacity(0);
            setPlayerOpacity(1);
            setSoloTokenOpacity(1);
            cardsMarketButton.setOnAction( actionEvent -> showCardsMarket());
            marbleMarketButton.setOnAction(actionEvent -> showMarbleMarket());
        });
    }
}
