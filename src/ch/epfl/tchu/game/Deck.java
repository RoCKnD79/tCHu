package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * @author Christopher Soriano (326354)
 */

//IMMUTABLE CLASS
    /*
    Represents a deck of any type of Cards
     */
public final class Deck<C extends Comparable<C>> {
private final List<C> cards;

    /**
     * Constructor for deck
     * @param cards, cards that form the deck
     */
    private Deck(List<C> cards) {
        this.cards = cards;
    }

    /**
     * shuffles cards entered in param and creates a new deck with these cards
     * @param cards, cards that form the deck
     * @param rng, random rng
     * @param <C>, type parameter
     * @return new Deck with cards that are shuffled
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        List<C> shuffledCards = new ArrayList<>(cards.toList());
        Collections.shuffle(shuffledCards, rng);
        return  (new Deck<>(shuffledCards));
    }

    /**
     * size method for deck, gives the size of the deck
     * @return size of deck
     */
    public int size(){
        return cards.size();
    }

    /**
     * check if deck is empty
     * @return true if deck is empty
     */
    public boolean isEmpty(){
        return (this.size() == 0);
    }

    /**
     * Method that gets the top card of the deck
     * @return the top card of the deck
     * @throws IllegalArgumentException if
     */
    public C topCard() throws IllegalArgumentException{
        if (this.isEmpty()){
            throw new IllegalArgumentException("Deck is empty");
        }
    return cards.get(size()-1);
    }

    /**
     * creates new deck that is equal to the original deck, but without the top card
     * @return new deck without top card
     */
    public Deck<C> withoutTopCard() throws IllegalArgumentException{
        if (this.isEmpty()){
            throw new IllegalArgumentException("Deck is empty");
        }
        List<C> cardsAll = new ArrayList<>(cards);
        cardsAll.remove(size()-1);
        return (new Deck<>(cardsAll));
    }

    /**
     * creates a new sorted bag with the a certain number of top cards from the deck
     * @param count, number of top cards we want
     * @return a sortedBag containing the topCards
     * @throws IllegalArgumentException if count is negative or higher than size of deck
     */
    public SortedBag<C> topCards(int count) throws IllegalArgumentException{
        if (count < 0 || count > this.size()){
            throw new IllegalArgumentException("count isn't in between 0 and size of Deck");
        }
        List<C> topCards = new ArrayList<>(cards.subList(cards.size() - count, cards.size()));


       SortedBag.Builder<C> topCardsSortedBuilder = new SortedBag.Builder<>();
       for(C c : topCards){
           topCardsSortedBuilder.add(c);
       }
       SortedBag<C> newCards = topCardsSortedBuilder.build();
       return newCards;

    }

    /**
     * creates a new deck, equal to the original but without the top cards
     * @param count, number of top cards we want to remove from the deck
     * @return a new deck without the top cars
     */
    public Deck<C> withoutTopCards(int count){
        if (count < 0 || count > this.size()){
            throw new IllegalArgumentException("count isn't in between 0 and size of Deck");
        }
        List<C> withoutTopCards = new ArrayList<>(cards.subList(0, cards.size()- count));

        return (new Deck<>(withoutTopCards));
    }


  /* public List<C> getCardsList(){
        return cards;
    }*/


}

