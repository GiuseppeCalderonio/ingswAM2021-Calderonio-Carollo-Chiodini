package it.polimi.ingsw.model;

import java.util.Arrays;
import java.util.Stack;
import java.util.stream.IntStream;

/**
 * this class represent the production power associated with the dashboard
 */
public class ProductionPower {

    /**
     * this attribute represent the cards of the dashboard to activate
     */
    private final Stack<DevelopmentCard>[] personalCards;

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
     * @param toCheck this is the card to check
     * @param position this is the position of the array of card in which the method have to check, it should go from 1 to 3
     * @return true if the level of the card in input is a level up compared to the card in the position in input or
     * the card in input has a level one and the deck of cards specified in input is empty, false if not, or if the position is not between 1 and 3
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
       /* for (int i = 0; i < 3; i++)
            toReturn = toReturn + personalCards[i].stream().flatMapToInt(x -> IntStream.of(1)).sum();
        return toReturn;*/
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
        /*for (int i = 0; i < 3; i++)
            toReturn = toReturn + personalCards[i].stream().flatMapToInt(x -> IntStream.of(x.getVictoryPoints())).sum();
        return toReturn; */
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
}