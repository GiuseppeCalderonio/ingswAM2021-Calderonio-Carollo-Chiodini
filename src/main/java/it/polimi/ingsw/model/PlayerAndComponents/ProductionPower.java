package it.polimi.ingsw.model.PlayerAndComponents;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.stream.IntStream;

/**
 * this class represent the production power associated with the dashboard
 */
@SuppressWarnings("unchecked")
public class ProductionPower {

    /**
     * this attribute represent the cards of the dashboard to activate
     */
    private final Stack<DevelopmentCard>[] personalCards;

    /***
     * this attribute indicates if the basic production has been activated during the last turn
     */
    private boolean basicProductionActivated = false;

    /**
     * this constructor create an empty stack of cards
     */
    public ProductionPower() {
        personalCards = new Stack[3];
        personalCards[0] = new Stack<>();
        personalCards[1] = new Stack<>();
        personalCards[2] = new Stack<>();
    }

    /**
     * this constructor is needed to create a run-time change of the
     * dynamic type of the class from ProductionPower to LeaderProduction
     * @param personalCards these are the personal cards to set
     */
    protected ProductionPower(Stack<DevelopmentCard>[] personalCards){
        this.personalCards = personalCards;
    }

    /**
     * this method get the personal cards
     * @return the personal cards
     */
    public Stack<DevelopmentCard>[] getPersonalCards() {
        return personalCards;
    }

    /**
     * this method place a development card in the stack selected by position
     * @param toPlace this is the card to place
     * @param position this is the position of the array of card in which the method have to insert the card in input, it should go from 1 to 3
     * @return the result of the method checkPlacement
     */
    public boolean placeCard(DevelopmentCard toPlace, int position){
        if (!checkPlacement(toPlace, position)) return false;
        personalCards[position-1].push(toPlace);
        return true;
    }

    /**
     * this method get the card at position specified at the position selected if it exists
     * @param position this is the position of the array of cards, it goes from 1 to 3
     * @return the card at position specified by the parameter if it exists, null if the deck is empty
     */
    public DevelopmentCard getCard(int position){
        if (position < 1 || position > 3) return null;
        if (personalCards[position-1].empty()) return null;
       return personalCards[position-1].peek();
    }

    /**
     * this method verify if a placement of a card is legit or not
     * @param toCheck this is the card to check, it requires that is not null
     * @param position this is the position of the array of card in which the method have to check, it should go from 1 to 3
     * @return true if the level of the card in input is a level up compared to the card in the position in input or
     *         the card in input has a level one and the deck of cards specified in input is empty, false if not,
     *         or if the position is not between 1 and 3
     */
    public boolean checkPlacement(DevelopmentCard toCheck, int position){
        if (position < 1 || position > 3) return false;
        if (toCheck.getLevel() > 3 || toCheck.getLevel() < 1) return false;
        if ((this.getCard(position) == null)) return toCheck.getLevel() == 1;
        return (this.getCard(position).getLevel() == toCheck.getLevel() - 1);
    }

    /**
     * this method get the number of cards owned
     * @return the total number of cards in the array of stacks
     */
    public int getNumOfCards(){
        //int toReturn = 0;
        return Arrays.stream(personalCards).
                flatMapToInt(x -> IntStream.of(x.stream().
                        flatMapToInt(y -> IntStream.of(1)).
                        sum())).
                sum();
    }

    /**
     * this method get all the victory points of the cards in the array
     * @return the sum of all the victory points referred to every card contained in the array
     */
    public int getVictoryPoints(){
        //int toReturn = 0;
        return Arrays.stream(personalCards).
                flatMapToInt(x -> IntStream.of(x.stream().
                        flatMapToInt(y -> IntStream.of(y.getVictoryPoints())).
                        sum())).
                sum();
    }

    /**
     * this method is empty because needs to be overried by the subclass LeaderProduction
     * @param index this is the index of the resource that should be got
     * @return null
     */
    public Resource getInput(int index){
            return null;
    }

    /**
     * this method is empty because needs to be overried by the subclass LeaderProduction
     * @param toAdd this is the resource to add
     */
    public void addLeaderProduction(Resource toAdd){ }

    /**
     * this method activate the production for the card selected.
     * in particular, it simply set the attribute productionActivated of the card
     * with the position specified in the parameter to true.
     * if the card selected does not exist, do nothing
     *
     * @param toActivate this is the index of the card to activate
     */
    public void activateProduction(int toActivate){
        try {
            personalCards[toActivate - 1].peek().setProductionActivated();
        } catch (EmptyStackException | IndexOutOfBoundsException ignored){ }
    }

    /**
     * this method activate the basic production.
     * in particular, it simply set the attribute basicProductionActivated to true
     */
    public void activateBasicProduction(){
        basicProductionActivated = true;
    }

    /**
     * this method activate the leader production if it exist, do nothing
     * if the leader production does not exist.
     * in particular, it simply set to true the attribute leaderProductionActivated
     * associated with the index specified in input
     * @param toActivate this is the index associated with the
     *                  leader production to activate, it should be from 1 to 2
     */
    public void activateLeaderProduction(int toActivate){ }

    /**
     * this method reset the productions for every card (counting leader ones) of the dashboard and the basic production,
     * so that the turn after the owner of the card can activate them again
     */
    public void resetProductions(){
        basicProductionActivated = false;
        Arrays.stream(personalCards).
                filter(card -> !card.empty()).
                forEach(card -> card.peek().resetProduction());
    }

    /**
     * this method verify if the card in position specified by the parameter
     * can be activated
     * if the card selected does not exist, return false
     *
     * @param toCheck this is the position of the card to check
     * @return true if the card has been already activated during the turn, false otherwise
     */
    public boolean isProductionActivated(int toCheck){
        try {
            return personalCards[toCheck - 1].peek().isProductionActivated();
        } catch (IndexOutOfBoundsException | EmptyStackException ignored) {
            return false;
        }
    }

    /**
     * this method verify if the basic production can be activated
     * @return true if the basic production has been already activated during the turn, false otherwise
     */
    public boolean isBasicProductionActivated(){
        return basicProductionActivated;
    }

    /**
     * this method verify if the leader production associated with the index in input
     * is active or not.
     * the method always return false when the card doesn't exist
     * @param toCheck this is the index associated with the leader production to activate
     * @return true if the leader production has already been activated during the turn, false otherwise
     */
    public boolean isLeaderProductionActivated(int toCheck){
        return false;
    }



    @Override
    public String toString() {
        return "ProductionPower :" + "\n" +
                "basic : (?) + (?) = (?)" + "\n" +
                "personalCards=" + Arrays.toString(personalCards);
    }
}