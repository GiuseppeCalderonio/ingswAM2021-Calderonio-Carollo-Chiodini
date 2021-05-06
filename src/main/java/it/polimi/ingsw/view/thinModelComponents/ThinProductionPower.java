package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;

import java.util.List;
import java.util.Stack;

public class ThinProductionPower {

    private final List<DevelopmentCard> productionPower1;
    private final List<DevelopmentCard> productionPower2;
    private final List<DevelopmentCard> productionPower3;

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
}
