package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Christopher Soriano (326354)
 * @author Roman Danylovych (327830)
 */

public enum Color {

    BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE;

    private static Color[] colors = Color.values();

    public static final List<Color> ALL = List.of(colors);
    public static final int COUNT = ALL.size();
}
