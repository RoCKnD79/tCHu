package ch.epfl.tchu.game;

/**
 * @author Christopher Soriano (326354)
 */

public interface StationConnectivity {
    /**
     * when called, tells if stations are connected of not.
     *
     * @param s1, first station
     * @param s2, second station
     * @return true if the station are connected, false if the stations are not connected.
     */
    boolean connected(Station s1, Station s2);

}
