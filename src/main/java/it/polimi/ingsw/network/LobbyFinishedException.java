package it.polimi.ingsw.network;

/**
 * this exception is used only in the specific case in which a client pass the
 * check to get into a lobby, the lobby finish in that moment after, and the client try
 * to get in the lobby even if it is finished and no one could join it
 */
public class LobbyFinishedException extends RuntimeException{
}
