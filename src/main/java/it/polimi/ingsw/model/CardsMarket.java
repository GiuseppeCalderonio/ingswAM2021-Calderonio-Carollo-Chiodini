package it.polimi.ingsw.model;

import java.util.*;

/**
 * this class represent the market of cards
 */
public class CardsMarket {
    /**
     * this attribute represent the market as a matrix of stacks of cards
     */
    private final List<DevelopmentCard>[][] cardMatrix;

    /**
     * this method create the market
     */

    public CardsMarket() {
        cardMatrix = new ArrayList[3][4];
        // stack of card of third level, green color
        cardMatrix[0][0] = new ArrayList<>();
        cardMatrix[0][0].add(new DevelopmentCard(CardColor.GREEN
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Shield(),new Coin(),new Coin(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Shield(),new Shield(),new Shield())))
                ,0));
        cardMatrix[0][0].add(new DevelopmentCard(CardColor.GREEN
                ,3
                ,11
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Shield(),new Shield(),new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,3));
        cardMatrix[0][0].add(new DevelopmentCard(CardColor.GREEN
                ,3
                ,10
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Shield(),new Shield(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Stone(),new Stone())))
                ,1));
        cardMatrix[0][0].add(new DevelopmentCard(CardColor.GREEN
                ,3
                ,9
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Shield(),new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone())))
                ,2));
        Collections.shuffle(cardMatrix[0][0]);
        // stack of card of third level, blue color
        cardMatrix[0][1] = new ArrayList<>();
        cardMatrix[0][1].add(new DevelopmentCard(CardColor.BLUE
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin(),new Coin(),new Stone(),new Stone(),new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Shield(),new Shield(),new Shield())))
                ,0));
        cardMatrix[0][1].add(new DevelopmentCard(CardColor.BLUE
                ,3
                ,11
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin(),new Coin(),new Coin(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Shield())))
                ,3));
        cardMatrix[0][1].add(new DevelopmentCard(CardColor.BLUE
                ,3
                ,10
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin(),new Coin(),new Coin(),new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Stone(),new Stone())))
                ,1));
        cardMatrix[0][1].add(new DevelopmentCard(CardColor.BLUE
                ,3
                ,9
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin(),new Coin(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield())))
                ,2));
        Collections.shuffle(cardMatrix[0][1]);
        // stack of card of third level, yellow color
        cardMatrix[0][2] = new ArrayList<>();
        cardMatrix[0][2].add(new DevelopmentCard(CardColor.YELLOW
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone(),new Stone(),new Servant(),new Servant(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Servant(),new Servant(),new Servant())))
                ,0));
        cardMatrix[0][2].add(new DevelopmentCard(CardColor.YELLOW
                ,3
                ,11
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone(),new Stone(),new Stone(),new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())))
                ,3));
        cardMatrix[0][2].add(new DevelopmentCard(CardColor.YELLOW
                ,3
                ,10
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone(),new Stone(),new Stone(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Shield(),new Shield())))
                ,1));
        cardMatrix[0][2].add(new DevelopmentCard(CardColor.YELLOW
                ,3
                ,9
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone(),new Stone(),new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant())))
                ,2));
        Collections.shuffle(cardMatrix[0][2]);
        // stack of card of third level, purple color
        cardMatrix[0][3] = new ArrayList<>();
        cardMatrix[0][3].add(new DevelopmentCard(CardColor.PURPLE
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant(),new Servant(),new Shield(),new Shield(),new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone(),new Servant())))
                ,0));
        cardMatrix[0][3].add(new DevelopmentCard(CardColor.PURPLE
                ,3
                ,11
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant(),new Servant(),new Servant(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,3));
        cardMatrix[0][3].add(new DevelopmentCard(CardColor.PURPLE
                ,3
                ,10
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant(),new Servant(),new Servant(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Servant(),new Servant())))
                ,1));
        cardMatrix[0][3].add(new DevelopmentCard(CardColor.PURPLE
                ,3
                ,9
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant(),new Servant(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(), new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin())))
                ,2));
        Collections.shuffle(cardMatrix[0][3]);
        // stack of card of second level, green color
        cardMatrix[1][0] = new ArrayList<>();
        cardMatrix[1][0].add(new DevelopmentCard(CardColor.GREEN
                ,2
                ,8
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Coin(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield())))
                ,1));
        cardMatrix[1][0].add(new DevelopmentCard(CardColor.GREEN
                ,2
                ,7
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone())))
                ,2));
        cardMatrix[1][0].add(new DevelopmentCard(CardColor.GREEN
                ,2
                ,6
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone())))
                ,0));
        cardMatrix[1][0].add(new DevelopmentCard(CardColor.GREEN
                ,2
                ,5
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,2));
        Collections.shuffle(cardMatrix[1][0]);
        // stack of card of second level, blue color
        cardMatrix[1][1] = new ArrayList<>();
        cardMatrix[1][1].add(new DevelopmentCard(CardColor.BLUE
                ,2
                ,8
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin(),new Stone(),new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone())))
                ,1));
        cardMatrix[1][1].add(new DevelopmentCard(CardColor.BLUE
                ,2
                ,7
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield())))
                ,2));
        cardMatrix[1][1].add(new DevelopmentCard(CardColor.BLUE
                ,2
                ,6
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin(),new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant())))
                ,0));
        cardMatrix[1][1].add(new DevelopmentCard(CardColor.BLUE
                ,2
                ,5
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())))
                ,new CollectionResources(new ArrayList<>())
                ,2));
        Collections.shuffle(cardMatrix[1][1]);
        // stack of card of second level, yellow color
        cardMatrix[1][2] = new ArrayList<>();
        cardMatrix[1][2].add(new DevelopmentCard(CardColor.YELLOW
                ,2
                ,8
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone(),new Servant(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin())))
                ,1));
        cardMatrix[1][2].add(new DevelopmentCard(CardColor.YELLOW
                ,2
                ,7
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone(),new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant())))
                ,2));
        cardMatrix[1][2].add(new DevelopmentCard(CardColor.YELLOW
                ,2
                ,6
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone(),new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin())))
                ,0));
        cardMatrix[1][2].add(new DevelopmentCard(CardColor.YELLOW
                ,2
                ,5
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Shield())))
                ,new CollectionResources(new ArrayList<>())
                ,2));
        Collections.shuffle(cardMatrix[1][2]);
        // stack of card of second level, purple color
        cardMatrix[1][3] = new ArrayList<>();
        cardMatrix[1][3].add(new DevelopmentCard(CardColor.PURPLE
                ,2
                ,8
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant(),new Shield(),new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant())))
                ,1));
        cardMatrix[1][3].add(new DevelopmentCard(CardColor.PURPLE
                ,2
                ,7
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin())))
                ,2));
        cardMatrix[1][3].add(new DevelopmentCard(CardColor.PURPLE
                ,2
                ,6
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield())))
                ,0));
        cardMatrix[1][3].add(new DevelopmentCard(CardColor.PURPLE
                ,2
                ,5
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>())
                ,2));
        Collections.shuffle(cardMatrix[1][3]);
        // stack of card of first level, green color
        cardMatrix[2][0] = new ArrayList<>();
        cardMatrix[2][0].add(new DevelopmentCard(CardColor.GREEN
                ,1
                ,4
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin())))
                ,1));
        cardMatrix[2][0].add(new DevelopmentCard(CardColor.GREEN
                ,1
                ,3
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Stone(),new Coin())))
                ,0));
        cardMatrix[2][0].add(new DevelopmentCard(CardColor.GREEN
                ,1
                ,2
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Stone(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())))
                ,0));
        cardMatrix[2][0].add(new DevelopmentCard(CardColor.GREEN
                ,1
                ,1
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>())
                ,1));
        Collections.shuffle(cardMatrix[2][0]);
        // stack of card of first level, blue color
        cardMatrix[2][1] = new ArrayList<>();
        cardMatrix[2][1].add(new DevelopmentCard(CardColor.BLUE
                ,1
                ,4
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant())))
                ,1));
        cardMatrix[2][1].add(new DevelopmentCard(CardColor.BLUE
                ,1
                ,3
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Servant(),new Coin())))
                ,0));
        cardMatrix[2][1].add(new DevelopmentCard(CardColor.BLUE
                ,1
                ,2
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Coin(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,0));
        cardMatrix[2][1].add(new DevelopmentCard(CardColor.BLUE
                ,1
                ,1
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Shield())))
                ,new CollectionResources(new ArrayList<>())
                ,1));
        Collections.shuffle(cardMatrix[2][1]);
        // stack of card of first level, yellow color
        cardMatrix[2][2] = new ArrayList<>();
        cardMatrix[2][2].add(new DevelopmentCard(CardColor.YELLOW
                ,1
                ,4
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield())))
                ,1));
        cardMatrix[2][2].add(new DevelopmentCard(CardColor.YELLOW
                ,1
                ,3
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Coin(),new Servant())))
                ,0));
        cardMatrix[2][2].add(new DevelopmentCard(CardColor.YELLOW
                ,1
                ,2
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Coin(),new Stone())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,0));
        cardMatrix[2][2].add(new DevelopmentCard(CardColor.YELLOW
                ,1
                ,1
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())))
                ,new CollectionResources(new ArrayList<>())
                ,1));
        Collections.shuffle(cardMatrix[2][2]);
        // stack of card of first level, purple color
        cardMatrix[2][3] = new ArrayList<>();
        cardMatrix[2][3].add(new DevelopmentCard(CardColor.PURPLE
                ,1
                ,4
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Stone())))
                ,1));
        cardMatrix[2][3].add(new DevelopmentCard(CardColor.PURPLE
                ,1
                ,3
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(),new Shield(),new Servant())))
                ,0));
        cardMatrix[2][3].add(new DevelopmentCard(CardColor.PURPLE
                ,1
                ,2
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Servant(),new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Shield())))
                ,0));
        cardMatrix[2][3].add(new DevelopmentCard(CardColor.PURPLE
                ,1
                ,1
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,1));
        Collections.shuffle(cardMatrix[2][3]);
    }

    /**
     * this method get and remove the card specified by the parameters,
     * it requires that the deck of cards isn't empty
     * @param level this is the level of the card
     * @param color this is the color of the card
     * @return the card with the level and colour specified
     */
    public DevelopmentCard popCard(int level, CardColor color){
        int row = 2-(level-1);
        int column = color.getIndex();
        int index = cardMatrix[row][column].size()-1;
        DevelopmentCard toReturn = cardMatrix[row][column].get(index);
        cardMatrix[row][column].remove(index);
        return toReturn;
    }

    /**
     * this method verify if a column of the market is empty
     * @return true if a column is empty, false if it isn't
     */
    public boolean checkMissColumn() {
        boolean missColumn;
        for (int column = 0; column < 4; column++) {
            missColumn = true;
            for (int row = 0; row < 3; row++) {
                if (!cardMatrix[row][column].isEmpty()) {
                    missColumn = false;
                    break;
                }
            }
            if (missColumn) return true;
        }
        return false;
    }

    /**
     * this method get the card with the color and level specified by the parameters,
     * it returns null if the deck of cards is empty
     * @param level this is the level of the card
     * @param color this is the color of the card
     * @return the card with the level and colour specified
     */
    public DevelopmentCard getCard(int level, CardColor color){
        int row = 2-(level-1);
        int column = color.getIndex();
        int index = cardMatrix[row][column].size()-1;
        if (cardMatrix[row][column].isEmpty()) return null;
        return  cardMatrix[row][column].get(index);
    }

    /**
     * this method check if a deck of card is empty
     * @param level this is the level of the card to check
     * @param color this is the color of the card to check
     * @return true if there is a card in the deck with the level and color specified
     * in the parameters in the market, false otherwise
     */
    public boolean checkCard(int level , CardColor color){

        return !(getCard(level , color) == null);
    }

}