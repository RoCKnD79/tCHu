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
    private Card(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();

    //public static final List<Card> CARS = List.of(Card.values()).subList(0, 8);
    public static final List<Card> CARS = List.of(Card.values()).subList(0, Card.values().length - 1);

    public static Card of(Color color) {
        for (int i = 0; i < CARS.size(); ++i) {
            if (color.equals(CARS.get(i).getColor())) {
                return CARS.get(i);
            }
        }
        return null;
    }

    public Color color() {
        if (this == LOCOMOTIVE) {
            return null;
        }
        return this.getColor();
    }

}
