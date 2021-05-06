package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.network.Client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WhiteMarblesConversionResponse extends ResponseToClient{

    private final List<Resource> gainedFromMarbleMarket;

    public WhiteMarblesConversionResponse(CollectionResources resources, List<Resource> gainedFromMarbleMarket){
        super( "you gained " + resources.toString() + ", decide how to place them into the warehouse",
        new ArrayList<>(Collections.singletonList("insert_in_warehouse")) );
        this.gainedFromMarbleMarket = gainedFromMarbleMarket;
    }

    /**
     * this method update the state of a client.
     * in particular, it shows the message sent from the server and,
     * if the command is not of dynamic type ResponseToClient,
     * set the values that have to change after a model's change
     *
     * @param client this is the client to update
     */
    @Override
    public void updateClient(Client client) {

        client.setGainedFromMarbleMarket(gainedFromMarbleMarket);

        super.updateClient(client);
    }
}
