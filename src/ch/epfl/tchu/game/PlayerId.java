package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Roman Danylovych (327830)
 */
public enum PlayerId {
    PLAYER_1,
    PLAYER_2;

    public static final List<PlayerId> ALL = List.of(PlayerId.values());
    public static final int COUNT = ALL.size();

    /**
     * @return PLAYER_2 if called through PLAYER_1 and PLAYER_1 otherwise
     */
    public PlayerId next() {
        if(this.equals(PLAYER_1)) {
            return PLAYER_2;
        }
        return PLAYER_1;
    }

}
