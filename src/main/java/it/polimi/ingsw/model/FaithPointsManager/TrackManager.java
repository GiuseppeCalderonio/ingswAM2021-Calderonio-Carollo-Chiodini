package it.polimi.ingsw.model.FaithPointsManager;

import java.util.Arrays;

/**
 * this class represent the track manager
 */
public class TrackManager {

    /**
     * this attribute represent the player position
     */
    private int position;

    /**
     * this method represent the 3 pope favor tiles
     */
    private final PopeFavorTile[] popeFavorTiles;

    /**
     * this attribute represent the track as an int vector, in particular every
     * cell of the vector contains the victory points associated with the position
     */
    private final int[] track;

    /**
     * this constructor create the track initialising the position at 0
     */
    public TrackManager(){
        position = 0;
        popeFavorTiles = new PopeFavorTile[3];
        popeFavorTiles[0] = new PopeFavorTile(2, 8,5);
        popeFavorTiles[1] = new PopeFavorTile(3, 16,12);
        popeFavorTiles[2] = new PopeFavorTile(4, 24,19);
        track = new int[]{0,0,0,1,1,1,2,2,2,4,4,4,6,6,6,9,9,9,12,12,12,16,16,16,20};
    }

    /**
     * this method increase the position in the faith track
     * @param toAdd these are the faith points to add to the position
     */
    public void positionProgress(int toAdd){
        int toSet = position + toAdd;
        position = Math.min(toSet, 24);
    }

    /**
     * this method get the position of the player
     * @return the position of the player
     */
    public int getPosition() {
        return position;
    }

    /**
     * this method set the pope favor tile specified by the parameter if
     * the position of the player is higher than the start position of the tile
     * do anything otherwise
     * @param index this is the index of the pope favor tile to set
     */
    public void setPopeFavorTile(int index){
        popeFavorTiles[index-1].setActive(position);
    }

    /**
     * this method check if the pope favor tile specified in the input can be trowed
     * @param index this is the index of the pope favor tile to check
     * @return true if the position of the player is higher than
     * the end position of the pope favor tile, false otherwise
     */
    public boolean checkFavorTile(int index){
        return position >= popeFavorTiles[index-1].getEndPosition();
    }

    /**
     * this method get all the victory points relative to the track
     * in particular, it get the victory points based on the position on the track
     * and sum them to the victory points of every active pope favor tile
     * @return the victory points based on the position on the track
     * and sum them to the victory points of every active pope favor tile
     */
    public int getVictoryPoints(){
        return  track[position] +
                (int) Arrays.stream(popeFavorTiles).filter(PopeFavorTile::getActive).count();
    }

    /**
     * this method get the pope favor tile specified by the parameter
     * @param index this is the index of the pope favor tile to get
     * @return the pope favor tile specified by the parameter
     */
    public PopeFavorTile getPopeFavorTiles(int index) {
        return popeFavorTiles[index - 1];
    }

    @Override
    public String toString() {
        String toPrint1 = "[ ,  ,  , 1,  ,  , 2,  ,  , 4,  ,  , 6,  ,  , 9,  ,  , 12,   ,   , 16,   ,   , 20]";

        return "PersonalTrack :" + "\n" +
                "position=" + position + "\n" +
                "faithPointsPerPosition" + "\n" +
                toPrint1 + "\n" +
                "track=" + "\n" +
                Arrays.toString(track);
    }
}
