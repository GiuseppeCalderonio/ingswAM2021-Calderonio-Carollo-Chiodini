package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represent the graphical faith track.
 * in particular, it implement the CharFigure interface because
 * is a part of the cli done with colours and ascii art
 */
public class GraphicalFaithTrack implements CharFigure {

    /**
     * this attribute represent the charStream used to draw
     */
    private final CharStream stream;

    /**
     * this attribute represent the width of the figure to draw
     */
    private final int width = 155;

    /**
     * this attribute represent the height of the figure to draw
     */
    private final int height = 15;

    /**
     * this attribute represent the list of thin players from
     * which get the pope favour tiles
     */
    private final List<ThinPlayer> thinPlayers;

    /**
     * this attribute represent the positions of the players as
     * a list of integers
     */
    private final List<Integer> positionPlayers;

    /**
     * this attribute represent the nicknames of the players as
     * a list of strings
     */
    private final List<String> nicknames;

    /**
     * this is the constructor of the class, here we take the players nickname, players position and we create two different list
     * @param stream is the console where i print the track
     * @param thinPlayers this is a thin player; contains all information about a player
     */
    public GraphicalFaithTrack(CharStream stream, List<ThinPlayer> thinPlayers) {
        this.stream = stream;
        this.thinPlayers=thinPlayers;
        nicknames = new ArrayList<>();
        for (ThinPlayer thinPlayer : thinPlayers){
            try{
                this.nicknames.add(thinPlayer.getNickName().substring(0, 12));
            }catch (StringIndexOutOfBoundsException e){
                this.nicknames.add(thinPlayer.getNickName());
            }

        }
        this.positionPlayers = new ArrayList<>();
        for (ThinPlayer thinPlayer : thinPlayers) this.positionPlayers.add(thinPlayer.getTrack().getPosition());
    }

    /**
     * this method returns the width of the GraphicalFaithTrack
     */
    public int getWidth() {
        return width;
    }

    /**
     * this method returns the height of the GraphicalFaithTrack
     */
    public int getHeight() {
        return height;
    }

