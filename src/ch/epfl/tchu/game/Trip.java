package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Roman Danylovych (327830)
 */

public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     * Constructor of trips
     * @param from
     * @param to
     * @param points
     * @throws NullPointerException, if the first station or the second station is null.
     * @throws IllegalArgumentException, if points for the trip is lower or equal to 0.
     */
    public Trip(Station from, Station to, int points) throws NullPointerException, IllegalArgumentException {

        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);

        if (points <= 0) {
            throw new IllegalArgumentException("points should be > 0");
        } else {
            this.points = points;
        }
    }

    /**
     * creates a list list containing all trips
     * @param from
     * @param to
     * @param points
     * @return list, the list containing the trips.
     * @throws IllegalArgumentException, if first station, or second station, or the number of points are lower or equal to 0.
     */
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

    /**
     * method that when called returns the starting station of the trip
     * @return from, the first station
     */
    public Station from() { return from; }

    /**
     * method that when called returns the end station of the trip
     * @return to, the station at the end of the trip
     */
    public Station to() { return to; }

    /**
     * method that when called returns the number of points that are assigned to that specific trip.
     * @return points, number of points assigned to the trip
     */
    public int points() {
        return points;
    }

    /**
     * checks if connectivity was established and decides depending on connectivity how points should be returned.
     * @param connectivity
     * @return points, if connection is established, it returns the normal amount of points. If the connection isn't established, then it returns a negative amount of points
     */
    public int points(StationConnectivity connectivity) {
        if (connectivity.connected(from, to) == true) {
            return points;
        } else {
            return -points;
        }
    }

}
