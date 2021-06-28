package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Resources.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

/**
 * this interface represent the gui controller
 */
public interface GuiController {

    /**
     * this method update the gui, changing the root if necessary
     */
    void update();

    /**
     * this method shows the error message
     */
    default void showErrorMessage(){

    }

    /**
     * this method send the command in input to the server
     * @param toSend this is the command to send
     */
    void sendNewCommand(Command toSend);

    /**
     * this method rollback an action if possible, do nothing otherwise
     */
    default void rollBack(){

    }

    /**
     * this method get the coin image
     * @return the coin image
     */
    default Image getCoinImage(){
        return new Image("/punchboard/Resource-" + new Coin().getId() + ".png");
    }

    /**
     * this method get the stone image
     * @return the stone image
     */
    default Image getStoneImage(){
        return new Image("/punchboard/Resource-" + new Stone().getId() + ".png");
    }

    /**
     * this method get the shield image
     * @return the shield image
     */
    default Image getShieldImage(){
        return new Image("/punchboard/Resource-" + new Shield().getId() + ".png");
    }

    /**
     * this method get the servant image
     * @return the servant image
     */
    default Image getServantImage(){
        return new Image("/punchboard/Resource-" + new Servant().getId() + ".png");
    }

    /**
     * this method get the resource image
     * @param resource this is the resource to draw
     * @return the resource image
     */
    default Image getResourceImage(Resource resource){
        return new Image("/punchboard/Resource-" + resource.getId() + ".png");
    }

    /**
     * this method center an imageview
     * @param toCenter this is the imageView to center
     */
    default void centerImageView(ImageView toCenter){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        double layoutX = width/2 - toCenter.getFitWidth()/2;
        double layoutY = height/2 - toCenter.getFitHeight()/2;
        toCenter.setLayoutX(layoutX);
        toCenter.setLayoutY(layoutY);
    }

    /**
     * this method center an HBox
     * @param toCenter this is the HBox to center
     */
    default void centerBox(HBox toCenter){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        double layoutX = width/2 - 150;
        double layoutY = height/2 - 200;
        toCenter.setLayoutX(layoutX);
        toCenter.setLayoutY(layoutY);
    }

    /**
     * this method center an HBox with the layout chosen in input
     * @param toCenter this is the HBox to center
     * @param layoutX this is the x layout
     * @param layoutY this is the y layout
     */
    default void centerBox(HBox toCenter, double layoutX, double layoutY){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();

        toCenter.setLayoutX(width / 2 - layoutX);
        toCenter.setLayoutY(height / 2 - layoutY);
    }

    /**
     * this method get a card image
     * @param card this is the card from which get the image
     * @return the image associated with the card in input
     */
    default Image getCardImage(DevelopmentCard card){
        return new Image("/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-" + card.getId() + "-1.png");
    }

    /**
     * this method get a leader card image
     * @param leaderCard this is the card from which get the image
     * @return the image associated with the leader card in input
     */
    default Image getLeaderCardImage(LeaderCard leaderCard){

        if (leaderCard.getId() == 0)
            return new Image("/back/Masters of Renaissance__Cards_BACK_3mmBleed-0-1.png");

        return new Image("/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-" + leaderCard.getId() + "-1.png");

    }

    /**
     * this method get the card width for a generic card
     * @return get the card width for a generic card
     */
    default double getCardWidth(){
        return 150;
    }

    /**
     * this method get the card height for a generic card
     * @return get the card height for a generic card
     */
    default double getCardHeight(){
        return 200;
    }

    /**
     * this method get the basic production image
     * @return the basic production image
     */
    default Image getBasicProductionImage(){
        return new Image("/board/BasicProduction.png");
    }

}
