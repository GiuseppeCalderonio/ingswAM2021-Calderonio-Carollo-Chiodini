package it.polimi.ingsw.model.FaithPointsManager;

/**
 * this class describe PopeFavorTile, indeed this class contains all attributes of a PopeFavorTile and all getters and
 * setters of this attribute
 */
public class PopeFavorTile {

    /**
     * this attribute indicates victoryPoints of each PopeFavorTile
     */
    private final int victoryPoints;

    /**
     * this attribute indicates the last position in the track associate to a PopeFavorTile
     */
    private final int endPosition;

    /**
     * this attribute indicates the first position in the track associate to a PopeFavorTile
     */
    private final int startPosition;

    /**
     * this attribute indicates if a PopeFavorTile is active: true if is active, else false
     */
    private boolean isActive;

    /**
     * this is the constructor of the class, it initialize all attribute to theirs default value
     * @param victoryPoints these are the victory point to set
     * @param endPosition this is the start position for the pope favour tile
     * @param startPosition this is the end position for the pope favour tile
     */
    public PopeFavorTile(int victoryPoints, int endPosition, int startPosition){
        this.victoryPoints = victoryPoints;
        this.endPosition = endPosition;
        this.startPosition = startPosition;
        isActive = false;
    }

    /**
     * this method set the state of isActive starting from the position.
     * in particular, when the position is > = of the start position of this object,
     * set it to true, set it to false otherwise
     * @param position this is the position of the owner of this pope favour tile
     */
    public void setActive(int position){
        isActive = position >= startPosition;
    }

    /**
     * this method return the state of isActive
     * @return true if the pope favour tile is active, false otherwise
     */
    public boolean getActive(){
        return isActive;
    }

    /**
     * this method get and return the number of PopeFavorTile victoryPoints
     * @return number of PopeFavorTile victoryPoints
     */
    public int getVictoryPoints(){
        return victoryPoints;
    }

    /**
     * this method get the ending position of the pope favour tile
     * @return the ending position of the poe favour tile
     */
    public int getEndPosition() {
        return endPosition;
    }

    /**
     * this method get the starting position of the pope favour tile
     * @return the starting position of the poe favour tile
     */
    public int getStartPosition() {
        return startPosition;
    }
}
