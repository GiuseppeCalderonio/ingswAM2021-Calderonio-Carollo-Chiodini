package it.polimi.ingsw.model.DevelopmentCards;

/**
 * this enumeration represent the color of the DevelopmentCard, they can be of four colors,
 * Green, Blue, Yellow, Purple
 */
public enum CardColor {

    /**
     * this attribute represent the green card color,
     * and the column index associated with the cards market = 0 (first column)
     */
    GREEN(0),

    /**
     * this attribute represent the blue card color,
     * and the column index associated with the cards market = 1 (second column)
     */
    BLUE(1),

    /**
     * this attribute represent the yellow card color,
     * and the column index associated with the cards market = 2 (third column)
     */
    YELLOW(2),

    /**
     * this attribute represent the purple card color,
     * and the column index associated with the cards market = 3 (fourth column)
     */
    PURPLE(3);


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

    /**
     * this method return the market index of the card associated
     * with the color, in order to find it on the matrix of cards
     * @return the market index associated with the color
     */
    public int getIndex(){return index;}
}
