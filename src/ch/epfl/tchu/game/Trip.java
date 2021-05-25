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
     * Constructor
     * @param from, station of departure
     * @param to, station of arrival
     * @param points, number of points this trip is worth
     * @throws NullPointerException, if the first station or the second station is null.
     * @throws IllegalArgumentException, if points for the trip is lower or equal to 0.
     */
    public Trip(Station from, Station to, int points) throws NullPointerException, IllegalArgumentException {
        if (points <= 0) {
            throw new IllegalArgumentException("points should be > 0");
        }

        this.points = points;
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
    }

    /**
     * creates a list list containing all trips
     * @param from, List of stations of departure
     * @param to, list of stations of arrival
     * @param points, number of points each trip is worth
     * @return list containing all trips possible with given stations
     * @throws IllegalArgumentException, if from or to is null or number of points is <= 0
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points) throws IllegalArgumentException {

        if (from == null || to == null)
            throw new IllegalArgumentException("list of stations 'from' or 'to' is null");
        if(points <= 0)
            throw new IllegalArgumentException("points <= 0");

        List<Trip> list = new ArrayList<>();
        for(Station f : from) {
            for(Station t : to) {
                list.add(new Trip(f, t, points));
            }
        }
        return list;
    }

    /**
     * @return the station of departure
     */
    public Station from() { return from; }

    /**
     * @return the station of arrival
     */
    public Station to() { return to; }

    /**
     * @return number of points the trip is worth
     */
    public int points() { return points; }

    /**
     * checks if connectivity was established and decides depending on connectivity how points should be returned.
     * @param connectivity, StationConnectivity, offers the method connected() to verify connection between 2 stations
     * @return points, if connection is established, it returns the normal amount of points.
     *                  If the connection isn't established, then it returns a negative amount of points
     */
    public int points(StationConnectivity connectivity) {
        return connectivity.connected(from, to) ? points : (-points);
    }

}
