package it.polimi.ingsw.model.LeaderCard;

import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;

/**
 * this interface represent the leader card requirements
 */
public interface LeaderCardRequirements {

    /**
     * this method verify if a player can activate a leader
     * card
     * @param toVerify this is the player that the method have to check if
     *                 contains all the requirements
     * @return true if the player contains all the requirements
     *         false otherwise
     */
    boolean containsRequirements(RealPlayer toVerify);
}
