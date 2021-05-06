package it.polimi.ingsw.model.PlayerAndComponents;

import it.polimi.ingsw.model.Resources.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

public class LeaderWarehouse extends Warehouse {
    private final Shelf[] leaderShelf;

    /** this constructor initialize the leaderWarehouse adding a new leaderShelf of input type to the existing warehouse
     * @param input resource to instantiate a leaderShelf of input type inserted at the first position (0) in vector
     *             LeaderShelf
     * @param warehouse already existing regular warehouse
     */
    public LeaderWarehouse(Resource input, Warehouse warehouse) {
        super(warehouse);
        leaderShelf = new LeaderShelf[2];
        leaderShelf[0] = new LeaderShelf(input.getType());
    }

    /**
     * this method overrides the empty method in Warehouse and add the LeaderShelf of input type to the warehouse
     *  @param input resource to instantiate a leaderShelf of input type inserted at the second position (1) in vector
     *         LeaderShelf
     */
    public void addShelf(Resource input) {
        if (leaderShelf[1] == null) {
            leaderShelf[1] = new LeaderShelf(input.getType());
        }
    }


    /** this method that overrides the method in Warehouse return the Shelf at position numOfShelf that if it is between
     *  1 and 3 it refers to shelf in Warehouse, otherwise if numOfShelf is 4 or 5 it refers to leaderShelf in LeaderWarehouse
     * @param numOfShelf this is the index between 1 and 5 where the last two index refer to leaderShelves
     * @return the Shelf at position numOfShelf
     */
    @Override
    public Shelf getShelf(int numOfShelf) {
        if (numOfShelf >= 1 && numOfShelf <= 3)
            return super.getShelf(numOfShelf);
        return leaderShelf[numOfShelf - 4];
    }


    /** this method that overrides the method in Warehouse add the resources in ToAdd in the Shelf indicated in the
     * param numShelf
     * @param toAdd contains the resources need to be added. This collection must contain resources of only one type.
     * @param numShelf indicates the shelf in which it is needed to operate. If numShelf is between 1 and 3 it refers to
     *                 shelf contained in the class Warehouse otherwise if numOfShelf is 4 or 5 it refers to leaderShelf
     *                 in LeaderWarehouse
     * @return the number of discarded resources, that actually are faith points for the other players
     */
    @Override
    public int addResources(CollectionResources toAdd, int numShelf) {
        int leaderFaithPoints;

        if (!(toAdd instanceof ShelfCollection))
            return toAdd.getSize();

        if (numShelf >= 1 && numShelf <= 3){
            int defaultShelf = 4;
            leaderFaithPoints = super.addResources(toAdd, numShelf);

            CollectionResources toAddDefault = new ShelfCollection(((ShelfCollection) toAdd).getType());
            for (int i = 0; i < leaderFaithPoints; i++) {
                toAddDefault.add( ((ShelfCollection) toAdd).getType().getResource());
            }
            //toAdd.getMaps().get(0).add(-leaderFaithPoints);
            for (int i = 0; i < leaderShelf.length; i++) {
                if (leaderShelf[i] != null &&
                        leaderShelf[i].getResourceType().equals(toAdd.getMaps().get(0).getResource().getType()))
                    defaultShelf = i + 4;
            }
            return addResources(toAddDefault, defaultShelf);
        }

        if (!toAdd.isCompatible(leaderShelf[numShelf -4].getResources()))
            return toAdd.getSize();

        leaderFaithPoints = (int)toAdd.asList().stream().
                filter(leaderShelf[numShelf - 4]::addResource).
                count();
        return leaderFaithPoints;
    }


    /** this method that overrides the method in Warehouse, removes the resources from the LeaderWarehouse and
     * Warehouse, first takes the resources off the leaderShelves, then if other resources need to be removed it takes
     * the remaining resources off the shelves contained in Warehouse.
     * The method requires that the resources to remove are contained in the Warehouse.
     * @param toRemove these are the resources to remove
     */
    public void removeResources(CollectionResources toRemove) {
        CollectionResources temp = new CollectionResources();
        temp.sum(toRemove);

        temp.forEach(x -> Arrays.stream(leaderShelf).
                filter(Objects::nonNull).
                filter(y -> x.getType().equals(y.getResourceType())).
                filter(y -> !y.isEmpty()).
                map(y -> y.removeResource(x)).
                forEach(y -> temp.remove(x)));


        if (!temp.getMaps().isEmpty())
            super.removeResources(temp);
    }

    /** this method that overrides the method in Warehouse get all the resources contained in all the shelves (leader
     * and not)
     *  @return all the resources contained in all the shelves (leader and not)
     */
    @Override
    public CollectionResources getTotalResources() {
        CollectionResources temp;
        //int i;

        temp = super.getTotalResources();

        Arrays.stream(leaderShelf).
                filter(Objects::nonNull).
                forEach(x -> x.getResources().forEach(temp::add));

        return temp;
    }

