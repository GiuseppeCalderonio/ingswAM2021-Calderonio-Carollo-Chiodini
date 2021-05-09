package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.Coin;
import it.polimi.ingsw.model.Resources.Stone;
import it.polimi.ingsw.view.graphic.GraphicalWarehouse;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.utilities.CharStream;

import java.util.Arrays;

public class Cli {

    private static int width = 200;
    private static int height = 200;

    public static void main(String[] args) {


        CharStream console = new CharStream(width, height);
        Game game = new Game(Arrays.asList("pippo"));
        game.getPlayers().get(0).addLeaderShelf(new Coin());
        game.getPlayers().get(0).addLeaderShelf(new Stone());
        GraphicalWarehouse graphicalWarehouse = new GraphicalWarehouse(console,new ThinPlayer(game.findPlayer("pippo")));
        graphicalWarehouse.draw();
        console.print(System.out);

    }
}
