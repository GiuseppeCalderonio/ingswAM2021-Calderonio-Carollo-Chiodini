package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.model.Resources.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public interface GuiController {


    void update(CommandName name);

    default void showErrorMessage(String errorMessage){

    };

    void sendNewCommand(Command toSend);

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

}
