package it.polimi.ingsw.model;

/**
 * this class is used to define all matrix position to initialize marble market casually
 */
public class MarblePosition {
    /**
     * these attributes indicate row and column position respectively in marble market matrix
     */
    private final int rows;
    private final int columns;

    /**
     * this is the constructor
     * @param rows this is the number of rows
     * @param columns this is the number of columns
     */
    public MarblePosition (int rows , int columns){
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * this method get the column
     * @return the index of column
     */
    public int getColumns() {

        return columns;
    }

    /**
     * this method get the row
     * @return the index of row
     */
    public int getRows() {

        return rows;
    }
}
