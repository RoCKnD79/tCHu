package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.*;

public class GameTest {
    private Map<PlayerId, String> playerNames = new HashMap<>();
    private Map<PlayerId, Player> players = new HashMap<>();
    private static List<Route> claimableRoutes = new ArrayList<>();
    private static int numberOfTurns = 0;

    @Test
    void playTest(){
        playerNames.put(PlayerId.PLAYER_1, "Chris");
        playerNames.put(PlayerId.PLAYER_2, "ManRo");
        players.put(PlayerId.PLAYER_1, new TestPlayer(1, ChMap.routes()));
        players.put(PlayerId.PLAYER_2, new TestPlayer(1, ChMap.routes()));
        Random rng = new Random();
        Game.play(players, playerNames, SortedBag.of(ChMap.tickets()), rng);
        System.out.println(numberOfTurns);
    }

    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;

        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;
        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;
        private SortedBag<Ticket> initialTickets;



        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
        }

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        }

        @Override
        public void receiveInfo(String info) {

        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            this.gameState = newState;
            this.ownState = ownState;
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            initialTickets = tickets;
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {

            SortedBag.Builder<Ticket> sbb = new SortedBag.Builder<>();
            sbb.add(initialTickets.get(0));
            sbb.add(initialTickets.get(1));
            sbb.add(initialTickets.get(2));

            return sbb.build();
        }

        @Override
        public TurnKind nextTurn() {
            numberOfTurns += 1;
            System.out.println("deck size : " + gameState.cardState().deckSize());
            System.out.println(("CurPla : " + gameState.currentPlayerState().cardCount()) + "SecPla : " + gameState.playerState(gameState.currentPlayerId().next()).cardCount());
            System.out.println("discard size : " + gameState.cardState().discardsSize());
            System.out.println("number total cards : " + ((((gameState.cardState().discardsSize()) +  gameState.playerState(gameState.currentPlayerId().next()).cardCount()) + gameState.currentPlayerState().cardCount()) + gameState.cardState().deckSize() + 5));
           // System.out.println("each turn deck size : " + gameState.cardState().deckSize());
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer
            List<Route> allClaimableRoutes = new ArrayList<>(ChMap.routes());


            claimableRoutes.clear();
            //System.out.println("size of allclaimableroutes : " + allClaimableRoutes.size());
            for(Route r : allClaimableRoutes) {
                if(ownState.carCount() >= r.length()) {
                    for (SortedBag<Card> listOfCards : r.possibleClaimCards()) {
                        for (int i = 0; i < ownState.possibleClaimCards(r).size(); ++i) {
                            if (listOfCards.contains(ownState.possibleClaimCards(r).get(i))) {
                                claimableRoutes.add(r);
                            }
                        }
                    }
                }
            }
            //System.out.println("claimable routes size before : " + claimableRoutes.size());
            //System.out.println("claimed routes size : " + gameState.claimedRoutes().size());
            for(Route r : gameState.claimedRoutes()){
               // System.out.println("claimed route is in all claimableRoutes : " + claimableRoutes.contains(r));
                claimableRoutes.remove(r);
                //System.out.println("claimed route is STILL in all claimableRoutes : " + claimableRoutes.contains(r));
            }
           //System.out.println("claimable routes size after : " + claimableRoutes.size());
           // System.out.println("Claimable routes size :" + claimableRoutes.size());
            if (claimableRoutes.isEmpty()) {
                return TurnKind.DRAW_CARDS;
            } else {
                //System.out.println("claimable routes size : " + claimableRoutes.size());
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                //System.out.println("players possible claim cards for route : " + ownState.possibleClaimCards(route));
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                //System.out.println("cards size" + cards.size());
                initialClaimCards = cards.get(0);

                return TurnKind.CLAIM_ROUTE;
            }
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            List<Ticket> chosenTickets = new ArrayList<>();
            chosenTickets.add(options.get(0));
            chosenTickets.add(options.get(1));
            chosenTickets.add(options.get(2));
            return SortedBag.of(chosenTickets);
        }

        @Override
        public int drawSlot() {
            return rng.nextInt(6)-1;
        }

        @Override
        public Route claimedRoute() {
            return claimableRoutes.get(0);
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return ownState.possibleClaimCards(claimedRoute()).get(0);
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {

            /*List<Card> additionalCards = new ArrayList<>();
            for(Card c : ownState.cards()){
                for(int i = 0; i < options.size(); ++i){
                    for(int j = 0; j < options.get(i).size(); ++j) {
                        if (additionalCards.size() < options.get) {
                            if (c.equals(options.get(i).get(j))) {
                                additionalCards.add(c);
                            }
                        }
                    }
            }}*/
            if (options.isEmpty()){
                return SortedBag.of();
            }
            return options.get(0);
        }
    }
}
