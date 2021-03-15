package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Christopher Soriano (326354)
 */

//IMMUTABLE class
    /*
    represents the cards that the players can't see, this includes :
    - the discards
    - the deck

    */
public final class CardState extends PublicCardState{


    private final Deck<Card> deck;
    private final SortedBag<Card> discards;
    private final SortedBag<Card> emptyDiscards = SortedBag.of();

    /**
     * Constructor for cardState
     * @param faceUpCards, cards that are face up
     * @param discards, discard sorted list
     * @param deck, "pioche"
     */
    private CardState(List<Card> faceUpCards, SortedBag<Card> discards, Deck<Card> deck) {
        super(faceUpCards, deck.size(), discards.size());
        this.deck = deck;
        this.discards = SortedBag.of(discards);
    }

    /**
     * Creates a card state.
     * @param deck, a deck
     * @return a new cardstate with an empty discard list, a new deck, and a new face up cards
     * @throws IllegalArgumentException, if deck contains less than 5 cards
     */
    public static CardState of(Deck<Card> deck) throws IllegalArgumentException{
        if(deck.size() < 5){
            throw new IllegalArgumentException("Deck contains less than 5 cards");
        }
        SortedBag<Card> emptyDiscards = SortedBag.of();
    return new CardState(deck.topCards(5).toList(), emptyDiscards, deck.withoutTopCards(5));
    }

    /**
     * replaces the card in slot with the card on top of the deck
     * @param slot, slot of the card that will be replaced
     * @return new Card state with replaced face up card
     * @throws IndexOutOfBoundsException if the slot is higher than 5 or lower than 0
     * @throws IllegalArgumentException, if the deck is empty
     */
    public CardState withDrawnFaceUpCard(int slot) throws IndexOutOfBoundsException, IllegalArgumentException{
        if ((slot >= 5) || (slot < 0)){
            throw new IndexOutOfBoundsException("slot index is not between 0 and 5");
        }
        if (deck.isEmpty()){
            throw new IllegalArgumentException("deck is empty");
        }
        List<Card> newFaceUpCards = new ArrayList<>(this.faceUpCards());
        newFaceUpCards.set(slot, deck.topCard());
        return new CardState(newFaceUpCards,emptyDiscards, deck.withoutTopCard());
    }

    /**
     * Gives the card on top of the deck
     * @return the top card of the deck
     * @throws IllegalArgumentException, if the deck is empty
     */
    public Card topDeckCard() throws IllegalArgumentException{
        if (deck.isEmpty()){
            throw new IllegalArgumentException("deck is empty");
        }
    return deck.topCard();
    }

    /**
     * creates a cardstate but without the top card card in the deck
     * @return new card state without top card
     * @throws IllegalArgumentException, if the deck is empty
     */
    public CardState withoutTopDeckCard() throws IllegalArgumentException{
        if (deck.isEmpty()){
            throw new IllegalArgumentException("deck is empty");
        }
        return new CardState(faceUpCards(), emptyDiscards, deck.withoutTopCard());
    }

    /**
     * puts all of the discard cards in an shuffled manner and creates a new deck from these shuffled discards cards
     * @param rng, random rng
     * @return a new Cardsate with a new deck
     * @throws IllegalArgumentException, if the deck isn't empty
     */
    public CardState withDeckRecreatedFromDiscards(Random rng) throws IllegalArgumentException{

        if(!deck.isEmpty()){
            throw new IllegalArgumentException("deck is not empty");
        }
        Deck newDeckFromDiscards = Deck.of(discards, rng);
        ;
        return new CardState(faceUpCards(), emptyDiscards, newDeckFromDiscards);
    }

    /**
     *adds new cards to the discard list
     * @param additionalDiscards, discards to add to the list
     * @return a new cardstate with a new discard list
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){

        System.out.println("additional discards size : " + additionalDiscards.size());
        List<Card> discardsWithAdditional = discards.toList();

        for(Card c : additionalDiscards){
            discardsWithAdditional.add(c);
        }

        SortedBag.Builder discardSortedBuilder = new SortedBag.Builder();
        for(Card c : discardsWithAdditional){
            discardSortedBuilder.add(c);
        }
        SortedBag<Card> discardsWithAddedCards = discardSortedBuilder.build();
        System.out.println("discarded with added cards size : " +discardsWithAddedCards.size());

    return new CardState(faceUpCards(), discardsWithAddedCards, deck);

    }
/*
    public List<Card> getFaceUpCards(){
        return faceUpCards();
    }
    public Deck getDeck(){
        return deck;
    }
    public SortedBag<Card> getDiscards(){
        return discards;
    }*/
}
