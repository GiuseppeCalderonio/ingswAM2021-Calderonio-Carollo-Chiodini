package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.*;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.MarbleMarket;
import it.polimi.ingsw.model.Marble.RedMarble;
import it.polimi.ingsw.model.Marble.WhiteMarble;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.model.SingleGame.SoloToken;

import java.util.*;
import java.util.stream.Collectors;


/**
 * this is the game class, every check and player action use a game method
 */
public class Game {
    /**
     * this attribute is a vector of players who are participating at the game
     */
    private final List<RealPlayer> players;

    /**
     * this attribute is the marble matrix of the market initialized randomly
     */
    private final MarbleMarket marketBoard;

    /**
     * this attribute contains marketCard
     */
    private final CardsMarket setOfCards;

    /**
     * this attribute contains the number of current player
     */
    private int turnManager;

    /**
     * this attribute contains 3 booleans, is used to check if a vatican report is already
     * activated or not
     */
    private final boolean[] vaticanReports;

    /**
     * this is the constructor of the class, it initialize the three boolean of vatican report
     * at false; set the nickname of players; initialize randomly MarbleMarket and CardsMarket, create all leaderCards and assign four
     * of them to each player
     */
    public Game(List<String> nicknames ){

        // create all the leader cards
        List<LeaderCard> leaders = createAllLeaderCards();

        players = new ArrayList<>();
        Collections.shuffle(nicknames); // shuffle the order of the players
        int i = 0;
        for(String name : nicknames) {
            List<LeaderCard> temp = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                temp.add(leaders.get(i));
                i++;
            }
            players.add(new RealPlayer(name , temp));
        }
        marketBoard = new MarbleMarket();
        setOfCards = new CardsMarket();
        vaticanReports = new boolean[]{false, false, false};
        turnManager = -nicknames.size();
    }

    /**
     * this method create all the leader cards of the game, and returns
     * them as an array list
     * @return the arraylist of leader cards containing all the cards of the game
     */
    private List<LeaderCard> createAllLeaderCards(){


        List<LeaderCard> leaders = new ArrayList<>(); // create an arraylist of leaderCard
        LeaderCardRequirements requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.BLUE, CardColor.YELLOW)));
        LeaderCardRequirements requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.PURPLE, CardColor.GREEN)));
        LeaderCardRequirements requirements3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.GREEN, CardColor.PURPLE)));
        LeaderCardRequirements requirements4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.YELLOW, CardColor.BLUE)));

        //NewWhiteMarble leaderCards
        leaders.add(new NewWhiteMarble(requirements2,5,new Coin()));
        leaders.add(new NewWhiteMarble(requirements1 ,5,new Stone()));
        leaders.add(new NewWhiteMarble(requirements4,5,new Servant()));
        leaders.add(new NewWhiteMarble(requirements3,5,new Shield())); //NewWhiteMarble leaderCard
        //NewShelf leaderCards
        leaders.add(new NewShelf(new ResourcesRequired(new Shield()),3,new Coin()));
        leaders.add(new NewShelf(new ResourcesRequired(new Servant()),3,new Shield()));
        leaders.add(new NewShelf(new ResourcesRequired(new Stone()),3,new Servant()));
        leaders.add(new NewShelf(new ResourcesRequired(new Coin()),3,new Stone())); //NewShelf leaderCard
        //NewProduction leaderCards
        leaders.add(new NewProduction(new LevelRequired(CardColor.GREEN) ,4 , new Coin())); leaders.add(new NewProduction(new LevelRequired(CardColor.PURPLE) ,4 , new Stone())); leaders.add(new NewProduction(new LevelRequired(CardColor.BLUE) ,4 , new Servant())); leaders.add(new NewProduction(new LevelRequired(CardColor.YELLOW) ,4 , new Shield())); //NewProduction leaderCard

        LeaderCardRequirements requirement1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.PURPLE)));
        LeaderCardRequirements requirement2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN , CardColor.BLUE)));
        LeaderCardRequirements requirement3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE , CardColor.PURPLE)));
        LeaderCardRequirements requirement4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.GREEN)));

        //NewDiscount leaderCards
        leaders.add(new NewDiscount(requirement1,2,new Coin()));
        leaders.add(new NewDiscount(requirement2,2,new Stone()));
        leaders.add(new NewDiscount(requirement3,2,new Shield()));
        leaders.add(new NewDiscount(requirement4,2,new Servant())); //NewDiscount leaderCard

        Collections.shuffle(leaders); //shuffle the deck of leader cards


        return leaders;
    }

    /**
     * this method get a list of the players that are playing the game
     * @return a list of the players that are playing the game
     */
    public synchronized List<RealPlayer> getPlayers(){
        return players;
    }

    /**
     * this method get the market board of the game
     * @return the market board of the game
     */
    public synchronized MarbleMarket getMarketBoard() {
        return marketBoard;
    }

    /**
     * this method get the set of cards of the game
     * @return the set of cards of the game
     */
    public synchronized CardsMarket getSetOfCard() {
        return setOfCards;
    }

    /**
     * this method get the turn manager of the game, that indicates the turn
     * @return the turn manager of the game, that indicates the turn
     */
    public synchronized int getTurnManager() {
        return turnManager;
    }

    /**
     * this method get an array of 3 boolean representing the 3 game vatican reports
     * @return an array of 3 boolean representing the 3 game vatican reports
     */
    public synchronized boolean[] getVaticanReports() {
        return vaticanReports;
    }

    /**
     * this method get a player representing lorenzo il magnifico.
     * in particular, if the game is a single game, it returns the right player,
     * it returns null otherwise
     * @return a player representing lorenzo il magnifico
     */
    public synchronized Player getLorenzoIlMagnifico(){ return null; }

    /**
     * this method get the list of solo tokens of the game.
     * in particular, if the game is a single game, it returns the right list,
     * it returns null otherwise
     * @return the list of solo tokens of the game
     */
    public synchronized List<SoloToken> getSoloTokens(){ return null; }

    /**
     * this method is used to set vaticanReports vector
     * @param index is the index of the vector
     */
    protected synchronized void setVaticanReports(int index){
        vaticanReports[index - 1] = true;
    }

    /**
     * this method verify if the nickname is associated with one player
     * and if the resources are correct based on the position of the inkwell
     * @param nickname this is the nickname of the player to check
     * @param resources these are the resources to check
     * @return true if the parameters are correct, false otherwise
     */
    public synchronized boolean checkInitialising(String nickname, CollectionResources resources){
        if(resources == null) return false;
        if(players.stream().map(RealPlayer::getNickname).noneMatch(x -> x.equals(nickname))) return false;
        try{
            if(players.get(0).getNickname().equals(nickname) && resources.getSize() == 0) return true;
            if(players.get(1).getNickname().equals(nickname) && resources.getSize() == 1) return true;
            if(players.get(2).getNickname().equals(nickname) && resources.getSize() == 1) return true;
            return players.get(3).getNickname().equals(nickname) && resources.getSize() == 2;
        } catch (IndexOutOfBoundsException e){
            return false;
        }
    }

    /**
     * this method get the player from his nickname
     * it return null if the game does not contain any player
     * with that nickname
     * @param nickname this is the nickname to check
     * @return the player if the game contains it, null otherwise
     */
    public synchronized RealPlayer findPlayer(String nickname){
        for(RealPlayer p : players){
            if(p.getNickname().equals(nickname)) return p;
        }
        return null;
    }

    /**
     * this method verify if the player that have the nickname in input
     * is the player that have to play the turn
     * @param nickname this is the nickname of the player to check
     * @return true if is the turn of this player, false otherwise
     */
    public boolean isYourTurn(String nickname){
        if (turnManager < 0) return false;
        return Objects.equals(findPlayer(nickname), getActualPlayer());
    }

    /**
     * this method is called for each player, and it
     * gives him some resources and some faith point
     * depending on the position of the inkwell
     * and discard 2 leader cards
     * the resources get added automatically in the free shelves
     * then the player could shift them
     * @param nickname this is the nickname of the player, it requires that is associated
     *                 with one and only one player
     * @param toAdd these are the resources to add in the player warehouse, it requires is not null
     * @param leaderCardToReject1 this is the index of the first leader card to reject,
     *                           it requires is between 1 and 4
     * @param leaderCardToReject2 this is the index of the second leader card to reject,
     *                            it requires is between 1 and 4
     */
    public synchronized void initialiseGame(String nickname, CollectionResources toAdd, int leaderCardToReject1, int leaderCardToReject2){
        // adding resources
        // do nothing for the first player
        if(toAdd.getSize() == 1){ // the second and third players
            CollectionResources resources = new ShelfCollection(toAdd.asList().get(0).getType());
            resources.sum(toAdd);
            Objects.requireNonNull(findPlayer(nickname)).getPersonalDashboard().addResourcesToWarehouse(resources, 1);
        }
        if(toAdd.getSize() == 2){ //the fourth player
            Resource first = toAdd.asList().get(0); //get the first resource
            Resource second = toAdd.asList().get(1); //get the second resource

            if (first.equals(second)) { //if the resources are equals
                CollectionResources equalResources = new ShelfCollection(first.getType());
                equalResources.add(first);
                equalResources.add(second);
                Objects.requireNonNull(findPlayer(nickname)).
                        getPersonalDashboard().
                        addResourcesToWarehouse(equalResources, 2); //add them in the same shelf
            }
            else { //if the resources are not equals
                CollectionResources notEqualResource1 = new ShelfCollection(first.getType());
                notEqualResource1.add(first);
                CollectionResources notEqualResource2 = new ShelfCollection(second.getType());
                notEqualResource2.add(second);
                Objects.requireNonNull(findPlayer(nickname)).
                        getPersonalDashboard().
                        addResourcesToWarehouse(notEqualResource1, 1); //add the first in the first shelf
                Objects.requireNonNull(findPlayer(nickname)).
                        getPersonalDashboard().
                        addResourcesToWarehouse(notEqualResource2, 2); //add the second in the second shelf
            }
        }
        //rejecting leader cards
        // here is chosen the higher leader card before and the lower after to not having errors
        Objects.requireNonNull(findPlayer(nickname)).discardLeaderCard(Math.max(leaderCardToReject1, leaderCardToReject2));
        Objects.requireNonNull(findPlayer(nickname)).discardLeaderCard(Math.min(leaderCardToReject1, leaderCardToReject2));

        //adding faith points
        int i = -1;
        RealPlayer temp = null;
        for (RealPlayer player : players) {
            i++;
            if(player.getNickname().equals(nickname)) {
                temp = player;
                break;
            }
        }
        if(i >= 2 && temp != null) temp.addFaithPoints(1);
        //increasing the turn
        turnManager++;

    }
