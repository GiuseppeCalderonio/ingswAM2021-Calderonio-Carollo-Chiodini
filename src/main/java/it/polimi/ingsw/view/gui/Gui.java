package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.thinModelComponents.ThinGame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
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

    public Gui(){

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
        scene = new Scene(loadFXML("/SetSizeWindow"), 640, 480);
        stage.setScene(scene);
        stage.show();

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource(fxml + ".fxml"));
        controller = fxmlLoader.getController();
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
            if (command.getCmd().equals(CommandName.SET_SIZE) && message.equals(Status.ACCEPTED))
                setRoot("/LoginWindow");
            if (command.getCmd().equals(CommandName.LOGIN) && message.equals(Status.ACCEPTED))
                setRoot("/InitialisingWindow");
            if (command.getCmd().equals(CommandName.INITIALISE_RESOURCES) && message.equals(Status.ACCEPTED))
                setRoot("/TurnsWindow");
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
}