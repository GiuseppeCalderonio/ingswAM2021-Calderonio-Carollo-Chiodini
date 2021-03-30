package it.polimi.ingsw.model;

/**
 * this enumeration represent the type of a resource
 */

public enum ResourceType {
    /**
     * this are the four types of resource with their personal abbreviation associated
     */
    COIN("coin"),STONE("stone"),SERVANT("servant"),SHIELD("shield");
    /**
     * this attribute is the personal abbreviation associated with a type
     */
    private final String name;

    /**
     * this constructor create the enumeration
     * @param name is the abbreviation to associate with the type
     */

    ResourceType(String name) {
        this.name = name;
    }

    /**
     * this getter return the abbreviation associated with the type
     * @return the abbreviation associated with the type
     */
    public String getName(){
        return name;
    }
}
