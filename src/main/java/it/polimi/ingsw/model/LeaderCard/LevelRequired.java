package it.polimi.ingsw.model.LeaderCard;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;

import java.util.*;

/**
 * this class represent the card color and the level of a card
 * required for a player to activate the card
 */
public class LevelRequired implements LeaderCardRequirements {

    /**
     * this attribute represent the color of a card that
     * a player must contain to activate the leader card
     */
    private final CardColor color;

    public LevelRequired(CardColor color) {
        this.color = color;
    }

    /**
     * this method verify if a player can activate a leader
     * card
     *
     * @param toVerify this is the player that the method have to check if
     *                 contains all the requirements
     * @return true if the player contains all the requirements
     * false otherwise
     */
    @Override
    public boolean containsRequirements(RealPlayer toVerify) {
        Stack<DevelopmentCard>[] cards = toVerify.getPersonalDashboard().getPersonalProductionPower().getPersonalCards();
        Map<CardColor, Integer> requirement = new HashMap<>();
        requirement.put(color, 2);
        List<Map<CardColor, Integer>> requirements = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for( DevelopmentCard card : cards[i]){
                Map<CardColor, Integer> temp = new HashMap<>();
                temp.put(card.getColor(), card.getLevel());
                requirements.add(temp);
            }
        }
        return requirements.contains(requirement);

    }
}