    /**
     * this method draw the GraphicalFaithTrack in the CharStream at the default position
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     * this method draw the GraphicalFaithTrack in the CharStream at X,Y position;
     *
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {
        for (int i = 0; i <= width; ++i) {
            for (int j = 0; j <= height; ++j) {
                //this is the base of FaithTrack
                stream.addColor(i + relX - 1, j + relY - 9, BackColor.ANSI_BRIGHT_BG_WHITE);
            }
        }
        //descriptions of players
        stream.addString(relX, relY - 8, "PLAYERS", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        stream.addString(relX, relY - 7, "LIST:", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        for (int i = 1; i <= nicknames.size(); i++) {
            stream.addString(relX, relY - 7 + i, i + "->" + nicknames.get(i - 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        }
        //here there is the print of faithTrack
        //position from 0 to 2
        for(int box=0 ; box < 3 ; box++){
            for (int i = 0; i < 8; i++) stream.addColor(relX + i + 8*box, relY, BackColor.ANSI_BG_BLACK); //first black row
            for (int i = 0; i < 9; i++) stream.addColor(relX + i + 8*box, relY + 4, BackColor.ANSI_BG_BLACK); //last black row
            for (int j = 0; j < 4; j++) stream.addColor(relX + 8*box, relY + j, BackColor.ANSI_BG_BLACK); //first black column
            for (int j = 0; j < 4; j++) stream.addColor(relX + 8 + 8*box, relY + j, BackColor.ANSI_BG_BLACK); //last black column
            stream.addString(relX + 1 + 8*box, relY + 1, "PV0", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            stream.addString(relX + 6 + 8*box, relY + 1, Integer.toString(box), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        }

        //position 3
        for (int i = 0; i < 8; i++) stream.addColor(relX + i + 16, relY - 4, BackColor.ANSI_BG_BLACK); //first black row
        for (int j = 0; j < 5; j++)
            stream.addColor(relX + 16, relY + j - 4, BackColor.ANSI_BG_BLACK); //first black column
        for (int j = 0; j < 5; j++)
            stream.addColor(relX + 8 + 16, relY + j - 4, BackColor.ANSI_BG_BLACK); //last black column
        for (int i = 1; i < 7; i++)
            stream.addColor(relX + i + 17, relY - 3, BackColor.ANSI_BRIGHT_BG_YELLOW); // yellow row for faith points
        stream.addString(relX + 17, relY - 3, "PV1", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);
        stream.addString(relX + 22, relY - 3, "3", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);

        //position 4
        for (int i = 0; i < 8; i++) stream.addColor(relX + i + 16, relY - 8, BackColor.ANSI_BG_BLACK); //first black row
        for (int j = 0; j < 5; j++)
            stream.addColor(relX + 16, relY + j - 8, BackColor.ANSI_BG_BLACK); //first black column
        for (int j = 0; j < 5; j++)
            stream.addColor(relX + 8 + 16, relY + j - 8, BackColor.ANSI_BG_YELLOW); //last yellow column
        stream.addString(relX + 17, relY - 7, "PV1", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        stream.addString(relX + 22, relY - 7, "4", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);

        //position 5
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 24, relY - 8, BackColor.ANSI_BG_YELLOW); //first yellow row
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 24, relY - 4, BackColor.ANSI_BG_YELLOW); //last yellow row
        for (int j = 1; j < 5; j++)
            stream.addColor(relX + 32, relY + j - 8, BackColor.ANSI_BG_BLACK); //last yellow column
        stream.addString(relX + 25, relY - 7, "PV1", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        stream.addString(relX + 30, relY - 7, "5", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);

        //position 6
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 32, relY - 8, BackColor.ANSI_BG_YELLOW); //first yellow row
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 32, relY - 4, BackColor.ANSI_BG_YELLOW); //last yellow row
        for (int j = 1; j < 5; j++)
            stream.addColor(relX + 40, relY + j - 8, BackColor.ANSI_BG_BLACK); //last black column
        for (int i = 1; i < 8; i++)
            stream.addColor(relX + i + 32, relY - 7, BackColor.ANSI_BRIGHT_BG_YELLOW); // yellow row for faith points
        stream.addString(relX + 33, relY - 7, "PV2", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);
        stream.addString(relX + 38, relY - 7, "6", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);

        //first pope favour tile
        for (int i = 0; i < 17; i++)
            for (int j = 0; j < 4; j++)
                stream.addColor(relX + i + 32, relY + j - 3, BackColor.ANSI_BG_YELLOW); //first pope favour tile completely yellow
        stream.addString(relX + 33, relY - 3, "VICTORY POINTS", ForeColor.ANSI_BLACK, BackColor.ANSI_BG_YELLOW);
        stream.addString(relX + 40, relY - 2, "2", ForeColor.ANSI_BLACK, BackColor.ANSI_BG_YELLOW);

        //position 7
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 40, relY - 8, BackColor.ANSI_BG_YELLOW); //first yellow row
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 40, relY - 4, BackColor.ANSI_BG_YELLOW); //last yellow row
        for (int j = 1; j < 5; j++)
            stream.addColor(relX + 48, relY + j - 8, BackColor.ANSI_BG_BLACK); //last black column
        stream.addString(relX + 41, relY - 7, "PV2", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        stream.addString(relX + 46, relY - 7, "7", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);

        //position 8 - pope space
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 48, relY - 8, BackColor.ANSI_BG_YELLOW); //first yellow row
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 48, relY - 4, BackColor.ANSI_BG_YELLOW); //last yellow row
        for (int j = 1; j < 4; j++)
            stream.addColor(relX + 56, relY + j - 8, BackColor.ANSI_BG_YELLOW); //last yellow column
        for (int i = 1; i < 8; i++)
            for (int j = 1; j < 3; j++)
                stream.addColor(relX + i + 48, relY + j - 7, BackColor.ANSI_BG_PURPLE); //all position 8
        stream.addString(relX + 49, relY - 7, "P.SPACE", ForeColor.ANSI_BLACK, BackColor.ANSI_BG_PURPLE);

        //position 9
        for (int i = 1; i < 9; i++) stream.addColor(relX + i + 56, relY - 8, BackColor.ANSI_BG_BLACK); //first black row
        for (int i = 1; i < 9; i++) stream.addColor(relX + i + 56, relY - 4, BackColor.ANSI_BG_BLACK); //last black row
        for (int j = 1; j < 5; j++)
            stream.addColor(relX + 64, relY + j - 8, BackColor.ANSI_BG_BLACK); //last black column
        for (int i = 2; i < 7; i++)
            stream.addColor(relX + i + 57, relY - 7, BackColor.ANSI_BRIGHT_BG_YELLOW); // yellow row for faith points
        stream.addString(relX + 57, relY - 7, "PV4", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);
        stream.addString(relX + 62, relY - 7, "9", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);

        //position 10
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 56, relY, BackColor.ANSI_BG_BLACK); //last black row
        for (int j = 1; j < 5; j++)
            stream.addColor(relX + 56, relY + j - 4, BackColor.ANSI_BG_BLACK); //first black column
        for (int j = 1; j < 5; j++)
            stream.addColor(relX + 64, relY + j - 4, BackColor.ANSI_BG_BLACK); //last black column
        stream.addString(relX + 57, relY - 3, "PV4", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        stream.addString(relX + 62, relY - 3, "10", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);

        //position 11
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 56, relY + 4, BackColor.ANSI_BG_BLACK); //last black row
        for (int j = 1; j < 4; j++) stream.addColor(relX + 56, relY + j, BackColor.ANSI_BG_BLACK); //first black column
        for (int j = 1; j < 4; j++) stream.addColor(relX + 64, relY + j, BackColor.ANSI_BG_RED); //last red column
        stream.addString(relX + 57, relY + 1, "PV4", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        stream.addString(relX + 62, relY + 1, "11", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);

        //position 12
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 64, relY, BackColor.ANSI_BG_RED); //first red row
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 64, relY + 4, BackColor.ANSI_BG_RED); //last red row
        for (int j = 1; j < 4; j++) stream.addColor(relX + 72, relY + j, BackColor.ANSI_BG_BLACK); //last black column
        for (int i = 1; i < 7; i++)
            stream.addColor(relX + i + 65, relY + 1, BackColor.ANSI_BRIGHT_BG_YELLOW); // yellow row for faith points
        stream.addString(relX + 65, relY + 1, "PV6", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);
        stream.addString(relX + 70, relY + 1, "12", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);

        //position 13 and 14
        for(int box=0; box<2 ; box++){
            for (int i = 0; i < 9; i++) stream.addColor(relX + i + 72 + 8*box, relY, BackColor.ANSI_BG_RED); //first red row
            for (int i = 0; i < 9; i++) stream.addColor(relX + i + 72 + 8*box, relY + 4, BackColor.ANSI_BG_RED); //last red row
            for (int j = 1; j < 4; j++) stream.addColor(relX + 80 + 8*box, relY + j, BackColor.ANSI_BG_BLACK); //last black column
            stream.addString(relX + 73 + 8*box , relY + 1, "PV6", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            stream.addString(relX + 78 + 8*box , relY + 1, Integer.toString(13+box), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        }

        //pope favor tile 2
        for (int i = 0; i < 17; i++)
            for (int j = 0; j < 5; j++)
                stream.addColor(relX + i + 72, relY + j - 4, BackColor.ANSI_BG_RED); //first pope favour tile completely yellow
        stream.addString(relX + 73, relY - 3, "VICTORY POINTS", ForeColor.ANSI_BLACK, BackColor.ANSI_BG_RED);
        stream.addString(relX + 80, relY - 2, "3", ForeColor.ANSI_BLACK, BackColor.ANSI_BG_RED);

        //position 15
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 88, relY, BackColor.ANSI_BG_RED); //first red row
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 88, relY + 4, BackColor.ANSI_BG_RED); //last red row
        for (int j = 1; j < 4; j++) stream.addColor(relX + 96, relY + j, BackColor.ANSI_BG_BLACK); //last black column
        for (int i = 1; i < 7; i++)
            stream.addColor(relX + i + 89, relY + 1, BackColor.ANSI_BRIGHT_BG_YELLOW); // yellow row for faith points
        stream.addString(relX + 89, relY + 1, "PV9", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);
        stream.addString(relX + 94, relY + 1, "15", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);

        //position 16 - pope space
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 96, relY, BackColor.ANSI_BG_RED); //first red row
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 96, relY + 4, BackColor.ANSI_BG_RED); //last red row
        for (int j = 1; j < 4; j++) stream.addColor(relX + 104, relY + j, BackColor.ANSI_BG_RED); //last red column
        for (int i = 1; i < 8; i++)
            for (int j = 1; j < 3; j++)
                stream.addColor(relX + i + 96, relY + j + 1, BackColor.ANSI_BG_PURPLE); //all position 16
        stream.addString(relX + 97, relY + 1, "P.SPACE", ForeColor.ANSI_BLACK, BackColor.ANSI_BG_PURPLE);

        //position 17
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 96, relY - 4, BackColor.ANSI_BG_BLACK); //first black row
        for (int j = 1; j < 4; j++)
            stream.addColor(relX + 96, relY + j - 4, BackColor.ANSI_BG_BLACK); //first black column
        for (int j = 1; j < 4; j++)
            stream.addColor(relX + 104, relY + j - 4, BackColor.ANSI_BG_BLACK); //last black column
        stream.addString(relX + 97, relY - 3, "PV9", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        stream.addString(relX + 102, relY - 3, "17", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);

        //position 18
        for (int i = 0; i < 9; i++) stream.addColor(relX + i + 96, relY - 8, BackColor.ANSI_BG_BLACK); //first black row
        for (int j = 1; j < 5; j++)
            stream.addColor(relX + 96, relY + j - 8, BackColor.ANSI_BG_BLACK); //first black column
        for (int j = 0; j < 5; j++)
            stream.addColor(relX + 104, relY + j - 8, BackColor.ANSI_BRIGHT_BG_RED); //last red column
        for (int i = 1; i < 7; i++)
            stream.addColor(relX + i + 96, relY - 7, BackColor.ANSI_BRIGHT_BG_YELLOW); // yellow row for faith points
        stream.addString(relX + 97, relY - 7, "PV12", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);
        stream.addString(relX + 102, relY - 7, "18", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);

        //position 19 and 20
        for(int box=0; box<2 ; box++){
            for (int i = 0; i < 9; i++) stream.addColor(relX + i + 104 + box*8 , relY - 8, BackColor.ANSI_BRIGHT_BG_RED); //first red row
            for (int i = 0; i < 9; i++) stream.addColor(relX + i + 104 + box*8 , relY - 4, BackColor.ANSI_BRIGHT_BG_RED); //last red row
            for (int j = 0; j < 4; j++) stream.addColor(relX + 112 + box*8 , relY + j - 8, BackColor.ANSI_BG_BLACK); //last black column
            stream.addString(relX + 105 + box*8, relY - 7, "PV12", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            stream.addString(relX + 110 + box*8, relY - 7, Integer.toString(19+box), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        }

        //position 21
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 120, relY - 8, BackColor.ANSI_BRIGHT_BG_RED); //first red row
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 120, relY - 4, BackColor.ANSI_BRIGHT_BG_RED); //last red row
        for (int j = 0; j < 4; j++)
            stream.addColor(relX + 128, relY + j - 8, BackColor.ANSI_BG_BLACK); //last red column
        for (int i = 1; i < 7; i++)
            stream.addColor(relX + i + 121, relY - 7, BackColor.ANSI_BRIGHT_BG_YELLOW); // yellow row for faith points
        stream.addString(relX + 121, relY - 7, "PV16", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);
        stream.addString(relX + 126, relY - 7, "21", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);

        //third pope favour tile
        for (int i = 0; i < 17; i++)
            for (int j = 0; j < 5; j++)
                stream.addColor(relX + i + 112, relY + j - 4, BackColor.ANSI_BRIGHT_BG_RED); //first pope favour tile completely yellow
        stream.addString(relX + 113, relY - 3, "VICTORY POINTS", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_RED);
        stream.addString(relX + 120, relY - 2, "4", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_RED);

        //position 22 and 23
        for(int box=0; box<2 ; box++){
            for (int i = 0; i < 9; i++) stream.addColor(relX + i + 128 + box*8 , relY - 8, BackColor.ANSI_BRIGHT_BG_RED); //first red row
            for (int i = 0; i < 9; i++) stream.addColor(relX + i + 128 + box*8 , relY - 4, BackColor.ANSI_BRIGHT_BG_RED); //last red row
            for (int j = 0; j < 4; j++) stream.addColor(relX + 136 + box*8 , relY + j - 8, BackColor.ANSI_BG_BLACK); //last black column
            stream.addString(relX + 129 + box*8, relY - 7, "PV16", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            stream.addString(relX + 134 + box*8, relY - 7, Integer.toString(22+box), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        }

        //position 24
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 144, relY - 8, BackColor.ANSI_BRIGHT_BG_RED); //first red row
        for (int i = 0; i < 9; i++)
            stream.addColor(relX + i + 144, relY - 4, BackColor.ANSI_BRIGHT_BG_RED); //last red row
        for (int j = 0; j < 4; j++)
            stream.addColor(relX + 152, relY + j - 8, BackColor.ANSI_BRIGHT_BG_RED); //last red column
        stream.addString(relX + 145, relY - 7, "P.SPACE", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_YELLOW);//the yellow symbolize that in this position a player obtain 20 victory points
        for (int i = 1; i < 8; i++)
            for (int j = 1; j < 3; j++)
                stream.addColor(relX + i + 144, relY + j - 7, BackColor.ANSI_BG_PURPLE); //all position 24

        //here there is the end of the faith track print and i begin to print the players
        dynamicPositions(relX , relY);
        //here i print on every pope favor tile card all players that have got 'em
        dynamicPopeCard(relX , relY);
    }

    /**
     * this method print dynamically all the owner of a pope favor tile card on each pope favour tile card
     * @param RELX is the position x of the faith track
     * @param RELY is the position y of the faith track
     */
    private void dynamicPopeCard(int RELX ,int RELY){
        for (int i=0 ; i< thinPlayers.size() ; i++){
            for(int j=0 ; j<3 ; j++){
                if(thinPlayers.get(i).getTrack().getPopeFavourTiles()[j])
                    stream.addString(RELX + 33 + (40*j), RELY - 1, (i + 1) +"V", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
                else{
                    stream.addString(RELX + 33 + (40*j), RELY - 1, (i + 1) +"X", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
                }
            }
            RELX=RELX+4;
        }
    }

    /**
     * i call this method to print dynamically all players position on the faith track
     */
    private void dynamicPositions(int RELX , int RELY) {
        //here i calculate dynamically the positions of all players in faith track
        for (int i = 0; i < nicknames.size(); i++) {
            checkHighPositions(positionPlayers.get(i), RELX, RELY, i);
            checkMiddlePositions(positionPlayers.get(i), RELX, RELY, i);
            checkLowPositions(positionPlayers.get(i), RELX, RELY, i);
        }
    }

    /**
     * this method check the position of each player, if the position is between 4 and 9 or between 18 and 24 insert here the player correctly
     */
    private void checkHighPositions(int pos, int x, int y, int index) {
        //player 1 and 2 high positions
        if((index == 0) || (index == 1)){
            if  (3 < pos && pos < 10) {
                stream.addString(x + 18 + (pos - 4) * 8 + (4*index), y - 6, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            }
            if (17 < pos && pos < 25) {
                stream.addString(x + 98 + (pos - 18) * 8 + (4*index), y - 6, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            }
        }
        //player 3 and 4 low positions
        if((index == 2) || (index == 3)) {
            int translations = index - 2;
            if (3 < pos && pos < 10)
                stream.addString(x + 18 + (pos - 4) * 8 + 4*translations, y - 5, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            if (17 < pos && pos < 25)
                stream.addString(x + 98 + (pos - 18) * 8 + 4*translations, y - 5, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        }
    }

    /**
     * this method check the position of each player, if the position is 3 or 10 or 17 insert here the player correctly
     */
    private void checkMiddlePositions(int pos, int x, int y, int index) {
        if ((pos == 3) || (pos == 10) || (pos == 17)) {
            //player 1 and 2 middle positions
            if((index == 0) || (index == 1))  stream.addString(x + 18 + (40 * translationIndex(pos)) + 4*index, y - 2, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            //player 3 and 4 low positions
            if((index == 2) || (index == 3)) {
                int translations = index - 2;
                stream.addString(x + 18 + (40 * translationIndex(pos)) + 4*translations, y - 1, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            }
        }
    }

    /**
     * this method check the position of each player, if the position is between 0 and 2 or between 11 and 16 insert here the player correctly
     */
    private void checkLowPositions(int pos, int x, int y, int index) {
        //player 1 and 2 low positions
        if((index == 0) || (index == 1)) {
            if (-1 < pos && pos < 3)  stream.addString(x + 2 + pos * 8 + 4*index, y + 2, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            if (10 < pos && pos < 17) stream.addString(x + 58 + (pos - 11) * 8 + 4*index, y + 2, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        }
        //player 3 and 4 low positions
        if((index == 2) || (index == 3)) {
            int translations = index - 2;
            if (-1 < pos && pos < 3)
                stream.addString(x + 2 + pos * 8 + 4*translations , y + 3, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            if (10 < pos && pos < 17)
                stream.addString(x + 58 + (pos - 11) * 8 + 4*translations , y + 3, Integer.toString(index + 1), ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        }
    }

    /**
     * this method is called only if a player reach the position 3 or 10 or 17. We have to check the player position to translate correctly the index.
     * @param playerPosition is the player position
     * @return a index, this index is equals to 0 if the player position is 3, 1 if the player position is 10 and 2 if the player position is 17
     */
    private int translationIndex(int playerPosition) {
        int toReturn;
        //for position number 3
        if (playerPosition == 3) toReturn = 0;
        //for position number 10
        else if (playerPosition == 10) toReturn = 1;
        //for position number 17
        else toReturn = 2;
        return toReturn;
    }
}