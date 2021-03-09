package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class Deck<C extends Comparable<C>> {
//Jai lu sur piazb que un deck ne contient pas ses cartes dans une SortedBag étant donné que les cartes ne doivent pas être triées.
private final List<C> cardsSorted;
Random rng = new Random();



//private final List<C> shuffledCards;



    private Deck(List<C> cards) { //(SortedBag<C> cards : j'hésite entre les deux ){


        this.cardsSorted = cards;
    }

    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){
        List<C> shuffledCards = new ArrayList<>(cards.toList());
        Collections.shuffle(shuffledCards);


        return  (new Deck(shuffledCards));
    }

    public int size(){
        return cardsSorted.size();
    }

    //je suis pas sûr la parce que est ce que lorsqu'un deck est vide il est null ?
    public boolean isEmpty(){
        if (this.equals(null)){
            return true;
        }
        return false;
    }

    public C topCard() throws IllegalArgumentException{
    return cardsSorted.get(size());
    }

    public Deck<C> withoutTopCard(){
        List<C> cardsAll = new ArrayList<>(cardsSorted);
        cardsAll.remove(size());
        return (new Deck(cardsAll));
    }

    public SortedBag<C> topCards(int count) throws IllegalArgumentException{
        if (count < 0 || count > this.size()){
            throw new IllegalArgumentException("count isn't in between 0 and size of Deck");
        }
        List<C> topCards = new ArrayList<>(cardsSorted.subList(cardsSorted.size() - count, cardsSorted.size()));
       SortedBag.Builder topCardsSortedBuilder = new SortedBag.Builder();
       for(C c : topCards){
           topCardsSortedBuilder.add(c);
       }
       SortedBag<C> cards = topCardsSortedBuilder.build();
       return cards;

    }

    public Deck<C> withoutTopCards(int count){
        if (count < 0 || count > this.size()){
            throw new IllegalArgumentException("count isn't in between 0 and size of Deck");
        }
        List<C> withoutTopCards = new ArrayList<>(cardsSorted.subList(0, cardsSorted.size()- count));

        return (new Deck(withoutTopCards));


    }
}

