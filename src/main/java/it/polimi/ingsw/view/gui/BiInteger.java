package it.polimi.ingsw.view.gui;

/**
 * this class represent a container of two integers.
 * it is used to represent two coordinates to draw positions on the gui
 */
public class BiInteger {

    /**
     * this attribute represent the x coordinate
     */
    private final double x;

    /**
     * this attribute represent the y coordinate
     */
    private final double y;

    /**
     * this constructor create the object starting from the coordinates to set
     * @param x this is the x coordinate to set
     * @param y this is the y coordinate to set
     */
    public BiInteger( double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * this method get the x coordinate
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * this method get the y coordinate
     * @return the y coordinate
     */
    public double getY() {
        return y;
    }
}
