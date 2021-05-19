package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.Resources.Resource;

import java.util.List;

/**
 * this class represent the thin model.
 * in particular, contains all the attributes that are used to manage the game from the client
 */
public class ThinModel {

    private ThinGame game;
    private int position = 0;
    private List<Resource> gainedFromMarbleMarket;
    private int marbles;

    public List<Resource> getGainedFromMarbleMarket() {
        return gainedFromMarbleMarket;
    }

    public void setGainedFromMarbleMarket(List<Resource> gainedFromMarbleMarket) {
        this.gainedFromMarbleMarket = gainedFromMarbleMarket;
    }

    public ThinGame getGame() {
        return game;
    }

    public void setGame(ThinGame game) {
        this.game = game;
    }

    public int getMarbles() {
        return marbles;
    }

    public void setMarbles(int marbles) {
        this.marbles = marbles;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }



}
