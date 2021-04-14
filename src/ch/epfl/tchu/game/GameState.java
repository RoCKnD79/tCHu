package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * @author Roman Danylovych (327830)
 */
public final class GameState extends PublicGameState {

    private final Deck<Ticket> tickets;
    private final Map<PlayerId, PlayerState> map;
    private final CardState allCardState;

    /**
     *
     * @param tickets, SortedBag of the tickets in pile
     * @param allCardState, CardState of the game
     * @param map, map linking each PlayerId to its PlayerState
     * @param currentPlayerId, id of the first player to play
     * @param lastPlayer, lastPlayer to play in the game (is null until lastTurnBegins)
     */
    private GameState(Deck<Ticket> tickets, CardState allCardState, Map<PlayerId, PlayerState> map,
                      PlayerId currentPlayerId, PlayerId lastPlayer) {

        super(tickets.size(), new PublicCardState(allCardState.faceUpCards(),
                allCardState.deckSize(), allCardState.discardsSize()),
                currentPlayerId, Map.copyOf(map), lastPlayer);

        this.tickets = tickets;
        this.allCardState = allCardState;
        this.map = map;
    }

    /**
     * initializes the game => designates first player, creates a playerState for each player,
     *                          distributes 4 cards to each player, initializes deck of cards and ticket deck,
     *                          prepares the 5 face up cards, creates initial CardState of the whole game
     *
     * @param tickets, the tickets we start the game with
     * @param rng, used to designate first and second player and to shuffle cards
     * @return new GameState
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {
        PlayerId firstPlayer = PlayerId.PLAYER_1;

        //decides at random who will be the first to play
        if(rng.nextInt(2) == 1) {
            firstPlayer = PlayerId.PLAYER_2;
        }
        PlayerId secondPlayer = firstPlayer.next();

        //initialise deck of tickets and deck of cards
        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS , rng);

        //initialise the playerState of first player
        PlayerState playerState1 = PlayerState.initial(distributeInitCards(cardDeck));
        cardDeck = cardDeck.withoutTopCards(Constants.INITIAL_CARDS_COUNT);

        //initialise the playerState of next player
        PlayerState playerState2 = PlayerState.initial(distributeInitCards(cardDeck));
        cardDeck = cardDeck.withoutTopCards(Constants.INITIAL_CARDS_COUNT);

        //faceUpCards are automatically removed from Deck when calling of()
        CardState cardState = CardState.of(cardDeck);

        //initialise map connecting each PlayerId with a corresponding PlayerState
        Map<PlayerId, PlayerState> map = new EnumMap<>(PlayerId.class);
        map.put(firstPlayer, playerState1);
        map.put(secondPlayer, playerState2);

        return new GameState(ticketDeck, cardState, map, firstPlayer, null);
    }

    /**
     * used in initial()
     * @param cards, deck of cards
     * @return five cards from the top of the deck
     */
    private static SortedBag<Card> distributeInitCards(Deck<Card> cards) {
        return cards.topCards(Constants.INITIAL_CARDS_COUNT);
    }


    //-----------------------FIRST GROUP OF METHODS: CONCERNING CARDS AND TICKETS-----------------------

    /**
     * @param playerId, id of the player of whom we'd like the PlayerState
     * @return PlayerState of playerId
     */
    @Override
    public PlayerState playerState(PlayerId playerId) {
        return map.get(playerId);
    }

    /**
     * @return playerState of current player
     */
    @Override
    public PlayerState currentPlayerState() {
        return playerState(currentPlayerId());
    }

    /**
     * @param count, number of tickets we'd like to select from top of pile
     * @return SortedBag of the tickets selected from top of pile
     * @throws IllegalArgumentException, if number of tickets n is not:  0 <= n <= ticketsCount()
     */
    public SortedBag<Ticket> topTickets(int count) {
        if(count < 0 || count > ticketsCount()) {
            throw new IllegalArgumentException("number of tickets must be >= 0 and <= " + ticketsCount());
        }
        return tickets.topCards(count);
    }

    /**
     *
     * @param count, number of tickets we'd like to draw from top of pile
     * @return new GameState without the number [count] of tickets on top of pile
     * @throws IllegalArgumentException if count doesn't belong to [0, ticketsCount]
     */
    public GameState withoutTopTickets(int count) {
        if(count < 0 || count > ticketsCount()) {
            throw new IllegalArgumentException("number passed in argument must be 0 <= n <= ticketsCount");
        }
        return new GameState(tickets.withoutTopCards(count), allCardState, map, currentPlayerId(), lastPlayer());
    }

    /**
     * @return returns the card on top of deck without removing it from the deck
     * @throws IllegalArgumentException if deck is empty
     */
    public Card topCard() {
        if(cardState().isDeckEmpty()) {
            throw new IllegalArgumentException("Deck is empty");
        }
        return allCardState.topDeckCard();
    }

    /**
     * @return new GameState without the card at the top of the deck
     * @throws IllegalArgumentException if deck is empty
     */
    public GameState withoutTopCard() {
        if(allCardState.deckSize() == 0) {
            throw new IllegalArgumentException("Deck is empty");
        }
        return new GameState(tickets, allCardState.withoutTopDeckCard(), map, currentPlayerId(), lastPlayer());
    }

