package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this class represent the storage for the resources that when given in input can generate a leader production
 * this class will extend ProductionPower if and only if the player activate a NewProduction leader card
 */
public class LeaderProduction extends ProductionPower{

    /**
     * this attribute represent the input resources for a leader card
     */
    private final List<Resource> inputs;

    /**
     * this constructor initialise the list of resource with one resource,
     * that should be resource of the first leader card activated
     * @param input this is the input resource
     * @param productionPower this is the production power to copy in the father class
     */
    public LeaderProduction(Resource input, ProductionPower productionPower) {
        super(productionPower.getPersonalCards());
        inputs = new ArrayList<>(Collections.singletonList(input));
    }

    /**
     * this method get the resource that will be used as the input for a leader production
     * @param index this is the index of the resource that should be got, it should be between 1 and 2
     * @return the resource associated with the input, null if the input is not correct
     */
    public Resource getInput(int index){
        try{
            return inputs.get(index - 1);
        } catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    /**
     * this method add a new resource to use as input of a leader production
     * @param toAdd this is the resource to add
     */
    public void addLeaderProduction(Resource toAdd){
        inputs.add(toAdd);
    }
}
