package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.UnknownCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.newView.ClientNetwork;
import it.polimi.ingsw.network.localGame.LocalClient;
import it.polimi.ingsw.view.thinModelComponents.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static javafx.application.Platform.runLater;


public class Gui extends Application implements View {

    private static Scene scene;
    //private static Client client;
    private static GuiController controller;
    private static NetworkUser <Command, ResponseToClient> clientNetwork;
    private ThinModel model;
    private static Command lastCommand = new UnknownCommand();

    public static void setLastCommand(Command lastCommandToSet){
        lastCommand = lastCommandToSet;
    }



    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/SetSizeWindow", new SetSizeController(clientNetwork)), 640, 480);
        stage.setScene(scene);
        stage.show();

    }

    static void setRoot(String fxml, GuiController controller) {
        try {
            scene.setRoot(loadFXML(fxml, controller));
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

    }

    private static Parent loadFXML(String fxml, GuiController controllerToSet) throws IOException {
        controller = controllerToSet;
        FXMLLoader fxmlLoader = new FXMLLoader(Gui.class.getResource(fxml + ".fxml"));
        fxmlLoader.setControllerFactory((Class<?> guiController) -> controller);
        return fxmlLoader.load();
    }

    public void startGui(String hostName, int portNumber) {
        try {
            if (hostName != null || portNumber == 0){
                clientNetwork = new ClientNetwork(hostName, portNumber, this);
                //client.startNetwork(hostName, portNumber);
            }
            else {
                clientNetwork = new LocalClient(this);
            }
        } catch (IOException e){
            System.exit(1);
        }

        launch();

    }

    @Override
    public Command createCommand() throws IOException {
      return null;
    }

    @Override
    public void showContextAction( Status message) {
        Runnable r;
            if (message.equals(Status.ACCEPTED))

                r = () -> controller.update(lastCommand.getCmd());

            else
                 r = () -> controller.showErrorMessage(lastCommand.getErrorMessage());

                runLater(r);
    }

    @Override
    public void showWelcomeMessage() {

    }

    @Override
    public void showCli() {



    }

    @Override
    public void showErrorMessage(Exception e) {

    }

    @Override
    public void showInitialisingPhase(List<LeaderCard> leaderCards, int position){
        setRoot("/InitialisingWindow", new InitialisisngController(leaderCards, position, clientNetwork));
    }

    @Override
    public void showCompleteGame() {
        setRoot("/TurnsWindow", new TurnsController());
    }

    @Override
    public void updateStartGame(DevelopmentCard[][] cardsMarket, Marble[][] marbleMarket, Marble lonelyMarble, SoloToken soloToken, ThinPlayer actualPlayer, List<ThinPlayer> opponents) {
        model.setGame(new ThinGame(cardsMarket, marbleMarket, lonelyMarble, soloToken, actualPlayer, opponents));
        //this.model = new ThinModel(cardsMarket, marbleMarket, lonelyMarble, soloToken, actualPlayer, opponents);
    }

    @Override
    public void updateWarehouse(ThinWarehouse warehouse, String nickname) {

    }

    @Override
    public void updateStrongbox(CollectionResources strongbox, String nickname) {

    }

    @Override
    public void updateMarbleMarket(Marble[][] marbleMarket, Marble lonelyMarble) {

    }

    @Override
    public void updateCardsMarket(DevelopmentCard[][] cardsMarket) {

    }

    @Override
    public void updateTrack(Map<String, ThinTrack> tracks) {

    }

    @Override
    public void updatePosition(int position) {

    }

    @Override
    public void updateBufferMarbles(int marbles) {

    }

    @Override
    public void updateBufferGainedMarbles(List<Resource> gainedMarbles) {

    }

    @Override
    public void updateProductionPower(ThinProductionPower productionPower, String nickname) {

    }

    @Override
    public void updateCard(int level, CardColor color, DevelopmentCard card) {

    }

    @Override
    public void updateToken(SoloToken token) {

    }

    @Override
    public List<LeaderCard> getAllLeaderCards() {
        return null;
    }

    @Override
    public void updateLeaderCards(List<ThinLeaderCard> leaderCards, String nickname) {

    }

    @Override
    public void updateTrack(ThinTrack track, String nickname) {

    }


}