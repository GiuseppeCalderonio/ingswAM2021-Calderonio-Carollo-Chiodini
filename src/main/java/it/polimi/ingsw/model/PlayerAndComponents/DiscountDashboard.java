package it.polimi.ingsw.model.PlayerAndComponents;

import it.polimi.ingsw.model.PlayerAndComponents.Dashboard;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

/**
 * this class contains all methods of DiscountDashboard
 */
public class DiscountDashboard extends Dashboard {
    /**
     * this collection resources contain the resources on which the player has a discount(max 2 different elements)
     */
    private final CollectionResources discountedResources;

    /**
     * this is the constructor of the class, create the class when a leader card is activated for the first time
     * @param discount is the resource on which the player has a discount
     * @param dashboard is the same class dashboard that the player is using
     */
    public DiscountDashboard(Resource discount , Dashboard dashboard){
        super(dashboard);
        discountedResources = new CollectionResources();
        discountedResources.add(discount);
    }

    /**
     * this method add the second resource on which the player has a discount
     * @param discount is the discounted resource
     */
    public void addDiscount(Resource discount){
        discountedResources.add(discount);
    }

    /**
     * this method is a getter
     * @return the discounted resources
     */
    public CollectionResources getDiscount(){
        return discountedResources;
    }

    /** this method is a getter, when a player activate a leader card that generate a discount
     * we save the discount resource in CollectionResource
     * and this method is used to see the state of this collection
     *
     * @return the list of the total resources minus the discount
     */
    public CollectionResources getTotalDiscountResource(){
        CollectionResources toReturn = new CollectionResources();
        toReturn.sum(super.getTotalDiscountResource());
        toReturn.sum(discountedResources);
        return toReturn;
    }
}