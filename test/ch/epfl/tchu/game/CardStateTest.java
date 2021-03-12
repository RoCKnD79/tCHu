package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class CardStateTest {
    Random random = new Random();
    SortedBag emptySortedBag = SortedBag.of();
    Deck<Card> deck = Deck.of(Constants.ALL_CARDS, random);
    Deck<Card> emptyDeck = Deck.of(emptySortedBag, random);


    /*
    @Test
    public void ofTest(){
      CardState cardstate =  CardState.of(deck);
      for(int i = 0; i < cardstate.getFaceUpCards().size(); ++i) {
          System.out.println("face up cards : " + cardstate.getFaceUpCards().get(i));
      }
      for(int i = 0; i < deck.topCards(5).size(); ++i){
          System.out.println(("top cards : " + deck.topCards(5).get(i)));
      }
    }
*/

    /*
    @Test
    public void withDrawnFaceUpTest(){
        CardState cardstate =  CardState.of(deck);
        System.out.println("old deck size : " + deck.size());
        System.out.println("deck top card : " + deck.topCard());
        cardstate =cardstate.withDrawnFaceUp(1);
        for(int i = 0; i < cardstate.getFaceUpCards().size(); ++i) {
            System.out.println("face up cards : " + cardstate.getFaceUpCards().get(i));
        }
        System.out.println("new deck size : " + deck.size());
        System.out.println("deck new top card " + deck.getCardsList().get(deck.size()-6));
    }

    @Test
    public void withDrawnFaceUpTestSlotOver5(){
        CardState cardstate =  CardState.of(deck);
        System.out.println("old deck size : " + deck.size());
        System.out.println("deck top card : " + deck.topCard());
        cardstate =cardstate.withDrawnFaceUp(-1);
        for(int i = 0; i < cardstate.getFaceUpCards().size(); ++i) {
            System.out.println("face up cards : " + cardstate.getFaceUpCards().get(i));
        }
        System.out.println("new deck size : " + deck.size());
        System.out.println("deck new top card " + deck.getCardsList().get(deck.size()-6));
    }*/
/*
    @Test
    public void topDeckCardTest(){
        CardState cardstate =  CardState.of(deck);
        System.out.println("Deck top card expected : " + deck.topCard());
        System.out.println("Deck top card actual : " + cardstate.topDeckCard());
        for(int i = 0; i < deck.size(); ++i) {
            System.out.println("deck card number "+ i + deck.getCardsList().get(i));
        }
    }*/
/*
    @Test
    public void withoutTopDeckCardTest(){
        CardState cardstate =  CardState.of(deck);
        for(int i = 0; i < deck.size(); ++i) {
            System.out.println("deck card number "+ i + deck.getCardsList().get(i));
        }
        System.out.println("TOP DECK CARD " +cardstate.topDeckCard());
        cardstate = cardstate.withoutTopDeckCard();
        for(int i = 0; i < cardstate.deckSize(); ++i){
            System.out.println("without top card deck card number "+ i + cardstate.getDeck().getCardsList().get(i));
        }
    }
*/
    /*
    @Test
    public void withMoreDiscardedCardsTest(){
        System.out.println("hello");
        CardState cardstate =  CardState.of(emptyDeck);
        SortedBag discardsToAdd = SortedBag.of(Constants.ALL_CARDS);
        cardstate.withMoreDiscardedCards(discardsToAdd);
    }
*/
    /*
    @Test
    public void withMoreDiscardedCardsTest(){

        CardState cardstate =  CardState.of(deck);
        SortedBag discardsToAdd = SortedBag.of(Constants.ALL_CARDS);
       cardstate = cardstate.withMoreDiscardedCards(discardsToAdd);

       for(int i = 0; i < cardstate.getDiscards().size(); ++i){
           System.out.println(cardstate.getDiscards().get(i));
    }
*/
}
