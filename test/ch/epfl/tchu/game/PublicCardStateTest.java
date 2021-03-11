package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PublicCardStateTest {


    @Test
    void faceUpCardsNotOfSize5ThrowsIllArgExc() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.GREEN);
        assertThrows(IllegalArgumentException.class, () -> {new PublicCardState(cards, 5, 7);});
    }

    @Test
    void IllArgExcIfDeckSizeNegative() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.GREEN);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        assertThrows(IllegalArgumentException.class, () -> {new PublicCardState(cards, -1, 7);});
    }

    @Test
    void IllArgExcIfDiscardsSizeNegative() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.GREEN);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        assertThrows(IllegalArgumentException.class, () -> {new PublicCardState(cards, 5, -1);});
    }

    @Test
    void negativeSlotOutOfBoundsThrowsException() {

        List<Card> cards = new ArrayList<>();
        cards.add(Card.GREEN);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        PublicCardState pcs = new PublicCardState(cards, 5, 7);

        assertThrows(IndexOutOfBoundsException.class, () -> {pcs.faceUpCard(-1);});
    }

    @Test
    void outOfBoundsSlotThrowsException() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.GREEN);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        PublicCardState pcs = new PublicCardState(cards, 5, 7);

        assertThrows(IndexOutOfBoundsException.class, () -> {pcs.faceUpCard(6);});
    }

    @Test
    void returnsCorrectDeckSize() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.GREEN);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        int expectedDeckSize = 5;
        PublicCardState pcs = new PublicCardState(cards, expectedDeckSize, 7);

        assertEquals(expectedDeckSize, pcs.deckSize());
    }

    @Test
    void returnsCorrectDiscardsSize() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.GREEN);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        int expectedDiscardsSize = 7;
        PublicCardState pcs = new PublicCardState(cards, 5, 7);

        assertEquals(expectedDiscardsSize, pcs.discardsSize());
    }

    @Test
    void trueIfDeckEmpty() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.GREEN);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        PublicCardState pcs = new PublicCardState(cards, 0, 7);

        assertEquals(true, pcs.isDeckEmpty());
    }

    @Test
    void faceUpCardReturnsCorrectCard() {
        List<Card> cards = new ArrayList<>();
        cards.add(Card.GREEN);
        cards.add(Card.BLUE);
        cards.add(Card.RED);
        cards.add(Card.BLUE);
        cards.add(Card.BLUE);
        PublicCardState pcs = new PublicCardState(cards, 5, 7);

        Card expectedCard = Card.RED;

        assertEquals(expectedCard, pcs.faceUpCard(2));
    }

}
