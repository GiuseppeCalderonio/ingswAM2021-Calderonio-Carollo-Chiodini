package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.NewWhiteMarble;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.PlayerAndComponents.ProductionPower;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ThinPlayer {

    private final String nickName;
    private CollectionResources firstShelf;
    private CollectionResources secondShelf;
    private CollectionResources thirdShelf;
    private CollectionResources fourthShelf;
    private CollectionResources fifthShelf;
    private CollectionResources strongbox;
    private List<LeaderCard> leaderCards;
    private List<ThinLeaderCard> thinLeaderCards;
    private ThinProductionPower[] productionPower = new ThinProductionPower[3];
    private int position = 0;
    private boolean[] popeFavourTiles ={false, false, false};

    public ThinPlayer(RealPlayer player){
        this.nickName = player.getNickname();
        this.firstShelf = player.getPersonalDashboard().getPersonalWarehouse().getShelf(1).getResources();
        this.secondShelf = player.getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources();
        this.thirdShelf = player.getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources();
        try {
            this.fourthShelf = player.getPersonalDashboard().getPersonalWarehouse().getShelf(4).getResources();
        }catch (IndexOutOfBoundsException ignored){ }
        try {
            this.fifthShelf = player.getPersonalDashboard().getPersonalWarehouse().getShelf(5).getResources();
        }catch (IndexOutOfBoundsException ignored){ }
        this.strongbox = player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources();
        this.thinLeaderCards = player.getPersonalLeaderCards().stream().map(LeaderCard::getThin).collect(Collectors.toList());
        this.productionPower = getVectorThinProductionPower(player);
        this.position = player.getPersonalTrack().getPosition();
        this.popeFavourTiles = getVectorPopeFavourTiles(player);
    }

    public ThinPlayer(ThinPlayer thinPlayer, List<LeaderCard> allLeaderCards) {
        this.nickName = thinPlayer.nickName;
        this.firstShelf = thinPlayer.firstShelf;
        this.secondShelf = thinPlayer.secondShelf;
        this.thirdShelf = new CollectionResources();
        this.thinLeaderCards = thinPlayer.thinLeaderCards;
        this.position = thinPlayer.position;
        this.leaderCards = thinPlayer.thinLeaderCards.stream().
                map(leaderCard -> recreate(allLeaderCards, leaderCard)).
                collect(Collectors.toList());
    }

    public ThinPlayer(Player lorenzo){
        this.nickName = lorenzo.getNickname();
        this.position = lorenzo.getPersonalTrack().getPosition();
        this.popeFavourTiles = getVectorPopeFavourTiles(lorenzo);
    }

    public CollectionResources getFifthShelf() {
        return fifthShelf;
    }

    public void setFifthShelf(CollectionResources fifthShelf) {
        this.fifthShelf = fifthShelf;
    }

    public CollectionResources getFirstShelf() {
        return firstShelf;
    }

    public void setFirstShelf(CollectionResources firstShelf) {
        this.firstShelf = firstShelf;
    }

    public CollectionResources getFourthShelf() {
        return fourthShelf;
    }

    public void setFourthShelf(CollectionResources fourthShelf) {
        this.fourthShelf = fourthShelf;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CollectionResources getThirdShelf() {
        return thirdShelf;
    }

    public void setThirdShelf(CollectionResources thirdShelf) {
        this.thirdShelf = thirdShelf;
    }

    public List<ThinLeaderCard> getThinLeaderCards() {
        return thinLeaderCards;
    }

    public void setThinLeaderCards(List<ThinLeaderCard> thinLeaderCards) {
        this.thinLeaderCards = thinLeaderCards;
    }

    public CollectionResources getStrongbox() {
        return strongbox;
    }

    public void setStrongbox(CollectionResources strongbox) {
        this.strongbox = strongbox;
    }

    public CollectionResources getSecondShelf() {
        return secondShelf;
    }

    public void setSecondShelf(CollectionResources secondShelf) {
        this.secondShelf = secondShelf;
    }

    public ThinProductionPower[] getProductionPower() {
        return productionPower;
    }

    public boolean[] getPopeFavourTiles() {
        return popeFavourTiles;
    }

    public void setPopeFavourTiles(boolean[] popeFavourTiles) {
        this.popeFavourTiles = popeFavourTiles;
    }

    public void setLeaderCards(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    /**
     * this method build the leader cards starting from the victory points and the resource associated
     * @param leaderCard this is the leader card to recreate
     * @return the card
     */
    private LeaderCard recreate(List<LeaderCard> allLeaderCards, ThinLeaderCard leaderCard){
        Resource resource = leaderCard.getResource();
        int victoryPoints = leaderCard.getVictoryPoints();
        for (LeaderCard card : allLeaderCards){
            if (card.getResource().equals(resource) && card.getVictoryPoints() == victoryPoints){
                if (leaderCard.isActive())
                    card.setActive();
                return card;
            }
        }
        return new NewWhiteMarble(null , 0 , null);
    }

    private boolean[] getVectorPopeFavourTiles(Player player){
        boolean[] toReturn = new boolean[3];
        for (int i = 1; i <= 3; i++) {
            toReturn[i - 1] = player.getPersonalTrack().getPopeFavorTiles(i).getActive();
        }
        return toReturn;
    }

    private ThinProductionPower[] getVectorThinProductionPower(RealPlayer player){
        ThinProductionPower[] toReturn = new ThinProductionPower[3];
        ProductionPower productionPower = player.getPersonalDashboard().getPersonalProductionPower();
        for (int i = 1; i <= 3; i++) {
            toReturn[i - 1] = new ThinProductionPower(productionPower.getCard(i),
                    productionPower.getPersonalCards()[i - 1].
                            stream().mapToInt(DevelopmentCard::getVictoryPoints).
                            sum());
        }

        return toReturn;
    }

    /**
     * this method verify if two thin player are equals, comparing the nickname
     * @param o this is the nickname
     * @return true if the two players have the same nickname, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThinPlayer that = (ThinPlayer) o;
        return nickName.equals(that.nickName);
    }

    @Override
    public String toString() {
        return "ThinPlayer{" +
                "nickName='" + nickName + '\'' +
                ", firstShelf=" + firstShelf +
                ", secondShelf=" + secondShelf +
                ", thirdShelf=" + thirdShelf +
                ", fourthShelf=" + fourthShelf +
                ", fifthShelf=" + fifthShelf +
                ", strongbox=" + strongbox +
                ", leaderCards=" + leaderCards +
                ", thinLeaderCards=" + thinLeaderCards +
                ", productionPower=" + Arrays.toString(productionPower) +
                ", position=" + position +
                ", popeFavourTiles=" + Arrays.toString(popeFavourTiles) +
                '}';
    }
}
