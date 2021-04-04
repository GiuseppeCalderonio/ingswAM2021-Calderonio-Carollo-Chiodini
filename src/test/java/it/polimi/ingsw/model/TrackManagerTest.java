package it.polimi.ingsw.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * this class test all methods of trackManager class
 */
public class TrackManagerTest {
    /**
     * this test check the correct position of a player in track and his victoryPoints; only one player
     */
    @Test
    public void getPositionInTrackAndVictoryPoints(){
        TrackManager TM = new TrackManager();
        assertEquals(0, TM.getVictoryPoints());
        assertEquals(0 , TM.getPosition());
        TM.positionProgress(2);
        assertEquals(2 , TM.getPosition());
        assertEquals(0 , TM.getVictoryPoints());
        TM.positionProgress(4);
        assertEquals(2, TM.getVictoryPoints());
        TM.positionProgress(9);
        assertEquals(9 , TM.getVictoryPoints());
    }

    /**
     * this test check some particular add position of a player in track; only one player
     */
    @Test
    public void checkPositionProgress(){
        TrackManager TM = new TrackManager();
        TM.positionProgress(0);
        assertEquals(0 , TM.getPosition());
        assertEquals(0 , TM.getVictoryPoints());
        TM.positionProgress(26);
        assertEquals(24, TM.getPosition());
        assertEquals(20 , TM.getVictoryPoints());
        TrackManager TM2 = new TrackManager();
        TM2.positionProgress(23);
        assertEquals(23 , TM2.getPosition());
        assertEquals(16 , TM2.getVictoryPoints());
        TM2.positionProgress(2);
        assertEquals(24 , TM2.getPosition());
        assertEquals(20 , TM2.getVictoryPoints());
    }
    /**
     * this method check if a PopeFavorTile can be activated; only one player
     */
    @Test
    public void activationOfPopeFavorTile1(){
        TrackManager TM = new TrackManager();
        assertFalse(TM.checkFavorTile(1));
        assertFalse(TM.checkFavorTile(2));
        TM.positionProgress(7);
        assertFalse(TM.checkFavorTile(1));
        TM.positionProgress(8);
        assertFalse(TM.checkFavorTile(2));
        assertTrue(TM.checkFavorTile(1));
        assertFalse(TM.checkFavorTile(3));
        TM.positionProgress(9);
        assertTrue(TM.checkFavorTile(3));
        assertTrue(TM.checkFavorTile(1));
    }

    /**
     * this method check if a PopeFavorTile can be activated; only one player
     */
    @Test
    public void activationOfPopeFavorTile2(){
        TrackManager TM = new TrackManager();
        assertFalse(TM.checkFavorTile(3));
        assertFalse(TM.checkFavorTile(2));
        assertFalse(TM.checkFavorTile(1));
        TM.positionProgress(8);
        assertTrue(TM.checkFavorTile(1));
        assertFalse(TM.checkFavorTile(3));
        assertFalse(TM.checkFavorTile(2));
        TM.positionProgress(8);
        assertTrue(TM.checkFavorTile(1));
        assertFalse(TM.checkFavorTile(3));
        assertTrue(TM.checkFavorTile(2));
        TM.positionProgress(8);
        assertTrue(TM.checkFavorTile(1));
        assertTrue(TM.checkFavorTile(3));
        assertTrue(TM.checkFavorTile(2));
    }

    /**
     * this method check if a PopeFavorTile can be activated; only one player
     */
    @Test
    public void activationOfPopeFavorTile3() {
        TrackManager TM = new TrackManager();
        TM.positionProgress(7);
        assertFalse(TM.checkFavorTile(1));
        TM.positionProgress(1);
        assertTrue(TM.checkFavorTile(1));
        TM.positionProgress(7);
        assertFalse(TM.checkFavorTile(2));
        TM.positionProgress(1);
        assertTrue(TM.checkFavorTile(2));
        TM.positionProgress(7);
        assertFalse(TM.checkFavorTile(3));
        TM.positionProgress(1);
        assertTrue(TM.checkFavorTile(3));
    }

    /**
     * this method check if a PopeFavorTile is been activated correctly; only one player
     */
    @Test
    public void setPopeFavorTile1 () {
        TrackManager TM = new TrackManager();
        TM.setPopeFavorTile(1); TM.setPopeFavorTile(2); TM.setPopeFavorTile(3);
        assertFalse(TM.getPopeFavorTiles(1).getActive());
        assertFalse(TM.getPopeFavorTiles(2).getActive());
        assertFalse(TM.getPopeFavorTiles(3).getActive());
        TM.positionProgress(4);
        TM.setPopeFavorTile(1); TM.setPopeFavorTile(2); TM.setPopeFavorTile(3);
        assertFalse(TM.getPopeFavorTiles(1).getActive());
        assertFalse(TM.getPopeFavorTiles(2).getActive());
        assertFalse(TM.getPopeFavorTiles(3).getActive());
        TM.positionProgress(1);
        TM.setPopeFavorTile(1); TM.setPopeFavorTile(2); TM.setPopeFavorTile(3);
        assertTrue(TM.getPopeFavorTiles(1).getActive());
        assertFalse(TM.getPopeFavorTiles(2).getActive());
        assertFalse(TM.getPopeFavorTiles(3).getActive());
        TM.positionProgress(6);
        TM.setPopeFavorTile(1); TM.setPopeFavorTile(2); TM.setPopeFavorTile(3);
        assertTrue(TM.getPopeFavorTiles(1).getActive());
        assertFalse(TM.getPopeFavorTiles(2).getActive());
        assertFalse(TM.getPopeFavorTiles(3).getActive());

    }

    /**
     * this method check if a PopeFavorTile is been activated correctly; only one player
     */
    @Test
    public void setPopeFavorTile2 () {
        TrackManager TM = new TrackManager();
        TM.positionProgress(12);
        TM.setPopeFavorTile(1); TM.setPopeFavorTile(2); TM.setPopeFavorTile(3);
        assertTrue(TM.getPopeFavorTiles(1).getActive());
        assertTrue(TM.getPopeFavorTiles(2).getActive());
        assertFalse(TM.getPopeFavorTiles(3).getActive());
        TM.positionProgress(6);
        TM.setPopeFavorTile(1); TM.setPopeFavorTile(2); TM.setPopeFavorTile(3);
        assertTrue(TM.getPopeFavorTiles(1).getActive());
        assertTrue(TM.getPopeFavorTiles(2).getActive());
        assertFalse(TM.getPopeFavorTiles(3).getActive());
        TM.positionProgress(1);
        TM.setPopeFavorTile(1); TM.setPopeFavorTile(2); TM.setPopeFavorTile(3);
        assertTrue(TM.getPopeFavorTiles(1).getActive());
        assertTrue(TM.getPopeFavorTiles(2).getActive());
        assertTrue(TM.getPopeFavorTiles(3).getActive());
    }
}

