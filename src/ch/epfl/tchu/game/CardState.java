package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class CardState extends PublicCardState{


    private final Deck<Card> deck;
    private final SortedBag<Card> discards;


    private CardState(List<Card> faceUpCards, SortedBag<Card> discards, Deck<Card> deck) throws IllegalArgumentException {
        super(faceUpCards, deck.size(), discards.size());
        this.deck = deck;
        this.discards = discards;
    }

    public CardState of(Deck<Card> deck) throws IllegalArgumentException{
        if(deck.size() < 5){
            throw new IllegalArgumentException("Deck contains less than 5 cards");
        }

    return new CardState(deck.topCards(5).toList(),null, deck.withoutTopCards(5));

    }

    public CardState withDrawnFaceUp(int slot) throws IndexOutOfBoundsException, IllegalArgumentException{
        if ((slot >= 5) || (slot < 0)){
            throw new IndexOutOfBoundsException("slot index is not between 0 and 5");
        }
        if (deck.isEmpty()){
            throw new IllegalArgumentException("deck is empty");
        }
        List<Card> newFaceUpCards = new ArrayList<>(this.faceUpCards());
        newFaceUpCards.set(slot, deck.topCard());
        return new CardState(deck.topCards(5).toList(),null, deck.withoutTopCard());
    }

    public Card topDeckCard() throws IllegalArgumentException{
        if (deck.isEmpty() == true){
            throw new IllegalArgumentException("deck is empty");
        }
    return deck.topCard();
    }

    public CardState withoutTopDeckCard() throws IllegalArgumentException{
        if (deck.isEmpty() == true){
            throw new IllegalArgumentException("deck is empty");
        }
        return new CardState(deck.topCards(5).toList(), null, deck.withoutTopCard());
    }

    public CardState withDeckRecreatedFromDiscards(Random rng) throws IllegalArgumentException{
        if (deck.isEmpty() == true){
            throw new IllegalArgumentException("deck is empty");
        }
        Collections.shuffle(discards.toList(), rng);
        return new CardState(deck.topCards(5).toList(), discards, deck);
    }

    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
    List<Card> discardsWithAdditional = discards.toList();
    for(Card c : additionalDiscards){
        discardsWithAdditional.add(c);
    }
        SortedBag.Builder discardSortedBuilder = new SortedBag.Builder();
        for(Card c : discardsWithAdditional){
            discardSortedBuilder.add(c);
        }
        SortedBag<Card> discardsSorted = discardSortedBuilder.build();
    return new CardState(deck.topCards(5).toList(), discardsSorted, deck);

    }
}
