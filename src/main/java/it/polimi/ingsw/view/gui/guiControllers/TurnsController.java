package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.EndTurnCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.ProductionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.gui.ThinTrackGuiManager;
import it.polimi.ingsw.view.thinModelComponents.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * this class represent the turns controller
 */
public class TurnsController implements GuiController, Initializable {


    @FXML
    private ImageView lorenzoPosition = new ImageView();

    @FXML
    private HBox fourthShelf = new HBox();

    @FXML
    private HBox fifthShelf = new HBox();

    @FXML
    private ImageView fourthDepot = new ImageView();

    @FXML
    private ImageView fifthDepot = new ImageView();

    @FXML
    private AnchorPane productionPower = new AnchorPane();

    @FXML
    private ImageView popeFavourTile1 = new ImageView();

    @FXML
    private ImageView popeFavourTile2 = new ImageView();

    @FXML
    private  ImageView popeFavourTile3 = new ImageView();

    @FXML
    private ImageView trackPosition = new ImageView();

    @FXML
    private HBox secondShelf = new HBox();

    @FXML
    private HBox thirdShelf = new HBox();

    @FXML
    private HBox firstShelf = new HBox();

    @FXML
    private ToggleButton cardsMarketButton = new ToggleButton();

    @FXML
    private ToggleButton marbleMarketButton = new ToggleButton();

    @FXML
    private AnchorPane mainWindow = new AnchorPane();

    @FXML
    private HBox strongbox = new HBox();

    @FXML
    private Circle lonelyMarble = new Circle();

    @FXML
    private VBox marbleMarket = new VBox();

    @FXML
    private ToolBar actions = new ToolBar();

    @FXML
    private ImageView marbleMarketBig = new ImageView();

    @FXML
    private ImageView marbleMarketThin = new ImageView();

    @FXML
    private TabPane playersTab = new TabPane();

    @FXML
    private VBox leaderCards = new VBox();

    @FXML
    private HBox cardsMarket = new HBox();

    @FXML
    private ImageView soloToken = new ImageView();

    @FXML
    private ImageView playerBoard = new ImageView();

    /**
     * this attribute represent the thin model
     */
    private ThinModel model;

    /**
     * this attribute represent the nickname of the host player
     */
    private String nickname;

    /**
     * this attribute represent the client network user
     */
    private NetworkUser<Command, ResponseToClient> clientNetworkUser;

    /**
     * this attribute represent the track gui to handle drawing positions
     */
    private ThinTrackGuiManager trackGui;

    /**
     * this attribute represent if the host player can do a normal action
     */
    private boolean normalAction = true;

    /**
     * this attribute represent if the host player can do a leader action
     */
    private boolean leaderAction = true;


    public TurnsController(ThinModel model,
                           String nickname,
                           NetworkUser<Command, ResponseToClient> clientNetworkUser){

        this.clientNetworkUser = clientNetworkUser;
        this.model = model;
        this.nickname = nickname;
    }

    public TurnsController(ThinModel model,
                           String nickname,
                           NetworkUser<Command, ResponseToClient> clientNetworkUser,
                           boolean normalAction,
                           boolean leaderAction){

        this.clientNetworkUser = clientNetworkUser;
        this.model = model;
        this.nickname = nickname;
        this.normalAction = normalAction;
        this.leaderAction = leaderAction;

    }

    /**
     * this method get the model
     * @return the model
     */
    protected ThinModel getModel() {
        return model;
    }

    /**
     * this method get the nickname
     * @return the nickname
     */
    protected String getNickname(){
        return nickname;
    }

    /**
     * this method get the client network user
     * @return the client network user
     */
    protected NetworkUser<Command, ResponseToClient> getClientNetworkUser(){
        return clientNetworkUser;
    }

    @Override
    public void update(){

    }

