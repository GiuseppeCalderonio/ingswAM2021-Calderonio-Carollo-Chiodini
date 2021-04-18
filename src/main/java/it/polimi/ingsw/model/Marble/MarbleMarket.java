package it.polimi.ingsw.model.Marble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this class represents the market of marbles
 */
public class MarbleMarket {

    /**
     * this attribute represent the market of marble as a matrix
     */
    private final Marble[][] marketTray;

    /**
     * this attribute represent the lonely marble that is used to fill spaces after a transaction of the market
     */
    private Marble lonelyMarble;

    /**
     * this attribute represent the column dimension of the marble matrix
     */
    private final int columnDimension;

    /**
     * this attribute represent the row dimension of the marble matrix
     */
    private final int rowDimension;

    /**
     * this is the constructor of the class, in particular, it create a casual market calling the method setCasualMatrix
     */
    public MarbleMarket(){
        marketTray = new Marble[3][4];
        columnDimension = 4;
        rowDimension = 3;
        setCasualMatrix();
    }

    /**
     * this method create and shuffle all the marbles of the matrix, in particular it creates a random lookup table
     * and for each marble assign a random position
     */
    public void setCasualMatrix (){
        List<MarblePosition> matrixPosition = createPositionMatrix();
        List<Marble> marbles = createMarbleList();
        Collections.shuffle(matrixPosition);
        Collections.shuffle(marbles);
        for (int i = 0 ; i < 13 ; i++)
        {
            if (matrixPosition.get(i).getRows() == -1)
                setLonelyMarble(marbles.get(i));
            else
                setMarble(matrixPosition.get(i).getRows() , matrixPosition.get(i).getColumns() , marbles.get(i) );
        }
    }

    /**
     * this method define all matrix positions, is used to create positions to prepare the shuffle
     * @return a list of matrix positions
     */
    private List<MarblePosition> createPositionMatrix(){
        List<MarblePosition> matrixPosition = new ArrayList<>();
        matrixPosition.add(new MarblePosition(0,0));  matrixPosition.add(new MarblePosition(0,1));  matrixPosition.add(new MarblePosition(0,2)); matrixPosition.add(new MarblePosition(0,3));
        matrixPosition.add(new MarblePosition(1,0)); matrixPosition.add(new MarblePosition(1,1)); matrixPosition.add(new MarblePosition(1,2)); matrixPosition.add(new MarblePosition(1,3));
        matrixPosition.add(new MarblePosition(2,0)); matrixPosition.add(new MarblePosition(2,1)); matrixPosition.add(new MarblePosition(2,2)); matrixPosition.add(new MarblePosition(2,3));
        matrixPosition.add(new MarblePosition(-1,-1));
        return matrixPosition;
    }

    /**
     * this method define all marbles of the game, is used to create marble to prepare the shuffle
     * @return a list of all marbles
     */
    private List<Marble> createMarbleList (){
        List<Marble> insert = new ArrayList<>();
        insert.add(new WhiteMarble());  insert.add(new WhiteMarble());  insert.add(new WhiteMarble());  insert.add(new WhiteMarble());
        insert.add(new YellowMarble()); insert.add(new YellowMarble());  insert.add(new BlueMarble());  insert.add(new BlueMarble());
        insert.add(new GreyMarble());   insert.add(new GreyMarble());   insert.add(new PurpleMarble());  insert.add(new PurpleMarble());
        insert.add(new RedMarble());
        return insert;
    }

    /**
     * this method get a column of resources from market and shifts up the marbles of one position,
     * when the first position is filled by the lonely marble, and the lonely marble position is filled by
     * the marble at the last position
     * @param column this is the column chosen to get and shift
     * @return all the marbles contained in the selected column, null if the input is not between 0 and 3
     */
    public List<Marble> selectColumn(int column){
        if (column < 0 || column > 3) return null;
        List<Marble> selectedColumn = new ArrayList<>();
        Marble temp;
        for(int r = rowDimension-1 ; r >= 0 ; r--)
        {
            selectedColumn.add(getMarble(r , column));
            temp = getMarble(r , column);
            setMarble(r , column , lonelyMarble);
            setLonelyMarble(temp);
        }
        return selectedColumn;
    }

    /**
     * this method get a row of resources from market and shifts left the marbles of one position,
     * when the first position is filled by the lonely marble, and the lonely marble position is filled by
     * the marble at the last position
     * @param row this is the row chosen to get and shift
     * @return all the marbles contained in the selected row, null if the input is not between 0 and 3
     */
    public List<Marble> selectRow (int row){
        if (row < 0 || row > 2) return null;
        List<Marble> selectedRow = new ArrayList<>();
        Marble temp;
        for (int c = columnDimension-1 ; c >= 0; c--)
        {
            selectedRow.add(getMarble(row , c));
            temp = getMarble(row , c);
            setMarble(row , c , lonelyMarble);
            setLonelyMarble(temp);
        }
        return selectedRow;
    }

    /**
     * this method set a marble in the matrix at the position selected
     * it is private because it is used just to create the initial casual market
     * @param row this is the row selected
     * @param column this is the column selected
     * @param toSet this is the marble to set
     */
    private void setMarble (int row , int column, Marble toSet){

        marketTray[row][column] = toSet;
    }

    /**
     * this method set the lonely marble
     * it is private because it is used just to create the initial casual market
     * @param lonelyMarble this is the lonely marble to set
     */
    private void setLonelyMarble (Marble lonelyMarble){

        this.lonelyMarble = lonelyMarble;
    }

    /**
     * this method get the column dimension of the matrix
     * @return the column dimension of the matrix
     */
    public int getColumnDimension() {

        return columnDimension;
    }

    /**
     * this method get the row dimension of the matrix
     * @return the row dimension of the matrix
     */
    public int getRowDimension() {

        return rowDimension;
    }

    /**
     * this method get the lonely marble
     * @return the lonely marble
     */
    public Marble getLonelyMarble() {

        return lonelyMarble;
    }

    /**
     * this method get the marble matrix
     * @return the marble matrix
     */
    public Marble[][] getMarketTray() {

        return marketTray;
    }

    /**
     * this method get the marble from the matrix at the position selected
     * @param row this is the row selected
     * @param column this is the column selected
     * @return the marble from the matrix at the row and column selected
     */
    public Marble getMarble (int row , int column) {

        return marketTray[row][column];
    }
}
