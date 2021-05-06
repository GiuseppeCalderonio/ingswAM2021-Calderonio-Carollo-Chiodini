package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.LeaderCard.NewWhiteMarble;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.Client.createLeaderCards;

public class ThinPlayer {

    private String nickName;
    private ThinWarehouse warehouse;
    private CollectionResources strongbox;
    private List<LeaderCard> leaderCards;
    private List<ThinLeaderCard> thinLeaderCards;
    private ThinProductionPower productionPower;
    private ThinTrack track;

    public ThinPlayer(RealPlayer player){
        this.nickName = player.getNickname();
        this.warehouse = new ThinWarehouse(player);
        this.strongbox = player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources();
        this.thinLeaderCards = player.getPersonalLeaderCards().stream().map(LeaderCard::getThin).collect(Collectors.toList());
        this.productionPower = new ThinProductionPower(player);
        this.track = new ThinTrack(player);
    }

    public ThinPlayer(ThinPlayer thinPlayer) {
        this.nickName = thinPlayer.nickName;
        this.warehouse = thinPlayer.warehouse;
        this.strongbox = thinPlayer.strongbox;
        //this.thinLeaderCards = thinPlayer.thinLeaderCards;
        this.productionPower = thinPlayer.productionPower;
        this.track = thinPlayer.track;
        try{
            this.leaderCards = thinPlayer.thinLeaderCards.stream().
                    map(ThinPlayer::recreate).
                    collect(Collectors.toList());
        } catch (NullPointerException e){
            this.leaderCards = null;
        }

    }

    public ThinPlayer(Player lorenzo){
        this.nickName = lorenzo.getNickname();
        this.track = new ThinTrack(lorenzo);
    }



    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public List<ThinLeaderCard> getThinLeaderCards() {
        return thinLeaderCards;
    }

    public CollectionResources getStrongbox() {
        return strongbox;
    }

    public void setStrongbox(CollectionResources strongbox) {
        this.strongbox = strongbox;
    }

    public void setLeaderCards(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public String getNickName() {
        return nickName;
    }

    public ThinProductionPower getProductionPower() {
        return productionPower;
    }

    public void setProductionPower(ThinProductionPower productionPower) {
        this.productionPower = productionPower;
    }

    public ThinTrack getTrack() {
        return track;
    }

    public ThinWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(ThinWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setNickName(String nickName){
        this.nickName = nickName;
    }

    public void setTrack(ThinTrack track) {
        this.track = track;
    }

    /**
     * this method build the leader cards starting from the victory points and the resource associated
     * @param leaderCard this is the leader card to recreate
     * @return the card
     */
    public static LeaderCard recreate( ThinLeaderCard leaderCard){
        //----------------------------------------------

        List<LeaderCard> allLeaderCards = createLeaderCards();

        //------------------------------------------------
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
        if (leaderCards == null) {
            return "ThinPlayer{" + "\n" +
                    "nickName='" + nickName + '\'' +"\n" +
                    ", thinTrack " + track + "\n" +
                    '}' + "\n" ;
        }
        return "ThinPlayer{" + "\n" +
                "nickName='" + nickName + '\'' +"\n" +
                "warehouse" + warehouse + "\n" +
                ", strongbox=" + strongbox + "\n" +
                ", leaderCards=" + leaderCards + "\n" +
                ", productionPower=" + productionPower + "\n" +
                ", thinTrack " + track + "\n" +
                '}' + "\n" ;
    }
}
