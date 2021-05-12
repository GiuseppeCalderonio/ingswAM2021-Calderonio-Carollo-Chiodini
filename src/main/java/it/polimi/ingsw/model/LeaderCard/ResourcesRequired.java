package it.polimi.ingsw.model.LeaderCard;

import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.view.utilities.colors.BackColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this class represent the resources required for a player
 * to activate a leader card
 */
public class ResourcesRequired implements LeaderCardRequirements {

    /**
     * this attribute represent the resources associated with
     * the leader card requirements
     */
    private final CollectionResources resources;

    /**
     * this constructor create a collectionResources of 5 elements
     * with the same type from a single resource
     * @param resource this is the resource from which
     *                 the constructor create the collection
     */
    public ResourcesRequired(Resource resource){
        resources = new CollectionResources();
        for (int i = 0; i < 5; i++) {
            resources.add(resource);
        }
    }


    /**
     * this method verify if a player can activate a leader
     * card
     * in particular, if the player contains all the resources required
     * @param toVerify this is the player that the method have to check if
     *                 contains all the requirements
     * @return true if the player contains all the requirements
     * false otherwise
     */
    @Override
    public boolean containsRequirements(RealPlayer toVerify) {
        return toVerify.getTotalResources().containsAll(resources);
    }

    @Override
    public String toString() {
        return "ResourcesRequired :" + resources ;
    }

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the String that identifies the requirement
     */
    public String identifier() {
        return "res:";
    }

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the list of BackColor associated to the requirement in this case the list contains the
     * BackColor associated to the resource
     */
    @Override
    public List<BackColor> colors() {
        return new ArrayList<>(Collections.singletonList(resources.asList().get(0).getColor()));
        /*
        List<BackColor> list = new ArrayList<>();
        list.add(resources.asList().get(0).getColor());
        if (resources.isCompatible(new Coin()))
            list.add(BackColor.ANSI_BG_YELLOW);
        else if (resources.isCompatible(new Shield()))
            list.add(BackColor.ANSI_BRIGHT_BG_BLUE);
        else if (resources.isCompatible(new Stone()))
            list.add(BackColor.ANSI_GREY);
        else list.add(BackColor.ANSI_BG_PURPLE);
        return list;

         */

    }
}
