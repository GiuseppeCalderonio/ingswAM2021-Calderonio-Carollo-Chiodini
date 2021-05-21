package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.LeaderCard.*;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public static LeaderCard recreate(ThinLeaderCard leaderCard){
        Resource resource = leaderCard.getResource();
        int victoryPoints = leaderCard.getVictoryPoints();
        for (LeaderCard card : createAllLeaderCards()){
            if (card.getResource().equals(resource) && card.getVictoryPoints() == victoryPoints){
                if (leaderCard.isActive())
                    card.setActive();
                return card;
            }
        }
        return new NewWhiteMarble(null , 0 , null, 49);
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

    public static List<LeaderCard> createAllLeaderCards(){

        List<LeaderCard> leaders = new ArrayList<>(); // create an arraylist of leaderCard
        LeaderCardRequirements requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.BLUE, CardColor.YELLOW)));
        LeaderCardRequirements requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.PURPLE, CardColor.GREEN)));
        LeaderCardRequirements requirements3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.GREEN, CardColor.PURPLE)));
        LeaderCardRequirements requirements4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.YELLOW, CardColor.BLUE)));

        //NewWhiteMarble leaderCards
        leaders.add(new NewWhiteMarble(requirements2,5,new Coin(), 60));
        leaders.add(new NewWhiteMarble(requirements1 ,5,new Stone(), 59));
        leaders.add(new NewWhiteMarble(requirements4,5,new Servant(), 57));
        leaders.add(new NewWhiteMarble(requirements3,5,new Shield(), 58));
        //NewShelf leaderCards
        leaders.add(new NewShelf(new ResourcesRequired(new Shield()),3,new Coin(), 56));
        leaders.add(new NewShelf(new ResourcesRequired(new Servant()),3,new Shield(), 55));
        leaders.add(new NewShelf(new ResourcesRequired(new Stone()),3,new Servant(), 54));
        leaders.add(new NewShelf(new ResourcesRequired(new Coin()),3,new Stone(), 53));
        //NewProduction leaderCards
        leaders.add(new NewProduction(new LevelRequired(CardColor.GREEN) ,4 , new Coin(), 64));
        leaders.add(new NewProduction(new LevelRequired(CardColor.PURPLE) ,4 , new Stone(), 63));
        leaders.add(new NewProduction(new LevelRequired(CardColor.BLUE) ,4 , new Servant(), 62));
        leaders.add(new NewProduction(new LevelRequired(CardColor.YELLOW) ,4 , new Shield(), 61));

        LeaderCardRequirements requirement1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.PURPLE)));
        LeaderCardRequirements requirement2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN , CardColor.BLUE)));
        LeaderCardRequirements requirement3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE , CardColor.PURPLE)));
        LeaderCardRequirements requirement4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.GREEN)));

        //NewDiscount leaderCards
        leaders.add(new NewDiscount(requirement1,2,new Coin(), 52));
        leaders.add(new NewDiscount(requirement2,2,new Stone(), 51));
        leaders.add(new NewDiscount(requirement3,2,new Shield(), 50));
        leaders.add(new NewDiscount(requirement4,2,new Servant(), 49));
        return leaders;
    }
}