    @Override
    public void sendNewCommand(Command toSend) {
        clientNetworkUser.send(toSend);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setMainWindowSize();

        // draw the player
        drawPlayer(nickname);


        showSoloToken(model.getGame().getSoloToken());
        showLeaderCards(model.getGame().getPlayer(nickname).getLeaderCards());
        showMarbleMarket(model.getGame().getMarbleMarket(), model.getGame().getLonelyMarble());
        showCardsMarket(model.getGame().getCardsMarket());
        initializeActions();
        trackGui = new ThinTrackGuiManager(mainWindow.getPrefWidth() / 24, mainWindow.getPrefHeight() / 5 );

        playersTab.getTabs().clear();
        for (ThinPlayer player : getRealPlayers()){
            Tab tab = new Tab(player.getNickname());
            tab.setClosable(false);

            playersTab.getTabs().add(tab);

            tab.setOnSelectionChanged( (actionEvent) -> drawPlayer(tab.getText()));
        }

    }

    /**
     * this method set the main window size
     */
    protected void setMainWindowSize(){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        mainWindow.setPrefSize(width, height);
        drawBackGround();
    }

    /**
     * this method get the list of all the player of the game
     * @return the list of all the player of the game
     */
    protected List<ThinPlayer> getRealPlayers(){
        List< ThinPlayer> players = new ArrayList<>();
        players.add(model.getGame().getMyself());
        if (model.getGame().getOpponents().stream().
                anyMatch(player -> player.getNickname().equals(new Player().getNickname())))
            return players;

        players.addAll(model.getGame().getOpponents());

        return players;

    }

    /**
     * this method initialize the actions
     */
    private void initializeActions(){

        actions.getItems().clear();

        List<Button> possibleActions = new ArrayList<>();

        possibleActions.add(setButton("shift resources", actionEvent -> shiftResources()));

        if (normalAction){
            possibleActions.add(setButton("choose marbles", actionEvent -> chooseMarbles()));
            if (model.getGame().getMyself().areProductionsAffordable())
                possibleActions.add(setButton("start productions", actionEvent -> startProductions()));
            possibleActions.add(setButton("buy card", actionEvent -> buyCard()));
        } else {

            possibleActions.add(setButton("end turn", actionEvent -> endTurn()));
        }

        if (leaderAction && model.getGame().getMyself().areLeaderCardsAvailable()){
            possibleActions.add(setButton("leader action", actionEvent -> leaderAction()));
        }

        actions.getItems().addAll(possibleActions);


    }

    /**
     * this method handle the leader action
     */
    private void leaderAction(){

        if (!leaderAction){
            showErrorMessage();
            return;
        }

        if (!model.getGame().getMyself().areLeaderCardsAvailable()){
            showErrorMessage();
            return;
        }



        Gui.setRoot("/LeaderActionWindow", new LeaderActionController(model, nickname, clientNetworkUser, normalAction));
    }

    /**
     * this method handle the buy card command
     */
    private void buyCard(){
        if (!normalAction){
            showErrorMessage();
            return;
        }

        Gui.setRoot("/BuyCardWindow", new BuyCardController(model, nickname, clientNetworkUser, leaderAction));
    }

    /**
     * this method handle the choose marbles command
     */
    private void chooseMarbles(){
        if (!normalAction){
            showErrorMessage();
            return;
        }

        Gui.setRoot("/ChooseMarblesWindow", new ChooseMarblesController(model, nickname, clientNetworkUser, leaderAction));

    }

    /**
     * this method handle the productions command
     */
    private void startProductions(){

        if (!normalAction){
            showErrorMessage();
            return;
        }

        clientNetworkUser.send(new ProductionCommand());
        Gui.setRoot("/ProductionWindow", new ProductionController(model, nickname, clientNetworkUser,true,  leaderAction));
    }

    /**
     * this method handle the end turn command
     */
    private void endTurn(){
        clientNetworkUser.send(new EndTurnCommand());
    }

    /**
     * this method handle the shift resources command
     */
    private void shiftResources(){
        Gui.setRoot("/ShiftResourcesWindow", new ShiftResourcesController(model, nickname, clientNetworkUser, normalAction, leaderAction));
    }



    @Override
    public void showErrorMessage(){

        Label errorMessage = new Label("Error");

        errorMessage.setPrefWidth(mainWindow.getPrefWidth() / 2);
        errorMessage.setPrefHeight(mainWindow.getPrefHeight() / 10);

        errorMessage.setAlignment(Pos.CENTER);

        errorMessage.setLayoutX( mainWindow.getPrefWidth() / 2);
        errorMessage.setLayoutY( mainWindow.getPrefHeight() / 10);

        errorMessage.setTextFill(Color.RED);
        errorMessage.setFont(Font.font(31));


        mainWindow.getChildren().add(errorMessage);

    }

