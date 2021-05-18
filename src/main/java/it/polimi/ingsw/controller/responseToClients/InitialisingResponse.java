package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.graphic.CharFigure;
import it.polimi.ingsw.view.graphic.GraphicalInitializingLeaderCard;
import it.polimi.ingsw.view.thinModelComponents.ThinLeaderCard;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * this class represent the initialising response.
 * in particular, when every player do the login the server
 * send to them the 4 casual leader cards and the casual position
 * for the turns
 */
public class InitialisingResponse extends ResponseToClient{

    /**
     * this attribute represent the casual position assigned to a client
     */
    private final int position;

    /**
     * this attribute represent the 4 initial leader cards
     */
    private final List<ThinLeaderCard> leaderCards;

    /**
     * this constructor create the response starting from the client,
     * getting from him all the data needed and setting all of them,
     * and the integer position representing the position
     *
     * @param client this is the client that send the response
     * @param position this is the casual position of the client to start the turn
     */
    public InitialisingResponse(ClientHandler client, int position) {
        // create a new response to send to the client setting the possible commands
        super(Status.ACCEPTED);
        // get the leader cards of the client
        List<ThinLeaderCard> leaderCards = client.getGame().
                findPlayer(client.getNickname()).
                getPersonalLeaderCards().stream().
                map(LeaderCard::getThin).
                collect(Collectors.toList());

        // set the position (that works because the clients are sorted with the game casual order)
        this.position = position;
        // send the thin leader cards
        this.leaderCards = leaderCards;
    }

    /**
     * this method update the state of a client.
     * in particular, it shows the message sent from the server and,
     * if the command is not of dynamic type ResponseToClient,
     * set the values that have to change after a model's change
     * @param client this is the client to update
     */
    @Override
    public void updateClient(Client client){
        // set the position
        client.setPosition(position);
        // set the leader cards, recreating them
        List<LeaderCard> leaderCards = this.leaderCards.
                stream().
                map(leaderCard -> recreate(leaderCard, client.getAllLeaderCards())).
                collect(Collectors.toList());

        // print the leader cards with ascii art
        CharStream console = new CharStream(200, 30);
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 60; j++) {
                console.addColor(i, j, BackColor.ANSI_BRIGHT_BG_BLACK);
            }
        }

        CharFigure graphicLeaderCards = new GraphicalInitializingLeaderCard(console, leaderCards);
        graphicLeaderCards.draw();
        console.print(System.out);
        console.reset();
        super.updateClient(client);
    }

    /**
     * this method build the leader cards starting from the victory points and the resource associated
     * @param leaderCard this is the leader card to recreate
     * @return the card
     */
    private LeaderCard recreate(ThinLeaderCard leaderCard, List<LeaderCard> allLeaderCards){
        Resource resource = leaderCard.getResource();
        int victoryPoints = leaderCard.getVictoryPoints();
        for (LeaderCard card : allLeaderCards){
            if (card.getResource().equals(resource) && card.getVictoryPoints() == victoryPoints){
                if (leaderCard.isActive())
                    card.setActive();
                return card;
            }
        }
        return null;
    }


}
