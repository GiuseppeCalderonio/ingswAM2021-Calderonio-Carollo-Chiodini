package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.QuitCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.network.ClientNetwork;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.network.localGame.LocalClient;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.guiControllers.*;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static com.sun.javafx.application.PlatformImpl.runLater;


public class Gui extends Application implements View {

    private static Scene scene;
    private static GuiController controller;
    private static NetworkUser <Command, ResponseToClient> clientNetwork;
    private final ThinModel model = new ThinModel();
    private static final String windowPath = "/FxmlWindows";


    public void startGui(String hostName, int portNumber) {
        try {
            if (hostName != null || portNumber != 0){
                clientNetwork = new ClientNetwork(hostName, portNumber, this);
            }
            else {
                clientNetwork = new LocalClient(this);
            }
        } catch (IOException e){
            System.exit(1);
        }

        launch();
        /*
        Platform.startup( () -> {
            try {
                start(new Stage());
            }catch (IOException e){
                System.exit(1);
            }
        });
        */
    }

    public static Scene getScene(){
        return scene;
    }


    private String getPathFirstWindow(){

        if (clientNetwork instanceof LocalClient)
            return "/LoginWindow";

        return "/SetSizeWindow";
    }

    private GuiController getFirstController(){
        if (clientNetwork instanceof LocalClient)
            return new LoginController(clientNetwork);
        return new SetSizeController(clientNetwork);
    }



    @Override
    public void start(Stage stage) throws IOException {

        scene = new Scene(loadFXML(getPathFirstWindow(), getFirstController()), 640, 480);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setOnCloseRequest(windowEvent -> {
            clientNetwork.send(new QuitCommand());
            System.exit(1);
        });
        stage.show();

    }

    public static void setRoot(String fxml, GuiController controller) {
        try {
            scene.setRoot(loadFXML( fxml, controller));
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static Parent loadFXML(String fxml, GuiController controllerToSet) throws IOException {
        controller = controllerToSet;
        FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource(windowPath + fxml + ".fxml"));
        fxmlLoader.setControllerFactory((Class<?> guiController) -> controller);
        return fxmlLoader.load();
    }

    @Override
    public void showContextAction( Status message) {

        try {
            if (message.equals(Status.REFUSED))
                runLater( () -> controller.showErrorMessage());
            else if (message.equals(Status.ERROR))
                System.exit(1);

        } catch (NullPointerException ignored) { }

        runLater( () -> controller.update());
    }

    @Override
    public void showErrorMessage(Exception e) {

    }

    @Override
    public void showInitialisingPhase(List<LeaderCard> leaderCards, int position){
        runLater( () -> setRoot("/InitialisingWindow", new InitialisisngController(leaderCards, position, clientNetwork)) );
    }

    @Override
    public void showCompleteGame() {
        if (model.getPosition() == 1)
            runLater(() -> setRoot("/TurnsWindow", new TurnsController(model, model.getGame().getMyself().getNickname(), clientNetwork)));

        else
            runLater( () -> setRoot("/TurnsWindow", new TurnsController(model, model.getGame().getMyself().getNickname(), clientNetwork, false, false)));

    }

    @Override
    public ThinModel getModel() {
        return model;
    }


    @Override
    public void quit() {
        runLater( () -> setRoot("/WaitingWindow", new WaitingController("A client disconnected, the game finish")));

    }

    @Override
    public void updateTurn(String ownerTurnNickname) {

        boolean actions = ownerTurnNickname.equals(model.getGame().getMyself().getNickname());

        runLater( () -> setRoot("/TurnsWindow", new TurnsController(model,
                model.getGame().getMyself().getNickname(),
                clientNetwork,
                actions,
                actions)));

    }

    @Override
    public void showWinner(String winner, int victoryPoints) {

        runLater( () -> setRoot("/WaitingWindow", new WaitingController("the winner is " + winner + ", you gained: " + victoryPoints)));
    }
}