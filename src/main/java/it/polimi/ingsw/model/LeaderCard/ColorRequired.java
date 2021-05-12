package it.polimi.ingsw.model.LeaderCard;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.view.utilities.colors.BackColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * this class represent the colors of the cards required for a player
 * to activate the card
 */
public class ColorRequired implements LeaderCardRequirements {

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

        return this.colors.stream().allMatch(colors::remove);


    }

    @Override
    public String toString() {
        return "CardColorsRequired :" + colors ;
    }

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the String that identifies the requirement
     */
    public String identifier() {
        return "col:";
    }

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method returns the list of BackColor associated to the requirement
     */
    @Override
    public List<BackColor> colors() {
        List<BackColor> list = new ArrayList<>();
        if (colors.get(0).equals(CardColor.BLUE))
            list.add(BackColor.ANSI_BRIGHT_BG_BLUE);
        else if (colors.get(0).equals(CardColor.GREEN))
            list.add(BackColor.ANSI_BRIGHT_BG_GREEN);
        else if(colors.get(0).equals(CardColor.PURPLE))
            list.add(BackColor.ANSI_BG_PURPLE);
        else list.add(BackColor.ANSI_BG_YELLOW);

        if (colors.get(1).equals(CardColor.BLUE))
            list.add(BackColor.ANSI_BRIGHT_BG_BLUE);
        else if (colors.get(1).equals(CardColor.GREEN))
            list.add(BackColor.ANSI_BRIGHT_BG_GREEN);
        else if(colors.get(1).equals(CardColor.PURPLE))
            list.add(BackColor.ANSI_BG_PURPLE);
        else list.add(BackColor.ANSI_BG_YELLOW);

        if (colors.size()==3) {
            if (colors.get(2).equals(CardColor.BLUE))
                list.add(BackColor.ANSI_BRIGHT_BG_BLUE);
            else if (colors.get(2).equals(CardColor.GREEN))
                list.add(BackColor.ANSI_BRIGHT_BG_GREEN);
            else if (colors.get(2).equals(CardColor.PURPLE))
                list.add(BackColor.ANSI_BG_PURPLE);
            else list.add(BackColor.ANSI_BG_YELLOW);
        }

        return list;

    }
}
