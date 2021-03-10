package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Roman Danylovych (327830)
 */

//IMMUTABLE class
/*
représente (une partie de) l'état des cartes wagon/locomotive qui ne sont pas en main des joueurs, à savoir:
- les 5 cartes disposées face visible à côté du plateau,
- la pioche,
- la défausse.
 */
public class PublicCardState {

    private final List<Card> faceUpCards;
    private final int deckSize;
    private final int discardsSize;

    /**
     * Builds a public state of the cards
     * @param faceUpCards
     * @param deckSize
     * @param discardsSize
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize) throws IllegalArgumentException {
        if(faceUpCards.size() != 5) {
            throw new IllegalArgumentException("there must strictly be 5 face-up/visible cards.");
        } else if(deckSize < 0 || discardsSize < 0) {
            throw new IllegalArgumentException("deck size or discards size must be >= 0");
        }
        //TODO n'y aura-t-il pas un problème de référence ici ? car on fait List = List ?
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
     * @param slot
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
        if(deckSize == 0) {
            return true;
        }
        return false;
    }

    public int discardsSize() {
        return discardsSize;
    }

}
