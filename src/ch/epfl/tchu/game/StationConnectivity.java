package ch.epfl.tchu.game;

public interface StationConnectivity {
    /**
     * when called, tells if stations are connected of not.
     * @param s1
     * @param s2
     * @return true if the station are connected, false if the stations are not connected.
     */
    public abstract boolean connected(Station s1, Station s2);

}