// shift check and methods--------------------------------------------------------------------------------------------

    /**
     * this method verify if the shelf selected is actually correct,
     * is not correct if the input is not between 1 and the
     * number of shelves that a player own (worst case = 5)
     * @param numOfShelf this is the index of the shelf to check
     * @return true if the number of the shelves is more than input
     *         false otherwise
     */
    public synchronized boolean checkShelfSelected(int numOfShelf){
        if (numOfShelf < 1 || numOfShelf > 5) return false;
        return (numOfShelf <=
                getActualPlayer().              //get the player of the current turn
                        getPersonalDashboard(). //get his dashboard
                        getPersonalWarehouse().  //get his warehouse
                        getNumOfShelves());   //get how many shelves he own
    }

    /**
     * this method shift two shelves if they are both normal,
     * it move a single resource from the source shelf to the destination one
     * if one of them is a leader shelf
     * in the first case, if some resources are discarded, the method adds the
     * faith points to every player except to the actual one and handle a vatican report
     * in the second case, it can return false if the action is not possible
     * @param source this is the index of the source shelf
     * @param destination this is the index of the destination shelf
     * @return false if one of the two shelves is a leader one
     *        and is not possible to move a resource, false otherwise
     * @throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     *                         or when a column of the cardsMarket is empty, only in a single game
     */
    public synchronized boolean shiftResources(int source, int destination) throws EndGameException {
        int faithPoints = getActualPlayer().shiftResources(source, destination);
        if (faithPoints == -1) return false;
        addFaithPointsExceptTo(getActualPlayer(), faithPoints);
        return true;
    }

    // buy resources to the market ------------------------------------------------------------------------

    /**
     * this method substitute one of the white marbles of the
     * array given in input with the marble selected from the parameter
     * leaderWhiteMarble in the actual player
     * @param marbles this is the list of white marbles
     * @param leaderWhiteMarble this is the index associated with the
     *                          marble to substitute
     * @return false if the leaderWhiteMarbles do not contain any white marble,
     *         or if the marbles are null,
     *         or if the parameter leaderWhiteMarble is not between 1 and 2
     *         or if the leaderWhiteMarble selected doesn't exist
     *         true otherwise
     */
    public synchronized boolean changeWhiteMarble(List<Marble> marbles, int leaderWhiteMarble){

        if (marbles == null) return false;

        if (!marbles.contains(new WhiteMarble()))
            return false; // if the array given in input doesn't contain any white marble

        if(leaderWhiteMarble < 1 || leaderWhiteMarble > 2)
            return false; // the index of the leader marble is wrong
        try {
            Marble toSubstitute = getActualPlayer().getLeaderWhiteMarbles().get(leaderWhiteMarble - 1);
            marbles.remove(new WhiteMarble());
            marbles.add(toSubstitute);
            return true;

        }catch (IndexOutOfBoundsException e){
            return false; // if the leader marble does not exist
        }
    }

    /**
     * this method convert an array of marbles to his equivalent collectionResources
     * giving to the actual player a faith point if the array contains a red marble
     * @param marbles these are the marbles to convert
     * @return the collectionResources associated with the marbles
     * @throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     *                         or when a column of the cardsMarket is empty, only in a single game
     */
    public synchronized CollectionResources convert(List<Marble> marbles) throws EndGameException {
        List<Marble> temp = marbles.stream().
                filter( marble -> !marble.equals(new WhiteMarble())). //remove all the white marbles
                collect(Collectors.toList());
        int faithPoints = temp.stream().mapToInt(Marble::faithPoints).sum();
        addFaithPointsTo(getActualPlayer(), faithPoints); //add a faith point when contains the red marble
        temp.remove(new RedMarble());

        /*
        if(marbles.remove(new RedMarble())) addFaithPointsTo(getActualPlayer(), 1); //add a faith point when contains the red marble
        */

        CollectionResources toReturn = new CollectionResources();
        temp.forEach(marble -> toReturn.add(marble.convert())); // create a collectionResources from the marbles converted
        return toReturn;
    }

    /**
     * this method get the row selected from the market board
     * and shift left all the marbles of that row of one position
     * @param row this is the row to shift
     * @return the arraylist of marbles given
     */
    public synchronized List<Marble> selectRow(int row){
        return marketBoard.selectRow(row - 1);
    }

    /**
     * this method get the column selected from the market board
     * and shift up all the marbles of that column of one position
     * @param column this is the column to shift
     * @return the arraylist of marbles given
     */
    public synchronized List<Marble> selectColumn(int column){
        return marketBoard.selectColumn(column - 1);
    }

    /**
     * this method insert in warehouse all the resources of the collection
     * marbleConverted that are equals to the resource typeRequired in
     * the shelf selected
     * @param shelf this is the shelf in which add the resources
     * @param typeRequired this is the resource that must be taken and insert
     *                     from marblesConverted to the warehouse
     * @param marblesConverted these are the resources that have to be filtered
     *                         they are associated with the marbles already got
     *                         from the market
     * @throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     *                         or when a column of the cardsMarket is empty, only in a single game
     */
    public synchronized void insertInWarehouse(int shelf, Resource typeRequired, CollectionResources marblesConverted) throws EndGameException {
        CollectionResources toAdd = new ShelfCollection(typeRequired.getType());
        marblesConverted.asList().stream().
                filter(resource -> resource.equals(typeRequired)). //remove all the resources without the type required
                forEach(toAdd::add); //add them to the collection toAdd
        int faithPoints = getActualPlayer().addResourcesToWarehouse(toAdd, shelf);
        addFaithPointsExceptTo(getActualPlayer(), faithPoints);
    }

    // buy card----------------------------------------------------------------------------------------------

    /**
     * this method check if a deck of card is empty, or if the inputs are correct,
     * and if the actual player contains all the resources to buy the card,
     * and if the card selected can be placed in the player dashboard
     * @param level this is the level of the card to check, it should be between 1 and 3
     * @param color this is the color of the card to check, it shouldn't be null
     * @return true if there is a card in the deck with the level and color specified
     * in the parameters in the market and the player has enough resources to buy the card
     * and the card can be placed in the dashboard,
     * false if the deck selected is empty or if the inputs aren't correct
     * or if the player can't afford the card selected or if the card
     * can't be placed in the dashboard
     */
    public synchronized boolean checkBuyCard(int level, CardColor color){
        if (!setOfCards.checkCard(level, color)) return false;
        DevelopmentCard card = setOfCards.getCard(level, color);
        boolean checkPlacement = false;
        for (int i = 1; i <= 3; i++) {
                checkPlacement = checkPlacement || getActualPlayer().
                        getPersonalDashboard().
                        getPersonalProductionPower().
                        checkPlacement(card, i);
        }
        if (!checkPlacement) return false;
        return containsWithDiscount(card.getCost());
    }

    /**
     * this method verify if a placement of a card of the actual player is legit or not
     * @param toCheck this is the card to check, it requires that is not null
     * @param position this is the position of the array of card in which the method have to check, it should go from 1 to 3
     * @return true if the level of the card in input is a level up compared to the card in the position in input or
     *         the card in input has a level one and the deck of cards specified in input is empty, false if not,
     *         or if the position is not between 1 and 3
     */
    public synchronized boolean checkPlacement(DevelopmentCard toCheck, int position ){
        return getActualPlayer().
                getPersonalDashboard().
                getPersonalProductionPower().
                checkPlacement(toCheck, position);
    }

    /**
     * this method verify if the actual player can buy the card in input
     * taking from the warehouse the resources given in input
     * in particular, it verify 3 things
     * 1) the resources to pay from warehouse are actually all contained into it
     * 2) the resources to pay from strongbox are actually all contained into it
     * 3) the sum of the resources to get from warehouse and the resources to get
     *    from strongbox are equals to the cost of the card
     * the method works correctly even if the actual player contain a discount of
     * resources, inferring the cost of the card discounted
     * NB: the resources to pay from strongbox can be calculated as
     *     the cost (eventually discounted) of the card minus
     *     the resources to get from the warehouse
     * @param card this is the card to check if the player can buy
     * @param toPayFromWarehouse these are the resources that the player want to
     *                           pay from the warehouse
     * @return true if the player can buy the card, false otherwise
     */
    public boolean checkWarehouseResources(DevelopmentCard card, CollectionResources toPayFromWarehouse){

        if (toPayFromWarehouse == null) return false;

        if(!containsInWarehouse(toPayFromWarehouse))
            return false; // if the warehouse doesn't contain the resources given in input

        // get the cost discounted of the card
        CollectionResources discountedCost = new CollectionResources();
        discountedCost.sum(card.getCost()); // sum the cost of the card

        discountedCost.sub(new CollectionResources(getActualPlayer().
                getPersonalDashboard().
                getDiscount().
                asList().stream().filter(resource -> card.getCost().contains(resource)).
                collect(Collectors.toList()))); //sub the discount filtered with only the resources of the cost of the card

        //get the resources that the player want to pay from strongbox
        // as the resources to get from warehouse minus the cost discounted of the card
        CollectionResources toPayFromStrongbox = new CollectionResources();
        toPayFromStrongbox.sum(discountedCost); // sum the cost discounted of the card
        toPayFromStrongbox.sub(toPayFromWarehouse); //sub the resources to get from warehouse

        if (!containsInStrongbox(toPayFromStrongbox))
            return false; // if the strongbox doesn't contain enough resources

        // verify if the discounted card is equals to the resources
        // to pay from warehouse plus the resources to pay from strongbox
        CollectionResources toPay = new CollectionResources();
        toPay.sum(toPayFromStrongbox); //sum the resources to pay from warehouse
        toPay.sum(toPayFromWarehouse); //sum the resources to pay from strongbox
        return toPay.equals(discountedCost);
    }

    /**
     * this method buy a card paying its cost and locate it in the dashboard,
     * in particular, it get and remove the card selected by the level and the color
     * from the market, it place the card in the dashboard at the position selected,
     * it infer the resources to pay from the strongbox as the resources to pay
     * from warehouse given in input minus the cost (eventually discounted) of the card,
     * and pay the resources from the warehouse and the dashboard
     * NB: it requires that the inputs are all correct
     * @param level this is the level of the card to buy
     * @param color this is the color of the card to buy
     * @param position this is the position in which place the card in the dashboard
     * @param toPayFromWarehouse these are the resources to pay from warehouse
     * @throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     *                         or when a column of the cardsMarket is empty, only in a single game
     */
    public void buyCard(int level, CardColor color, int position, CollectionResources toPayFromWarehouse) throws EndGameException {

        // get and remove the card from the market with level and color selected
        DevelopmentCard toBuy = setOfCards.popCard(level, color);

        //locate the card in the dashboard at the position selected
        getActualPlayer().locateDevelopmentCard(toBuy, position);

        // get the cost discounted of the card
        CollectionResources discountedCost = new CollectionResources();
        discountedCost.sum(toBuy.getCost()); // sum the cost of the card
        discountedCost.sub(new CollectionResources(getActualPlayer().
                getPersonalDashboard().
                getDiscount().
                asList().stream().filter(resource -> toBuy.getCost().contains(resource)).
                collect(Collectors.toList()))); //sub the discount filtered with only the resources of the cost of the card

        //get the resources that the player want to pay from strongbox
        // as the resources to get from warehouse minus the cost discounted of the card
        CollectionResources toPayFromStrongbox = new CollectionResources();
        toPayFromStrongbox.sum(discountedCost); // sum the cost discounted of the card
        toPayFromStrongbox.sub(toPayFromWarehouse); //sub the resources to get from warehouse

        // remove the resources from warehouse and from strongbox
        getActualPlayer().buyDevelopmentCard(toPayFromWarehouse, toPayFromStrongbox);
    }

    //activate productions------------------------------------------------------------------------------------------

    /**
     * this method verify if a player can activate the production associated
     * with the position.
     * In particular, if the production has already been activated in the turn return false,
     * if the position is between 1 and 3, that means a normal production,
     * the method check if the actual player has a cart in that position, and if he
     * owns the input resources,
     * if the position is between 4 and 5, verify if the player own a leader
     * production into his dashboard, and if the resource associated with it is
     * owned by the player.
     * the controller have to sum position 3 if it comes from a leader production
     * if the position is not between 1 and 5 return false
     *
     * @param position this is the position on the dashboard of the production
     *                 that the actual player want to activate
     * @return true if the conditions to activate a production are respected,
     *         false otherwise
     */
    public boolean checkProduction(int position){
        if (position < 0 || position > 5) return false;

        if (position == 0) {
            if (getActualPlayer().getPersonalDashboard().getPersonalProductionPower().isBasicProductionActivated())
                return false; // return false if the production has already been activated during the turn
            return getActualPlayer().getTotalResources().getSize() >= 2; //basic production case
        }

        if ( position > 3){ // leader production case

            if (getActualPlayer().getPersonalDashboard().getPersonalProductionPower().isLeaderProductionActivated(position - 3))
                return false; // return false if the production has already been activated during the turn
            Resource toCheck = getActualPlayer().
                    getPersonalDashboard().
                    getPersonalProductionPower().
                    getInput(position - 3); //get the resource associated with the production
            if ( toCheck == null) return false; //if the leader production selected does not exist
            return getActualPlayer().
                    getTotalResources().contains(toCheck); // the player contains the resource selected
        }
        // normal production case
        DevelopmentCard toCheck = getActualPlayer().
                getPersonalDashboard().
                getPersonalProductionPower().
                getCard(position); //get the card associated with the position
        if (toCheck == null) return false; // if the card not exist
        if (toCheck.isProductionActivated() || getActualPlayer().getPersonalDashboard().getPersonalProductionPower().isProductionActivated(position))
            return false; // return false if the production has already been activated during the turn
        return contains(toCheck.getProductionPowerInput()); //if the player doesn't contain the resources in input
    }

    /**
     * this method verify if the actual player can activate the card specified
     * by the position in input taking from the warehouse the resources given in input
     * in particular, it verify 3 things
     * 1) the resources to pay from warehouse are actually all contained into it
     * 2) the resources to pay from strongbox are actually all contained into it
     * 3) the sum of the resources to get from warehouse and the resources to get
     *    from strongbox are equals to the input resources of the card
     * NB: the resources to pay from strongbox can be calculated as
     *     the input resources of the card minus the resources to get from the warehouse
     * @param position this is the position of the card to activate, it requires that
     *                 the card in the position exist
     * @param toPayFromWarehouse these are the resources to pay from the warehouse
     * @return true if the card can be activated, false otherwise
     */
    public boolean checkActivateProduction(int position, CollectionResources toPayFromWarehouse){

        if (toPayFromWarehouse == null)
            return false;

        if (!containsInWarehouse(toPayFromWarehouse))
            return false; // if the resources to pay from warehouse are not contained in the warehouse of the actual player

        DevelopmentCard card = getActualPlayer().
                getPersonalDashboard().
                getPersonalProductionPower().
                getCard(position); // get the card that the player want to activate

        CollectionResources toPayFromStrongbox = new CollectionResources(); // resources to pay from strongbox in order to activate the production

        toPayFromStrongbox.sum(card.getProductionPowerInput()); // sum the input resources of the card
        toPayFromStrongbox.sub(toPayFromWarehouse); // subtract the resources that the player want to get from the warehouse

        if (!containsInStrongbox(toPayFromStrongbox))
            return false; // if the resources to pay from strongbox are not contained in the strongbox of the actual player

        CollectionResources toPay = new CollectionResources(); // total resources to pay
        toPay.sum(toPayFromWarehouse);
        toPay.sum(toPayFromStrongbox);
        return toPay.equals(card.getProductionPowerInput()); // if the resources to pay are equal to the input resources of the card
    }

    /**
     * this method activate the normal production of the actual player
     * in particular, it pay the resources to pay from warehouse from the warehouse,
     * it calculate the resources to get from strongbox as card.getInput - toPayFromWarehouse
     * and remove them from strongbox, finally it get the output resources and place them in the
     * buffer
     * last, but not least, it adds to the actual player the faith points associated with the
     * output production of the card
     * @param position this is the position of the card to activate
     *                 it requires that the card exist
     * @param toPayFromWarehouse these are the resources to pay from warehouse
     *                           them require that the method checkActivateProduction(position, toPayFromWarehouse ) == true
     * @throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     *                         or when a column of the cardsMarket is empty, only in a single game
     */
    public void activateProduction(int position, CollectionResources toPayFromWarehouse ) throws EndGameException {
        DevelopmentCard card = getActualPlayer().
                getPersonalDashboard().
                getPersonalProductionPower().
                getCard(position); // get the card to activate

        CollectionResources productionPowerInput = card.getProductionPowerInput(); // get the input resources
        CollectionResources productionPowerOutput = card.getProductionPowerOutput(); // get the output resources

        CollectionResources toPayFromStrongbox = new CollectionResources(); // resources to pay from strongbox
        toPayFromStrongbox.sum(productionPowerInput); // sum the input resources
        toPayFromStrongbox.sub(toPayFromWarehouse); // subtract the resources to pay from warehouse
        // activate the production
        getActualPlayer().activateProduction(toPayFromWarehouse, toPayFromStrongbox, productionPowerOutput);

        addFaithPointsTo(getActualPlayer(), card.getProductionPowerFaithPoints());

        card.setProductionActivated(); // set the card to activated
    }

    /**
     * this method verify if the actual player can activate a basic production.
     * in particular, if:
     * 1) the resources to pay from warehouse are actually all contained into it
     * 2) the resources to pay from strongbox are actually all contained into it
     * 3) the size of the resources to pay fro warehouse plus the resources to pay from
     *    strongbox is not 2
     * @param toPayFromWarehouse these are the resources to pay from warehouse
     * @param toPayFromStrongbox these are the resources to pay from strongbox
     * @param output this is the resource to get in output
     * @return true if the production can be activated, false otherwise
     */
    public boolean checkActivateBasicProduction(CollectionResources toPayFromWarehouse, CollectionResources toPayFromStrongbox, Resource output){
        if (toPayFromStrongbox == null) return false;
        if (toPayFromWarehouse == null) return false;
        if (output == null) return false;

        if (!containsInStrongbox(toPayFromStrongbox)) return false; //resources not contained in the strongbox
        if (!containsInWarehouse(toPayFromWarehouse)) return false; //resources not contained in the warehouse

        CollectionResources toPay = new CollectionResources();
        toPay.sum(toPayFromStrongbox);
        toPay.sum(toPayFromWarehouse);
        return toPay.getSize() == 2; //not 2 resources to pay
    }

    /**
     * this method activate the basic production, paying the resources from the warehouse
     * and from the strongbox, getting as output the resource output
     * it requires that the method checkActivateBasicProduction with the
     * same inputs return true
     * @param toPayFromWarehouse these are the resources to pay from warehouse
     *                           them require that are contained in the warehouse
     * @param toPayFromStrongbox these are the resources to pay from strongbox
     *                           them require that are contained in the strongbox
     * @param output this is the resource to get as output
     */
    public void activateBasicProduction(CollectionResources toPayFromWarehouse, CollectionResources toPayFromStrongbox, Resource output){

        getActualPlayer().activateProduction(toPayFromWarehouse, toPayFromStrongbox, new CollectionResources(output));

        getActualPlayer().getPersonalDashboard().getPersonalProductionPower().activateBasicProduction(); // set the basic production to activated
    }

    /**
     * this method verify if the resource associated with the leader production given
     * in input is contained in the warehouse of the actual player when fromWarehouse == true,
     * if it is contained in the strongbox of the actual player when fromWarehouse == false
     * @param toCheck this is the index of the resource to check
     *                it requires that the resource exist
     * @param fromWarehouse this is the flag that verify if the check should be done
     *                      in the warehouse or in the strongbox
     * @return true if the player contains the resource selected correctly, false otherwise
     */
    public boolean checkActivateLeaderProduction(int toCheck, boolean fromWarehouse){

        CollectionResources toVerify = new CollectionResources();
        toVerify.add(getActualPlayer().
                getPersonalDashboard().
                getPersonalProductionPower().
                getInput(toCheck)); // get the resource associated with the leader production chosen
        if (fromWarehouse){
            return containsInWarehouse(toVerify); //verify if the warehouse contains the resource
        }
        else
            return  containsInStrongbox(toVerify); //verify if the strongbox contains the resource
    }

    /**
     * this method activate a leader production, it requires that the inputs are correct
     * in particular, it adds a faith point to the actual player, it pay the resource
     * associated with the leader production toActivate from warehouse or from strongbox,
     * depending on the flag fromWarehouse, and it get as output the resource given as parameter
     * @param toActivate this is the index of the leader production to activate
     * @param output this is the resource to get as output of the production
     * @param fromWarehouse this is the flag that verify if the payment of the resource
     *                     should be done in the warehouse or in the strongbox
     *@throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     *                         or when a column of the cardsMarket is empty, only in a single game
     */
    public synchronized void activateLeaderProduction(int toActivate, Resource output, boolean fromWarehouse) throws EndGameException {

        Resource toPay = getActualPlayer().
                getPersonalDashboard().
                getPersonalProductionPower().
                getInput(toActivate); // get the resource associated with the leader production chosen

        CollectionResources toPayFromWarehouse = new CollectionResources();
        CollectionResources toPayFromStrongbox = new CollectionResources();
        if (fromWarehouse)
            toPayFromWarehouse.add(toPay);
        else
            toPayFromStrongbox.add(toPay);

        CollectionResources productionPowerOutput = new CollectionResources();
        productionPowerOutput.add(output); // this is the output chosen for the leader production

        getActualPlayer().activateProduction(toPayFromWarehouse, toPayFromStrongbox, productionPowerOutput);

        addFaithPointsTo(getActualPlayer(), 1); // add 1 faith point

        getActualPlayer().getPersonalDashboard().getPersonalProductionPower().activateLeaderProduction(toActivate); // set the leader production to activated, so that cannot be activated anymore in the same turn
    }

    /**
     * this method terminate a production, it is called once that
     * the player decides to end it, and it fill the strongbox with the
     * resources stored in the buffer during the productions
     */
    public synchronized void endProduction(){

        getActualPlayer().fillStrongboxWithBuffer(); // get the strongbox resources gained from the productions

        getActualPlayer().getPersonalDashboard().getPersonalProductionPower().resetProductions(); // reset the productions
    }

