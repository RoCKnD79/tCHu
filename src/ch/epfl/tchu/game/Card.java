package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Christopher Soriano (326354)
 * @author Roman Danylovych (327830)
 */

public enum Card {

    BLACK(Color.BLACK),
    VIOLET(Color.VIOLET),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE),
    RED(Color.RED),
    WHITE(Color.WHITE),
    LOCOMOTIVE(null);


    private final Color color;

    /**
     * constructor for Cards
     * @param c, color to assign to card
     */
    private Card(Color c) {
        color = c;
    }

    /**
     * method that returns the Color of the card
     * @return color, color of the card
     */
    public Color getColor() {
        return color;
    }

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();

    public static final List<Card> CARS = List.of(Card.values()).subList(0, Card.values().length - 1);

    /**
     * method that gets in list CARS a card of same color.
     * @param color, color of the card we're looking for
     * @return null or Card, a card of the same color as the card in param
     */
    public static Card of(Color color) {
        for (Card card : CARS) {
            if (color.equals(card.getColor())) {
                return card;
            }
        }
        return null;
    }

    /**
     * method that returns the corresponding color to card.
     * @return color of the card
     */
    public Color color() {
        return this.getColor();
    }

}