    /**
     * this method set the useful buttons
     * @param buttonName this is the button name
     * @param eventHandler this is the event handler to use the button
     * @return the button created
     */
    protected Button setButton(String buttonName, EventHandler<ActionEvent> eventHandler){
        Button button = new Button();
        button.setText(buttonName);
        button.setOnAction(eventHandler);
        return button;
    }

    /**
     * this method draw the player with the nickname passed in input
     * @param nickname this is the nickname of the player to draw
     */
    public void drawPlayer(String nickname){
        showWarehouse(model.getGame().getPlayer(nickname).getWarehouse());
        showStrongbox(model.getGame().getPlayer(nickname).getStrongbox());
        showLeaderCards(model.getGame().getPlayer(nickname).getLeaderCards());
        showTrack(model.getGame().getPlayer(nickname).getTrack());
        showProductionPower(model.getGame().getPlayer(nickname).getProductionPower());
    }

    /**
     * this method draw the background
     */
    public void drawBackGround(){
        playerBoard.setImage(new Image("/board/Masters of Renaissance_PlayerBoard.png"));
        playerBoard.setFitWidth(mainWindow.getPrefWidth());
        playerBoard.setFitHeight(mainWindow.getPrefHeight() - 60);
        playerBoard.setPreserveRatio(false);
        playerBoard.setLayoutX(0);
        playerBoard.setLayoutY(mainWindow.getHeight() + 20);
    }

    /**
     * this method set the player opacity
     * @param opacity this is the opacity to set
     */
    protected void setPlayerOpacity(double opacity){
        playerBoard.setOpacity(opacity);
        setWarehouseOpacity(opacity);
        setStrongboxOpacity(opacity);
        setLeaderCardsOpacity(opacity);
        setSoloTokenOpacity(opacity);
        setTrackOpacity(opacity);
        setProductionPowerOpacity(opacity);
    }

    /**
     * this method show the track passed in input
     * @param track this is the track to draw
     */
    public void showTrack(ThinTrack track){

        trackPosition.setImage(new Image("/punchboard/croce.png"));

        trackPosition.setFitWidth(mainWindow.getPrefWidth() / 25);
        trackPosition.setFitHeight(mainWindow.getPrefHeight() / 16);

        ThinTrackGuiManager trackGui = new ThinTrackGuiManager(
                mainWindow.getPrefWidth() / 24,
                mainWindow.getPrefHeight() / 5);

        trackPosition.setLayoutX(trackGui.getXPosition(track.getPosition()));
        trackPosition.setLayoutY(trackGui.getYPosition(track.getPosition()));

        if(model.getGame().getOpponents().get(0).getNickname().equals(new Player().getNickname()))
            drawLorenzo();

        showPopeFavourTile(popeFavourTile1, 1,
                mainWindow.getPrefWidth() / 4,
                mainWindow.getPrefHeight() / 7
        );
        showPopeFavourTile(popeFavourTile2, 2,
                 mainWindow.getPrefWidth() / 2,
                mainWindow.getPrefHeight() / 11
                );
        showPopeFavourTile(popeFavourTile3, 3,
                mainWindow.getPrefWidth() * 63 / 80,
                mainWindow.getPrefHeight() / 7
                );


    }

    /**
     * this method draw lorenzo il magnifico
     */
    private void drawLorenzo(){


        lorenzoPosition.setImage(new Image("/punchboard/retro cerchi.png"));

        lorenzoPosition.setFitWidth(mainWindow.getPrefWidth() / 25);
        lorenzoPosition.setFitHeight(mainWindow.getPrefHeight() / 16);

        ThinTrackGuiManager trackGui = new ThinTrackGuiManager(
                mainWindow.getPrefWidth() / 24,
                mainWindow.getPrefHeight() / 5);

        lorenzoPosition.setLayoutX(trackGui.getXPosition(model.getGame().getOpponents().get(0).getTrack().getPosition()));
        lorenzoPosition.setLayoutY(trackGui.getYPosition(model.getGame().getOpponents().get(0).getTrack().getPosition()));
    }

