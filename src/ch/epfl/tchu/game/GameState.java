package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.*;

public final class GameState extends PublicGameState {

    private final Deck tickets;
    private final Deck cards;
    private final Map<PlayerId, PlayerState> map;

    /**
     * public constructor of PublicGameState
     *
     * @param cardState
     * @param currentPlayerId
     * @param playerState
     * @param lastPlayer
     */
    private GameState(Deck<Ticket> tickets, Deck<Card> cards, Map<PlayerId, PlayerState> map, PublicCardState cardState,
                      PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer) {
        super(tickets.size(), cardState, currentPlayerId, playerState, lastPlayer);
        this.tickets = tickets;
        this.cards = cards;
        this.map = map;
    }

    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {
        PlayerId firstPlayer = PlayerId.PLAYER_1;
        PlayerId secondPlayer = PlayerId.PLAYER_2;
        if (rng.nextInt(2) > 1) {
            firstPlayer = PlayerId.PLAYER_2;
            secondPlayer = PlayerId.PLAYER_1;
        }
        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS , rng);

        PlayerState playerState1 = PlayerState.initial(distributeInitCards(cardDeck));
        cardDeck = cardDeck.withoutTopCards(Constants.INITIAL_CARDS_COUNT);

        PlayerState playerState2 = PlayerState.initial(distributeInitCards(cardDeck));
        cardDeck = cardDeck.withoutTopCards(Constants.INITIAL_CARDS_COUNT);

        CardState cardState = CardState.of(cardDeck);

        Map<PlayerId, PlayerState> map = new EnumMap<PlayerId, PlayerState>(PlayerId.class);
        map.put(firstPlayer, playerState1);
        map.put(secondPlayer, playerState2);

        return new GameState(ticketDeck, cardDeck, map, new PublicCardState(cardState.faceUpCards(), cardState.deckSize(),
                            cardState.discardsSize()) ,firstPlayer, Map.copyOf(map), null);
    }

    @Override
    public PlayerState playerState(PlayerId playerId) {
        return map.get(playerId);
    }

    @Override
    public PlayerState currentPlayerState() {
        return map.get(currentPlayerId());
    }

    public SortedBag<Ticket> topTickets(int count) {
        if(count < 0 || count > ticketsCount()) {

        }
    }

    private static SortedBag<Card> distributeInitCards(Deck<Card> cards) {
        SortedBag<Card> cardBag = cards.topCards(Constants.INITIAL_CARDS_COUNT);
        //cards = cards.withoutTopCards(Constants.INITIAL_CARDS_COUNT);
        return cardBag;
    }

}
