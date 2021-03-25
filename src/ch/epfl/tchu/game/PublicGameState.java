package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PublicGameState {

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final PlayerId lastPlayer;
    private final Map<PlayerId, PublicPlayerState> playerState;


    /**
     * public constructor of PublicGameState
     * @param ticketsCount, number of tickets in ticket deck
     * @param cardState, public state of the cards of the game
     * @param currentPlayerId, id of current player
     * @param playerState, public states of each player
     * @param lastPlayer, id of the player that'll play last in the game
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId,
                           Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {

        if (ticketsCount < 0) {
            throw new IllegalArgumentException("ticketsCount must be >= 0");
        }
        if (playerState.size() != 2) {
            throw new IllegalArgumentException("playerState must contain 2 key/value pairs");
        }

        this.ticketsCount = ticketsCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = playerState;
        this.lastPlayer = lastPlayer;
    }

    /**
     * @return number of tickets still available
     */
    public int ticketsCount() {
        return ticketsCount;
    }

    /**
     * @return true if number of tickets available is > 0
     */
    public boolean canDrawTickets() {
        return !(ticketsCount <= 0);
    }

    /**
     * @return, PublicCardState of the game
     */
    public PublicCardState cardState() {
        return new PublicCardState(cardState.faceUpCards(), cardState.deckSize(), cardState.discardsSize());
    }

    /**
     * @return true if there are at least 5 cards available when adding up the number of cards in deck and discard
     */
    public boolean canDrawCards() {
        return (cardState.deckSize() + cardState.discardsSize()) >= 5;
    }

    /**
     * @return Id of the player calling the method
     */
    public PlayerId currentPlayerId() {
        return currentPlayerId;
    }

    /**
     * @param playerId, id of the player of whom we'd like the PublicPlayerState
     * @return PublicPlayerState of the player whose Id is playerId
     */
    public PublicPlayerState playerState(PlayerId playerId) {
        PublicPlayerState temp = playerState.get(playerId);
        return new PublicPlayerState(temp.ticketCount(), temp.cardCount(), temp.routes());
    }

    /**
     * @return PublicPlayerState of current player
     */
    public PublicPlayerState currentPlayerState() {
        return playerState(currentPlayerId);
    }

    /**
     * @return a list with all the routes that have been claimed during the game
     */
    public List<Route> claimedRoutes() {
        List<Route> list = new ArrayList<>(playerState(currentPlayerId).routes());
        list.addAll(playerState(currentPlayerId.next()).routes());
        return list;
    }

    /**
     * @return lastPlayer
     */
    public PlayerId lastPlayer() {
        return lastPlayer == null ? null : lastPlayer;
    }

}
