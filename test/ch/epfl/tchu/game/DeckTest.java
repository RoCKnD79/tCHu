package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;
import java.util.Random;

public class DeckTest {
    Random random = new Random();
    Deck deck = Deck.of(Constants.ALL_CARDS, random);
/*
    @Test
   public void simpleDeckTestWithSortedBag(){
        Random random = new Random();
        Deck deck = Deck.of(Constants.ALL_CARDS, random);
        System.out.println(deck.size());
        System.out.println(deck.topCard());
        System.out.println();
        System.out.println(deck);
        System.out.println(Constants.ALL_CARDS.size());

    }
*/
/*
    //Fonctionne
    @Test
    public void topCardTest(){
        Random random = new Random();
        Deck deck = Deck.of(Constants.ALL_CARDS, random);
        System.out.println(deck.topCard() + " : top card");
        System.out.println(deck.getCardsList().get(109) + " : last card");
    }*/
/*
    @Test
    public void withoutTopCardTest() {
        Random random = new Random();
        Deck deck = Deck.of(Constants.ALL_CARDS, random);
        System.out.println(deck.topCard() + " : top card");
        for (int i = 0; i < deck.withoutTopCard().size(); ++i) {
            System.out.println("Carte numéro : " + i + deck.withoutTopCard().getCardsList().get(i));
        }
        for (int i = 0; i < deck.size(); ++i) {
            System.out.println("Carte numéro : " + i + deck.getCardsList().get(i));
        }
*/

        //Fonctionne
    /*
    @Test§
    public void emptyDeckTest(){
        //SortedBag<Card> emptySortedBag = null;
        Random random = new Random();
        //Deck deck = Deck.of(emptySortedBag, random);
        SortedBag.Builder topCardsSortedBuilder = new SortedBag.Builder();

        SortedBag<Card> emptySortedBag = topCardsSortedBuilder.build();
        Deck deck = Deck.of(emptySortedBag, random);
        System.out.println(deck.isEmpty());
    }
    */
/*
    @Test
    public void topCardsTest(){
        Random random = new Random();
        Deck deck = Deck.of(Constants.ALL_CARDS, random);
        for(int i = 0; i < deck.topCards(5).size(); ++i){
            System.out.println("Top card " +deck.topCards(-1).get(i));
            System.out.println("Verfication : " +deck.getCardsList().get(109-i));
        }
*/


        // }
/*
    @Test
    public void withoutTopCardsTest(){
        Random random = new Random();
        Deck deck = Deck.of(Constants.ALL_CARDS, random);
        System.out.println("liste complète");
        for(int i = 0; i < deck.size(); ++i) {
            System.out.println("numéro : " + i + deck.getCardsList().get(i));
        }
        System.out.println("liste without top cards :");
        for(int i = 0; i < deck.withoutTopCards(5).size(); ++i)
        System.out.println("numéro : " + i +deck.withoutTopCards(5).getCardsList().get(i));
    }

    @Test
    public void withoutTopCardsTestCountUnder0(){
        Random random = new Random();
        Deck deck = Deck.of(Constants.ALL_CARDS, random);
        System.out.println("liste complète");
        for(int i = 0; i < deck.size(); ++i) {
            System.out.println("numéro : " + i + deck.getCardsList().get(i));
        }
        System.out.println("liste without top cards :");
        for(int i = 0; i < deck.withoutTopCards(-1).size(); ++i)
            System.out.println("numéro : " + i +deck.withoutTopCards(-1).getCardsList().get(i));
    }

    @Test
    public void withoutTopCardsTestCountOverDeckSize(){
        Random random = new Random();
        Deck deck = Deck.of(Constants.ALL_CARDS, random);
        System.out.println("liste complète");
        for(int i = 0; i < deck.size(); ++i) {
            System.out.println("numéro : " + i + deck.getCardsList().get(i));
        }
        System.out.println("liste without top cards :");
        for(int i = 0; i < deck.withoutTopCards(deck.size() + 1).size(); ++i)
            System.out.println("numéro : " + i +deck.withoutTopCards(deck.size() + 1).getCardsList().get(i));
    }
*/

    //}
}
