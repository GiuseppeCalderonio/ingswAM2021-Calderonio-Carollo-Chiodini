

package it.polimi.ingsw.view.graphic;


import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

public class GraphicalDevelopmentCard implements CharFigure{
    private final CharStream stream;
    private final DevelopmentCard developmentCard;
    private final int width;
    private final int height;


    public GraphicalDevelopmentCard(CharStream stream, DevelopmentCard developmentCard, int width, int height) {
        this.stream = stream;
        this.developmentCard = developmentCard;
        this.width = width;
        this.height = height;
    }

    /**
     *this method returns the width of the GraphicalDevelopmentCard
     */
    public int getWidth() {
        return width;
    }

    /**
     *this method returns the height of the GraphicalDevelopmentCard
     */
    public int getHeight() {
        return height;
    }

    /**
     *this method draws the GraphicalDevelopmentCard in the CharStream at the default position
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     *this method draws the GraphicalDevelopmentCard in the CharStream at X,Y position
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {
        int midPointHeight = (int) (Math.ceil(height * 1.0 / 2.0));
        int midPointWidth = (int) (relX + Math.ceil(width * 1.0 / 2.0));
        int k=0;

        //initialize the card a the respective color
        for(int i = 0; i <= width; ++i){
            for(int j = 0; j <= height; ++j){
                stream.addColor(i + relX, j + relY, developmentCard.getBackColor());
            }
        }


        //add the cost to the card
        stream.addString(midPointWidth + k, relY, String.valueOf(developmentCard.getCost().getMaps().get(0).getCardinality()), ForeColor.ANSI_BLACK, developmentCard.getCost().getMaps().get(0).getResource().getColor());
        k++;
        if(developmentCard.getCost().getMaps().size()>=2) {
            stream.addString( midPointWidth + k, relY, String.valueOf(developmentCard.getCost().getMaps().get(1).getCardinality()), ForeColor.ANSI_BLACK, developmentCard.getCost().getMaps().get(1).getResource().getColor());
            k++;
        }
        if(developmentCard.getCost().getMaps().size() == 3) {
            stream.addString(midPointWidth + k, relY, String.valueOf(developmentCard.getCost().getMaps().get(2).getCardinality()), ForeColor.ANSI_BLACK, developmentCard.getCost().getMaps().get(2).getResource().getColor());
           // k++;
        }

        //add the level to the card
        stream.addString(relX,relY +1, "LEVEL:" + developmentCard.getLevel(), ForeColor.ANSI_BLACK, developmentCard.getBackColor());

        //add the production at the card
        GraphicalProduction cardProduction = new GraphicalProduction(stream, developmentCard.getProductionPowerInput(), developmentCard.getProductionPowerOutput(), developmentCard.getProductionPowerFaithPoints(), 0,5);
        cardProduction.draw(relX, relY+midPointHeight);

        //add the victory points to the card
        stream.addString(midPointWidth, height + relY, String.valueOf(developmentCard.getVictoryPoints()), developmentCard.getBackColor()); // victory points
    }


}