package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
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
 * every information to represent the state of a client in the game,
 * and contains a bit of logic of the game
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

    /**
     * this method get the leader cards of the player
     * @return the leader cards of the player
     */
    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    /**
     * this method get the thin leader cards of the player
     * @return the thin leader cards of the player
     */
    public List<ThinLeaderCard> getThinLeaderCards() {
        return thinLeaderCards;
    }

    /**
     * this method get the collection resources associated with the strongbox of the player
     * @return the collection resources associated with the strongbox of the player
     */
    public CollectionResources getStrongbox() {
        return strongbox;
    }

    /**
     * this method get the collection resources associated with the strongbox of the player
     * @param strongbox this is the collection resources to set
     */
    public void setStrongbox(CollectionResources strongbox) {
        this.strongbox = strongbox;
    }

    /**
     * this method set the leader cards of the player
     * @param leaderCards these are the leader card to set
     */
    public void setLeaderCards(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    /**
     * this method get the nickname of the player
     * @return the nickname of the player
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * this method get the thin production power associated with the player
     * @return the thin production power associated with the player
     */
    public ThinProductionPower getProductionPower() {
        return productionPower;
    }

    /**
     * this method set the thin production power associated with the player
     * @param productionPower this is the thin production power associated with the player to set
     */
    public void setProductionPower(ThinProductionPower productionPower) {
        this.productionPower = productionPower;
    }

    /**
     * this method get the thin track associated with the player
     * @return the thin track associated with the player
     */
    public ThinTrack getTrack() {
        return track;
    }

    /**
     * this method get the thin warehouse associated with the player
     * @return the thin warehouse associated with the player
     */
    public ThinWarehouse getWarehouse() {
        return warehouse;
    }

    /**
     * this method set the thin warehouse associated with the player
     * @param warehouse this is the thin warehouse associated with the player to set
     */
    public void setWarehouse(ThinWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * this method set the nickname associated with the player
     * @param nickName this is the nickname associated with the player to set
     */
    public void setNickName(String nickName){
        this.nickname = nickName;
    }

    /**
     * this method set the thin track associated with the player
     * @param track this is the thin track associated with the player to set
     */
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
        return new NewWhiteMarble(null , 0 , null, 0);
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

    /**
     * this method returns a list with all the leader cards of the game,
     * with their id in order to print them
     * @return a list with all the leader cards of the game
     */
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

    /**
     * this method verify if the player can activate one of his productions
     * @return true if the player can activate one of the productions, false otherwise
     */
    public boolean areProductionsAffordable(){

        if (isNormalProductionAffordable(productionPower.getProductionPower1()))
            return true;
        if (isNormalProductionAffordable(productionPower.getProductionPower2()))
            return true;
        if (isNormalProductionAffordable(productionPower.getProductionPower3()))
            return true;

        try {
            if (isLeaderProductionAffordable(leaderCards.get(0)))
                return true;
        } catch (NullPointerException | IndexOutOfBoundsException ignored){ }

        try {
            if (isLeaderProductionAffordable(leaderCards.get(1)))
                return true;
        } catch (NullPointerException | IndexOutOfBoundsException ignored){ }

        return isBasicProductionAffordable();
    }

    /**
     * this method get all the resources of the player
     * @return all the resources of the player
     */
    public CollectionResources getTotalResources(){
        CollectionResources toReturn = new CollectionResources();
        toReturn.sum(strongbox);
        toReturn.sum(warehouse.getFirstShelf());
        toReturn.sum(warehouse.getSecondShelf());
        toReturn.sum(warehouse.getThirdShelf());
        try {
            toReturn.sum(warehouse.getFourthShelf());
        } catch (NullPointerException ignored){ }

        try {
            toReturn.sum(warehouse.getFifthShelf());
        } catch (NullPointerException ignored){ }

        return toReturn;

    }

    /**
     * this method verify if the player can activate the basic production
     * @return true if the player have 2 resources or more, false otherwise
     */
    public boolean isBasicProductionAffordable(){
        return getTotalResources().getSize() >=2;
    }

    /**
     * this method verify if the player can activate the last card contained
     * in the list of cards passed in input
     * @param deck this is the list of cards to verify if the last one can be activated
     * @return true if the last card of the deck can be activated
     */
    public boolean isNormalProductionAffordable(List<DevelopmentCard> deck){

        try {
            return getTotalResources().containsAll(deck.get(deck.size() - 1).getProductionPowerInput());
        } catch (NullPointerException | IndexOutOfBoundsException ignored) {
            return false;
        }
    }

    /**
     * this method verify if the leader card passed in input can be activated by the player
     * @param toVerify this is the card to verify if the player can activate it
     * @return true if the card passed in input isn't null, if is active and of
     * new production type, and if the player contains the resource associated with the card
     */
    public boolean isLeaderProductionAffordable(LeaderCard toVerify){
        try {
            if (toVerify.isActive() && toVerify instanceof NewProduction){

                return getTotalResources().contains(leaderCards.get(leaderCards.indexOf(toVerify)).getResource());
            }
            return false;
        } catch (NullPointerException e){
            return false;
        }
    }

    /**
     * this method verify if the player own at least one leader card not active
     * @return true if the player own a not active leader card, false if the deck of leader cards
     * is empty or if all the leader cards of the player are active
     */
    public boolean areLeaderCardsAvailable(){
        if (leaderCards.isEmpty())
            return false;

        return leaderCards.stream().anyMatch(leaderCard -> !leaderCard.isActive());
    }


}
