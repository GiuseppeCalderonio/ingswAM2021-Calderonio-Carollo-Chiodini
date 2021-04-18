package it.polimi.ingsw.model.DevelopmentCards;

/**
 * this enumeration represent the color of the DevelopmentCard
 */
public enum CardColor {
    /**
     * this are the four colors for a development card with their abbreviation
     */
    GREEN(0),BLUE(1),YELLOW(2),PURPLE(3);
    /**
     * this attribute represent the index column of the card market associated with the color of the card
     */
    private final int index;


    /**
     * this constructor create the enumeration from the index
     * @param index is the index associated with the color
     */
    CardColor(int index) {
        this.index = index;
    }

    public int getIndex(){return index;}
}
