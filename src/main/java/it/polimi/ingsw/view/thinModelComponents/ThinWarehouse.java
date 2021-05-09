package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.PlayerAndComponents.Warehouse;
import it.polimi.ingsw.model.Resources.CollectionResources;

public class ThinWarehouse {

    private final CollectionResources firstShelf;
    private final CollectionResources secondShelf;
    private final CollectionResources thirdShelf;
    private CollectionResources fourthShelf;
    private CollectionResources fifthShelf;

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
                ", fourthShelf=" + fourthShelf.asList() + "\n" +
                ", fifthShelf=" + fifthShelf.asList() + "\n" +
                '}';
    }
}
