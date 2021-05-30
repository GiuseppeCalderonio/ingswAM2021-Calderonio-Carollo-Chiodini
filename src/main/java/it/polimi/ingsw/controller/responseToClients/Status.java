package it.polimi.ingsw.controller.responseToClients;

/**
 * this enum represent the possible serer status responses
 */
public enum Status {

    /**
     * this attribute represent the error response.
     * in particular, it is sent when the server had an internal problem
     */
    ERROR,

    /**
     * this attribute represent the wrong turn response
     */
    WRONG_TURN,



    ACCEPTED,
    QUIT,
    REFUSED,
    PING
}
