package it.polimi.ingsw.model.LeaderCard;

import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.view.utilities.colors.BackColor;

import java.io.Serializable;
import java.util.List;

/**
 * this interface represent the leader card requirements
 */
public interface LeaderCardRequirements extends Serializable {

    /**
     * this method verify if a player can activate a leader
     * card
     * @param toVerify this is the player that the method have to check if
     *                 contains all the requirements
     * @return true if the player contains all the requirements
     *         false otherwise
     */
    boolean containsRequirements(RealPlayer toVerify);

    String toString();

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method returns the String that identifies the requirement
     */
    String identifier();

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the list of BackColor associated to the requirement
     */
    List<BackColor> colors();
}
