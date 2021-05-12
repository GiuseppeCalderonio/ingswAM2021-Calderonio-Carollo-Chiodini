package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.WhiteMarble;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.Servant;
import it.polimi.ingsw.model.SingleGame.CardToken;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.controller.gsonManager.PersonalGsonBuilder.createPersonalGsonBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestsForFun {

    @Test
    void test(){
        SoloToken token = new CardToken(CardColor.BLUE);
        Resource r1 = new Servant();
        Gson gson = createPersonalGsonBuilder();
        // token
        String tok = gson.toJson(token, SoloToken.class);
        SoloToken token1 = gson.fromJson(tok, SoloToken.class);
        assertEquals(token, token1);
        // resource
        tok = gson.toJson(r1, Resource.class);
        Resource r2 = gson.fromJson(tok, Resource.class);
        assertEquals(r1, r2);
        // marble
        tok = gson.toJson(new WhiteMarble(), Marble.class);
        Marble m2 = gson.fromJson(tok, Marble.class);
        assertEquals(new WhiteMarble(), m2);


    }
}
