package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.thinModelComponents.ThinGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * JavaFX App
 */
public class Gui extends Application implements View {

    private static Scene scene;
    private static final Object lock = new Object();
    private static Command command;
    private static Client client;
    private static GuiController controller;

    public static void setClient(Client clientToSet){
        client = clientToSet;
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setCommand(Command commandToSet){
        synchronized (lock){
            command = commandToSet;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/SetSizeWindow", new SetSizeController(client)), 640, 480);
        stage.setScene(scene);
        stage.show();

    }

    static void setRoot(String fxml, GuiController controller) throws IOException {
        scene.setRoot(loadFXML(fxml, controller));
    }

    private static Parent loadFXML(String fxml, GuiController controllerToSet) throws IOException {
        controller = controllerToSet;
        FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource(fxml + ".fxml"));
        fxmlLoader.setControllerFactory((Class<?> guiController) -> {
            return controller;
        });
        return fxmlLoader.load();
    }

    public static void startGui() {
        launch();

    }

    @Override
    public Command createCommand() throws IOException {
        try {
            while (true){
                TimeUnit.MILLISECONDS.sleep(200);
                synchronized (lock){
                    if (command != null){
                        Command toReturn = command;
                        command = null;
                        return toReturn;
                    }
                }

            }
        } catch (InterruptedException e){
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    @Override
    public void showContextAction(Command command, Status message) {
        try {
            if (message.equals(Status.ACCEPTED))
                controller.update(command.getCmd());
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public void showWelcomeMessage() {

    }

    @Override
    public void show(ThinGame game) {
    }

    @Override
    public void showErrorMessage(Exception e) {

    }

    @Override
    public void showInitialisingPhase(List<LeaderCard> leaderCards, int position){
        try {
            setRoot("/InitialisingWindow", new InitialisisngController(leaderCards, position, client));
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

    }
}