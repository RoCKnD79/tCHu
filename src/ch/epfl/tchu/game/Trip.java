package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    public Trip(Station from, Station to, int points) throws NullPointerException, IllegalArgumentException {

        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);

        if (points <= 0) {
            throw new IllegalArgumentException("points should be > 0");
        } else {
            this.points = points;
        }
    }

    public static List<Trip> all(List<Station> from, List<Station> to, int points)
            throws IllegalArgumentException {

        if (from == null || to == null || points <= 0) {
            throw new IllegalArgumentException("List null or points <= 0");
        }

        List<Trip> list = new ArrayList<Trip>();
        for(Station f : from) {
            for(Station t : to) {
                list.add(new Trip(f, t, points));
            }
        }
        return list;
    }

    public Station from() { return from; }

    public Station to() { return to; }

    public int points() {
        return points;
    }

    public int points(StationConnectivity connectivity) {
        if (connectivity.connected(from, to) == true) {
            return points;
        } else {
            return -points;
        }
    }

}
