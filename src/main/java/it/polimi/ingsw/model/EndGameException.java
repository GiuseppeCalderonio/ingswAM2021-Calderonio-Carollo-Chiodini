package it.polimi.ingsw.model;

/**
 * this exception represent the end game exception.
 * in particular, when the conditions for ending a match are met, the model
 * methods that can throw this exception, and the message will contain the
 * winner of the game
 */
public class EndGameException extends RuntimeException{

    /**
     * this constructor create the exception setting the message selected
     * @param message this is the message to set, it should be the nickname of the winner
     */
    public EndGameException(String message){
        super(message);
    }
}
