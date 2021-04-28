package it.polimi.ingsw.model.DevelopmentCards;


import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.view.utilities.colors.BackColor;

public class DevelopmentCard {
    /**
     * this attribute represent the color of the card
     */
    private final CardColor color;
    /**
     * this attribute represent the level of the card
     */
    private final int level;
    /**
     * this attribute represent the victory points of the card
     */
    private final int victoryPoints;
    /**
     * this attribute represent the cost of the card
     */
    private final CollectionResources cost;
    /**
     * this attribute represent the resources to give in input to activate the production
     */
    private final CollectionResources productionPowerInput;
    /**
     * this attribute represent the resources that are gained if someone activate the production of this card
     */
    private final CollectionResources productionPowerOutput;
    /**
     * this attribute represent the faith points that are gained if someone activate the production of this card
     */
    private final int productionPowerFaithPoints;

    /**
     * this constructor create a card with all the parameters
     * @param color this is the color to set
     * @param level this is the level to set
     * @param victoryPoints these are the victory points to set
     * @param cost this is the cost of the card
     * @param productionPowerInput these are the resources to pay in input for a production to set
     * @param productionPowerOutput these are the resources gained in case of activation of the card to set
     * @param productionPowerFaithPoints these are the faith points gained in case of activation of the card to set
     */
    public DevelopmentCard(CardColor color,
                           int level,
                           int victoryPoints,
                           CollectionResources cost,
                           CollectionResources productionPowerInput,
                           CollectionResources productionPowerOutput,
                           int productionPowerFaithPoints) {
        this.color = color;
        this.level = level;
        this.victoryPoints = victoryPoints;
        this.cost = cost;
        this.productionPowerInput = productionPowerInput;
        this.productionPowerOutput = productionPowerOutput;
        this.productionPowerFaithPoints = productionPowerFaithPoints;
    }

    /**
     * this method return the color of the card
     * @return the color of the card
     */
    public CardColor getColor() {
        return color;
    }
    /**
     * this method return the level of the card
     * @return the level of the card
     */
    public int getLevel() {
        return level;
    }
    /**
     * this method return the victory points of the card
     * @return the victory points of the card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * this method return the cost of the card
     * @return the cost of the card
     */
    public CollectionResources getCost() {
        return cost;
    }

    /**
     * this method return the input production power of the card
     * @return the input production power of the card
     */
    public CollectionResources getProductionPowerInput() {
        return productionPowerInput;
    }
    /**
     * this method return the resources in output of the production power of the card
     * @return the resources in output of the production power of the card
     */
    public CollectionResources getProductionPowerOutput() {
        return productionPowerOutput;
    }
    /**
     * this method return the faith points in output of the production power of the card
     * @return the faith points in output of the production power of the card
     */
    public int getProductionPowerFaithPoints() {

        return productionPowerFaithPoints;
    }

    /**
     * this method verify if two cards are equals
     * @param toCompare is the card to compare
     * @return true if all the attributes of the card that calls the method are the same of toCompare, false otherwise
     */
    public boolean equals(Object toCompare){
        if (this == toCompare) return true;
        if (!(toCompare instanceof DevelopmentCard)) return false;
        DevelopmentCard CardToCompare = (DevelopmentCard) toCompare;
        return (color.equals(CardToCompare.getColor())
        && level == CardToCompare.getLevel()
        && victoryPoints == CardToCompare.getVictoryPoints()
        && cost.equals(CardToCompare.getCost())
        && productionPowerInput.equals(CardToCompare.getProductionPowerInput())
        && productionPowerOutput.equals(CardToCompare.getProductionPowerOutput())
        && productionPowerFaithPoints == CardToCompare.getProductionPowerFaithPoints()
        );

    }

    @Override
    public String toString() {
        return "DevelopmentCard :" +
                "color=" + color +
                ", level=" + level +
                ", VPoints=" + victoryPoints +
                ", cost=" + cost +
                ", Input=" + productionPowerInput +
                ", Output=" + productionPowerOutput +
                ", FPoints=" + productionPowerFaithPoints + "\n";
    }
}
