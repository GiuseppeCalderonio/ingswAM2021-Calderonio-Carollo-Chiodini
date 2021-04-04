package it.polimi.ingsw.model;

/**
 * this class represent whe warehouse with the 3 shelfs
 */
public class Warehouse {

    /**
     * this attribute represent the 3 shelfs as a static vector
     */
    private final Shelf[] shelf;

    /**
     * this constructor create 3 empty shelfs, the first with cardinality = 1,
     * the second with cardinality = 2, the third with cardinality = 3
     */
    public Warehouse() {
        shelf = new Shelf[3];
        shelf[0] = new Shelf(1);
        shelf[1] = new Shelf(2);
        shelf[2] = new Shelf(3);
    }

    protected Warehouse(Warehouse warehouse){
        this.shelf = warehouse.shelf;
    }

    /**
     * this method get the shelf selected from the index numOfShelf
     * @param numOfShelf this is the index between 1 and 3
     * @return the shelf selected if the index is between 1 and 3, null otherwise
     */
    public Shelf getShelf(int numOfShelf){

        return shelf[numOfShelf-1];
    }

    /**
     * this method get the index of a shelf in which are stored the resources of type specified
     * if it exists, get 0 if not
     * @param type this is the resource type to check
     * @return the index of a shelf in which are stored the resources of type specified if it exists, get 0 if not
     */
    public int getShelfFromType(ResourceType type){
        int i;
        for(i=0;i<3;i++){
            if (!shelf[i].isEmpty()){
                if(shelf[i].getResourceType().equals(type))
                    return i+1;
            }
        }
        return 0;
    }

    /**
     * this method add the resources to the shelf specified and return the number of resources discarded,
     * that actually are the faith points to add for the other players
     * @param toAdd these are the resources to add
     * @param numShelf this is the shelf selected, it should be from 1 to 3
     * @return the number of the resources discarded, that actually are the faith points to add for the other players
     */
    public int addResources(CollectionResources toAdd, int numShelf){
        int faithPoints=0;
        //is a shelfCollection
        if(!(toAdd instanceof ShelfCollection))
            return toAdd.getSize();
        //the shelf selected isn't compatible with the resources in input
        if(!(shelf[numShelf-1].isEmpty())) {
            if (!(toAdd.isCompatible(shelf[numShelf - 1].getResources())))
                return toAdd.getSize();
        }
        // if does not exist a shelf of that type
        for (Shelf s: shelf) {
            if (!s.isEmpty() && !s.equals(shelf[numShelf - 1])) {
                if (toAdd.isCompatible(s.getResources()))
                    return toAdd.getSize();
            }
        }
        for (Resource r: toAdd) {
            if(shelf[numShelf - 1].addResource(r))
                faithPoints++;
        }
        return faithPoints;
    }

    /**
     * this method remove the resources from the shelf selected,
     * it requires that the resources to remove are contained in the shelf selected
     * @param toRemove these are the resources to remove
     */
    public void removeResources(CollectionResources toRemove){
        for (Resource r: toRemove) {
            for (int i = 0; i < 3; i++){
                if (r.getType().equals(shelf[i].getResourceType()))
                    shelf[i].removeResource(r);
            }
        }
    }

    /**
     * this method shift two shelfs, and return the number of resources that have to be discarded
     * @param source this is the index of the first shelf between 1 and 3
     * @param destination this is the index of the second shelf between 1 and 3
     * @return the number of discarded resources, that actually are faith points for the other players
     */
    public int shiftResources(int source, int destination){
        if(source == destination) return 0;
        int faithPoints = 0;


        //SOLUZIONE DI GABRI SOTTO
        Shelf temp= new Shelf(3);

        if(!(shelf[source-1].isEmpty())){
            shelf[source-1].getResources().forEach(temp::addResource);
            shelf[source-1].removeAll();
        }
        if (!(shelf[destination-1].isEmpty())) {
            for (Resource r : shelf[destination - 1].getResources()) {
                if (shelf[source - 1].addResource(r))
                    faithPoints++;
            }
        }
        shelf[destination - 1].removeAll();
        if (!(temp.isEmpty())) {
            for (Resource r : temp.getResources()) {
                if (shelf[destination - 1].addResource(r))
                    faithPoints++;
            }
        }
        return faithPoints;
    }

    /**
     * this method get all the resources contained in all the shelfs
     * @return all the resources contained in all the shelfs
     */
    public CollectionResources getTotalResources(){
        int i;
        CollectionResources temp = new CollectionResources();
        for(i=0;i<3;i++){
            shelf[i].getResources().forEach(temp::add);
        }
        return temp;
    }

    /**
     * this method get the number of all the resources contained in all the shelfs
     * @return the number of all the resources contained in all the shelfs
     */
    public int getNumberOfResources(){
        int i;
        int counter = 0;
        for(i=0;i<3;i++) {
            counter = counter + shelf[i].getResources().getSize();
        }
        return counter;
    }

    /**
     * this method is overrided by LeaderWarehouse
     * @param input the input resource
     */
    protected void addShelf(Resource input){
    }

}

