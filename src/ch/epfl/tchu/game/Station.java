package ch.epfl.tchu.game;

public final class Station {

    private final int ID;
    private final String NAME;

    public Station(int id, String name) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("only positive id are accepted");
        } else {
            ID = id;
        }
        NAME = name;
    }

    public int id() {
        return ID;
    }

    public String name() {
        return NAME;
    }

    @Override
    public String toString() {
        //return "Gare de " + NAME;
        return NAME;
    }

}
