package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * this class represent the colors of the cards required for a player
 * to activate the card
 */
public class ColorRequired implements LeaderCardRequirements{

    /**
     * this attributes represent the colors required to activate
     * the leader card as a list
     */
    private final List<CardColor> colors;

    public ColorRequired(List<CardColor> colors){
        this.colors = colors;
    }

    /**
     * this method verify if a player can activate a leader
     * card
     * in particular, verify if the player in input contains cards with
     * all the colors associated with the requirement
     * @param toVerify this is the player that the method have to check if
     *                 contains all the requirements
     * @return true if the player contains all the requirements
     * false otherwise
     */
    @Override
    public boolean containsRequirements(RealPlayer toVerify) {
        Stack<DevelopmentCard>[] cards = toVerify.getPersonalDashboard().getPersonalProductionPower().getPersonalCards();
        List<CardColor> colors = new ArrayList<>();
        Arrays.stream(cards).forEach(x -> x.forEach( y -> colors.add(y.getColor())));
        /*for (int i = 0; i < 3; i++) {
            cards[i].forEach(x -> colors.add(x.getColor()));
        } */

        return this.colors.stream().allMatch(colors::remove);
        /*
        for ( CardColor color : this.colors){
            if( !colors.remove(color) ) return false;
        }
        return true;*/
        /*for ( CardColor color : this.colors){
            if(colors.contains(color)) colors.remove(color);
        }

        return true;*/
        //return colors.stream().filter(x -> x.equals());
        //return colors.containsAll(this.colors);

    }
}
