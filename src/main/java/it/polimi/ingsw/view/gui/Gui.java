package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.network.ClientNetwork;
import it.polimi.ingsw.network.NetworkUser;
import it.polimi.ingsw.network.localGame.LocalClient;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.guiControllers.*;
import it.polimi.ingsw.view.thinModelComponents.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sun.javafx.application.PlatformImpl.startup;
import static it.polimi.ingsw.view.thinModelComponents.ThinPlayer.createAllLeaderCards;


public class Gui extends Application implements View {

    private static Scene scene;
    private static GuiController controller;
    private static NetworkUser <Command, ResponseToClient> clientNetwork;
    private final ThinModel model = new ThinModel();



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
        stage.show();

    }

    public static void setRoot(String fxml, GuiController controller) {
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



    @Override
    public Command createCommand() throws IOException {
      return null;
    }

    @Override
    public void showContextAction( Status message) {

        startup( () -> controller.update());
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
        if (model.getPosition() == 1)
            setRoot("/TurnsWindow", new TurnsController(model, model.getGame().getMyself().getNickname(), clientNetwork));
        else
            setRoot("/TurnsWindow", new TurnsController(model, model.getGame().getMyself().getNickname(), clientNetwork, false, false));
    }

    @Override
    public void updateStartGame(DevelopmentCard[][] cardsMarket, Marble[][] marbleMarket, Marble lonelyMarble, SoloToken soloToken, ThinPlayer actualPlayer, List<ThinPlayer> opponents) {
        model.setGame(new ThinGame(cardsMarket, marbleMarket, lonelyMarble, soloToken, actualPlayer, opponents));
    }

    @Override
    public void updateWarehouse(ThinWarehouse warehouse, String nickname) {
        model.getGame().getPlayer(nickname).setWarehouse(warehouse);
    }

    @Override
    public void updateStrongbox(CollectionResources strongbox, String nickname) {
        model.getGame().getPlayer(nickname).setStrongbox(strongbox);

    }

    @Override
    public void updateMarbleMarket(Marble[][] marbleMarket, Marble lonelyMarble) {
        model.getGame().setMarbleMarket(marbleMarket);
        model.getGame().setLonelyMarble(lonelyMarble);
    }

    @Override
    public void updateCardsMarket(DevelopmentCard[][] cardsMarket) {
        model.getGame().setCardsMarket(cardsMarket);
    }

    @Override
    public void updateTrack(Map<String, ThinTrack> tracks) {
        model.getGame().updateTracks(tracks);
    }

    @Override
    public void updatePosition(int position) {
        model.setPosition(position);
    }

    @Override
    public void updateBufferMarbles(int marbles) {
        model.setMarbles(marbles);
    }

    @Override
    public void updateBufferGainedMarbles(List<Resource> gainedMarbles) {
        model.setGainedFromMarbleMarket(gainedMarbles);
    }

    @Override
    public void updateProductionPower(ThinProductionPower productionPower, String nickname) {
        model.getGame().getPlayer(nickname).setProductionPower(productionPower);
    }

    @Override
    public void updateCard(int level, CardColor color, DevelopmentCard card) {
        model.getGame().setCard(level, color, card);
    }

    @Override
    public void updateToken(SoloToken token) {
        model.getGame().setSoloToken(token);
    }

    @Override
    public List<LeaderCard> getAllLeaderCards() {
        return createAllLeaderCards();
    }

    @Override
    public void updateLeaderCards(List<ThinLeaderCard> leaderCards, String nickname) {
        if (!nickname.equals(model.getGame().getMyself().getNickname()))
            leaderCards.stream().filter(card -> !card.isActive()).forEach(ThinLeaderCard::hide);

        model.getGame().getPlayer(nickname).
                setLeaderCards(leaderCards.stream().map(ThinPlayer::recreate).collect(Collectors.toList()));
    }

    @Override
    public void updateTrack(ThinTrack track, String nickname) {
        model.getGame().getPlayer(nickname).setTrack(track);
    }

    @Override
    public void quit() {
        setRoot("/WaitingWindow", new WaitingController("A client disconnected, the game finish"));
    }

    @Override
    public void updateTurn(String ownerTurnNickname) {

        boolean actions = ownerTurnNickname.equals(model.getGame().getMyself().getNickname());

        setRoot("/TurnsWindow", new TurnsController(model,
                model.getGame().getMyself().getNickname(),
                clientNetwork,
                actions,
                actions));
    }

}