package it.polimi.ingsw.view.cli.graphic;

import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.cli.graphic.utilities.CharStream;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.ForeColor;

public class GraphicalPlayer implements CharFigure{
    private final CharStream stream;
    private final ThinPlayer thinPlayer;
    private static final int width = 75;
    private static final int height = 10;
    private static final int developmentCardWidth = 6;
    private static final int leaderCardWidth = 5;
    private static final int leaderCardHeight = 2;
    private static final BackColor backgroundColor = BackColor.ANSI_BRIGHT_BG_CYAN;

    public GraphicalPlayer(CharStream stream, ThinPlayer thinPlayer) {
        this.stream = stream;
        this.thinPlayer = thinPlayer;
    }

    /**
     *this method returns the width of the GraphicalPlayer
     */
    public int getWidth() {
        return width;
    }

    /**
     *this method returns the height of the GraphicalPlayer
     */
    public int getHeight() {
        return height;
    }

    /**
     *this method draws the GraphicalPlayer in the CharStream at the default position
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     *this method draws the GraphicalPlayer in the CharStream at X,Y position
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {

        //NICKNAME
        stream.addString(relX,relY, thinPlayer.getNickname(), ForeColor.ANSI_BRIGHT_WHITE, backgroundColor);

        //WAREHOUSE
        // GraphicalWarehouse graphicalWarehouse = new GraphicalWarehouse(stream, thinPlayer.getFirstShelf(), thinPlayer.getSecondShelf(), thinPlayer.getThirdShelf());
        GraphicalWarehouse graphicalWarehouse = new GraphicalWarehouse(stream, thinPlayer);
        graphicalWarehouse.draw(relX, relY+1);

        //STRONGBOX
        GraphicalStrongbox graphicalStrongbox = new GraphicalStrongbox(stream,thinPlayer.getStrongbox());
        graphicalStrongbox.draw(relX+graphicalWarehouse.getWidth()+2,relY+1);

        //BASIC PRODUCTION
        GraphicalBasicProduction graphicalBasicProduction = new GraphicalBasicProduction(stream);
        graphicalBasicProduction.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+4, relY+1);

        //DEVELOPMENT CARDS
        for (int i=0; i<=22; i++)
            for (int j=0; j<=5; j++)
                stream.addColor(relX+i+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+6, relY+1+j, BackColor.ANSI_BG_BLACK);

        //PRODUCTION POWER 1
        if(thinPlayer.getProductionPower().getProductionPower1()!= null) {
            if (thinPlayer.getProductionPower().getProductionPower1().size()==1) {
                GraphicalDevelopmentCard graphicalDevelopmentCard = new GraphicalDevelopmentCard(stream,thinPlayer.getProductionPower().getProductionPower1().get(0));
                graphicalDevelopmentCard.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+6, relY+1);
            }
            if (thinPlayer.getProductionPower().getProductionPower1().size()==2) {

                //card on the top of the deck
                GraphicalDevelopmentCard graphicalDevelopmentCard2 = new GraphicalDevelopmentCard(stream,thinPlayer.getProductionPower().getProductionPower1().get(1));
                graphicalDevelopmentCard2.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+6, relY+1);

                //card below
                GraphicalDevelopmentCardBelow graphicalDevelopmentCardBelow = new GraphicalDevelopmentCardBelow(stream, thinPlayer.getProductionPower().getProductionPower1().get(0));
                graphicalDevelopmentCardBelow.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+6, relY+1+graphicalDevelopmentCard2.getHeight()+1);

            }
            if (thinPlayer.getProductionPower().getProductionPower1().size()==3) {

                //card on the top of the deck
                GraphicalDevelopmentCard graphicalDevelopmentCard2 = new GraphicalDevelopmentCard(stream,thinPlayer.getProductionPower().getProductionPower1().get(2));
                graphicalDevelopmentCard2.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+6, relY+1);

                //card below
                GraphicalDevelopmentCardBelow graphicalDevelopmentCardBelow = new GraphicalDevelopmentCardBelow(stream, thinPlayer.getProductionPower().getProductionPower1().get(1));
                graphicalDevelopmentCardBelow.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+6, relY+1+graphicalDevelopmentCard2.getHeight()+1);

                //card below
                GraphicalDevelopmentCardBelow graphicalDevelopmentCardBelow2 = new GraphicalDevelopmentCardBelow(stream, thinPlayer.getProductionPower().getProductionPower1().get(0));
                graphicalDevelopmentCardBelow2.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+6, relY+1+graphicalDevelopmentCard2.getHeight()+2);
            }
        }

        //PRODUCTION POWER 2
        if(thinPlayer.getProductionPower().getProductionPower2()!= null) {
            if (thinPlayer.getProductionPower().getProductionPower2().size()==1) {
                GraphicalDevelopmentCard graphicalDevelopmentCard = new GraphicalDevelopmentCard(stream,thinPlayer.getProductionPower().getProductionPower2().get(0));
                graphicalDevelopmentCard.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+graphicalDevelopmentCard.getWidth()+8, relY+1);
            }
            if (thinPlayer.getProductionPower().getProductionPower2().size()==2) {

                //card on the top of the deck
                GraphicalDevelopmentCard graphicalDevelopmentCard2 = new GraphicalDevelopmentCard(stream,thinPlayer.getProductionPower().getProductionPower2().get(1));
                graphicalDevelopmentCard2.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()+8, relY+1);

                //card below
                GraphicalDevelopmentCardBelow graphicalDevelopmentCardBelow = new GraphicalDevelopmentCardBelow(stream, thinPlayer.getProductionPower().getProductionPower2().get(0));
                graphicalDevelopmentCardBelow.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()+8, relY+1+graphicalDevelopmentCard2.getHeight()+1);
            }
            if (thinPlayer.getProductionPower().getProductionPower2().size()==3) {

                //card on the top of the deck
                GraphicalDevelopmentCard graphicalDevelopmentCard2 = new GraphicalDevelopmentCard(stream,thinPlayer.getProductionPower().getProductionPower2().get(2));
                graphicalDevelopmentCard2.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()+8, relY+1);

                //card below
                GraphicalDevelopmentCardBelow graphicalDevelopmentCardBelow = new GraphicalDevelopmentCardBelow(stream, thinPlayer.getProductionPower().getProductionPower2().get(1));
                graphicalDevelopmentCardBelow.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()+8, relY+1+graphicalDevelopmentCard2.getHeight()+1);

                //card below
                GraphicalDevelopmentCardBelow graphicalDevelopmentCardBelow2 = new GraphicalDevelopmentCardBelow(stream, thinPlayer.getProductionPower().getProductionPower2().get(0));
                graphicalDevelopmentCardBelow2.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()+8, relY+1+graphicalDevelopmentCard2.getHeight()+2);
            }

        }

        //PRODUCTION POWER 3
        if(thinPlayer.getProductionPower().getProductionPower3()!= null) {
            if (thinPlayer.getProductionPower().getProductionPower3().size()==1) {
                GraphicalDevelopmentCard graphicalDevelopmentCard = new GraphicalDevelopmentCard(stream,thinPlayer.getProductionPower().getProductionPower3().get(0));
                graphicalDevelopmentCard.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+graphicalDevelopmentCard.getWidth()*2)+10, relY+1);
            }
            if (thinPlayer.getProductionPower().getProductionPower3().size()==2) {

                //card on the top of the deck
                GraphicalDevelopmentCard graphicalDevelopmentCard2 = new GraphicalDevelopmentCard(stream,thinPlayer.getProductionPower().getProductionPower3().get(1));
                graphicalDevelopmentCard2.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()*2)+10, relY+1);

                //card below
                GraphicalDevelopmentCardBelow graphicalDevelopmentCardBelow = new GraphicalDevelopmentCardBelow(stream, thinPlayer.getProductionPower().getProductionPower3().get(0));
                graphicalDevelopmentCardBelow.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()*2)+10, relY+1+graphicalDevelopmentCard2.getHeight()+1);
            }
            if (thinPlayer.getProductionPower().getProductionPower3().size()==3) {

                //card on the top of the deck
                GraphicalDevelopmentCard graphicalDevelopmentCard2 = new GraphicalDevelopmentCard(stream,thinPlayer.getProductionPower().getProductionPower3().get(2));
                graphicalDevelopmentCard2.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()*2)+10, relY+1);

                //card below
                GraphicalDevelopmentCardBelow graphicalDevelopmentCardBelow = new GraphicalDevelopmentCardBelow(stream, thinPlayer.getProductionPower().getProductionPower3().get(1));
                graphicalDevelopmentCardBelow.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()*2)+10, relY+1+graphicalDevelopmentCard2.getHeight()+1);

                //card below
                GraphicalDevelopmentCardBelow graphicalDevelopmentCardBelow2 = new GraphicalDevelopmentCardBelow(stream, thinPlayer.getProductionPower().getProductionPower3().get(0));
                graphicalDevelopmentCardBelow2.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+graphicalDevelopmentCard2.getWidth()*2)+10, relY+1+graphicalDevelopmentCard2.getHeight()+2);
            }
        }

        // LEADER CARDS
        if(thinPlayer.getLeaderCards()!=null) {

            if (thinPlayer.getLeaderCards().size()==1) {
                leaderCardInFirstPosition(relX, relY, graphicalWarehouse, graphicalStrongbox, graphicalBasicProduction);
            }

            if (thinPlayer.getLeaderCards().size()==2) {
                leaderCardInFirstPosition(relX, relY, graphicalWarehouse, graphicalStrongbox, graphicalBasicProduction);
                if (thinPlayer.getLeaderCards().get(1).getVictoryPoints() <= 0) {  //COVERED CARD
                    for (int i=0; i<=leaderCardWidth; i++)
                        for (int j=0; j<=leaderCardHeight; j++)
                            stream.addColor(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+developmentCardWidth*3)+12+i, relY+1 + j + leaderCardHeight+2, ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_BLACK);
                } else { //PRINT LEADERCARD
                    GraphicalLeaderCard graphicalLeaderCard = new GraphicalLeaderCard(stream, thinPlayer.getLeaderCards().get(1),5, 2);
                    graphicalLeaderCard.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+developmentCardWidth*3)+12, relY+ leaderCardHeight+3);
                }
                stream.addString(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+developmentCardWidth*3)+12+leaderCardWidth+1,relY+2+leaderCardHeight+2, "<-2",ForeColor.ANSI_RED, backgroundColor);
            }
        }
    }


    /**
     * this method draws the first leaderCard in the GraphicalPlayer
     */
    private void leaderCardInFirstPosition(int relX, int relY, GraphicalWarehouse graphicalWarehouse, GraphicalStrongbox graphicalStrongbox, GraphicalBasicProduction graphicalBasicProduction) {
        if (thinPlayer.getLeaderCards().get(0).getVictoryPoints() <= 0) { //COVERED CARD
            for (int i=0; i<=leaderCardWidth; i++)
                for (int j=0; j<=leaderCardHeight; j++)
                    stream.addColor(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+developmentCardWidth*3)+12+i, relY+1+ j, ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_BLACK);
        } else { //PRINT LEADERCARD
            GraphicalLeaderCard graphicalLeaderCard = new GraphicalLeaderCard(stream, thinPlayer.getLeaderCards().get(0),5, 2);
            graphicalLeaderCard.draw(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+developmentCardWidth*3)+12, relY+1);
        }
        stream.addString(relX+graphicalWarehouse.getWidth()+graphicalStrongbox.getWidth()+(graphicalBasicProduction.getWidth()+developmentCardWidth*3)+12+leaderCardWidth+1,relY+2, "<-1",ForeColor.ANSI_RED, backgroundColor);
    }

}
