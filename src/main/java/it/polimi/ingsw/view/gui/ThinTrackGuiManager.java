package it.polimi.ingsw.view.gui;

public class ThinTrackGuiManager {

    private final BiInteger[] track;


    public ThinTrackGuiManager(double mainWindowWidth, double mainWindowHeight){

        double relativeX = mainWindowWidth * 72 / 61;
        double relativeY = mainWindowHeight * 9 / 28;

        track = new BiInteger[]{ new BiInteger(mainWindowWidth, mainWindowHeight), // 0
                new BiInteger(mainWindowWidth + relativeX, mainWindowHeight),   // 1
                new BiInteger(mainWindowWidth + 2 * relativeX, mainWindowHeight), // 2
                new BiInteger(mainWindowWidth + 2 * relativeX, mainWindowHeight - relativeY), // 3
                new BiInteger(mainWindowWidth + 2 * relativeX, mainWindowHeight - 2 * relativeY), // 4
                new BiInteger(mainWindowWidth + 3 *  relativeX, mainWindowHeight - 2 * relativeY), // 5
                new BiInteger(mainWindowWidth + 4 *  relativeX, mainWindowHeight - 2 * relativeY), // 6
                new BiInteger(mainWindowWidth + 5 *  relativeX, mainWindowHeight - 2 * relativeY), // 7
                new BiInteger(mainWindowWidth + 6 *  relativeX, mainWindowHeight - 2 * relativeY), // 8
                new BiInteger(mainWindowWidth + 7 *  relativeX, mainWindowHeight - 2 * relativeY), // 9
                new BiInteger(mainWindowWidth + 7 *  relativeX, mainWindowHeight - 1 * relativeY), // 10
                new BiInteger(mainWindowWidth + 7 *  relativeX, mainWindowHeight), // 11
                new BiInteger(mainWindowWidth + 8 *  relativeX, mainWindowHeight), // 12
                new BiInteger(mainWindowWidth + 9 *  relativeX, mainWindowHeight), // 13
                new BiInteger(mainWindowWidth + 10 * relativeX, mainWindowHeight), // 14
                new BiInteger(mainWindowWidth + 11 * relativeX, mainWindowHeight), // 15
                new BiInteger(mainWindowWidth + 12 * relativeX, mainWindowHeight), // 16
                new BiInteger(mainWindowWidth + 12 * relativeX, mainWindowHeight - relativeY), // 17
                new BiInteger(mainWindowWidth + 12 * relativeX, mainWindowHeight - 2 * relativeY), // 18
                new BiInteger(mainWindowWidth + 13 * relativeX, mainWindowHeight - 2 * relativeY), // 19
                new BiInteger(mainWindowWidth + 14 * relativeX, mainWindowHeight - 2 * relativeY), // 20
                new BiInteger(mainWindowWidth + 15 * relativeX, mainWindowHeight - 2 * relativeY), // 21
                new BiInteger(mainWindowWidth + 16 * relativeX, mainWindowHeight - 2 * relativeY), // 22
                new BiInteger(mainWindowWidth + 17 * relativeX, mainWindowHeight - 2 * relativeY), // 23
                new BiInteger(mainWindowWidth + 18 * relativeX, mainWindowHeight - 2 * relativeY), // 24

        };

    }

    public double getXPosition(int position){
        return track[position].getX();
    }

    public double getYPosition(int position){
        return track[position].getY();
    }



}
