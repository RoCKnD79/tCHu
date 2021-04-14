package ch.epfl.tchu.game;

/**
 * @author Christopher Soriano (326354)
 * @author Roman Danylovych (327830)
 */

public final class Station {

    private final int ID;
    private final String NAME;

    /**
     * constructor of a station
     *
     * @param id,   id of station
     * @param name, name of station
     * @throws IllegalArgumentException if the id is not positive.
     */
    public Station(int id, String name) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("only positive id are accepted");
        }

        ID = id;
        NAME = name;
    }

    /**
     * when called, gives the id of the station
     *
     * @return id of station
     */
    public int id() {
        return ID;
    }

    /**
     * when called, gives the name of the station
     *
     * @return name of station
     */
    public String name() {
        return NAME;
    }

    @Override
    public String toString() {
        return NAME;
    }

}
