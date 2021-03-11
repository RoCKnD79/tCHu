package ch.epfl.tchu.game;

import java.util.List;
import java.util.Objects;

/**
 * @author Roman Danylovych (327830)
 */

//IMMUTABLE class
    /*
    represents (a part of) the state of the wagon/locomotive cards that are not in the players' hands, this includes:
    - the 5 cards facing up (=> visible cards)
    - the deck
    - the discards pile
    */

public class PublicCardState {

    private final List<Card> faceUpCards;
    private final int deckSize;
    private final int discardsSize;

    /**
     * Builds a public state of the cards
     * @param faceUpCards, the "visible" cards, so the cards facing up
     * @param deckSize, size of the deck
     * @param discardsSize, size of the discards pile
     * @throws IllegalArgumentException, if number of cards facing up is not 5 OR if size of the deck or of the discards is < 0
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize) throws IllegalArgumentException {
        if(faceUpCards.size() != 5) {
            throw new IllegalArgumentException("there must strictly be 5 face-up/visible cards.");
        } else if(deckSize < 0 || discardsSize < 0) {
            throw new IllegalArgumentException("deck size or discards size must be >= 0");
        }

        this.faceUpCards = List.copyOf(faceUpCards);
        this.deckSize = deckSize;
        this.discardsSize = discardsSize;
    }

    public int totalSize() {
        return faceUpCards.size() + deckSize + discardsSize;
    }

    /*
    returns the 5 visible cards in the form of a list containing exactly 5 elements
     */
    public List<Card> faceUpCards() {
        return List.copyOf(faceUpCards);
    }

    /**
     * @param slot, slot of the card we want to return
     * @return 1 card (at chosen slot) out of the 5 face-up cards
     * @throws IndexOutOfBoundsException if slot < 0   or   slot >= 5
     */
    public Card faceUpCard(int slot) throws IndexOutOfBoundsException {
        return faceUpCards.get(Objects.checkIndex(slot, faceUpCards.size()));
    }

    public int deckSize() {
        return deckSize;
    }

    public boolean isDeckEmpty() {
        return deckSize == 0;
    }

    public int discardsSize() {
        return discardsSize;
    }

}
