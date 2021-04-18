package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Marble.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the methods of the market of marbles
 */
class MarbleMarketTest {
    /**
     * this test verify that the market contains all the marble correctly
     */
    @Test
    public void testContainsAllMarbles(){
        MarbleMarket market = new MarbleMarket();
        Marble toCompare;
        int white = 0, red = 0, blue = 0, yellow = 0, purple = 0, grey = 0
                , rowDim = market.getRowDimension(), columnDim = market.getColumnDimension();
        for (int row = 0; row < rowDim; row++){
            for (int column = 0; column < columnDim; column++){
                toCompare = market.getMarble(row,column);
                if(toCompare instanceof WhiteMarble) white++;
                if(toCompare instanceof RedMarble) red++;
                if(toCompare instanceof PurpleMarble) purple++;
                if(toCompare instanceof GreyMarble) grey++;
                if(toCompare instanceof YellowMarble) yellow++;
                if(toCompare instanceof BlueMarble) blue++;
            }
        }
        toCompare = market.getLonelyMarble();
        if(toCompare instanceof WhiteMarble) white++;
        if(toCompare instanceof RedMarble) red++;
        if(toCompare instanceof PurpleMarble) purple++;
        if(toCompare instanceof GreyMarble) grey++;
        if(toCompare instanceof YellowMarble) yellow++;
        if(toCompare instanceof BlueMarble) blue++;

        assertEquals(4 , white);
        assertEquals(2 , grey);
        assertEquals(2 , purple);
        assertEquals(2 , yellow);
        assertEquals(2 , blue);
        assertEquals(1 , red);
    }

    /**
     * this test verify that the creation of a market is casual
     */
    @Test
    public void testCasualMarket(){
        MarbleMarket market1;
        MarbleMarket market2;
        float counter;
        float toCheck = 0;
        for (int i = 1; i <= 100 ; i++) {
            market1 = new MarbleMarket();
            market2 = new MarbleMarket();
            counter = 0;
            for (int row = 0; row < market1.getRowDimension(); row++){
                for (int column = 0; column < market1.getColumnDimension(); column++){
                    if(!(market1.getMarble(row , column).equals(market2.getMarble(row , column)))) counter ++;
                }
            }
            if(!market1.getLonelyMarble().equals(market2.getLonelyMarble())) counter ++;
            float row = market1.getRowDimension(), column = market1.getColumnDimension();
            toCheck = (toCheck + counter / (row * column + 1)) / 2;
        }
        assertTrue(toCheck < 1.0 && toCheck > 0.0);

    }

    /**
     * this test verify that the marble exchange of the column is executed correctly for the first one
     */
    @Test
    public void testSelectColumn1(){
        MarbleMarket market = new MarbleMarket();
        Marble marble1 = market.getMarble(0 ,0);
        Marble marble2 = market.getMarble(1,0);
        Marble marble3 = market.getMarble(2,0);
        Marble lonely = market.getLonelyMarble();
        market.selectColumn(0);
        assertEquals(marble1, market.getLonelyMarble());
        assertEquals(marble2, market.getMarble(0, 0));
        assertEquals(marble3, market.getMarble(1, 0));
        assertEquals(lonely, market.getMarble(2, 0));
    }

    /**
     * this test verify that the method selectColumn return an arraylist with
     * all and only the marbles in the column selected
     */
    @Test
    public void testSelectColumn2(){
        MarbleMarket market = new MarbleMarket();
        List<Marble> staticMarbles = new ArrayList<>();
        List<Marble> dynamicMarbles;
        staticMarbles.add( market.getMarble(0 ,0));
        staticMarbles.add( market.getMarble(1 ,0));
        staticMarbles.add( market.getMarble(2 ,0));
        dynamicMarbles = market.selectColumn(0);
        assertTrue(dynamicMarbles.containsAll(staticMarbles));
        assertTrue(staticMarbles.containsAll(dynamicMarbles));
    }

    /**
     * this test verify that a row of marbles when for each column the method selectColumn is called
     * shifts up of one position
     */
    @Test
    public void testSelectColumn3(){
        MarbleMarket market = new MarbleMarket();
        Marble marble1 = market.getMarble(2,0);
        Marble marble2 = market.getMarble(2,1);
        Marble marble3 = market.getMarble(2,2);
        Marble marble4 = market.getMarble(2,3);
        Marble lonely = market.getLonelyMarble();
        market.selectColumn(0);
        assertEquals(lonely, market.getMarble(2,0));
        lonely = market.getLonelyMarble();
        market.selectColumn(1);
        assertEquals(lonely, market.getMarble(2,1));
        lonely = market.getLonelyMarble();
        market.selectColumn(2);
        assertEquals(lonely, market.getMarble(2,2));
         lonely = market.getLonelyMarble();
        market.selectColumn(3);
        assertEquals(lonely, market.getMarble(2,3));
        assertEquals(marble1, market.getMarble(1, 0));
        assertEquals(marble2, market.getMarble(1, 1));
        assertEquals(marble3, market.getMarble(1, 2));
        assertEquals(marble4, market.getMarble(1, 3));
    }

