package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseLeaderCardsCommand;
import it.polimi.ingsw.controller.commands.initialisingCommands.InitialiseResourcesCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.ActivateCardCommand;
import it.polimi.ingsw.controller.commands.leaderCommands.DiscardCardCommand;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseLeaderCardsCommand;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.ChooseMarblesCommand;
import it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands.InsertInWarehouseCommand;
import it.polimi.ingsw.controller.commands.normalCommands.ShiftResourcesCommand;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.BuyCardAction;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.SelectPositionCommand;
import it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands.SelectResourcesFromWarehouseCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.BasicProductionCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.LeaderProductionCommand;
import it.polimi.ingsw.controller.commands.normalCommands.productionCommands.NormalProductionCommand;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.Resources.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CliCommandCreator {

    protected boolean invalidResource(String toVerify){
        List<String> resources = new ArrayList<>(Arrays.asList("COIN", "STONE", "SHIELD", "SERVANT"));
        return !resources.contains(toVerify);
    }

    protected Resource convertResource(String resource){
        return ResourceType.valueOf(ResourceType.class, resource).getResource();
    }

    protected Command initialiseResources(BufferedReader stdIn, int position) throws IOException {
        String firstResource;
        String secondResource;
        CollectionResources initialisingResources = new CollectionResources();
        switch (position){
            case 1:
                break;
            case 2:
            case 3:
                System.out.println("write the resource that you want to obtain[coin, stone, shield, servant]");
                firstResource = stdIn.readLine().toUpperCase();
                if(invalidResource(firstResource)){
                    throw new InvalidParameterException("Resource not valid");
                }
                initialisingResources.add(convertResource(firstResource));
                break;
            case 4:
                System.out.println("write the first resource that you want to obtain[coin, stone, shield, servant]");
                firstResource = stdIn.readLine().toUpperCase();
                if(invalidResource(firstResource)){
                    throw new InvalidParameterException("Resource not valid");
                }
                System.out.println("write the second resource that you want to obtain[coin, stone, shield, servant]");
                secondResource = stdIn.readLine().toUpperCase();
                if(invalidResource(secondResource)){
                    throw new InvalidParameterException("Resource not valid");
                }
                initialisingResources.add(convertResource(firstResource));
                initialisingResources.add(convertResource(secondResource));
                break;
        }
        return new InitialiseResourcesCommand(initialisingResources);
    }

    protected Command initialiseLeaderCard(BufferedReader stdIn) throws IOException {
        System.out.println("Write the first leader card to discard");
        String firstCard = stdIn.readLine();
        int firstCard1;
        int secondCard1;
        try {
            firstCard1 = Integer.parseInt(firstCard);
        }catch (NumberFormatException e){
            throw new InvalidParameterException("Number not valid");
        }
        System.out.println("Write the second leaderCard to discard");
        String secondCard = stdIn.readLine();
        try {
            secondCard1 = Integer.parseInt(secondCard);
        }catch (NumberFormatException e){
            throw new InvalidParameterException("Number not valid");
        }

        return (new InitialiseLeaderCardsCommand(firstCard1, secondCard1));
    }

    protected Command shiftResources(BufferedReader stdIn) throws IOException {
        System.out.println("Write the first shelf in which do the shift");
        String shelf = stdIn.readLine();
        int source, destination;
        try {
            source = Integer.parseInt(shelf);
        }catch (NumberFormatException e) {
            throw new InvalidParameterException("Number not valid");
        }
        System.out.println("Write the second shelf in which do the shift");
        shelf = stdIn.readLine();
        try {
            destination = Integer.parseInt(shelf);
        }catch (NumberFormatException e) {
            throw new InvalidParameterException("Number not valid");
        }
        return new ShiftResourcesCommand(source, destination);
    }

    protected Command chooseMarbles(BufferedReader stdIn) throws IOException {
        System.out.println("Where do you want to get marbles? [row/column]");
        String dimension = stdIn.readLine();
        if (!dimension.equals("row") && !dimension.equals("column")){
            throw new InvalidParameterException("Dimension not valid");
        }


        System.out.println("Write the index in which get the marbles: [1, 2, 3] for row, [1, 2, 3 ,4] for column");
        String index = stdIn.readLine();
        int index1;
        try {
            index1 = Integer.parseInt(index);
        }catch (NumberFormatException e) {
            throw new InvalidParameterException("Number not valid");
        }

        return (new ChooseMarblesCommand(dimension, index1));
    }

    protected Command chooseLeaderCard(BufferedReader stdIn, int marbles) throws IOException {
        int[] indexes = new int[marbles];
        for (int i = 0; i < marbles; i++) {
            System.out.println("Write the leader card index from which convert the " + (i+1) + "° white marble gained");
            String index = stdIn.readLine();
            try {
                indexes[i] = Integer.parseInt(index);
            }catch (NumberFormatException e) {
                throw new InvalidParameterException("Number not valid");
            }
        }

        return (new ChooseLeaderCardsCommand(indexes));
    }

    protected Command insertInWarehouse(BufferedReader stdIn, List<Resource> gainedFromMarbleMarket) throws IOException {
        if (gainedFromMarbleMarket == null)
            throw new InvalidParameterException("you can't do this action");

        int[] shelves = new int[gainedFromMarbleMarket.size()];
        int i = 0;

        for (Resource gained : gainedFromMarbleMarket){
            System.out.println("Decide in which shelf of the warehouse place: "+ gained);
            String index = stdIn.readLine();
            try {
                shelves[i] = Integer.parseInt(index);
            }catch (NumberFormatException e) {
                throw new InvalidParameterException("Number not valid");
            }
            i++;
        }
        return (new InsertInWarehouseCommand(shelves));
    }

    protected boolean isValidColor(String color){
        List<String> colors = new ArrayList<>(Arrays.asList("GREEN", "YELLOW", "PURPLE", "BLUE"));
        return colors.contains(color);
    }

    protected CardColor convertColor(String color){
        return ResourceType.valueOf(CardColor.class, color);
    }

    protected Command buyCard(BufferedReader stdIn) throws IOException {
        System.out.println("Write the color of the card that you want to buy: [green, yellow, purple, blue]");
        String color = stdIn.readLine().toUpperCase();
        if (!isValidColor(color)){
            throw new InvalidParameterException("Color not valid");
        }
        System.out.println("write the level of the card that you want to buy: [1, 2, 3]");
        String level = stdIn.readLine();
        int level1;
        try {
            level1 = Integer.parseInt(level);
        }catch (NumberFormatException e) {
            throw new InvalidParameterException("Number not valid");
        }
        return (new BuyCardAction(convertColor(color), level1));
    }

    protected Command selectPosition(BufferedReader stdIn) throws IOException {
        System.out.println("Write where you want to place the card in your dashboard [1, 2, 3]");
        String dashboardPosition = stdIn.readLine();
        int dashboardPosition1;
        try {
            dashboardPosition1 = Integer.parseInt(dashboardPosition);
        }catch (NumberFormatException e) {
            throw new InvalidParameterException("Number not valid");
        }
        return (new SelectPositionCommand(dashboardPosition1));
    }

    protected CollectionResources createCollectionResources(BufferedReader stdIn, String message) throws IOException {
        String resource;
        CollectionResources resources = new CollectionResources();
        while (true){
            System.out.println(message + ": [coin, servant, stone, shield] \n write [stop] if you don't want to get other resources");
            resource = stdIn.readLine().toUpperCase();
            if (resource.equals("STOP")) break;

            if (invalidResource(resource)){
                System.out.println("resource not valid");
            }else {
                resources.add(convertResource(resource));
            }
            System.out.println("Resources chosen :" + resources);
        }
        return resources;
    }

    protected Command selectResourcesFromWarehouse(BufferedReader stdIn) throws IOException {
        CollectionResources toPayFromWarehouse = createCollectionResources( stdIn, "choose resources to get from warehouse");
        return (new SelectResourcesFromWarehouseCommand(toPayFromWarehouse));
    }

    protected Command basicProduction(BufferedReader stdIn) throws IOException {
        String output;
        CollectionResources toPayFromWarehouse = createCollectionResources( stdIn, "choose resources to get from warehouse");
        CollectionResources toPayFromStrongbox = createCollectionResources( stdIn, "choose resources to get from strongbox");
        System.out.println("choose the resource to gain as output of the basic production[coin, stone, shield, servant]");
        output = stdIn.readLine().toUpperCase();
        if(invalidResource(output)){
            throw new InvalidParameterException("Resource not valid");
        }
        Resource output1 = convertResource(output);
        return (new BasicProductionCommand(toPayFromWarehouse, toPayFromStrongbox, output1));
    }

    protected Command normalProduction(BufferedReader stdIn) throws IOException {
        System.out.println("choose the production to activate from your dashboard [1, 2, 3]");
        String position = stdIn.readLine();
        int position1;
        try {
            position1 = Integer.parseInt(position);
        }catch (NumberFormatException e) {
            throw new InvalidParameterException("Number not valid");
        }
        CollectionResources toPayFromWarehouse = createCollectionResources( stdIn, "choose resources to get from warehouse");
        return (new NormalProductionCommand(position1, toPayFromWarehouse));
    }

    protected boolean isValidBoolean(String decision){
        return new ArrayList<>(Arrays.asList("yes", "no")).contains(decision);
    }

    protected boolean convertBoolean(String decision){
        return decision.equals("yes");
    }

    protected Command leaderProduction(BufferedReader stdIn) throws IOException {
        System.out.println("Decide what leader production choose [1, 2]");
        String position = stdIn.readLine();
        int position1;
        try {
            position1 = Integer.parseInt(position);
        }catch (NumberFormatException e) {
            throw new InvalidParameterException("Number not valid");
        }
        System.out.println("you want to pay the input cost from warehouse? [yes/no]" );
        String decision = stdIn.readLine().toLowerCase();
        if (!isValidBoolean(decision)){
            throw new InvalidParameterException("Decision not valid");
        }
        boolean fromWarehouse = convertBoolean(decision);

        System.out.println("choose the resource to gain as output of the leader production[coin, stone, shield, servant]");
        String output = stdIn.readLine().toUpperCase();
        if(invalidResource(output)){
            throw new InvalidParameterException("Resource not valid");
        }
        Resource output1 = convertResource(output);

        return (new LeaderProductionCommand(position1, fromWarehouse, output1));
    }

    protected Command activateCard(BufferedReader stdIn) throws IOException {
        System.out.println("Decide the leader card to activate [1, 2]: ");
        String toActivate = stdIn.readLine();
        int toActivate1;
        try {
            toActivate1 = Integer.parseInt(toActivate);
        }catch (NumberFormatException e) {
            throw new InvalidParameterException("Number not valid");
        }
        return (new ActivateCardCommand(toActivate1));
    }

    protected Command discardCard(BufferedReader stdIn) throws IOException {
        System.out.println("Decide the leader card to discard [1, 2]: ");
        String toDiscard = stdIn.readLine();
        int toDiscard1;
        try {
            toDiscard1 = Integer.parseInt(toDiscard);
        }catch (NumberFormatException e) {
            throw new InvalidParameterException("Number not valid");
        }
        return (new DiscardCardCommand(toDiscard1));
    }

    public void showLegend(){
        System.out.println("Commands:");
        System.out.println(" show = show"); // show
        System.out.println(" setp = set_players"); // sp
        System.out.println(" l = login"); // l
        System.out.println(" il = initialise_leaderCards"); //il
        System.out.println(" ir = initialise_resources"); // ir
        System.out.println(" cm = choose_marbles"); // cm
        System.out.println(" cl = choose_leaderCards"); // cl
        System.out.println(" sr = shift_resources"); // sr
        System.out.println(" iiw = insert_in_warehouse"); //iin
        System.out.println(" bc = buy_card"); // bc
        System.out.println(" sp = select_position"); // sp
        System.out.println(" srfw = select_resources_from_warehouse"); //srfw
        System.out.println(" p = production"); // p
        System.out.println(" bp = basic_production"); // bp
        System.out.println(" np = normal_production"); // np
        System.out.println(" lp = leader_production"); // lp
        System.out.println(" ep = end_production"); // ep
        System.out.println(" la = leader_action"); // la
        System.out.println(" ac = activate_card"); // ac
        System.out.println(" dc = discard_card"); // dc
        System.out.println(" et = end_turn"); // et
        System.out.println(" q = quit");
        System.out.println("examples of possible commands flow");
        System.out.println("set_players -> login -> initialise_leaderCards -> initialise_resources");
        System.out.println("choose_marbles -> [choose_leaderCards] -> shift_resources -> insert_in_warehouse");
        System.out.println("buy_card -> select_position -> select_resources_from_warehouse");
        System.out.println("production -> [basic_production] -> [normal_production] -> [leader_production] -> end_production");
        System.out.println("leader_action -> activate_card / discard_card" );

    }
    
}
