package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.PlayerAndComponents.Warehouse;
import it.polimi.ingsw.model.Resources.CollectionResources;

/**
 * this class represent the thin warehouse.
 * in particular, it is used only from the client in order to
 * represent the state of a warehouse in the game
 */
public class ThinWarehouse {

    /**
     * this attribute represent the first shelf
     */
    private final CollectionResources firstShelf;

    /**
     * this attribute represent the second shelf
     */
    private final CollectionResources secondShelf;

    /**
     * this attribute represent the third shelf
     */
    private final CollectionResources thirdShelf;

    /**
     * this attribute represent the fourth shelf,
     * if not present is null
     */
    private CollectionResources fourthShelf;

    /**
     * this attribute represent the fifth shelf,
     * if not present is null
     */
    private CollectionResources fifthShelf;

    /**
     * this constructor create the warehouse starting from a real player,
     * getting from it all the shelves and setting them to the attributes
     * @param player this is the player from which get the shelves
     */
    public ThinWarehouse(RealPlayer player){
        Warehouse warehouse = player.getPersonalDashboard().getPersonalWarehouse();
        this.firstShelf = warehouse.getShelf(1).getResources();
        this.secondShelf = warehouse.getShelf(2).getResources();
        this.thirdShelf = warehouse.getShelf(3).getResources();
        try {
            this.fourthShelf = warehouse.getShelf(4).getResources();
        }catch (IndexOutOfBoundsException | NullPointerException ignored){ }
        try {
            this.fifthShelf = warehouse.getShelf(5).getResources();
        }catch (IndexOutOfBoundsException | NullPointerException ignored){ }


    }

    @Override
    public String toString() {

        return "ThinWarehouse{" + "\n" +
                "firstShelf=" + firstShelf.asList() + "\n" +
                ", secondShelf=" + secondShelf.asList() + "\n" +
                ", thirdShelf=" + thirdShelf.asList() + "\n" +
                ", fourthShelf=" + fourthShelf+ "\n" +
                ", fifthShelf=" + fifthShelf+ "\n" +
                '}';
    }

    /**
     * this method get the first shelf of the warehouse as a CollectionResources
     * @return the first shelf of the warehouse as a collectionResources
     */
    public CollectionResources getFirstShelf() {
        return firstShelf;
    }

    /**
     * this method get the second shelf of the warehouse as a CollectionResources
     * @return the second shelf of the warehouse as a collectionResources
     */
    public CollectionResources getSecondShelf() {
        return secondShelf;
    }

    /**
     * this method get the third shelf of the warehouse as a CollectionResources
     * @return the third shelf of the warehouse as a collectionResources
     */
    public CollectionResources getThirdShelf() {
        return thirdShelf;
    }

    /**
     * this method get the fourth shelf of the warehouse as a CollectionResources,
     * null if it doesn't exist
     * @return the fourth shelf of the warehouse as a collectionResources
     */
    public CollectionResources getFourthShelf() {
        return fourthShelf;
    }

    /**
     * this method get the fifth shelf of the warehouse as a CollectionResources,
     * null if it doesn't exist
     * @return the fifth shelf of the warehouse as a collectionResources
     */
    public CollectionResources getFifthShelf() {
        return fifthShelf;
    }

}
