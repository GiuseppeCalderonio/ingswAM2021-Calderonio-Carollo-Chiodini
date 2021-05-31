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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        stage.getIcons().add(new Image("/punchboard/calamaio.png"));
        stage.setMaximized(true);
        stage.setScene(scene);
        /*
        Scale scale = new Scale(Screen.getPrimary().getBounds().getWidth(), Screen.getPrimary().getBounds().getHeight());
        scale.setPivotX(Screen.getPrimary().getBounds().getWidth());
        scale.setPivotY(Screen.getPrimary().getBounds().getHeight());
        scene.getRoot().getTransforms().setAll(scale);

         */


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

    /**
     * this method show the action associated with a specific
     * state of the game, as a suggestion or an error message
     * @param message this is the message to show
     */
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

    /**
     * this method show the error message whenever is present
     * @param e this is the exception trowed
     */
    @Override
    public void showErrorMessage(Exception e) {

    }

    /**
     * this method is used to show the initialising phase.
     * in particular, after that every player do the login, he have
     * to select the leader cards and the initial resources
     * @param leaderCards these are the initial leader cards
     * @param position this is the position of the player, used
     *                 to manage the choice of the resources
     */
    @Override
    public void showInitialisingPhase(List<LeaderCard> leaderCards, int position){
        runLater( () -> setRoot("/InitialisingWindow", new InitialisisngController(leaderCards, position, clientNetwork)) );
    }

    /**
     * this method show the game after that every player complete the initialization phase
     */
    @Override
    public void showCompleteGame() {
        if (model.getPosition() == 1)
            runLater(() -> setRoot("/TurnsWindow", new TurnsController(model, model.getGame().getMyself().getNickname(), clientNetwork)));

        else
            runLater( () -> setRoot("/TurnsWindow", new TurnsController(model, model.getGame().getMyself().getNickname(), clientNetwork, false, false)));

    }

    /**
     * this method get the thin model associated with the game
     * @return the thin model associated with the game
     */
    @Override
    public ThinModel getModel() {
        return model;
    }


    @Override
    public void quit() {
        runLater( () -> setRoot("/WaitingWindow", new WaitingController("A client disconnected, the game finish")));

    }

    /**
     * this method is used to change the turn, after that a player finish it.
     * in particular, if is the turn of the player specified by the nickname,
     * the view will notify the player, do nothing otherwise (or eventually show the
     * nickname of the player that own the turn)
     * @param ownerTurnNickname this is the nickname of the player that now own the turn
     */
    @Override
    public void updateTurn(String ownerTurnNickname) {

        boolean actions = ownerTurnNickname.equals(model.getGame().getMyself().getNickname());

        runLater( () -> setRoot("/TurnsWindow", new TurnsController(model,
                model.getGame().getMyself().getNickname(),
                clientNetwork,
                actions,
                actions)));

    }

    /**
     * this method show the winner of the game.
     * @param winner this is the nickname of the winner
     * @param victoryPoints these are the victory points of the winner
     */
    @Override
    public void showWinner(String winner, int victoryPoints) {

        runLater( () -> setRoot("/WaitingWindow", new WaitingController("the winner is " + winner + ", you gained: " + victoryPoints)));
    }
}