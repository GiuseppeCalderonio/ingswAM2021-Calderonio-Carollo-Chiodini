package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.PlayerAndComponents.Player;

import java.util.Arrays;

public class ThinTrack {

    private final int position;
    private final boolean[] popeFavourTiles;

    public ThinTrack(Player player){
        this.position = player.getPersonalTrack().getPosition();
        this.popeFavourTiles = getVectorPopeFavourTiles(player);
    }

    private boolean[] getVectorPopeFavourTiles(Player player){
        boolean[] toReturn = new boolean[3];
        for (int i = 1; i <= 3; i++) {
            toReturn[i - 1] = player.getPersonalTrack().getPopeFavorTiles(i).getActive();
        }
        return toReturn;
    }

    @Override
    public String toString() {
        return "ThinTrack{" +
                "position=" + position +  "\n" +
                ", popeFavourTiles=" + Arrays.toString(popeFavourTiles) +  "\n" +
                '}';
    }
}
