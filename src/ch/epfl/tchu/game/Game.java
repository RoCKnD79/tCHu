package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

/**
 * @author Christopher Soriano (326354)
 */

public final class Game {

    private Game() {
    }

    /**
     * makes the players play a game of tCHu
     * note that the methods are all called in the right order following the rules of tCHu.
     * this method's main role is to figure out what kind of turn each player wants to play, and then call
     *
     * @param players,     map from playerIds to Players
     * @param playerNames, map from playerIds to player Names
     * @param tickets,     sorted bag of tickets for the game
     * @param rng,         attribute of Random
     * @throws IllegalArgumentException, if players size is inferior to 2, or if playerNames is also inferior to two.
     */
    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng)
            throws IllegalArgumentException {
        System.out.println("play has started");
        if ((players.size() < 2) || (playerNames.size() < 2)) {
            throw new IllegalArgumentException();
        }

        Player player1 = players.get(PlayerId.PLAYER_1);
        Player player2 = players.get(PlayerId.PLAYER_2);

        /*Player player1 = players.get(gameState.currentPlayerId());
        Player player2 = players.get(gameState.currentPlayerId().next());*/

        player1.initPlayers(PlayerId.PLAYER_1, playerNames);
        player2.initPlayers(PlayerId.PLAYER_2, playerNames);

        GameState gameState = GameState.initial(tickets, rng);

        informBothPlayers((new Info(playerNames.get(gameState.currentPlayerId())).willPlayFirst()), players);

        SortedBag<Ticket> player1InitialTickets = SortedBag.of(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
        gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);
        SortedBag<Ticket> player2InitialTickets = SortedBag.of(gameState.topTickets(Constants.INITIAL_TICKETS_COUNT));
        gameState = gameState.withoutTopTickets(Constants.INITIAL_TICKETS_COUNT);

        player1.setInitialTicketChoice(player1InitialTickets);
        player2.setInitialTicketChoice(player2InitialTickets);

        informBothPlayerOfAGameStateChange(players, gameState.currentPlayerState(), gameState.playerState(gameState.currentPlayerId().next()), gameState);

        SortedBag<Ticket> player1Tickets = player1.chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(gameState.currentPlayerId(), player1Tickets);
        SortedBag<Ticket> player2Tickets = player2.chooseInitialTickets();
        gameState = gameState.withInitiallyChosenTickets(gameState.currentPlayerId().next(), player2Tickets);

        /*gameState = gameState.withInitiallyChosenTickets(gameState.currentPlayerId(), player1Tickets);
        gameState = gameState.withInitiallyChosenTickets(gameState.currentPlayerId().next(), player2Tickets);*/

        informBothPlayers(new Info(playerNames.get(PlayerId.PLAYER_1)).keptTickets(player2Tickets.size()), players);
        informBothPlayers(new Info(playerNames.get(PlayerId.PLAYER_2)).keptTickets(player1Tickets.size()), players);

        boolean doWhile = true;
        boolean lastTurn = false;
        while (doWhile || !lastTurn) {
            Player currentPlayer = players.get(gameState.currentPlayerId());
            Info currentPlayerInfo = new Info(playerNames.get(gameState.currentPlayerId()));
            PlayerState currentPlayerState = gameState.currentPlayerState();
            PlayerState secondPlayerState = gameState.playerState(gameState.currentPlayerId().next());

            if (!(gameState.lastPlayer() == null) && gameState.lastPlayer().equals(gameState.currentPlayerId())) {
                informBothPlayers(currentPlayerInfo.lastTurnBegins(secondPlayerState.carCount()), players);
            }
            informBothPlayers(currentPlayerInfo.canPlay(), players);

            informBothPlayerOfAGameStateChange(players, currentPlayerState, secondPlayerState, gameState);


            switch (currentPlayer.nextTurn()) {
                case DRAW_TICKETS:

                    if(gameState.canDrawTickets()) {
                        SortedBag<Ticket> proposedTickets = gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT);
                        informBothPlayers(currentPlayerInfo.drewTickets(proposedTickets.size()), players);

                        SortedBag<Ticket> chosenTickets = currentPlayer.chooseTickets(proposedTickets);

                        informBothPlayers(currentPlayerInfo.keptTickets(chosenTickets.size()), players);

                        gameState = gameState.withChosenAdditionalTickets(proposedTickets, chosenTickets);

                        informBothPlayerOfAGameStateChange(players, currentPlayerState, secondPlayerState, gameState);


                    }
                    break;

                case DRAW_CARDS:
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    if(gameState.canDrawCards()){
                    int cardSlot1 = currentPlayer.drawSlot();

                    if (cardSlot1 != Constants.DECK_SLOT) {
                        informBothPlayers(currentPlayerInfo.drewVisibleCard(gameState.cardState().faceUpCard(cardSlot1)), players);
                        gameState = gameState.withDrawnFaceUpCard(cardSlot1);
                    } else {
                        informBothPlayers(currentPlayerInfo.drewBlindCard(), players);
                        gameState = gameState.withBlindlyDrawnCard();
                    }}

                   // informBothPlayerOfAGameStateChange(players, currentPlayerState, secondPlayerState, gameState);

                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);

                    player1.updateState(gameState, gameState.playerState(PlayerId.PLAYER_1));
                    player2.updateState(gameState, gameState.playerState(PlayerId.PLAYER_2));

                    if(gameState.canDrawCards()){
                    int cardSlot2 = currentPlayer.drawSlot();
                    if (cardSlot2 != Constants.DECK_SLOT) {
                        informBothPlayers(currentPlayerInfo.drewVisibleCard(gameState.cardState().faceUpCard(cardSlot2)), players);
                        gameState = gameState.withDrawnFaceUpCard(cardSlot2);
                    } else {
                        informBothPlayers(currentPlayerInfo.drewBlindCard(), players);
                        gameState = gameState.withBlindlyDrawnCard();
                    }}


                    break;

                case CLAIM_ROUTE:

                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    Route routeToBeClaimed = currentPlayer.claimedRoute();
                    SortedBag<Card> cardInitiallyUsedToClaim = currentPlayer.initialClaimCards();

                    if ((routeToBeClaimed.level().equals(Route.Level.OVERGROUND))) {
                        if (currentPlayerState.canClaimRoute(routeToBeClaimed)) {
                            informBothPlayers(currentPlayerInfo.claimedRoute(routeToBeClaimed, cardInitiallyUsedToClaim), players);
                            gameState = gameState.withClaimedRoute(routeToBeClaimed, cardInitiallyUsedToClaim);

                        } else {
                            informBothPlayers(currentPlayerInfo.didNotClaimRoute(routeToBeClaimed), players);
                        }
                    }
                    if ((routeToBeClaimed.level().equals(Route.Level.UNDERGROUND))) {
                        informBothPlayers(currentPlayerInfo.attemptsTunnelClaim(routeToBeClaimed, cardInitiallyUsedToClaim), players);
                        if (currentPlayerState.canClaimRoute(routeToBeClaimed)) {
                            List<Card> cardsDrawn = new ArrayList<>();
                            for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; ++i) {
                                gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                                cardsDrawn.add(gameState.topCard());
                                gameState = gameState.withoutTopCard();
                            }

                            SortedBag<Card> cardsDrawnSorted = SortedBag.of(cardsDrawn);
                            int numberOfAdditionalCards = routeToBeClaimed.additionalClaimCardsCount(cardInitiallyUsedToClaim, cardsDrawnSorted);
                            informBothPlayers(currentPlayerInfo.drewAdditionalCards(cardsDrawnSorted, numberOfAdditionalCards), players);

                            if (numberOfAdditionalCards == 0) {
                                gameState = gameState.withClaimedRoute(routeToBeClaimed, cardInitiallyUsedToClaim);
                                gameState = gameState.withMoreDiscardedCards(cardsDrawnSorted);
                                informBothPlayers(currentPlayerInfo.claimedRoute(routeToBeClaimed, cardInitiallyUsedToClaim), players);
                            } else {
                                /*
                                list of POSSIBLE additional cards => cards player may play to claim,
                                but doesn't necessarily have
                                 */
                                List<SortedBag<Card>> possibleAdditionalCards = currentPlayerState.possibleAdditionalCards(numberOfAdditionalCards, cardInitiallyUsedToClaim);

                                if (possibleAdditionalCards.isEmpty()) {
                                    gameState.withMoreDiscardedCards(cardsDrawnSorted);
                                    informBothPlayers(currentPlayerInfo.didNotClaimRoute(routeToBeClaimed), players);

                                } else {
                                    SortedBag<Card> additionalCardsChosen = (currentPlayer.chooseAdditionalCards(possibleAdditionalCards));
                                    if (additionalCardsChosen.isEmpty()) {
                                        gameState = gameState.withMoreDiscardedCards(cardsDrawnSorted);
                                        informBothPlayers(currentPlayerInfo.didNotClaimRoute(routeToBeClaimed), players);
                                    } else {
                                        gameState = gameState.withClaimedRoute(routeToBeClaimed, additionalCardsChosen.union(cardInitiallyUsedToClaim));
                                        gameState = gameState.withMoreDiscardedCards(cardsDrawnSorted);
                                        informBothPlayers(currentPlayerInfo.claimedRoute(routeToBeClaimed, additionalCardsChosen.union(cardInitiallyUsedToClaim)), players);
                                    }
                                }
                            }

                                /*SortedBag<Card> playersCardAfterInitialClaim = currentPlayerState.cards().difference(cardInitiallyUsedToClaim);

                                for(SortedBag<Card> bag : new ArrayList<>(possibleAdditionalCards)) {
                                    if(!playersCardAfterInitialClaim.contains(bag)) {
                                        possibleAdditionalCards.remove(bag);

                                    }
                                }

                                SortedBag<Card> additionalCardsChosen = SortedBag.of();
                                if (possibleAdditionalCards.size() > 0) {
                                    additionalCardsChosen = (currentPlayer.chooseAdditionalCards(possibleAdditionalCards));
                                }

                                if ((additionalCardsChosen.size() == 0) || (additionalCardsChosen.size() != numberOfAdditionalCards)) {
                                    informBothPlayers(currentPlayerInfo.didNotClaimRoute(routeToBeClaimed), players);
                                    gameState = gameState.withMoreDiscardedCards(cardsDrawnSorted);
                                } else {
                                    SortedBag<Card> allCardsUsedToClaim = additionalCardsChosen.union(cardInitiallyUsedToClaim);
                                    gameState = gameState.withClaimedRoute(routeToBeClaimed, allCardsUsedToClaim);
                                    gameState = gameState.withMoreDiscardedCards(cardsDrawnSorted);
                                    informBothPlayers(currentPlayerInfo.claimedRoute(routeToBeClaimed, allCardsUsedToClaim), players);
                                }


                        } else {
                            informBothPlayers(currentPlayerInfo.didNotClaimRoute(routeToBeClaimed), players);
                        }*/
                        }
                    }
                    break;
            }

            if (gameState.currentPlayerId().equals((gameState.lastPlayer()))) {
                lastTurn = true;
            }
            gameState = gameState.forNextTurn();
            doWhile = !gameState.currentPlayerId().equals((gameState.lastPlayer())) && (!(gameState.lastTurnBegins()));

        }

        informBothPlayerOfAGameStateChange(players, gameState.currentPlayerState(), gameState.playerState(gameState.currentPlayerId().next()), gameState);

        int currentPlayerPoints = gameState.currentPlayerState().finalPoints();
        int secondPlayerPoints = gameState.playerState(gameState.currentPlayerId().next()).finalPoints();
        if ((Trail.longest(gameState.currentPlayerState().routes()).length()) > Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()).length()) {
            currentPlayerPoints += Constants.LONGEST_TRAIL_BONUS_POINTS;
            informBothPlayers((new Info(playerNames.get(gameState.currentPlayerId()))).getsLongestTrailBonus(Trail.longest(gameState.currentPlayerState().routes())), players);
        }
        if ((Trail.longest(gameState.currentPlayerState().routes()).length()) < Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()).length()) {
            secondPlayerPoints += Constants.LONGEST_TRAIL_BONUS_POINTS;
            informBothPlayers(((new Info(playerNames.get(gameState.currentPlayerId().next()))).getsLongestTrailBonus(Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()))), players);
        }
        if ((Trail.longest(gameState.currentPlayerState().routes()).length()) == Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()).length()) {
            currentPlayerPoints += Constants.LONGEST_TRAIL_BONUS_POINTS;
            informBothPlayers((new Info(playerNames.get(gameState.currentPlayerId()))).getsLongestTrailBonus(Trail.longest(gameState.currentPlayerState().routes())), players);
            secondPlayerPoints += Constants.LONGEST_TRAIL_BONUS_POINTS;
            informBothPlayers(((new Info(playerNames.get(gameState.currentPlayerId().next()))).getsLongestTrailBonus(Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()))), players);
        }
        if (currentPlayerPoints > secondPlayerPoints) {
            informBothPlayers((new Info(playerNames.get(gameState.currentPlayerId()))).won(currentPlayerPoints, secondPlayerPoints), players);
        }
        if (currentPlayerPoints < secondPlayerPoints) {
            informBothPlayers((new Info(playerNames.get(gameState.currentPlayerId().next()))).won(secondPlayerPoints, currentPlayerPoints), players);
        }


        List<String> listOfPlayers = new ArrayList<>();
        listOfPlayers.add(playerNames.get(PlayerId.PLAYER_1));
        listOfPlayers.add(playerNames.get(PlayerId.PLAYER_2));

        if (currentPlayerPoints == secondPlayerPoints) {
            informBothPlayers(Info.draw(listOfPlayers, currentPlayerPoints), players);
        }
    }

    private static void informBothPlayerOfAGameStateChange(Map<PlayerId, Player> players, PlayerState currentPlayerState, PlayerState otherPlayState, PublicGameState newGameState) {
        if(newGameState.currentPlayerId().equals(PlayerId.PLAYER_1)){
            players.get(PlayerId.PLAYER_1).updateState(newGameState, currentPlayerState);
            players.get(PlayerId.PLAYER_2).updateState(newGameState, otherPlayState);
        }else{
            players.get(PlayerId.PLAYER_1).updateState(newGameState, otherPlayState);
            players.get(PlayerId.PLAYER_2).updateState(newGameState, currentPlayerState);
        }
    }

    private static void informBothPlayers(String string, Map<PlayerId, Player> players) {
        players.get(PlayerId.PLAYER_1).receiveInfo(string);
        players.get(PlayerId.PLAYER_2).receiveInfo(string);
    }

}
