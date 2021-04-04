package it.polimi.ingsw.model;

/**
 * this class represent the shelf of a leader card when activated
 */
public class LeaderShelf extends Shelf{

    /**
     * this attribute represent the unique type of the resource that the shelf can contain
     */
    private final ResourceType type;

    /**
     *this method initialize the leader shelf that is like a shelf but his type is defined final
     * @param type of the leader shelf
     */
    public LeaderShelf(ResourceType type) {
        super(2);
        this.type = type;
    }

    /**
     *this method get the type of the leaderShelf
     * @return the type of the leaderShelf
     */
    public ResourceType getResourceType() {
        return type;
    }

    /**
     *this method add a resource if this is of the type of the leaderShelf and if there is space in the shelf
     * @param toAdd resource
     * @return true if there isn't space in the shelf or the resource is not of the same type of the shelf false when
     * the resource is effectively added
     */
    public boolean addResource(Resource toAdd) {
        if (!(toAdd.getType().equals(getResourceType())))
            return true;
        return super.addResource(toAdd);
    }
}
