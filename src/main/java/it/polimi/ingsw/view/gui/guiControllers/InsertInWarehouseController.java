package it.polimi.ingsw.view.gui.guiControllers;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.InsertInWarehouseCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InsertInWarehouseController extends TurnsController{


    private final List<Integer> shelves = new ArrayList<>();
    private final List<Resource> gainedFromMarbleMarket = new ArrayList<>(getModel().getGainedFromMarbleMarket());
    private ImageView resource = new ImageView();
    private Label contextAction = new Label();
    private List<Node> buttons = new ArrayList<>();

    public InsertInWarehouseController(ThinModel model, String nickname, NetworkUser<Command, ResponseToClient> clientNetworkUser, boolean leaderAction) {
        super(model, nickname, clientNetworkUser, true, leaderAction);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMainWindowSize();

        drawPlayer(getNickname());
        setPlayerOpacity(0.5);
        setWarehouseOpacity(1);
        getMainWindow().getChildren().add(resource);
        getMainWindow().getChildren().add(contextAction);
        drawScenario();
    }

    public void drawScenario(){

        getMainWindow().getChildren().addAll(showButtons());
        showResource();



    }

    private List<Node> showButtons(){

        buttons = new ArrayList<>();

        Button firstShelf = setButton("1", actionEvent -> {
            selectedShelf(1);
        });

        firstShelf.setLayoutX(getFirstShelf().getLayoutX() * 4 / 6);
        firstShelf.setLayoutY(getFirstShelf().getLayoutY());

        buttons.add(firstShelf);

        Button secondShelf = setButton("2", actionEvent -> {
            selectedShelf(2);
        });

        secondShelf.setLayoutX(getSecondShelf().getLayoutX() * 4 / 6);
        secondShelf.setLayoutY(getSecondShelf().getLayoutY());

        buttons.add(secondShelf);

        Button thirdShelf = setButton("3" , actionEvent -> {
            selectedShelf(3);
        });

        thirdShelf.setLayoutX(getThirdShelf().getLayoutX() * 4 / 6);
        thirdShelf.setLayoutY(getThirdShelf().getLayoutY());


        buttons.add(thirdShelf);

        if (getModel().getGame().getMyself().getWarehouse().getFourthShelf() == null){
            return buttons;
        }

        Button fourthShelf = setButton("4", actionEvent -> {
            selectedShelf(4);
        });


        buttons.add(fourthShelf);

        if (getModel().getGame().getMyself().getWarehouse().getFifthShelf() == null){
            return buttons;
        }
        Button fifthShelf = setButton("5", actionEvent -> {
            selectedShelf(5);
        });




        buttons.add(fifthShelf);
        return buttons;

    }

    private void showResource(){

        resource.setImage(getResourceImage(gainedFromMarbleMarket.get(0)));
        resource.setFitWidth(getMainWindow().getPrefWidth()/ 4);
        resource.setFitHeight(getMainWindow().getPrefWidth()/ 4);

        centerImageView(resource);

        contextAction.setText("Decide in which shelf you want to place : " + gainedFromMarbleMarket.get(0).toString().toUpperCase());
        contextAction.setPrefWidth(10000);
        contextAction.setFont(Font.font(31));
        contextAction.setAlignment(Pos.CENTER);
        contextAction.setPrefSize( getMainWindow().getPrefWidth() - contextAction.getText().length(), getMainWindow().getPrefHeight() / 5 );


        //getMainWindow().getChildren().add(resource);
        //getMainWindow().getChildren().add(contextAction);

        gainedFromMarbleMarket.remove(0);

    }

    public void selectedShelf(int shelf){
        shelves.add(shelf);

        if (shelves.size() == getModel().getGainedFromMarbleMarket().size()){
            sendNewCommand(new InsertInWarehouseCommand(shelves.stream().mapToInt( Integer::intValue).toArray()));
            return;
        }

        showResource();

    }

    @Override
    public void update() {

        Gui.setRoot("/TurnsWindow",
                new TurnsController(getModel(), getNickname(), getClientNetworkUser(), false, getLeaderAction()));
    }
}
