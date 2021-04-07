package it.polimi.ingsw.model;

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
        int leaderFaithPoints = 0;

        if (numShelf >= 1 && numShelf <= 3)
            return super.addResources(toAdd, numShelf);

        if (leaderShelf[numShelf - 4] == null)
            return toAdd.getSize();

        if (!(toAdd instanceof ShelfCollection))
            return toAdd.getSize();

        if (!toAdd.isCompatible(leaderShelf[numShelf -4].getResources()))
            return toAdd.getSize();

        for (Resource r : toAdd) {
            if (leaderShelf[numShelf - 4].addResource(r))
                leaderFaithPoints++;
        }
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


        /*for (Resource r : temp) {
            for (int i = 0; i < 2; i++) {
                try {
                    if (r.getType().equals(leaderShelf[i].getResourceType()))
                        if (!leaderShelf[i].isEmpty()) {
                            leaderShelf[i].removeResource(r);
                            temp.remove(r);
                        }
                } catch(NullPointerException ignored){}
            }
        }*/
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

        /*for (i = 0; i < 2; i++) {
            try{
                leaderShelf[i].getResources().forEach(temp::add);
            }catch (NullPointerException ignored){}
        }*/
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

        /*for (i = 0; i < 2; i++) {
            if (leaderShelf[i] != null)
                counter = counter + leaderShelf[i].getResources().getSize();
        }*/
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

        int faithPoints = 0;
        Shelf tempSource = new Shelf(3);


        if (source <= 3) {
            if (!(super.getShelf(source).isEmpty())) {
                super.getShelf(source).getResources().forEach(tempSource::addResource);
                super.removeResources(super.getShelf(source).getResources());
            }
        } else {
            if (leaderShelf[source - 4] != null) {
                if (!(leaderShelf[source - 4].isEmpty())) {
                    leaderShelf[source - 4].getResources().forEach(tempSource::addResource);
                    leaderShelf[source - 4].removeAll();
                }
            }
        }

        if (destination <= 3) {
            if (!(super.getShelf(destination).isEmpty())) {
                for (Resource r : super.getShelf(destination).getResources()) {
                    if (leaderShelf[source - 4].addResource(r))
                        faithPoints++;
                }
            }
            super.removeResources(super.getShelf(destination).getResources());
        } else {
            if (leaderShelf[destination - 4] != null) {
                if (!(leaderShelf[destination - 4].isEmpty())) {
                    if (source <= 3) {
                        faithPoints = faithPoints + super.addResources(leaderShelf[destination - 4].getResources(), source);
                    } else {
                        for (Resource r : leaderShelf[destination - 4].getResources()) {
                            if (leaderShelf[source - 4].addResource(r))
                                faithPoints++;
                        }
                    }
                }
            }
            leaderShelf[destination - 4].removeAll();
        }


        if (!(tempSource.isEmpty())) {
            if (destination <= 3) {
                faithPoints = faithPoints + super.addResources(tempSource.getResources(), destination);
            } else {
                for (Resource r : tempSource.getResources()) {
                    if (leaderShelf[destination - 4].addResource(r))
                        faithPoints++;
                }
            }
        }
        return faithPoints;
    }
}
