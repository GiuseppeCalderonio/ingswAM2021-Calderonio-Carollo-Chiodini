package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TurnsController implements GuiController, Initializable {


    @FXML
    public TabPane playersTab;

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
    private ImageView firstLeaderCard = new ImageView(); // first leaderCard

    @FXML
    private ImageView secondLeaderCard = new ImageView(); // second leaderCard

    @FXML
    private ImageView cardLev3ColPurple = new ImageView(); // r = 0, c = 3

    @FXML
    private ImageView cardLev2ColPurple = new ImageView(); // r = 1, c = 3

    @FXML
    private ImageView cardLev1ColPurple = new ImageView(); // r = 2, c = 3

    @FXML
    private ImageView cardLev3ColYellow = new ImageView(); // r = 0, c = 2

    @FXML
    private ImageView cardLev2ColYellow = new ImageView(); // r = 1, c = 2

    @FXML
    private ImageView cardLev1ColYellow = new ImageView(); // r = 2, c = 2

    @FXML
    private ImageView cardLev3ColBlue = new ImageView(); // r = 0, c = 1

    @FXML
    private ImageView cardLev2ColBlue = new ImageView(); // r = 1, c = 1

    @FXML
    private ImageView cardLev1ColBlue = new ImageView(); // r = 2, c = 1

    @FXML
    private ImageView cardLev3ColGreen = new ImageView(); // r = 0, c = 0

    @FXML
    private ImageView cardLev2ColGreen = new ImageView(); // r = 1, c = 0

    @FXML
    private ImageView cardLev1ColGreen = new ImageView(); // r = 2, c =


    @FXML
    private ImageView marketMarble = new ImageView();

    @FXML
    private ImageView playerBoard = new ImageView();

    private ThinModel model;

    private String nickname;


    public TurnsController(ThinModel model, String nickname){
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
        showMarbleMarket(model.getGame().getMarbleMarket());
        marketMarble.setImage(new Image("/punchboard/plancia portabiglie.png"));
        showCardsMarket(model.getGame().getCardsMarket());
        showSoloToken(model.getGame().getSoloToken());
        // draw the player
        drawPlayer(nickname);
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

    public void drawPlayer(String nickname){
        showLeaderCards(model.getGame().getPlayer(nickname).getLeaderCards());
        showWarehouse(model.getGame().getPlayer(nickname).getWarehouse());
        showStrongbox(model.getGame().getPlayer(nickname).getStrongbox());
    }

    public void showCardsMarket(DevelopmentCard[][] cardsMarket){
        cardLev3ColPurple.setImage(new Image(cardsMarket[0][3].getPng()));
        cardLev2ColPurple.setImage(new Image(cardsMarket[1][3].getPng()));
        cardLev1ColPurple.setImage(new Image(cardsMarket[2][3].getPng()));
        cardLev3ColYellow.setImage(new Image(cardsMarket[0][2].getPng()));
        cardLev2ColYellow.setImage(new Image(cardsMarket[1][2].getPng()));
        cardLev1ColYellow.setImage(new Image(cardsMarket[2][2].getPng()));
        cardLev3ColBlue.setImage(new Image(cardsMarket[0][1].getPng()));
        cardLev2ColBlue.setImage(new Image(cardsMarket[1][1].getPng()));
        cardLev1ColBlue.setImage(new Image(cardsMarket[2][1].getPng()));
        cardLev3ColGreen.setImage(new Image(cardsMarket[0][0].getPng()));
        cardLev2ColGreen.setImage(new Image(cardsMarket[1][0].getPng()));
        cardLev1ColGreen.setImage(new Image(cardsMarket[2][0].getPng()));

    }

    public void showLeaderCards(List<LeaderCard> leaderCards){
        try {
            firstLeaderCard.setImage(new Image(leaderCards.get(0).getPng()));
        } catch (NullPointerException e){
            firstLeaderCard.imageProperty().setValue(null);
        }

        try {
            secondLeaderCard.setImage(new Image(leaderCards.get(1).getPng()));
        } catch (NullPointerException e){
            secondLeaderCard.imageProperty().setValue(null);
        }

    }

    public void showSoloToken(SoloToken token){
        try {
            soloToken.setImage(new Image(token.getPng()));
        } catch (NullPointerException ignored){ }
    }

    public void showMarbleMarket(Marble[][] marbleMarket){
        marketMarble.setImage(new Image("/punchboard/plancia portabiglie.png"));
    }

    public void showWarehouse(ThinWarehouse warehouse){
        showFirstShelf(warehouse.getFirstShelf());
        showSecondShelf(warehouse.getSecondShelf());
        showThirdShelf(warehouse.getThirdShelf());
    }

    private void showFirstShelf(CollectionResources firstShelf){
        try {
            firstShelfFirstResource.setImage(new Image(firstShelf.asList().get(0).getPng()));
        } catch (IndexOutOfBoundsException e){
            firstShelfFirstResource.imageProperty().setValue(null);
        }
    }

    private void showSecondShelf(CollectionResources secondShelf){
        try {
            secondShelfFirstResource.setImage(new Image(secondShelf.asList().get(0).getPng()));
        } catch (IndexOutOfBoundsException e){
            secondShelfFirstResource.imageProperty().setValue(null);
        }

        try {
            secondShelfSecondResource.setImage(new Image(secondShelf.asList().get(1).getPng()));
        } catch (IndexOutOfBoundsException e){
            secondShelfSecondResource.imageProperty().setValue(null);
        }

    }

    private void showThirdShelf(CollectionResources thirdShelf){
        try {
            thirdShelfFirstResource.setImage(new Image(thirdShelf.asList().get(0).getPng()));
        } catch (IndexOutOfBoundsException e){
            thirdShelfFirstResource.imageProperty().setValue(null);
        }

        try {
            thirdShelfFirstResource.setImage(new Image(thirdShelf.asList().get(1).getPng()));
        } catch (IndexOutOfBoundsException e){
            thirdShelfSecondResource.imageProperty().setValue(null);
        }

        try {
            thirdShelfFirstResource.setImage(new Image(thirdShelf.asList().get(2).getPng()));
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
