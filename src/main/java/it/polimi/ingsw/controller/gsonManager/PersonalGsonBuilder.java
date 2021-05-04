package it.polimi.ingsw.controller.gsonManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.*;
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

        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.
                        of(Command.class, "type").

                        registerSubtype(ActivateCardCommand.class, "ActivateCardCommand").
                        registerSubtype(BasicProductionCommand.class, "BasicProductionCommand").


                        registerSubtype(BuyCardAction.class, "BuyCardAction").
                        registerSubtype(ChooseLeaderCardsCommand.class, "ChooseLeaderCardsCommand").
                        registerSubtype(ChooseMarblesCommand.class, "ChooseMarblesCommand").

                        registerSubtype(DiscardCardCommand.class, "DiscardCardCommand").
                        registerSubtype(EndProductionCommand.class, "EndProductionCommand").

                        registerSubtype(EndTurnCommand.class, "EndTurnCommand").
                        registerSubtype(InitialiseLeaderCardsCommand.class, "InitialiseLeaderCardsCommand").
                        registerSubtype(InitialiseResourcesCommand.class, "InitialiseResourcesCommand").
                        registerSubtype(InsertInWarehouseCommand.class, "InsertInWarehouseCommand").
                        registerSubtype(LeaderCommand.class, "LeaderCommand").

                        registerSubtype(LeaderProductionCommand.class, "LeaderProductionCommand").

                        registerSubtype(LoginCommand.class, "LoginCommand").

                        registerSubtype(NormalProductionCommand.class, "NormalProductionCommand").

                        registerSubtype(ProductionCommand.class, "ProductionCommand").
                        registerSubtype(QuitCommand.class, "QuitCommand").
                        registerSubtype(SelectPositionCommand.class, "SelectPositionCommand").
                        registerSubtype(SelectResourcesFromWarehouseCommand.class, "SelectResourcesFromWarehouseCommand").
                        registerSubtype(SetSizeCommand.class, "SetSizeCommand").
                        registerSubtype(ShiftResourcesCommand.class, "ShiftResourcesCommand").
                        registerSubtype(UnknownCommand.class, "UnknownCommand"));
        /*
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.
                        of(LeaderCommand.class, "type").
                        registerSubtype(ActivateCardCommand.class, "ActivateCardCommand").
                        registerSubtype(DiscardCardCommand.class, "DiscardCardCommand"));
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ProductionCommand.class, "type").
                        registerSubtype(BasicProductionCommand.class, "BasicProductionCommand").
                        registerSubtype(NormalProductionCommand.class, "NormalProductionCommand").
                        registerSubtype(LeaderProductionCommand.class, "LeaderProductionCommand").
                        registerSubtype(EndProductionCommand.class, "EndProductionCommand"));

         */



        return builder.create();
    }
}
