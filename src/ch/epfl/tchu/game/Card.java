package ch.epfl.tchu.game;

import java.util.List;

public enum Card {

    BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE, LOCOMOTIVE;

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();

    //public static final List<Card> CARS = List.of(Card.values()).subList(0, 8);
    public static final List<Card> CARS = List.of(Card.values()).subList(0, Card.values().length - 1);

    public static Card of(Color color) {
        for(int i = 0; i < CARS.size(); ++i) {
            System.out.println("color: " + color.toString());
            if (color.toString().equals(CARS.get(i).toString())) {
                return CARS.get(i);
            }
        }
        return null;
    }

}
