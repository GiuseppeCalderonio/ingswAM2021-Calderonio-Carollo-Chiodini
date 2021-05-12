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

/**
 * this class represent the thin player.
 * in particular, this class used only by client, contains
 * every information to represent the state of a client in the game
 */
public class ThinPlayer {

    /**
     * this attribute represent the nickname of the player
     */
    private String nickname;

    /**
     * this attribute represent the warehouse of the player
     */
    private ThinWarehouse warehouse;

    /**
     * this attribute represent the strongbox of the player
     */
    private CollectionResources strongbox;

    /**
     * this attribute represent the leader cards of the player
     */
    private List<LeaderCard> leaderCards;

    /**
     * this attribute represent the thin leader cards of the player
     */
    private List<ThinLeaderCard> thinLeaderCards;

    /**
     * this attribute represent the production power of the player
     */
    private ThinProductionPower productionPower;

    /**
     * this attribute represent the faith track of the player
     */
    private ThinTrack track;

    /**
     * this constructor create the player starting from a real player.
     * in particular, it get all the necessary data from the player, and set them as
     * the attributes of the class
     * @param player this is the player from which get all the data to set
     */
    public ThinPlayer(RealPlayer player){
        this.nickname = player.getNickname();
        this.warehouse = new ThinWarehouse(player);
        this.strongbox = player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources();
        this.thinLeaderCards = player.getPersonalLeaderCards().stream().map(LeaderCard::getThin).collect(Collectors.toList());
        this.productionPower = new ThinProductionPower(player);
        this.track = new ThinTrack(player);
    }

    /**
     * this constructor create the player starting from another thin player.
     * in particular, it is used because the leader cards have to be
     * reconverted from thin leader cards to leader cards, and this constructor do it
     * @param thinPlayer this is the thin player to represent
     */
    public ThinPlayer(ThinPlayer thinPlayer) {
        this.nickname = thinPlayer.nickname;
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

    /**
     * this constructor create a thin player starting from a player.
     * in particular, this constructor is used only in case of single game,
     * so that the nickname will be the nickname of lorenzo il magnifico, and
     * will be set his track
     * @param lorenzo this is the player representing lorenzo il magnifico from which
     *                get the track
     */
    public ThinPlayer(Player lorenzo){
        this.nickname = lorenzo.getNickname();
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

    public String getNickname() {
        return nickname;
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
        this.nickname = nickName;
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
        return nickname.equals(that.nickname);
    }

    @Override
    public String toString() {
        if (leaderCards == null) {
            return "ThinPlayer{" + "\n" +
                    "nickName='" + nickname + '\'' +"\n" +
                    ", thinTrack " + track + "\n" +
                    '}' + "\n" ;
        }
        return "ThinPlayer{" + "\n" +
                "nickName='" + nickname + '\'' +"\n" +
                "warehouse" + warehouse + "\n" +
                ", strongbox=" + strongbox + "\n" +
                ", leaderCards=" + leaderCards + "\n" +
                ", productionPower=" + productionPower + "\n" +
                ", thinTrack " + track + "\n" +
                '}' + "\n" ;
    }
}
