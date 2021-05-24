package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Resources.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public interface GuiController {


    void update();

    default void showErrorMessage(){

    };

    void sendNewCommand(Command toSend);

    default void rollBack(){

    };

    default Image getCoinImage(){
        return new Image("/punchboard/Resource-" + new Coin().getId() + ".png");
    }

    default Image getStoneImage(){
        return new Image("/punchboard/Resource-" + new Stone().getId() + ".png");
    }

    default Image getShieldImage(){
        return new Image("/punchboard/Resource-" + new Shield().getId() + ".png");
    }

    default Image getServantImage(){
        return new Image("/punchboard/Resource-" + new Servant().getId() + ".png");
    }

    default Image getResourceImage(Resource resource){
        return new Image("/punchboard/Resource-" + resource.getId() + ".png");
    }

    default void centerImageView(ImageView toCenter){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        double layoutX = width/2 - toCenter.getFitWidth()/2;
        double layoutY = height/2 - toCenter.getFitHeight()/2;
        toCenter.setLayoutX(layoutX);
        toCenter.setLayoutY(layoutY);
    }

    default void centerBox(VBox toCenter){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        double layoutX = width/2 - 100;
        double layoutY = height/2 - 200;
        toCenter.setLayoutX(layoutX);
        toCenter.setLayoutY(layoutY);
    }

    default void centerBox(HBox toCenter){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        double layoutX = width/2 - 150;
        double layoutY = height/2 - 200;
        toCenter.setLayoutX(layoutX);
        toCenter.setLayoutY(layoutY);
    }

    default void centerBox(HBox toCenter, double layoutX, double layoutY){
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();

        toCenter.setLayoutX(width / 2 - layoutX);
        toCenter.setLayoutY(height / 2 - layoutY);
    }

    default Image getCardImage(DevelopmentCard card){
        return new Image("/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-" + card.getId() + "-1.png");
    }

    default Image getLeaderCardImage(LeaderCard leaderCard){

        if (leaderCard.getId() == 0)
            return new Image("/back/Masters of Renaissance__Cards_BACK_3mmBleed-0-1.png");

        return new Image("/front/Masters of Renaissance_Cards_FRONT_3mmBleed_1-" + leaderCard.getId() + "-1.png");

    }

    default double getCardWidth(){
        return 150;
    }

    default double getCardHeight(){
        return 200;
    }

    default Image getBasicProductionImage(){
        return new Image("/board/BasicProduction.png");
    }

}
