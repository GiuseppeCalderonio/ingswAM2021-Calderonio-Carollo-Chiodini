package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.PlayerAndComponents.Player;

import java.util.Arrays;

/**
 * this class represent the thin track.
 * in particular, it is used from the client to represent
 * the track with only the position and the pope favour tiles
 */
public class ThinTrack {

    /**
     * this attribute represent the position on the track
     */
    private int position;

    /**
     * this attribute represent the state of every pope favour tile (true = active, false = inactive)
     */
    private final boolean[] popeFavourTiles;

    /**
     * this constructor create the track starting from a player, getting from him all
     * the attributes needed
     * @param player this is the player from which get the pope favour tiles state and the position
     */
    public ThinTrack(Player player){
        this.position = player.getPersonalTrack().getPosition();
        this.popeFavourTiles = getVectorPopeFavourTiles(player);
    }

    /**
     * this method get an array of 3 booleans associated with the 3 pope favour tiles
     * starting from a player
     * @param player this is the player from which get the pope favour tiles state
     * @return an array of 3 booleans associated with the 3 pope favour tiles
     */
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

    /**
     * this method get the state of the pope favour tiles
     * @return the state of the pope favour tiles
     */
    public boolean[] getPopeFavourTiles() {
        return popeFavourTiles;
    }

    /**
     * this method get the position of the player in the track
     * @return the position of the player in the track
     */
    public int getPosition() {
        return position;
    }

    //TODELETE ONLT FOR TESTING
    public void add(int a){
        position = position + a;
    }
}
