package ch.epfl.tchu.game;

import java.util.List;

public enum Card {

    BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE, LOCOMOTIVE;

    public static final List<Card> ALL = List.of(Card.values());
    public static final int COUNT = ALL.size();

    //public static final List<Card> CARS = List.of(Card.values()).subList(0, 8);
    public static final List<Card> CARS = List.of(Card.values()).subList(0, Card.values().length - 1);

    /*
    CE BLOC NE FAIT PAS CE QU'ON VEUT
     */
    public static Card of(Color color) {
        for(int i = 0; i < CARS.size(); ++i) {
            //TODO à voir si .equals() fonctionne pour comparer
            //FollowUp, equals ne fonctionne pas wola
            if (color.equals(CARS.get(i))) {
                return CARS.get(i);
            }
        }
        return null;
    }

}
