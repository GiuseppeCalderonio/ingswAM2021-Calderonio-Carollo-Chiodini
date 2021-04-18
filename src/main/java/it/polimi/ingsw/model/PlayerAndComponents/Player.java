package it.polimi.ingsw.model.PlayerAndComponents;

import it.polimi.ingsw.model.FaithPointsManager.TrackManager;

/**
 * this abstract class represent the methods of
 * lorenzo il Magnifico and a real player
 */
public class Player {

    /**
     * this attribute represent the track of the player
     */
    private final TrackManager personalTrack;

    public Player() {
        this.personalTrack = new TrackManager();
    }

    /**
     * this method adds faith points to the player
     * increasing his position on the track
     * @param toAdd these are the faith points to add
     */
    public void addFaithPoints(int toAdd){
        personalTrack.positionProgress(toAdd);
    }

    /**
     * this method verify if a player is in condition to
     * handle a vatican report relative to the
     * index-th pope favour tile
     * in particular, if the position of the player is higher or equals than the
     * end position of the tile
     * @param index this is index relative to the pope favour tile to check
     * @return true if the player is in condition to handle a vatican report,
     *         false otherwise
     */
    public boolean checkVaticanReport(int index){
        return personalTrack.checkFavorTile(index);
    }

    /**
     * this method set the pope favor tile index-th
     * if the player position is higher or equals than
     * the end position of the tile
     * @param index this is index relative to the pope favour tile to eventually set
     */
    public void vaticanReport(int index){
        personalTrack.setPopeFavorTile(index);
    }

    /**
     * this method get the personal track
     * @return the personal track
     */
    public TrackManager getPersonalTrack() {
        return personalTrack;
    }

    /**
     * this method get the nickname of lorenzo il magnifico
     * @return the string "LorenzoIlMagnifico"
     */
    public String getNickname(){
        return "LorenzoIlMagnifico";
    }
}
