package ch.epfl.tchu.game;

import java.util.List;

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


    private Color color;

    /**
     * constructor for Cards
     * @param c
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

    //public static final List<Card> CARS = List.of(Card.values()).subList(0, 8);
    public static final List<Card> CARS = List.of(Card.values()).subList(0, Card.values().length - 1);

    /**
     * method that gets in list CARS a card of same color.
     * @param color
     * @return null or Card, a card of the same color as the card in param
     */
    public static Card of(Color color) {
        for (int i = 0; i < CARS.size(); ++i) {
            if (color.equals(CARS.get(i).getColor())) {
                return CARS.get(i);
            }
        }
        return null;
    }

    /**
     * method that finds the color of the card created.
     * @return color of the card
     */
    public Color color() {
        if (this == LOCOMOTIVE) {
            return null;
        }
        return this.getColor();
    }

}
