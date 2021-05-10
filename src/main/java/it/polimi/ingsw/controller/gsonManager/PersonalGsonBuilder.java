package it.polimi.ingsw.controller.gsonManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.commands.*;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseLeaderCardsCommand;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseResourcesCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.ActivateCardCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.DiscardCardCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.LeaderCommand;
import it.polimi.ingsw.controller.commands.normalCommands.EndTurnCommand;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseLeaderCardsCommand;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseMarblesCommand;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.InsertInWarehouseCommand;
import it.polimi.ingsw.controller.commands.normalCommands.ShiftResourcesCommand;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.BuyCardAction;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.SelectPositionCommand;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.SelectResourcesFromWarehouseCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.*;
import it.polimi.ingsw.controller.responseToClients.*;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.SingleGame.CardToken;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.model.SingleGame.TrackToken;

/**
 * this class represent the gson builder.
 * in particular, this class contains only a static method that return the
 * gson builder that can parse every interface needed
 */
public class PersonalGsonBuilder {

    /**
     * this method create a gson capable of parse this classes
     * (1) Marble with all his subclasses,
     * (2) Resource with all his subclasses,
     * (3) SoloToken with all his subclasses,
     * (4) Command with all his subclasses,
     * (5) ResponseToClient with all his subclasses
     * @return the gson capable to parse every class needed
     */
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

        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(ResponseToClient.class, "type").
                        registerSubtype(ResponseToClient.class, "ResponseToClient").
                        registerSubtype(BuyCardActionResponse.class, "BuyCardActionResponse").
                        registerSubtype(EndTurnSingleGameResponse.class, "EndTurnSingleGameResponse").
                        registerSubtype(InitialisingResponse.class, "InitialisingResponse").
                        registerSubtype(LeaderActionResponse.class, "LeaderActionResponse").
                        registerSubtype(MarbleActionResponse.class, "MarbleActionResponse").
                        registerSubtype(ProductionResponse.class, "ProductionResponse").
                        registerSubtype(ShiftResourcesResponse.class, "ShiftResourcesResponse").
                        registerSubtype(StartGameResponse.class, "StartGameResponse").
                        registerSubtype(TwoLeaderWhiteMarblesResponse.class, "TwoLeaderWhiteMarblesResponse").
                        registerSubtype(WhiteMarblesConversionResponse.class, "WhiteMarblesConversionResponse")

        );

        return builder.create();
    }
}
