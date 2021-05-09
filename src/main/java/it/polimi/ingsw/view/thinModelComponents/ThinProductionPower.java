package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;

import java.util.List;
import java.util.Stack;

/**
 * this class represent the thin production power.
 * in particular, it is used from the client in order to
 * represent the production power state of the game,
 * as 3 list of development cards
 */
public class ThinProductionPower {

    /**
     * this attribute represent the first deck of the production power
     */
    private final List<DevelopmentCard> productionPower1;

    /**
     * this attribute represent the second deck of the production power
     */
    private final List<DevelopmentCard> productionPower2;

    /**
     * this attribute represent the third deck of the production power
     */
    private final List<DevelopmentCard> productionPower3;

    /**
     * this constructor create the production power starting from the real player,
     * getting from him all the deck required and setting them to the attributes
     * @param player this is the player from which get all the cards
     */
    public ThinProductionPower(RealPlayer player) {
        Stack<DevelopmentCard>[] productionPower = player.getPersonalDashboard().getPersonalProductionPower().getPersonalCards();
        productionPower1 = productionPower[0];
        productionPower2 = productionPower[1];
        productionPower3 = productionPower[2];
    }

    @Override
    public String toString() {
        return "ThinProductionPower{" + "\n" +
                "productionPower1=" + productionPower1 + "\n" +
                ", productionPower2=" + productionPower2 + "\n" +
                ", productionPower3=" + productionPower3 + "\n" +
                '}';
    }

    /**
     * this method get the list of development card that represent the first deck of cards of the dashboard
     * @return the list of development card that represent the first deck of cards of the dashboard
     */
    public List<DevelopmentCard> getProductionPower1() {
        return productionPower1;
    }

    /**
     * this method get the list of development card that represent the second deck of cards of the dashboard
     * @return the list of development card that represent the second deck of cards of the dashboard
     */
    public List<DevelopmentCard> getProductionPower2() {
        return productionPower2;
    }

    /**
     * this method get the list of development card that represent the third deck of cards of the dashboard
     * @return the list of development card that represent the third deck of cards of the dashboard
     */
    public List<DevelopmentCard> getProductionPower3() {
        return productionPower3;
    }
}
