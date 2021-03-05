package ch.epfl.tchu.game;

public final class Station {

    private final int ID;
    private final String NAME;

    /**
     * constructor of a station
     * @param id
     * @param name
     * @throws IllegalArgumentException if the id is not positive.
     */
    public Station(int id, String name) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("only positive id are accepted");
        } else {
            ID = id;
        }
        NAME = name;
    }

    /**
     * when called, gives the id of the station
     * @return id of station
     */
    public int id() {
        return ID;
    }

    /**
     * when called, gives the name of the station
     * @return name of station
     */
    public String name() {
        return NAME;
    }

    @Override
    public String toString() {
        //return "Gare de " + NAME;
        return NAME;
    }

}
