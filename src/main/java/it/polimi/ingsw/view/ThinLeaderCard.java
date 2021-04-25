package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Resources.Resource;

public class ThinLeaderCard {

    private final int victoryPoints;
    private final boolean isActive;
    private final Resource resource;

    public ThinLeaderCard(boolean isActive, int victoryPoints, Resource resource) {
        this.isActive = isActive;
        this.victoryPoints = victoryPoints;
        this.resource = resource;
    }


    public boolean isActive() {
        return isActive;
    }

    public Resource getResource() {
        return resource;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }
}