    /** this method that overrides the method in Warehouse get the number of all the resources contained in all the
     *  shelves (leader and not)
     *  @return the number of all the resources contained in all the shelves (leader and not)
     */
    @Override
    public int getNumberOfResources() {
        //int i;
        int counter = super.getNumberOfResources();

        counter = counter + Arrays.stream(leaderShelf).
                filter(Objects::nonNull).
                flatMapToInt(x -> IntStream.of(x.getResources().getSize())).
                sum();

        return counter;
    }


    /** this method, that overrides the method in Warehouse, shift two shelves (leader or not) and return the number of
     *  resources that have to be discarded.
     * @param source this is the index of the first shelf between 1 and 5 where 4 and 5 refer to leaderShelves
     * @param destination this is the index of the second shelf between 1 and 5 where 4 and 5 refer to leaderShelves
     * @return the number of discarded resources, that actually are faith points for the other players
     */
    @Override
    public int shiftResources(int source, int destination) {

        if (source <= 3 && destination <= 3)
            return super.shiftResources(source, destination);

        if (source == destination) return 0;
        int i;


        if (source <= 3) { //source is a regular shelf while destination is a leaderShelf

            if (super.getShelf(source).isEmpty())
                return -1; //source doesn't contain any elements

            if (leaderShelf[destination - 4]!= null) { //a leaderShelf of destination index exists
                if (leaderShelf[destination - 4].getResourceType().equals(super.getShelf(source).getResourceType())) { //source and destination have same type
                    if (leaderShelf[destination - 4].getFreeSlots() > 0) {//there is free space in destination
                        leaderShelf[destination - 4].addResource(super.getShelf(source).getResources().getMaps().get(0).getResource());//add to destination
                        super.getShelf(source).removeResource(super.getShelf(source).getResources().getMaps().get(0).getResource()); //remove from source
                    }
                    else return -1; //there isn't free space in destination
                } else return -1; //source and destination have different types
            } else return -1; // a LeaderShelf of destination index doesn't exist

        }
        else { //source leaderShelf

            if (leaderShelf[source - 4] == null || leaderShelf[source-4].isEmpty()) {
                return -1; // a LeaderShelf of source destination doesn't exist or it doesn't contain elements
            }

            if (destination <= 3) { // source is a leaderShelf and destination is a regular shelf
                if (!super.getShelf(destination).isEmpty()) { //destination (regular) isn't empty
                    if (super.getShelf(destination).getResourceType().equals(leaderShelf[source - 4].getResourceType())) {//source and destination have same type
                        if (super.getShelf(destination).getFreeSlots()>0) { //there is free space in destination
                            super.getShelf(destination).addResource(super.getShelf(destination).getResources().getMaps().get(0).getResource()); //add to destination
                            leaderShelf[source - 4].removeResource(leaderShelf[source - 4].getResources().getMaps().get(0).getResource()); //remove from source
                        } else return -1; //there isn't free space in destination
                    } else return -1; //source and destination have different types
                } else { // destination (regular) is empty
                    for(i=1; i<=3; i++) {
                        if (!super.getShelf(i).isEmpty()){
                            if (super.getShelf(i).getResourceType().equals(leaderShelf[source -4].getResourceType()))
                                return -1; // a regular shelf, which index is different from destination and contains resources of the same type of those contained in source, exists
                        }
                    }
                    super.getShelf(destination).addResource(leaderShelf[source - 4].getResources().getMaps().get(0).getResource()); // add to destination
                    leaderShelf[source - 4].removeResource(leaderShelf[source - 4].getResources().getMaps().get(0).getResource());  // remove from source
                    }
                } else { //source and destination are leader shelves

                    if (leaderShelf[destination - 4] == null)
                        return -1; // a leaderShelf of destination index doesn't exist
                    if (leaderShelf[destination-4].getResourceType().equals(leaderShelf[source - 4].getResourceType())){ // //source and destination have same type
                        if (leaderShelf[destination - 4].getFreeSlots() > 0) { //there is free space in destination
                            leaderShelf[destination - 4].addResource(leaderShelf[source - 4].getResources().getMaps().get(0).getResource()); //add to destination
                            leaderShelf[source - 4].removeResource(leaderShelf[source - 4].getResources().getMaps().get(0).getResource()); // remove from source
                        } else return -1; //there isn't free space in destination
                    } else return -1; // //source and destination have different types
                }
            }
        return 0;
    }

    /**
     * this method get the number of shelves, in this
     * case always return 4 or 5, depending
     * on the number of leader shelves
     *
     * @return 4 if there is only one leader shelf 5 otherwise
     */
    @Override
    public int getNumOfShelves() {
        if (leaderShelf[1] == null) return 4;
        return 5;
    }

    @Override
    public String toString() {
        return super.toString() +
                "fourth shelf=" + leaderShelf[0].toString() + "\n" +
                "fifth shelf=" + leaderShelf[1].toString();
    }
}
