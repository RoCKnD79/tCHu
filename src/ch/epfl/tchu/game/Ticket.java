package ch.epfl.tchu.game;


import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

/**
 * @author Christopher Soriano (326354)
 * @author Roman Danylovych (327830)
 */

public final class Ticket implements Comparable<Ticket> {

    private final List<Trip> trips;
    private final String tripInfo;

    /**
     * First constructor for ticket class, verifies that list of trips isn't null and that all trips have same origin.
     * @param trips, trips we'd like to have on our ticket
     * @throws IllegalArgumentException, if list of trips is null or empty
     */
    public Ticket(List<Trip> trips) throws IllegalArgumentException {
        if (trips == null || trips.size() == 0) {
            throw new IllegalArgumentException("List can't be null or empty");
        }
        Station fromStation = trips.get(0).from();
        for (Trip trip : trips) {
            if (!fromStation.name().equals(trip.from().name())) {
                throw new IllegalArgumentException("Not all trips come from same Station");
            }
        }
        this.trips = List.copyOf(trips);
        tripInfo = computeText(trips);
    }

    /**Second constructor for ticket class, creates a unique trip.
      @param from, station of departure
      @param to, station of arrival
      @param points, points offered if ticket is accomplished
     */
    public Ticket(Station from, Station to, int points){
        this(List.of(new Trip(from, to, points)));
    }


    /**
     * returns number of points for the specific ticket and connectivity
     * @param connectivity, StationConnectivity, offers the method connected() to verify connection between 2 stations
     * @return max points if BEST connection was achieved,
     *         min points if NO connection was achieved,
     *         or a specific amount of points if a PARTIAL connection was established.
     */
    public int points(StationConnectivity connectivity) {
        boolean max = false;
        int maxPts = trips.get(0).points();
        int minPts = trips.get(0).points();
        for (Trip trip : trips) {
            if (connectivity.connected(trip.from(), trip.to())) {
                if (maxPts < trip.points()) {
                    maxPts = trip.points();
                }
                max = true;
            } else {
                if (minPts > trip.points()) {
                    minPts = trip.points();
                }

            }
        }

        if (!max){
            return -minPts;
        }
        return maxPts;
    }

    /**
     * method that when called returns the computed Text with the tickets information.
     * @return tripInfo, a variable containing the textual representation of the trip written on the ticket
     */
    public String text() { return tripInfo; }

    @Override
    public String toString() { return tripInfo; }

    /**
     * Computes Text for the ticket created, specific to the requirements.
     * @param trips, list of trips we'd like to represent in a textual form
     * @return textual representation of the trip written on the ticket
     */
    private static String computeText(List<Trip> trips) {
        TreeSet<String> text = new TreeSet<>();

        trips.forEach(trip -> text.add(new StringBuilder()
                .append(trip.to().name())
                .append(" (")
                .append(trip.points())
                .append(")")
                .toString()));

        String info;
        if(text.size() > 1) {
            info = new StringBuilder()
                    .append(trips.get(0).from().name())
                    .append(" - {")
                    .append(String.join(", ", text))
                    .append("}")
                    .toString();
        } else {
            info = new StringBuilder()
                    .append(trips.get(0).from().name())
                    .append(" - ")
                    .append(text.last())
                    .toString();
        }
        return info;
    }

    /**
     method that compares alphabetically the textual representations of two tickets
     @param that, ticket we'd like to compare this to
     @return negative number if for ex: "a".compareTo("z");
             positive number if for ex: "z".compareTo("a");
             zero if: "a".compareTo("a");
     */
    @Override
    public int compareTo(Ticket that) { return this.text().compareTo(that.text()); }
}
