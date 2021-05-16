package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.view.thinModelComponents.ThinGame;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

import java.util.ArrayList;
import java.util.List;

    public class GraphicalGame implements CharFigure {
        private final CharStream stream;
        private final ThinPlayer myPlayer;
        private final List<ThinPlayer> opponents;
        private final Marble[][] marketMarble;
        private final Marble lonelyMarble;
        private final DevelopmentCard[][] cardsMarket;
        private final BackColor backgroundColor = BackColor.ANSI_BRIGHT_BG_CYAN;
        private final SoloToken soloToken;


        public GraphicalGame(CharStream stream, ThinGame game){
            this.stream = stream;
            this.myPlayer = game.getMyself();
            this.opponents = game.getOpponents();
            this.marketMarble = game.getMarbleMarket();
            this.lonelyMarble = game.getLonelyMarble();
            this.cardsMarket = game.getCardsMarket();
            this.soloToken = game.getSoloToken();
        }

        public GraphicalGame(CharStream stream, ThinPlayer myPlayer, List<ThinPlayer> opponents, Marble[][] marketMarble, Marble lonelyMarble, DevelopmentCard[][] cardsMarket, SoloToken soloToken) {
            this.stream = stream;
            this.myPlayer = myPlayer;
            this.opponents = opponents;
            this.marketMarble = marketMarble;
            this.lonelyMarble = lonelyMarble;
            this.cardsMarket = cardsMarket;
            this.soloToken = soloToken;
        }

        /**
         *
         */
        @Override
        public void draw() {
            draw(CharStream.defaultX, CharStream.defaultY);
        }

        /**
         * @param relX X position to be considered as X absolute zero when drawing
         * @param relY Y position to be considered as Y absolute zero when drawing
         */
        @Override
        public void draw(int relX, int relY) {

            //MARBLE MARKET
            GraphicalMarbleMarket graphicalMarbleMarket = new GraphicalMarbleMarket(stream, marketMarble, lonelyMarble);
            graphicalMarbleMarket.draw(relX+8, relY+3);

            //CARDS MARKET
            GraphicalCardsMarket graphicalCardsMarket = new GraphicalCardsMarket(stream, cardsMarket);
            graphicalCardsMarket.draw(relX+ 54, relY);

            //FAITH TRACK
            List<ThinPlayer> thinPlayers = new ArrayList<>();
            thinPlayers.add(myPlayer);
            thinPlayers.addAll(opponents);

            GraphicalFaithTrack graphicalFaithTrack = new GraphicalFaithTrack(stream, thinPlayers);
            graphicalFaithTrack.draw(relX, relY+ graphicalCardsMarket.getHeight()+10);



            //PLAYERS
            GraphicalPlayer clientOwner = new GraphicalPlayer(stream, myPlayer); //client owner
            clientOwner.draw(relX,relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight()+ 2);

            if (opponents.size()==1) {
                if (!opponents.get(0).getNickname().equals("LorenzoIlMagnifico")) { //print only another player
                    GraphicalPlayer opponent = new GraphicalPlayer(stream, opponents.get(0));

                    for(int i=0; i<=clientOwner.getHeight(); i++) {
                        stream.addChar('|', relX+clientOwner.getWidth()+2, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2+i, ForeColor.ANSI_BRIGHT_GREEN, backgroundColor);
                    }
                    opponent.draw(relX+clientOwner.getWidth()+4, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2);

                } else { //print SoloTokens
                    GraphicalToken graphicalToken = new GraphicalToken(stream, soloToken);
                    graphicalToken.draw(relX+graphicalCardsMarket.getWidth()+graphicalMarbleMarket.getWidth()+64,relY +5 );
                }
            }

            if (opponents.size()==2) {
                GraphicalPlayer opponent1 = new GraphicalPlayer(stream, opponents.get(0));
                GraphicalPlayer opponent2 = new GraphicalPlayer(stream, opponents.get(1));

                drawCorners(relX, relY, graphicalCardsMarket, graphicalFaithTrack, clientOwner, opponent1);
                stream.addChar('+', relX+clientOwner.getWidth()+2, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2+clientOwner.getHeight()+1, ForeColor.ANSI_BRIGHT_GREEN, backgroundColor);

                for(int i=3; i<=(clientOwner.getWidth()+1); i++) {
                    stream.addChar('-', relX+clientOwner.getWidth()+i, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2+clientOwner.getHeight()+1, ForeColor.ANSI_BRIGHT_GREEN, backgroundColor);
                }

               opponent2.draw(relX, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2+ clientOwner.getHeight()+2);
            }

            if (opponents.size()==3) {
                GraphicalPlayer opponent1 = new GraphicalPlayer(stream, opponents.get(0));
                GraphicalPlayer opponent2 = new GraphicalPlayer(stream, opponents.get(1));
                GraphicalPlayer opponent3 = new GraphicalPlayer(stream, opponents.get(2));

                drawCorners(relX, relY, graphicalCardsMarket, graphicalFaithTrack, clientOwner, opponent1);


                for(int i=3; i<=(clientOwner.getWidth()+1); i++) {
                    stream.addChar('-', relX+clientOwner.getWidth()+i, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2+clientOwner.getHeight()+1, ForeColor.ANSI_BRIGHT_GREEN, backgroundColor);
                }

                opponent2.draw(relX, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2+ clientOwner.getHeight()+2);

                for(int i=0; i<=(clientOwner.getHeight()+1); i++) {
                    stream.addChar('|', relX+clientOwner.getWidth()+2, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2+i+clientOwner.getHeight()+1, ForeColor.ANSI_BRIGHT_GREEN, backgroundColor);
                }

                opponent3.draw(relX+clientOwner.getWidth()+4, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight()+clientOwner.getHeight()+4);

            }
        }

        private void drawCorners(int relX, int relY, GraphicalCardsMarket graphicalCardsMarket, GraphicalFaithTrack graphicalFaithTrack, GraphicalPlayer clientOwner, GraphicalPlayer opponent1) {
            for(int i=0; i<=clientOwner.getHeight(); i++) {
                stream.addChar('|', relX+clientOwner.getWidth()+2, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2+i, ForeColor.ANSI_BRIGHT_GREEN, backgroundColor);
            }

            opponent1.draw(relX+clientOwner.getWidth()+4, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2);

            for(int i=0; i<=(clientOwner.getWidth()+1); i++) {
                stream.addChar('-', relX+i, relY+ graphicalCardsMarket.getHeight()+ graphicalFaithTrack.getHeight() +2+clientOwner.getHeight()+1, ForeColor.ANSI_BRIGHT_GREEN, backgroundColor);
            }
        }
    }


