package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.Coin;
import it.polimi.ingsw.model.Resources.Stone;
import it.polimi.ingsw.view.graphic.GraphicalWarehouse;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.utilities.CharStream;


import java.util.Collections;

public class Cli {

    public static void main(String[] args) {


        int height = 200;
        int width = 200;
        CharStream console = new CharStream(width, height);
        Game game = new Game(Collections.singletonList("pippo"));
        game.getPlayers().get(0).addLeaderShelf(new Coin());
        game.getPlayers().get(0).addLeaderShelf(new Stone());
        GraphicalWarehouse graphicalWarehouse = new GraphicalWarehouse(console,new ThinPlayer(game.findPlayer("pippo")));
        graphicalWarehouse.draw();
        console.print(System.out);

    }
}