    /**
     * @param discardedCards, cards to discard
     * @return new GameState with discardedCards added to the discards pile
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {
        return new GameState(tickets, allCardState.withMoreDiscardedCards(discardedCards), map,
                currentPlayerId(), lastPlayer());
    }

    /**
     * @param rng, used to shuffle discards deck
     * @return new GameState with a new deck recreated with cards from the discards IF deck is empty
     *          otherwise, return just current instance (this)
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng) {
        return allCardState.isDeckEmpty()
                ? new GameState(tickets, allCardState.withDeckRecreatedFromDiscards(rng), map,
                  currentPlayerId(), lastPlayer())
                : this;
    }


    //-----------------------SECOND GROUP OF METHODS: CHANGE OF STATE DUE TO PLAYER'S ACTIONS-----------------------

    /**
     *
     * @param playerId, player to which we add the tickets
     * @param chosenTickets, tickets we would like to add to playerId
     * @return new GameState with playerId's playerState updated by adding the chosenTickets to his hand
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets) {

        if(map.get(playerId).ticketCount() > 0) {
            throw new IllegalArgumentException("player already has ticket(s)");
        }

        Map<PlayerId, PlayerState> temp = new EnumMap<>(PlayerId.class);
        temp.put(playerId, map.get(playerId).withAddedTickets(chosenTickets));
        temp.put(playerId.next(), map.get(playerId.next()));

        return new GameState(tickets, allCardState, temp,
                currentPlayerId(), lastPlayer());
    }

    /**
     *
     * @param drawnTickets, the 3 tickets the player draws
     * @param chosenTickets, tickets the player chose from the 3 drawn ones
     * @return new GameState with modified PlayerState (which will update info about chosen tickets) of currentPlayer
     * @throws IllegalArgumentException if player selects tickets that are not part of the drawn tickets
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets) {
        if(!drawnTickets.contains(chosenTickets)) {
            throw new IllegalArgumentException("some or all chosen tickets have not been chosen from drawn cards");
        }
        Map<PlayerId, PlayerState> temp = new EnumMap<>(PlayerId.class);
        temp.put(currentPlayerId(), map.get(currentPlayerId()).withAddedTickets(chosenTickets));
        temp.put(currentPlayerId().next(), map.get(currentPlayerId().next()));

        return new GameState(tickets.withoutTopCards(drawnTickets.size()), allCardState, temp,
                currentPlayerId(), lastPlayer());
    }

    /**
     * @param slot, the position of the faceUpCard the player chooses
     * @return new GameState where the card has been added to the player's state
     * @throws IllegalArgumentException if player cannot draw cards => canDrawCards() is false => size of deck + size of discards >= 5
     */
    public GameState withDrawnFaceUpCard(int slot) {
        if(!canDrawCards()) {
            throw new IllegalArgumentException("there are not 5 cards in total between deck and discards");
        }

        Map<PlayerId, PlayerState> temp = new EnumMap<>(PlayerId.class);
        temp.put(currentPlayerId(), map.get(currentPlayerId()).withAddedCard(cardState().faceUpCard(slot)));
        temp.put(currentPlayerId().next(), map.get(currentPlayerId().next()));

        return new GameState(tickets, allCardState.withDrawnFaceUpCard(slot), temp, currentPlayerId(), lastPlayer());
    }

    /**
     * @return new GameState with one card less from top of deck (which has been drawn)
     */
    public GameState withBlindlyDrawnCard() {

        if(!canDrawCards()) {
            throw new IllegalArgumentException("there are not 5 cards in total between deck and discards");
        }

        Map<PlayerId, PlayerState> temp = new EnumMap<>(PlayerId.class);
        temp.put(currentPlayerId(), map.get(currentPlayerId()).withAddedCard(this.topCard()));
        temp.put(currentPlayerId().next(), map.get(currentPlayerId().next()));

        return new GameState(tickets, allCardState.withoutTopDeckCard(), temp, currentPlayerId(), lastPlayer());
    }

    /**
     * @param route, claimed route
     * @param cards, cards used to claim route
     * @return a new GameState with modified PlayerState of the person who claimed the route
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards) {

        Map<PlayerId, PlayerState> temp = new EnumMap<>(PlayerId.class);
        temp.put(currentPlayerId(), map.get(currentPlayerId()).withClaimedRoute(route, cards));
        temp.put(currentPlayerId().next(), map.get(currentPlayerId().next()));

        return new GameState(tickets, allCardState.withMoreDiscardedCards(cards), temp, currentPlayerId(), lastPlayer());
    }


    //-----------------------THIRD GROUP OF METHODS: CHANGE OF STATE DUE TO PLAYER'S ACTIONS-----------------------

    /**
     * @return true if no last player has been designated yet and current player has <= 2 cars
     */
    public boolean lastTurnBegins() {
        return lastPlayer() == null && map.get(currentPlayerId()).carCount() <= 2;
    }

    /**
     * ends the round of current player and s last playeinforms us that the next player becomes the currentplayer
     *      * if current player has <= 2 cars then he is designated ar and will play last in the next turn
     * (which will be the last round of the game)
     * @return new GameState prepared for next turn, changing current player and possibly selecting a lastPlayer
     */
    public GameState forNextTurn() {
        PlayerId lastPlayer = lastPlayer();
        if(lastTurnBegins()) {
            lastPlayer = currentPlayerId();
        }
        return new GameState(tickets, allCardState, map, currentPlayerId().next(), lastPlayer);
    }

}