//------------LEADER CARDS----------------------------------------------------------------

    /**
     * this method verify if the actual player own the
     * leader card selected by the parameter in input
     * @param toCheck this is the index of the leader card to check
     * @return true if the player own a leader card in that position, false otherwise
     */
    public synchronized boolean checkLeaderCard(int toCheck){
        if (toCheck <= 0) return false;
        return getActualPlayer().getPersonalLeaderCards().size() >= toCheck;
    }

    /**
     * this method activate the card leader specified by the
     * parameter (it can be the first one or the second one)
     *  of the actual player,
     * the method verify if the player have all the requirements to
     * activate the card, and in case activating it
     * if the card selected is already active does not activate it
     * and return false
     * @param toActivate this is the index of the owned leader card to activate
     * @return true if the card got activated correctly, false otherwise
     */
    public synchronized boolean activateLeaderCard(int toActivate){
        return getActualPlayer().activateLeaderCard(toActivate);
    }

    /**
     * this method discard the leader card specified by the
     * parameter (it can be the first one or the second one)
     * of the actual player,
     * if the card has already been activated, it does not discard it,
     * if not, the method return true and add a faith point to the player
     * @param toDiscard this is the index of the owned leader card to discard
     *                  it should be from 1 to personalLeaderCards.getSize()
     * @return true if the card got discarded correctly, false otherwise
     * @throws EndGameException when a player reach the final vatican report, only in a single game
     */
    public synchronized boolean discardLeaderCard(int toDiscard) throws EndGameException {
        if (getActualPlayer().discardLeaderCard(toDiscard)){
            addFaithPointsTo(getActualPlayer(), 1);
            return true;
        }
        return false;
    }

    //---------------------------MANAGEMENT METHODS----------------------------------------------------

    /**
     * this method end the turn.
     * in particular, increment the turnManager
     * @throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     */
    public synchronized void endTurn() throws EndGameException {
        turnManager++; //increase the turn
        if (turnManager % players.size() == 0) checkEndGame(); //if the round is ended, check if the game must finish
    }

    /**
     * this method check if the conditions for the end of the game are met,
     * in particular, it get the number of cards in dashboard for every player,
     * take the max value, and check if the last vatican report is true or if
     * a player bought more than 6 cards
     * @throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     */
    public synchronized void checkEndGame() throws EndGameException {
        int cards = players.stream().
                mapToInt(player -> player.
                        getPersonalDashboard().
                        getPersonalProductionPower().
                        getNumOfCards()).
                max().orElse(0); //get the max number of cards of every player
        if (vaticanReports[2] || cards >= 7) throw new EndGameException(getWinner()); // check if the conditions to end a game are met
    }

    /**
     * this method is use to handle every vaticanReport; this method is called every time to check if we
     * must activate a popeFavorTile card in track
     * @throws EndGameException when a player reach the final vatican report, only in a single game
     */
    protected synchronized void handleVaticanReport() throws EndGameException {
        int i=0;
        while((i < 3) && (vaticanReports[i])) i++;
        if (i > 2) return;
        i++;
        for(RealPlayer p : players){
            if (p.checkVaticanReport(i)) {
                for(RealPlayer p1 : players){
                    setVaticanReports(i);
                    p1.vaticanReport(i);
                }
            }
        }
    }

    /**
     * this method is used to add faithPoints at only one player
     * @param actualPlayer is the player who can advance on the faith track
     * @param toAdd is the number of faithTrack position to add to the actual player
     * @throws EndGameException when a player reach the final vatican report, only in a single game
     */
    protected synchronized void addFaithPointsTo(RealPlayer actualPlayer , int toAdd) throws EndGameException {
        actualPlayer.addFaithPoints(toAdd);
        handleVaticanReport();
    }

    /**
     * this method is used to add faithPoints to all players except one, this one is the actualPlayer
     * @param actualPlayer is the player who remains in the same position;
     * @param toAdd is the number of faithTrack position to add to all players except one, the actualPlayer
     * @throws EndGameException when a player reach the final vatican report, only in a single game
     */
    public synchronized void addFaithPointsExceptTo(RealPlayer actualPlayer, int toAdd) throws EndGameException {
        players.stream().
                filter(player->!(actualPlayer.equals(player))).
                forEach(player -> player.addFaithPoints(toAdd));
        handleVaticanReport();
    }


    /**
     * this method get the player of the current turn
     * @return the player of the current turn
     */
    public synchronized RealPlayer getActualPlayer(){
        return players.get(turnManager % players.size()); // the get goes from 0 to players.size()
    }

    /**
     * this method return the nickname of the winner.
     * in particular, it get the nickname of the player with more victory points,
     * if there are some players with the same number of victory points, the method return
     * the player with more victory points and more resources,
     * if there are some players with the same victory points and amount of resources,
     * return a random player from them.
     * if there is a single game, it gets the single player nickname when
     * his number of cards is >= 7 or if his position reached the last vatican report
     * @return the nickname of the winner
     */
    public synchronized String getWinner(){
        // get the integer list of victory points of every player
        int maxVictoryPoints = players.stream().
                mapToInt(RealPlayer::getVictoryPoints).
                max().orElse(0);
        // get the list of players with more victory points
        List<RealPlayer> potentialWinners = players.stream().
                filter(player -> player.getVictoryPoints() == maxVictoryPoints).
                collect(Collectors.toList());
        // if there aren't more than 1 players with the same amount of max victory points
        if (potentialWinners.size() == 1)
            return potentialWinners.get(0).getNickname();
        // else

        // get the maximum number of resources contained in one of the player resources storages
        int maxResources = players.stream().
                mapToInt(player -> player.getTotalResources().getSize()).
                max().orElse(0);
        // get the first player (from the ones that have the maximum number of victory points) that have the maximum number of resources
        return potentialWinners.stream().
                filter(player -> player.getTotalResources().getSize() == maxResources).
                collect(Collectors.toList()).
                get(0).
                getNickname();


    }
    //-----------------------CHECKING RESOURCES-------------------------------------------------

    /**
     * this method verify if the resources of the
     * player of the current turn
     * contain all the resources given in input
     * @param toVerify these are the resources to check
     * @return true if the resources of the player of the current turn
     *         contains all the resources given in input
     */
    public synchronized boolean contains(CollectionResources toVerify){
        return getActualPlayer().
                getTotalResources().
                containsAll(toVerify);
    }

    /**
     * this method verify if the discounted resources of the
     * player of the current turn
     * contain all the resources given in input
     * @param toVerify these are the resources to check
     * @return true if the discounted resources of the player of the current turn
     *         contains all the resources given in input
     */
    public synchronized boolean containsWithDiscount(CollectionResources toVerify){
        return getActualPlayer().
                getTotalDiscountedResources().
                containsAll(toVerify);
    }

    /**
     * this method verify if the warehouse resources of the
     * player of the current turn
     * contain all the resources given in input
     * @param toVerify these are the resources to check
     * @return true if the warehouse resources of the player of the current turn
     *         contains all the resources given in input
     */
    public synchronized boolean containsInWarehouse(CollectionResources toVerify){
        return getActualPlayer().
                getPersonalDashboard().
                getPersonalWarehouse().
                getTotalResources().containsAll(toVerify);
    }

    /**
     * this method verify if the strongbox resources of the
     * player of the current turn
     * contain all the resources given in input
     * @param toVerify these are the resources to check
     * @return true if the strongbox resources of the player of the current turn
     *         contains all the resources given in input
     */
    public synchronized boolean containsInStrongbox(CollectionResources toVerify){
        return getActualPlayer().
                getPersonalDashboard().
                getPersonalStrongbox().
                getStrongboxResources().containsAll(toVerify);
    }
}