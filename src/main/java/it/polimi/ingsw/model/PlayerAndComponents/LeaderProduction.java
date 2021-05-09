package it.polimi.ingsw.model.PlayerAndComponents;

import it.polimi.ingsw.model.Resources.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * this class represent the storage for the resources that when given in input can generate a leader production
 * this class will extend ProductionPower if and only if the player activate a NewProduction leader card
 */
public class LeaderProduction extends ProductionPower {

    /**
     * this attribute represent the input resources for a leader card
     */
    private final List<Resource> inputs;

    /**
     * this attribute indicates if one of the two possible leader productions have
     * been activated during the turn
     */
    private boolean[] leaderProductionActivated = { false };

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
        leaderProductionActivated = new boolean[]{false, false};
        inputs.add(toAdd);
    }

    /**
     * this method activate the leader production if it exist, do nothing
     * if the leader production does not exist.
     * in particular, it simply set to true the attribute leaderProductionActivated
     * associated with the index specified in input
     * @param toActivate this is the index associated with the
     *                  leader production to activate, it should be from 1 to 2
     */
    public void activateLeaderProduction(int toActivate){

        try {
            leaderProductionActivated[toActivate - 1] = true;
        } catch (IndexOutOfBoundsException ignored){ }
    }

    /**
     * this method verify if the leader production associated with the index in input
     * is active or not.
     * the method always return false when the LeaderProduction doesn't exist
     * @param toCheck this is the index associated with the leader production to activate
     * @return true if the leader production has already been activated during the turn, false otherwise
     */
    @Override
    public boolean isLeaderProductionActivated(int toCheck){
        try {
            return leaderProductionActivated[toCheck - 1];
        } catch (IndexOutOfBoundsException ignored){
            return false;
        }
    }

    /**
     * this method reset the productions for every card (counting leader ones) of the dashboard and the basic production,
     * so that the turn after the owner of the card can activate them again
     */
    @Override
    public void resetProductions() {
            super.resetProductions();
            Arrays.fill(leaderProductionActivated, false);
    }

    @Override
    public String toString() {

        List<String> leaderProductions = new ArrayList<>();
        inputs.
                forEach(resource -> leaderProductions.
                        add("input :" +resource.toString() + ", output : (?) + 1 faithPoint"));

        return super.toString() + "\n" +
                "LeaderProductions :" + "\n" +
                leaderProductions ;
    }
}
