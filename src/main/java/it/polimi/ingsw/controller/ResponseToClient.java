package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.ResourceType;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.view.ThinLeaderCard;
import it.polimi.ingsw.view.ThinPlayer;
import it.polimi.ingsw.view.ThinProductionPower;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseToClient {

    String message;
    int position;
    List<String> possibleCommands;
    boolean ignorePossibleCommands = false;
    boolean serialize;
    List<ThinLeaderCard> leaderCards;
    int code; // 1 = leaderCards , 2 = shelf1, 3 = shelf2 , 4 = shelf3, 5 = shelf4, 6 = self5, 7 = strongbox, 8 = cardsMarket, 9 = marbleMarket, 10: lonelyMarble
    List<Resource> resourcesSet;
    DevelopmentCard[][] cardsMarket;
    Marble[][] marbleMarket;
    Marble lonelyMarble;
    SoloToken soloToken;
    ThinPlayer actualPlayer;
    List<ThinPlayer> opponents;
    //List<ThinProductionPower> thinProductionPower;
    //int faithPosition;
    //CollectionResources firstShelf;
    //CollectionResources secondShelf;
    //CollectionResources thirdShelf;
    //CollectionResources fourthShelf;
    //CollectionResources fifthShelf;
    //boolean[] popeFavourTiles;
    //String nickname;
    int marbles;

    public ResponseToClient(){

    }

    private void updatePlayers(Game game, String nickname){
        // create the thin single player relative to the player that is playing the turn
        actualPlayer = new ThinPlayer(game.findPlayer(nickname));
        // if the game is a single game
        if (game.getPlayers().size() == 1){
            ThinPlayer lorenzo = new ThinPlayer(game.getLorenzoIlMagnifico());
            opponents = new ArrayList<>(Collections.singletonList(lorenzo));
        } // if the game is not a single game
        else{
            opponents = game.getPlayers().stream().
                    filter(player -> !game.findPlayer(nickname).equals(player)).
                    map(ThinPlayer::new).
                    collect(Collectors.toList());
            hideLeaderCards(opponents);
        }
        serialize = true;
        //response.code = 3;
        //response.ignorePossibleCommands = true;
        //return response;
    }

    public static ResponseToClient changePlayerState(Game game, String nickname){
        ResponseToClient response = new ResponseToClient();
        response.updatePlayers(game, nickname);
        response.code = 3;
        response.serialize = true;
        return response;
    }

    private void updateMarbleMarket(Game game){
        marbleMarket = game.getMarketBoard().getMarketTray();
        lonelyMarble = game.getMarketBoard().getLonelyMarble();
        ignorePossibleCommands = true;
        serialize = true;
    }

    private void updateCardsMarket(Game game){
        cardsMarket = game.getSetOfCard().show();
        ignorePossibleCommands = true;
        serialize = true;
    }

    private void updateSoloToken(Game game){
        try {
            soloToken = game.getSoloTokens().get(game.getSoloTokens().size() - 1);
        } catch (IndexOutOfBoundsException | NullPointerException e){ // when there isn't a singleGame
            soloToken = null;
        }
        ignorePossibleCommands = true;
        serialize = true;
    }

    public static ResponseToClient responseToInsertInWarehouse(List<String> possibleCommands,
                                                        CollectionResources marblesConverted){
        ResponseToClient response = new ResponseToClient("you gained " + marblesConverted.toString() + ", decide how to place them into the warehouse", possibleCommands);
        response.serialize = true;
        response.code = 4;
        response.resourcesSet = new ArrayList<>(new HashSet<>(marblesConverted.asList()));
        return response;
    }

    public static ResponseToClient responseWhiteMarbles(String message, List<String> possibleCommands, int marblesSize){
        ResponseToClient response = new ResponseToClient(message, possibleCommands);
        response.marbles = marblesSize;
        return response;

    }

    public static ResponseToClient broadcastMarbleAction(Game game, String nickname){
        ResponseToClient response = new ResponseToClient();
        response.updateMarbleMarket(game);
        response.updatePlayers(game, nickname);
        response.serialize = true;
        response.code = 5;
        response.ignorePossibleCommands = true;
        return response;
    }

    public static ResponseToClient broadcastCardAction(Game game, String nickname){
        ResponseToClient response = new ResponseToClient();
        response.updatePlayers(game, nickname);
        response.updateCardsMarket(game);
        //response = changePlayerState(game, nickname);
        //response.cardsMarket = game.getSetOfCard().show();
        response.code = 6;
        response.ignorePossibleCommands = true;
        return response;

    }

    public static ResponseToClient endSingleGame(Game game, String nickname){
        ResponseToClient response = new ResponseToClient();
        response.updatePlayers(game, nickname);
        response.updateCardsMarket(game);
        response.updateSoloToken(game);
       // response = broadcastCardAction(game, nickname);
        /*
        try {
            response.soloToken = game.getSoloTokens().get(game.getSoloTokens().size() - 1);
        } catch (IndexOutOfBoundsException | NullPointerException e){ // when there isn't a singleGame
            response.soloToken = null;
        }

         */
        response.ignorePossibleCommands = true;
        response.code = 7;
        response.serialize = true;
        return response;
    }

    public static ResponseToClient broadCastStartGame(Game game, ClientHandler client){
        ResponseToClient response = new ResponseToClient();
        String message;
        List<String > possibleCommands = new ArrayList<>();

        if (client.isYourTurn()  /*getNickname().equals( client.getGame().getActualPlayer().getNickname()) */) {
            message = "the game start! Is your turn";
            possibleCommands.add("shift_resources");
            possibleCommands.add("choose_marbles");
            possibleCommands.add("production");
            possibleCommands.add("buy_card");
            possibleCommands.add("leader_action");
        }
        else {
            message = "the game start! Is not your turn, wait...";
        }

        response.message = message;
        response.possibleCommands = possibleCommands;
        response.updateMarbleMarket(game);
        response.updateCardsMarket(game);
        response.updateSoloToken(game);
        response.updatePlayers(game, client.getNickname());
        response.ignorePossibleCommands = false;

        response.code = 2;
        response.serialize = true;
        return response;
    }

    public static ResponseToClient broadcastInitialising(ClientHandler client, int i){
        // get the leader cards of the client
        List<LeaderCard> leaderCards = client.getGame().findPlayer(client.getNickname()).getPersonalLeaderCards();
        // create a new response to send to the client
        ResponseToClient message = new ResponseToClient();
        message.message = "the game initialization start! decide 2 different leader cards to discard";
        // set the only possible command
        message.possibleCommands = new ArrayList<>(Collections.singletonList("initialise_leaderCards"));
        // set the position (that works because the clients are sorted with the game casual order)
        message.position = i;
        // send the thin leader cards
        message.leaderCards = leaderCards.stream().map(LeaderCard::getThin).collect(Collectors.toList());
        // set the code to 1
        message.code = 1;
        // set the message for sending an object
        message.serialize = true;
        // send the message
        //client.send(message);
        return message;
    }



    /**
     * this method hide the leader cards of a thin player, in fact the cards
     * of a player different from the owner, when another player did not activate
     * a leader card, should not be visible
     * @param players these are the player with the leader cards to hide
     */
    private static void hideLeaderCards(List<ThinPlayer> players){
        for (ThinPlayer player : players){
            for (ThinLeaderCard card : player.getThinLeaderCards()){
                if (!card.isActive())
                    card.hide();
            }
        }
    }

    public ResponseToClient(String message){
        this.message = message;
        ignorePossibleCommands = true;
    }

    public ResponseToClient(String message, List<String> possibleCommands){
        this.message = message;
        this.possibleCommands = possibleCommands;
    }

    public DevelopmentCard[][] getCardsMarket() {
        return cardsMarket;
    }

    public void setCardsMarket(DevelopmentCard[][] cardsMarket) {
        this.cardsMarket = cardsMarket;
    }

    public ThinPlayer getActualPlayer() {
        return actualPlayer;
    }

    public void setActualPlayer(ThinPlayer actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public Marble getLonelyMarble() {
        return lonelyMarble;
    }

    public void setLonelyMarble(Marble lonelyMarble) {
        this.lonelyMarble = lonelyMarble;
    }

    public Marble[][] getMarbleMarket() {
        return marbleMarket;
    }

    public void setMarbleMarket(Marble[][] marbleMarket) {
        this.marbleMarket = marbleMarket;
    }

    public boolean isIgnorePossibleCommands() {
        return ignorePossibleCommands;
    }

    public void setIgnorePossibleCommands(boolean ignorePossibleCommands) {
        this.ignorePossibleCommands = ignorePossibleCommands;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSerialize() {
        return serialize;
    }

    public void setSerialize(boolean serialize) {
        this.serialize = serialize;
    }

    public List<Resource> getResourcesSet() {
        return resourcesSet;
    }

    public void setResourcesSet(List<Resource> resourcesSet) {
        this.resourcesSet = resourcesSet;
    }
}
