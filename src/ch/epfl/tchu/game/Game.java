package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.net.CookieHandler;
import java.util.*;

public final class Game {

    private final Info player1Info;
    private final Info player2Info;
    private final SortedBag<Ticket> tickets;
    SortedBag<Card> cards;



    List<PlayerId> bothIdsList = new ArrayList<>(PlayerId.ALL);

    private Game(Map<PlayerId, String> playerNames, Random rng, SortedBag<Ticket> tickets, SortedBag<Card> cards) {
        this.cards = cards;
        this.player1Info = new Info(playerNames.get(PlayerId.PLAYER_1));
        this.player2Info = new Info(playerNames.get(PlayerId.PLAYER_2));
        this.tickets = tickets;
        Collections.shuffle(bothIdsList, rng);
    }

    public void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) throws IllegalArgumentException {
        if ((players.size() < 2) || (playerNames.size() < 2)) {
            throw new IllegalArgumentException();
        }
        GameState gameState = GameState.initial(tickets, rng);

        Player player1 = players.get(PlayerId.PLAYER_1);
        Player player2 = players.get(PlayerId.PLAYER_2);

        player1.initPlayers(PlayerId.PLAYER_1, playerNames);
        player2.initPlayers(PlayerId.PLAYER_2, playerNames);

        informBothPlayers((new Info(playerNames.get(gameState.currentPlayerId())).willPlayFirst()), players);



        //TODO ici je propose la meme liste de tickets aux deux joueur, je pense que je suis censé utiliser des tickets différents, ca me parait plus logique

        SortedBag<Ticket> player1InitialTickets = SortedBag.of(gameState.topTickets(5));
        gameState = gameState.withoutTopTickets(5);
        SortedBag<Ticket> player2InitialTickets = SortedBag.of(gameState.topTickets(5));
        gameState = gameState.withoutTopTickets(5);
        player1.setInitialTicketChoice(player1InitialTickets);
        player2.setInitialTicketChoice(player2InitialTickets);

        informBothPlayerOfAGameStateChange(players, gameState.currentPlayerState(), gameState.playerState(gameState.currentPlayerId().next()), gameState);

        SortedBag<Ticket> player1Tickets = player1.chooseInitialTickets();
        SortedBag<Ticket> player2Tickets = player2.chooseInitialTickets();

        informBothPlayers(this.player1Info.keptTickets(player2Tickets.size()), players);
        informBothPlayers(this.player2Info.keptTickets(player1Tickets.size()), players);


        while (!((gameState.lastTurnBegins()) && (gameState.currentPlayerId().equals(gameState.lastPlayer())))){

            Player currentPlayer = players.get(gameState.currentPlayerId());
            Info currentPlayerInfo = new Info(playerNames.get(gameState.currentPlayerId()));
            PlayerState currentPlayerState = gameState.currentPlayerState();
            PlayerState secondPlayerState = gameState.playerState(gameState.currentPlayerId().next());
            Info secondPlayerInfo = new Info(playerNames.get(gameState.currentPlayerId().next()));

            if (gameState.lastTurnBegins()){
                informBothPlayers(secondPlayerInfo.lastTurnBegins(secondPlayerState.carCount()), players);
            }
            informBothPlayers(currentPlayerInfo.canPlay(), players);

            informBothPlayerOfAGameStateChange(players, currentPlayerState, secondPlayerState, gameState);
            switch (currentPlayer.nextTurn()) {
                case DRAW_TICKETS:
                    SortedBag<Ticket> chosenTickets = currentPlayer.chooseTickets(gameState.topTickets(3));
                    gameState = gameState.withChosenAdditionalTickets(gameState.topTickets(3), chosenTickets);
                    informBothPlayers(currentPlayerInfo.drewTickets(chosenTickets.size()), players);
                    informBothPlayerOfAGameStateChange(players, currentPlayerState, secondPlayerState, gameState);
                    break;

                case DRAW_CARDS:
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    int cardSlot1 = currentPlayer.drawSlot();
                    if (cardSlot1 <= 4){
                        informBothPlayers(currentPlayerInfo.drewVisibleCard(gameState.cardState().faceUpCards().get(cardSlot1)), players);
                        gameState = gameState.withDrawnFaceUpCard(cardSlot1);
                    }else{
                        informBothPlayers(currentPlayerInfo.drewBlindCard(), players);
                        gameState = gameState.withBlindlyDrawnCard();
                    }
                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                    informBothPlayerOfAGameStateChange(players, currentPlayerState, secondPlayerState, gameState);
                    int cardSlot2 = currentPlayer.drawSlot();
                    if (cardSlot2 <= 4){
                        gameState = gameState.withDrawnFaceUpCard(cardSlot2);
                    }else{
                        gameState = gameState.withBlindlyDrawnCard();
                    }
                    break;

                case CLAIM_ROUTE:
                    Route routeToBeClaimed = currentPlayer.claimedRoute();
                    SortedBag<Card> cardInitiallyUsedToClaim = currentPlayer.initialClaimCards();

                    if ((routeToBeClaimed.level().equals(Route.Level.OVERGROUND))) {
                        if(currentPlayerState.canClaimRoute(routeToBeClaimed)){
                            gameState = gameState.withClaimedRoute(routeToBeClaimed, cardInitiallyUsedToClaim);
                            informBothPlayers(currentPlayerInfo.claimedRoute(routeToBeClaimed, cardInitiallyUsedToClaim), players);
                        }
                        informBothPlayers(currentPlayerInfo.didNotClaimRoute(routeToBeClaimed), players);
                    }
                    if ((routeToBeClaimed.level().equals(Route.Level.UNDERGROUND))){
                        informBothPlayers(currentPlayerInfo.attemptsTunnelClaim(routeToBeClaimed, cardInitiallyUsedToClaim), players);
                        if(currentPlayerState.canClaimRoute(routeToBeClaimed)){
                            List<Card> cardsDrawn = new ArrayList<>();
                            for(int i = 0; i < 3; ++i) {
                                gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                               cardsDrawn.add(gameState.topCard());
                               gameState = gameState.withoutTopCard();
                            }
                            SortedBag<Card> cardsDrawnSorted = SortedBag.of(cardsDrawn);
                            SortedBag<Card> additionalCardsChosen = (currentPlayer.chooseAdditionalCards(currentPlayerState.possibleClaimCards(routeToBeClaimed)));
                            if ((additionalCardsChosen.size() == 0) || (additionalCardsChosen.size() != routeToBeClaimed.additionalClaimCardsCount(cardInitiallyUsedToClaim, cardsDrawnSorted))){
                                informBothPlayers(currentPlayerInfo.didNotClaimRoute(routeToBeClaimed), players);
                            }else{
                                List<Card> listOfCardsUsed = additionalCardsChosen.toList();
                                informBothPlayers(currentPlayerInfo.drewAdditionalCards(cardsDrawnSorted,listOfCardsUsed.size()), players);
                                for(Card c : cardInitiallyUsedToClaim){
                                    listOfCardsUsed.add(c);
                                }
                                SortedBag<Card> allCardsUsedToClaim = SortedBag.of(listOfCardsUsed);
                                gameState = gameState.withClaimedRoute(routeToBeClaimed, allCardsUsedToClaim);
                                informBothPlayers(currentPlayerInfo.claimedRoute(routeToBeClaimed, allCardsUsedToClaim), players);
                            }
                        }
                    }
               }
               gameState = gameState.forNextTurn();
            }

        informBothPlayerOfAGameStateChange(players, gameState.currentPlayerState(), gameState.playerState(gameState.currentPlayerId().next()), gameState);

        int currentPlayerPoints = gameState.currentPlayerState().finalPoints() ;
        int secondPlayerPoints = gameState.playerState(gameState.currentPlayerId().next()).finalPoints();
        if ((Trail.longest(gameState.currentPlayerState().routes()).length()) > Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()).length()){
            currentPlayerPoints += Constants.LONGEST_TRAIL_BONUS_POINTS;
            informBothPlayers((new Info(playerNames.get(gameState.currentPlayerId()))).getsLongestTrailBonus(Trail.longest(gameState.currentPlayerState().routes())),players);
        }
        if ((Trail.longest(gameState.currentPlayerState().routes()).length()) < Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()).length()){
            secondPlayerPoints += Constants.LONGEST_TRAIL_BONUS_POINTS;
            informBothPlayers(((new Info(playerNames.get(gameState.currentPlayerId().next()))).getsLongestTrailBonus(Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()))),players);
        }
        if ((Trail.longest(gameState.currentPlayerState().routes()).length()) == Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()).length()) {
           currentPlayerPoints += Constants.LONGEST_TRAIL_BONUS_POINTS;
           informBothPlayers((new Info(playerNames.get(gameState.currentPlayerId()))).getsLongestTrailBonus(Trail.longest(gameState.currentPlayerState().routes())),players);
           secondPlayerPoints += Constants.LONGEST_TRAIL_BONUS_POINTS;
           informBothPlayers(((new Info(playerNames.get(gameState.currentPlayerId().next()))).getsLongestTrailBonus(Trail.longest(gameState.playerState(gameState.currentPlayerId().next()).routes()))),players);
        }
        if (currentPlayerPoints > secondPlayerPoints) {
            informBothPlayers((new Info(playerNames.get(gameState.currentPlayerId()))).won(currentPlayerPoints, secondPlayerPoints), players);
            }

        List<String> listOfPlayers = new ArrayList<>();
        listOfPlayers.add(playerNames.get(PlayerId.PLAYER_1));
        listOfPlayers.add(playerNames.get(PlayerId.PLAYER_2));

        if (currentPlayerPoints == secondPlayerPoints){
            informBothPlayers(Info.draw(listOfPlayers, currentPlayerPoints), players);
        }
    }

    public void informBothPlayerOfAGameStateChange(Map<PlayerId, Player> players, PlayerState player1State, PlayerState player2State, PublicGameState newGameState){
        players.get(PlayerId.PLAYER_1).updateState(newGameState, player1State);
        players.get(PlayerId.PLAYER_2).updateState(newGameState, player2State);
    }
    public void informBothPlayers(String string, Map<PlayerId, Player> players ){
        players.get(PlayerId.PLAYER_1).receiveInfo(string);
        players.get(PlayerId.PLAYER_2).receiveInfo(string);
    }

}
