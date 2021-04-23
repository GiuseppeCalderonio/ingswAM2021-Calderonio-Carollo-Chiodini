package it.polimi.ingsw.model.Resources;

/**
 * this enumeration represent the type of a resource
 */

public enum ResourceType {

    /**
     * this are the four types of resource with their personal abbreviation associated
     */
    COIN("coin", new Coin()),STONE("stone", new Stone()),SERVANT("servant", new Servant()),SHIELD("shield", new Shield());

    /**
     * this attribute is the personal abbreviation associated with a type
     */
    private final String name;
    private final Resource resource;

    /**
     * this constructor create the enumeration
     * @param name is the abbreviation to associate with the type
     */
    ResourceType(String name, Resource resource) {

        this.name = name;
        this.resource = resource;
    }

    /**
     * this getter return the abbreviation associated with the type
     * @return the abbreviation associated with the type
     */
    public String getName(){
        return name;
    }

    /**
     * this getter returns the resource associated with the enum
     * @return the resource associated with the enum
     */
    public Resource getResource(){ return resource; }
}
