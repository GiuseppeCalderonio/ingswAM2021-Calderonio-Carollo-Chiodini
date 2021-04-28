package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.Servant;
import it.polimi.ingsw.model.SingleGame.CardToken;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.model.SingleGame.TrackToken;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestsForFun {

    @Test
    void test(){
        SoloToken token = new CardToken(CardColor.BLUE);
        Resource r1 = new Servant();
        GsonBuilder builder = new GsonBuilder();
        //
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.
                        of(SoloToken.class, "type").
                        registerSubtype(TrackToken.class, "trackToken").
                        registerSubtype(CardToken.class, "cardToken"));
        builder.registerTypeAdapter(Resource.class, new ResourceInterfaceAdapter());
        builder.registerTypeAdapter(Marble.class, new MarbleInterfaceAdapter());
        Gson gson = builder.create();
        //
        String tok = gson.toJson(token, SoloToken.class);
        SoloToken token1 = gson.fromJson(tok, SoloToken.class);
        assertEquals(token, token1);
        tok = gson.toJson(r1, Resource.class);
        Resource r2 = gson.fromJson(tok, Resource.class);
        assertEquals(r1, r2);


    }
}
