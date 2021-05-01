package it.polimi.ingsw.controller.gsonManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.SingleGame.CardToken;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.model.SingleGame.TrackToken;

public class PersonalGsonBuilder {

    public static Gson createPersonalGsonBuilder(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.
                        of(SoloToken.class, "type").
                        registerSubtype(TrackToken.class, "trackToken").
                        registerSubtype(CardToken.class, "cardToken"));
        builder.registerTypeAdapter(Resource.class, new ResourceInterfaceAdapter());
        builder.registerTypeAdapter(Marble.class, new MarbleInterfaceAdapter());
        return builder.create();
    }
}
