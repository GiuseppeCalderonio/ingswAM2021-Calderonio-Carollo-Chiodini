package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.view.ThinLeaderCard;
import it.polimi.ingsw.view.ThinPlayer;
import it.polimi.ingsw.view.ThinProductionPower;

import java.util.List;

public class ResponseToClient {

    String message;
    int position;
    List<String> possibleCommands;
    boolean ignorePossibleCommands;
    boolean serialize;
    List<ThinLeaderCard> leaderCards;
    int code; // 1 = leaderCards , 2 = shelf1, 3 = shelf2 , 4 = shelf3, 5 = shelf4, 6 = self5, 7 = strongbox, 8 = cardsMarket, 9 = marbleMarket, 10: lonelyMarble
    CollectionResources resources;
    DevelopmentCard[][] cardsMarket;
    Marble[][] marbleMarket;
    Marble lonelyMarble;
    SoloToken soloToken;
    ThinPlayer actualPlayer;
    List<ThinPlayer> opponents;
    List<ThinProductionPower> thinProductionPower;
    int faithPosition;
    CollectionResources firstShelf;
    CollectionResources secondShelf;
    CollectionResources thirdShelf;
    CollectionResources fourthShelf;
    CollectionResources fifthShelf;
    boolean[] popeFavourTiles;
    String nickname;

}
