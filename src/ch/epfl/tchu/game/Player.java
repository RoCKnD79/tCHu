package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

/**
 * @author Christopher Soriano (326354)
 */

public interface Player {

    /**
     * @author Christopher Soriano (326354)
     */
    enum TurnKind {
        DRAW_TICKETS, DRAW_CARDS, CLAIM_ROUTE;

        private static final TurnKind[] turnKinds = TurnKind.values();
        public static final List<TurnKind> ALL = List.of(turnKinds);
    }

    /**
     * method  that will be called in beginning of game to communicate to player his own id, and the names of the other players
     *
     * @param ownId,       id of the player
     * @param playerNames, map linking ids to names
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * called when information is given to players
     *
     * @param info, the information given
     */
    void receiveInfo(String info);

    /**
     * informs player of his new state
     *
     * @param newState, new state of the player
     * @param ownState, own state of the palyer
     */
    void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * communicates to player the five tickets that were distributed to him at the beginning of the game
     *
     * @param tickets, sorted list of tickets disteibuted
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * called at beginning of game to know which tickets the player is willing to keep
     *
     * @return sorted list of the tickets the player wants to keep
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * called at beginning of each turn to know which kind of turn the player wants to play
     *
     * @return TurnKind : kind of turn (DRAW_TICKETS, DRAW_CARDS, CLAIM_ROUTE)
     */
    TurnKind nextTurn();

    /**
     * called when a player chooses to draw new tickets in game
     *
     * @param options, sorted bag of the new tickets
     * @return the tickets that the player wants to keep
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * called when a player has drawn a new card
     *
     * @return a number between 0 and 4 if the card that is drawn is from the slots, of between Constants.DECK_SLOT
     */
    int drawSlot();

    /**
     * called when a player decides to try and take control of a route, to know which route he wants
     *
     * @return the route that the player wants to take control of.
     */
    Route claimedRoute();

    /**
     * called when a player tries to take control of a road to know which cards he is using initially
     *
     * @return a sorted list of the cards he wants to use to claim the route
     */
    SortedBag<Card> initialClaimCards();

    /**
     * called when a player wants to take control of a tunnel, gets which additional cards he wants to use to take control of that tunnel
     *
     * @param options, list of the different options of cards he can use
     * @return the additional cards he wants to use, if empty, it means the player does not want (or can't) to take control of the tunnel
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