    /**
     * this test verify that the market has a cyclic behaviour,
     * in particular, after 5 calling of the method selectColumn the market return to his original state
     */
    @Test
    public void testRotateColumn(){
        MarbleMarket market = new MarbleMarket();
        Marble marble1 = market.getMarble(0 ,1);
        Marble marble2 = market.getMarble(1,1);
        Marble marble3 = market.getMarble(2,1);
        Marble lonely = market.getLonelyMarble();
        market.selectColumn(2);
        market.selectColumn(2);
        market.selectColumn(2);
        market.selectColumn(2);
        assertEquals(lonely, market.getLonelyMarble());
        assertEquals(marble1, market.getMarble(0, 1));
        assertEquals(marble2, market.getMarble(1, 1));
        assertEquals(marble3, market.getMarble(2, 1));
    }

    /**
     * this test verify that the marble exchange of the row is executed correctly for the first one
     */
    @Test
    public void testSelectRow1(){
        MarbleMarket market = new MarbleMarket();
        Marble marble1 = market.getMarble(0 ,0);
        Marble marble2 = market.getMarble(0,1);
        Marble marble3 = market.getMarble(0,2);
        Marble marble4 = market.getMarble(0,3);
        Marble lonely = market.getLonelyMarble();
        market.selectRow(0);
        assertEquals(marble1, market.getLonelyMarble());
        assertEquals(marble2, market.getMarble(0, 0));
        assertEquals(marble3, market.getMarble(0, 1));
        assertEquals(marble4, market.getMarble(0, 2));
        assertEquals(lonely, market.getMarble(0, 3));
    }

    /**
     * this test verify that the method selectRow return an arraylist with
     * all and only the marbles in the row selected
     */
    @Test
    public void testSelectRow2(){
        MarbleMarket market = new MarbleMarket();
        List<Marble> staticMarbles = new ArrayList<>();
        List<Marble> dynamicMarbles;
        staticMarbles.add( market.getMarble(0 ,0));
        staticMarbles.add( market.getMarble(0 ,1));
        staticMarbles.add( market.getMarble(0 ,2));
        staticMarbles.add( market.getMarble(0 ,3));
        dynamicMarbles = market.selectRow(0);
        assertTrue(dynamicMarbles.containsAll(staticMarbles));
        assertTrue(staticMarbles.containsAll(dynamicMarbles));
    }

    /**
     * this test verify that a column of marbles when for each row the method selectRow is called
     * shifts left of one position
     */
    @Test
    public void testSelectRow3(){
        MarbleMarket market = new MarbleMarket();
        Marble marble1 = market.getMarble(0,3);
        Marble marble2 = market.getMarble(1,3);
        Marble marble3 = market.getMarble(2,3);
        Marble lonely = market.getLonelyMarble();
        market.selectRow(0);
        assertEquals(lonely, market.getMarble(0,3));
        lonely = market.getLonelyMarble();
        market.selectRow(1);
        assertEquals(lonely, market.getMarble(1,3));
        lonely = market.getLonelyMarble();
        market.selectRow(2);
        assertEquals(lonely, market.getMarble(2,3));
        assertEquals(marble1, market.getMarble(0, 2));
        assertEquals(marble2, market.getMarble(1, 2));
        assertEquals(marble3, market.getMarble(2, 2));
    }

    /**
     * this test verify that the market has a cyclic behaviour,
     * in particular, after 5 calling of the method selectRow the market return to his original state
     */
    @Test
    public void testRotateRow(){
        MarbleMarket market = new MarbleMarket();
        Marble marble1 = market.getMarble(2,0);
        Marble marble2 = market.getMarble(2,1);
        Marble marble3 = market.getMarble(2,2);
        Marble marble4 = market.getMarble(2,3);
        Marble lonely = market.getLonelyMarble();
        market.selectRow(2);
        market.selectRow(2);
        market.selectRow(2);
        market.selectRow(2);
        market.selectRow(2);
        assertEquals(lonely, market.getLonelyMarble());
        assertEquals(marble1, market.getMarble(2, 0));
        assertEquals(marble2, market.getMarble(2, 1));
        assertEquals(marble3, market.getMarble(2, 2));
        assertEquals(marble4, market.getMarble(2, 3));
    }

    /**
     * this test verify that the method selectRow return null if the input is not between 0 and 2
     */
    @Test
    public void testNullRow(){
        MarbleMarket market = new MarbleMarket();
        assertNull(market.selectRow(-2));
        assertNull(market.selectRow(-1));
        assertNull(market.selectRow(6));
        assertNull(market.selectRow(3));
        assertNotNull(market.selectRow(2));
    }

    /**
     * this test verify that the method selectColumn return null if the input is not between 0 and 3
     */
    @Test
    public void testNullColumn(){
        MarbleMarket market = new MarbleMarket();
        assertNull(market.selectColumn(-2));
        assertNull(market.selectColumn(-1));
        assertNull(market.selectColumn(6));
        assertNull(market.selectColumn(4));
        assertNotNull(market.selectColumn(3));
    }
}