package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

public class GraphicalWarehouse implements CharFigure{

    private final CharStream stream;
    private final CollectionResources shelf1;
    private final CollectionResources shelf2;
    private final CollectionResources shelf3;
    private CollectionResources shelf4;
    private CollectionResources shelf5;
    private int height = 11;

    public GraphicalWarehouse(CharStream stream,
                              CollectionResources shelf1,
                              CollectionResources shelf2,
                              CollectionResources shelf3,
                              CollectionResources shelf4,
                              CollectionResources shelf5){
        this.stream = stream;
        this.shelf1 = shelf1;
        this.shelf2 = shelf2;
        this.shelf3 = shelf3;
        this.shelf4 = shelf4;
        this.shelf5 = shelf5;
    }

    public GraphicalWarehouse(CharStream stream,
                              CollectionResources shelf1,
                              CollectionResources shelf2,
                              CollectionResources shelf3,
                              CollectionResources shelf4){
        this.stream = stream;
        this.shelf1 = shelf1;
        this.shelf2 = shelf2;
        this.shelf3 = shelf3;
        this.shelf4 = shelf4;
    }

    public GraphicalWarehouse(CharStream stream,
                              CollectionResources shelf1,
                              CollectionResources shelf2,
                              CollectionResources shelf3){
        this.stream = stream;
        this.shelf1 = shelf1;
        this.shelf2 = shelf2;
        this.shelf3 = shelf3;
    }

    /**
     * This method should be implemented by classes that will eventually use CharStream to draw themselves on it.
     */
    @Override
    public void draw() {
        draw(0, 0);
    }

    /**
     * This method should be implemented by classes that will eventually use CharStream to draw themselves on it given relative positions on the stream.
     *
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {

        if (shelf5 == null)
            height = 9;
        if (shelf4 == null)
            height= 7;

        //initialize black background
        int width = 15;
        for(int i = 0; i <= width; ++i) {
            for (int j = 0; j <= height; ++j) {
                stream.addColor(i + relX, j + relY, BackColor.ANSI_BRIGHT_BG_WHITE);
            }
        }

        // define the number to space shelves
        int heightOffset = 2;
        // define the space between each resource
        int widthOffset = 10;

        // write "warehouse" on the top
        stream.addString(relX, relY, " WAREHOUSE", ForeColor.ANSI_BRIGHT_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);


        BackColor firstColor;
        BackColor secondColor;
        BackColor thirdColor;
        ForeColor emptyResource = ForeColor.ANSI_RED;
        Resource first;
        Resource second;
        Resource third;

        // try to get the first resource
        stream.addString(relX  , relY + heightOffset , " 1° SHELF:  ", ForeColor.ANSI_BRIGHT_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        try {
             first = shelf1.asList().get(0);
             firstColor = first.getColor();
            stream.addColor(relX + widthOffset, relY +  heightOffset, firstColor);
        } catch (IndexOutOfBoundsException e){
            stream.addString(relX + widthOffset, relY + heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
        } // set the default color to ANSI_BG_BLACK

        // draw the first shelf


        stream.addString(relX  , relY + 2 * heightOffset, " 2° SHELF:  ", ForeColor.ANSI_BRIGHT_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        // try to get the first resource
        try {
            first = shelf2.asList().get(0);
            firstColor = first.getColor();
            stream.addColor(relX + widthOffset, relY + 2 * heightOffset, firstColor);
        } catch (IndexOutOfBoundsException e){
            stream.addString(relX + widthOffset, relY + 2 * heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
        } // set the default color to ANSI_BG_BLACK

        // try to get the first resource
        try {
            second = shelf2.asList().get(1);
            secondColor = second.getColor();
            stream.addColor(relX + widthOffset + 2, relY +  2 * heightOffset, secondColor);
        } catch (IndexOutOfBoundsException e){
            stream.addString(relX + widthOffset + 2, relY + 2 * heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
        } // set the default color to ANSI_BG_BLACK

        // draw the second shelf

        stream.addString(relX  , relY + 3 * heightOffset, " 3° SHELF:  ", ForeColor.ANSI_BRIGHT_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);

        try {
            first = shelf3.asList().get(0);
            firstColor = first.getColor();
            stream.addColor(relX + widthOffset, relY + 3 * heightOffset, firstColor);
        } catch (IndexOutOfBoundsException e){
            stream.addString(relX + widthOffset, relY + 3 * heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
        }

        try {
            second = shelf3.asList().get(1);
            secondColor = second.getColor();
            stream.addColor(relX + widthOffset + 2, relY +  3 * heightOffset, secondColor);
        } catch (IndexOutOfBoundsException e){
            stream.addString(relX + widthOffset + 2, relY + 3 * heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
        }

        try {
            third = shelf3.asList().get(2);
            thirdColor = third.getColor();
            stream.addColor(relX + widthOffset + 4, relY +  3 * heightOffset, thirdColor);
        } catch (IndexOutOfBoundsException e){
            stream.addString(relX + widthOffset + 4, relY + 3 * heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
        }

        // draw the third shelf




        if (shelf4 != null){
            stream.addString(relX  , relY + 4 * heightOffset, " 4° SHELF:  ", ForeColor.ANSI_BRIGHT_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            // try to get the first resource
            try {
                first = shelf4.asList().get(0);
                firstColor = first.getColor();
                stream.addColor(relX + widthOffset, relY + 4 * heightOffset, firstColor);
            } catch (IndexOutOfBoundsException e){
                stream.addString(relX + widthOffset, relY + 4 * heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
            } // set the default color to ANSI_BG_BLACK

            // try to get the first resource
            try {
                second = shelf4.asList().get(1);
                secondColor = second.getColor();
                stream.addColor(relX + widthOffset + 2, relY +  4 * heightOffset, secondColor);
            } catch (IndexOutOfBoundsException e){
                stream.addString(relX + widthOffset + 2, relY + 4 * heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
            } // set the default color to ANSI_BG_BLACK

            // draw the fourth shelf
        }

        if (shelf5 != null){
            // try to get the first resource
            stream.addString(relX  , relY + 5 * heightOffset, " 5° SHELF:  ", ForeColor.ANSI_BRIGHT_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
            try {
                first = shelf5.asList().get(0);
                firstColor = first.getColor();
                stream.addColor(relX + widthOffset, relY + 5 * heightOffset, firstColor);
            } catch (IndexOutOfBoundsException e){
                stream.addString(relX + widthOffset, relY + 5 * heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
            } // set the default color to ANSI_BG_BLACK

            // try to get the first resource
            try {
                second = shelf5.asList().get(1);
                secondColor = second.getColor();
                stream.addColor(relX + widthOffset + 2, relY + 5 * heightOffset, secondColor);
            } catch (IndexOutOfBoundsException e){
                stream.addString(relX + widthOffset + 2, relY + 5 * heightOffset, "X", emptyResource, BackColor.ANSI_BRIGHT_BG_WHITE);
            } // set the default color to ANSI_BG_BLACK

            // draw the fifth shelf


        }






    }
}
