package it.polimi.ingsw.controller.commands;

/**
 * this enum represent all the possible commands of the game,
 * equivalents to all the possible transactions of the game state
 */
public enum CommandName {

    /**
     * this command represent the login command
     */
    LOGIN,

    /**
     * this command represent the pong command.
     * in particular, every client, if receive a ping from the server,
     * response to a pong message
     */
    PONG,

    /**
     * this command is a quit command.
     * in particular, when the server receive it, it has to
     * disconnect every client of the current lobby
     */
    QUIT,

    /**
     * thi command represent the set size command.
     * in particular, when a player connect to the server, have to indicates the
     * number of the player of the lobby that he want to join/create
     */
    SET_SIZE,

    /**
     * this command represent an unknown command
     */
    UNKNOWN,

    /**
     * this command represent the initialize leader cards command.
     * in particular, it is used after a login to decide the 2 initial leader
     * cards
     */
    INITIALISE_LEADER_CARDS,

    /**
     * this command represent the initialize resources command.
     * in particular, it is used after the initialise leader cards command,
     * and it is used to decide the initial resources
     */
    INITIALISE_RESOURCES,

    /**
     * this command represent the activate card command, it is
     * used to activate a leader card
     */
    ACTIVATE_CARD,

    /**
     * this command represent the discard card command, it is
     * used to discard a leader card
     */
    DISCARD_CARD,

    /**
     * this command represent the leader action command, it is used before
     * to do an activate/ discard leader card
     */
    LEADER_ACTION,

    /**
     * this command represent the buy card command
     */
    BUY_CARD,

    /**
     * this command represent the select position command.
     * in particular, it is used after a buy card command and it is used
     * to decide the position of the card bought in the dashboard
     */
    SELECT_POSITION,

    /**
     * this command represent the select resources from warehouse command.
     * in particular, it is used after the select position command,
     * and it is used to decide how to pay the card with the warehouse resources
     */
    SELECT_RESOURCES_FROM_WAREHOUSE,

    /**
     * this command represent the choose leader cards command.
     * in particular, it is used after the choose marbles command if and
     * only if the player own 2 leader card white marble conversion,
     * and it is used to decide how to convert the marbles for each white one selected
     */
    CHOOSE_LEADER_CARDS,

    /**
     * this command represent the choose marbles command
     */
    CHOOSE_MARBLES,

    /**
     * this command represent the insert in warehouse command.
     * in particular, it is used after the choose marbles command or after the
     * choose leader cards command, and it is used to decide how to place
     * the resources gained from the marble market in the warehouse shelves
     */
    INSERT_IN_WAREHOUSE,

    /**
     * this command represent the production command
     */
    PRODUCTION,

    /**
     * this command represent the basic production command.
     */
    BASIC_PRODUCTION,

    /**
     * this command represent the normal production command.
     */
    NORMAL_PRODUCTION,

    /**
     * this command represent the leader production command.
     */
    LEADER_PRODUCTION,

    /**
     * this command represent the end production command.
     */
    END_PRODUCTION,

    /**
     * this command represent the end turn command.
     */
    END_TURN,

    /**
     * this command represent the shift resources command.
     * in particular, it can be used during every turn phase
     * @see it.polimi.ingsw.controller.GamePhase
     */
    SHIFT_RESOURCES

}
