package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

public interface Player {

    enum TurnKind{
        DRAW_TICKETS, DRAW_CARDS, CLAIM_ROUTE;

        private static final TurnKind[] turnKinds = TurnKind.values();
        private static final List<TurnKind> ALL = List.of(turnKinds);
    }

    /**
     *method that will be called in beginning of game to communicate to player his own id, and the names of the other players
     * @param ownId, id of the player
     * @param playerNames, map linking ids to names
     */
    public abstract void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     *called when information is given to players
     * @param info, the information given
     */
    public abstract void receiveInfo(String info);

    /**
     *informs player of his new state
     * @param newState, new state of the player
     * @param ownState, own state of the palyer
     */
    public abstract void updateState(PublicGameState newState, PlayerState ownState);

    /**
     *communicates to player the five tickets that were distributed to him at the beginning of the game
     * @param tickets, sorted list of tickets disteibuted
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * called at beginning of game to know which tickets the player is willing to keep
     * @return sorted list of the tickets the player wants to keep
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * called at beginning of each turn to know which kind of turn the player wants to play
     * @return TurnKind : kind of turn (DRAW_TICKETS, DRAW_CARDS, CLAIM_ROUTE)
     */
    TurnKind nextTurn();

    /**
     *
     * @param options
     * @return
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);
}