    /**
     * this method set th track opacity
     * @param opacity this is the opacity to set
     */
    public void setTrackOpacity(double opacity){
        trackPosition.setOpacity(opacity);
        lorenzoPosition.setOpacity(opacity);
        popeFavourTile1.setOpacity(opacity);
        popeFavourTile2.setOpacity(opacity);
        popeFavourTile3.setOpacity(opacity);

    }

    /**
     * this method show the pope favour tiles
     * @param popeFavourTile this is the pope favour tile to draw
     * @param id this is the id of the pope favour tile
     * @param layoutX this is the x layout
     * @param layoutY this is the y layout
     */
    private void showPopeFavourTile(ImageView popeFavourTile, int id , double layoutX, double layoutY){

        String isActive = "";

        if(model.getGame().getPlayer(nickname).getTrack().getPopeFavourTiles()[id - 1]){
            isActive = "Active";
        }

        popeFavourTile.setImage(new Image("/punchboard/" + isActive +  "PopeFavourTile" + id + ".png"));
        popeFavourTile.setFitHeight(mainWindow.getPrefWidth() / 16);
        popeFavourTile.setFitWidth(mainWindow.getPrefWidth() / 16);
        popeFavourTile.setLayoutX(layoutX);
        popeFavourTile.setLayoutY(layoutY);

    }

    /**
     * this method shows the production passed in input
     * @param productionPower this is the production power to draw
     */
    private void showProductionPower(ThinProductionPower productionPower){

        this.productionPower.getChildren().clear();

        this.productionPower.setLayoutX(mainWindow.getPrefWidth() * 45 / 100);
        this.productionPower.setLayoutY(mainWindow.getPrefHeight() * 58 / 100);

        drawDeck(productionPower.getProductionPower1(), 1);
        drawDeck(productionPower.getProductionPower2(), 2);
        drawDeck(productionPower.getProductionPower3(), 3);
    }

    /**
     * this method draw a deck of cards in a position
     * @param toDraw this is the deck to draw
     * @param position this is the position , it can be 1, 2, 3
     */
    private void drawDeck(List<DevelopmentCard> toDraw, int position){

        position--;

        double offsetCardY = mainWindow.getPrefHeight() / 20;
        double offsetCardX = mainWindow.getPrefWidth() / 5;

        int i = 0;

        for (DevelopmentCard card : toDraw){
            ImageView cardToDraw = new ImageView(getCardImage(card));
            cardToDraw.setFitHeight(150);
            cardToDraw.setFitWidth(100);
            cardToDraw.setLayoutX( position * offsetCardX);
            cardToDraw.setLayoutY( - i * offsetCardY);

            i ++;


            this.productionPower.getChildren().add(cardToDraw);

        }
    }

    /**
     * this method set the production power opacity
     * @param opacity this is the opacity to set
     */
    public void setProductionPowerOpacity(double opacity){
        productionPower.setOpacity(opacity);
    }

