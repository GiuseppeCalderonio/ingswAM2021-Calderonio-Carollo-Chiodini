package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * this class represent the response to send when
 * a client chosen a row/column of marbles from the marble market,
 * and have to decide how to add the resources gained into the warehouse
 */
public class WhiteMarblesConversionResponse extends ResponseToClient{

    /**
     * this attribute represent a set of the resources gained from the choice as a list
     */
    private final List<Resource> gainedFromMarbleMarket;

    /**
     * this constructor create the response setting the default message:
     * "you gained [x] resources, decide how to place them into the warehouse"
     * where x is the parameter resources converted to string.
     * it also set the default singleton list of possible commands [insert_in_warehouse],
     * and set the set of resources gained as a list, inferred from the collectionResources
     * passed as parameter in input
     * @param resources these are the resources gained
     */
    public WhiteMarblesConversionResponse(CollectionResources resources){
        super( Status.ACCEPTED);
        this.gainedFromMarbleMarket = new ArrayList<>(new HashSet<>(resources.asList()));
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
    public void updateClient(View view) {

        view.updateBufferGainedMarbles(gainedFromMarbleMarket);

        //client.setGainedFromMarbleMarket(gainedFromMarbleMarket);

        super.updateClient(view);
    }
}
