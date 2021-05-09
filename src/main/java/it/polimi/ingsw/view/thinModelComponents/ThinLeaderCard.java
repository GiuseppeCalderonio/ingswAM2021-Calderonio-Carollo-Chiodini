package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.Resources.Resource;

public class ThinLeaderCard {

    private int victoryPoints;
    private final boolean isActive;
    private Resource resource;

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

    public void hide(){
        victoryPoints = 0;
        resource = null;
    }
}