    /**
     * this method shows the cards market
     * @param cardsMarket this is the cards market to show
     */
    public void showCardsMarket(DevelopmentCard[][] cardsMarket){

        this.cardsMarket.getChildren().clear();

        for (int j = 0; j < 4; j++) {

            VBox cardColumn = new VBox();

            for (int i = 0; i < 3; i++) {

                ImageView card = new ImageView();
                card.setFitHeight(150);
                card.setFitWidth(100);

                try {
                    card.setImage(getCardImage(cardsMarket[i][j]));

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

    /**
     * this method set the cards market opacity
     * @param opacity this is the opacity to set
     */
    protected void setCardsMarketOpacity(double opacity){
        this.cardsMarket.setOpacity(opacity);

    }

    /**
     * this method shows the leader cards passed in input
     * @param leaderCards these are the leader cards to show
     */
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

    /**
     * this method set the leader cards opacity
     * @param opacity this is the opacity to set
     */
    protected void setLeaderCardsOpacity(double opacity){
        this.leaderCards.setOpacity(opacity);
    }

    /**
     * this method shows the solo token when it exists
     * @param token this is the token to draw
     */
    public void showSoloToken(SoloToken token){
        try {
            soloToken.setImage(new Image(token.getPng()));
            soloToken.setLayoutX(mainWindow.getPrefWidth() / 16);
            soloToken.setLayoutY(mainWindow.getPrefHeight() / 8);
        } catch (NullPointerException ignored){ }
    }

    /**
     * this method set the solo token opacity
     * @param opacity this is the opacity to set
     */
    protected void setSoloTokenOpacity(double opacity){
        soloToken.setOpacity(opacity);
    }

    /**
     * this method shows the marble market
     * @param marbleMarket this is the marble market to draw
     * @param lonelyMarble this is the lonely marble to draw
     */
    public void showMarbleMarket(Marble[][] marbleMarket, Marble lonelyMarble){

        int relativeLayoutMarbleMarketX = 29;
        int relativeLayoutMarbleMarketY = 28;

        Image marbleMarketBig = new Image("/punchboard/plancia portabiglie.png");
        Image marbleMarketThin = new Image("/board/ContenitoreBiglie.png");

        this.marbleMarketBig.setImage(marbleMarketBig);
        this.marbleMarketThin.setImage(marbleMarketThin);

        centerImageView(this.marbleMarketBig);
        centerImageView(this.marbleMarketThin);

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

    /**
     * this method set the marble market opacity
     * @param opacity this is the opacity to set
     */
    protected void setMarbleMarketOpacity(double opacity){
        marbleMarket.setOpacity(opacity);
        marbleMarketThin.setOpacity(opacity);
        marbleMarketBig.setOpacity(opacity);
        lonelyMarble.setOpacity(opacity);
    }

    /**
     * this method shows the warehouse passed in input
     * @param warehouse this is the warehouse to draw
     */
    public void showWarehouse(ThinWarehouse warehouse){

        firstShelf.getChildren().clear();
        secondShelf.getChildren().clear();
        thirdShelf.getChildren().clear();

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

        showLeaderShelf(warehouse.getFourthShelf(),
                fourthShelf,
                fourthDepot,
                mainWindow.getPrefWidth() * 27 / 100,
                mainWindow.getPrefHeight() * 43 / 100
                );

        showLeaderShelf(warehouse.getFifthShelf(),
                fifthShelf,
                fifthDepot,
                mainWindow.getPrefWidth() * 27 / 100,
                mainWindow.getPrefHeight() * 55 / 100);
    }

    /**
     * this method shows a shelf
     * @param shelfToDraw this is the shelf to draw
     * @param shelf these are the resources of the shelf to draw
     * @param layoutX this is the x layout
     * @param layoutY this is the y layout
     */
    private void showShelf(HBox shelfToDraw, CollectionResources shelf, double layoutX, double layoutY){

        for (Resource resource : shelf){

            ImageView resourceToDraw = new ImageView(getResourceImage(resource));
            resourceToDraw.setFitWidth(mainWindow.getPrefWidth()/ 35);
            resourceToDraw.setFitHeight(mainWindow.getPrefWidth()/ 35);

            shelfToDraw.getChildren().add(resourceToDraw);
        }

        shelfToDraw.setLayoutX(layoutX);
        shelfToDraw.setLayoutY(layoutY);

    }

    /**
     * this method show the leader shelf
     * @param shelf these are the resources of the shelf to draw
     * @param shelfToDraw this is the shelf to draw
     * @param depotToDraw this is the dept image to draw
     * @param layoutX this is the x layout
     * @param layoutY this is the y layout
     */
    private void showLeaderShelf(CollectionResources shelf, HBox shelfToDraw,  ImageView depotToDraw,  double layoutX, double layoutY){
        if (shelf == null)
            return;

        depotToDraw.setImage(new Image("/board/leaderShelf.png"));
        depotToDraw.setFitWidth(mainWindow.getPrefWidth() / 13);
        depotToDraw.setFitHeight(mainWindow.getPrefHeight() / 18);
        depotToDraw.setPreserveRatio(false);
        depotToDraw.setLayoutX(layoutX);
        depotToDraw.setLayoutY(layoutY);



        showShelf(shelfToDraw, shelf, depotToDraw.getLayoutX(), depotToDraw.getLayoutY());

    }

    /**
     * this method set the warehouse opacity
     * @param opacity this is the opacity to set
     */
    protected void setWarehouseOpacity(double opacity){
        firstShelf.setOpacity(opacity);
        secondShelf.setOpacity(opacity);
        thirdShelf.setOpacity(opacity);
        fourthDepot.setOpacity(opacity);
        fourthShelf.setOpacity(opacity);
        fifthShelf.setOpacity(opacity);
        fifthDepot.setOpacity(opacity);
    }

    /**
     * this method shows the strongbox
     * @param strongbox this is the strongbox to draw
     */
    public void showStrongbox(CollectionResources strongbox){

        this.strongbox.getChildren().clear();

        this.strongbox.setLayoutX(this.playerBoard.getFitWidth() /20);
        this.strongbox.setLayoutY(this.playerBoard.getFitHeight() * 13 / 16);

        for (Resource resource : new ArrayList<>(Arrays.asList(new Coin(), new Stone(), new Shield(), new Servant()))){
            VBox resourcesSet = new VBox();

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

    /**
     * this method set the strongbox opacity
     * @param opacity this is the opacity to set
     */
    protected void setStrongboxOpacity(double opacity){
        strongbox.setOpacity(opacity);
    }

    /**
     * this method shows the marble market
     */
    @FXML
    public void showMarbleMarket() {

        showMarbleMarket(model.getGame().getMarbleMarket(), model.getGame().getLonelyMarble());
        setMarbleMarketOpacity(1);
        setPlayerOpacity(0.5);
        setProductionPowerOpacity(0);
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

    /**
     * this method shows the cards market
     */
    @FXML
    public void showCardsMarket() {

        showCardsMarket(model.getGame().getCardsMarket());
        setCardsMarketOpacity(1);
        setSoloTokenOpacity(0.5);
        marbleMarketButton.setOnAction(actionEvent -> {});
        setPlayerOpacity(0.5);
        setProductionPowerOpacity(0);
        cardsMarketButton.setOnAction(action -> {
            setCardsMarketOpacity(0);
            setPlayerOpacity(1);
            setSoloTokenOpacity(1);
            cardsMarketButton.setOnAction( actionEvent -> showCardsMarket());
            marbleMarketButton.setOnAction(actionEvent -> showMarbleMarket());
        });
    }

    /**
     * this method get the main window
     * @return the main window
     */
    public AnchorPane getMainWindow() {
        return mainWindow;
    }

    /**
     * this method get the first shelf
     * @return the first shelf
     */
    public HBox getFirstShelf() {
        return firstShelf;
    }

    /**
     * this method get the second shelf
     * @return the second shelf
     */
    public HBox getSecondShelf() {
        return secondShelf;
    }

    /**
     * this method get the third shelf
     * @return the third shelf
     */
    public HBox getThirdShelf() {
        return thirdShelf;
    }

    /**
     * this method get the normal action indicator
     * @return the normal action indicator
     */
    public boolean getNormalAction() {
        return normalAction;
    }

    /**
     * this method get the leader action indicator
     * @return the leader action indicator
     */
    public boolean getLeaderAction() {
        return leaderAction;
    }

    /**
     * this method get the cards market
     * @return the cards market
     */
    public HBox getCardsMarket() {
        return cardsMarket;
    }

    /**
     * this method set the normal action indicator
     * @param normalAction this is the normal action indicator to set
     */
    public void setNormalAction(boolean normalAction) {
        this.normalAction = normalAction;
    }

    /**
     * this method set the leader action indicator
     * @param leaderAction this is the leader action indicator to set
     */
    public void setLeaderAction(boolean leaderAction) {
        this.leaderAction = leaderAction;
    }

    /**
     * this method get the leader cards
     * @return the leader cards
     */
    public VBox getLeaderCards() {
        return leaderCards;
    }

    /**
     * this method get the fourth depot
     * @return the fourth depot
     */
    public ImageView getFourthDepot() {
        return fourthDepot;
    }

    /**
     * this method get the fifth depot
     * @return the fifth depot
     */
    public ImageView getFifthDepot() {
        return fifthDepot;
    }
}
