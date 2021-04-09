package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Christopher Soriano (326354)
 * @author Roman Danylovych (327830)
 */

public final class Ticket implements Comparable<Ticket> {

    //public final Trip trip;
    private final Station fromStation;
    //private final ArrayList<String> text = new ArrayList<>();
    private final List<Trip> trips;
    private final String tripInfo;

    /**First constructor for ticket class, verifies that list of trips isn't null and that all trips have same origin.
     @param trips
    */
    public Ticket(List<Trip> trips) throws IllegalArgumentException {
        if (trips == null || trips.size() == 0) {
            throw new IllegalArgumentException("List can't be null");
        }
        fromStation = trips.get(0).from();
        for(int i = 0; i < trips.size(); ++i) {
            if (!fromStation.name().equals(trips.get(i).from().name())) {
                throw new IllegalArgumentException("Not all trips come from same Station");
            }
        }
        //TODO Si ya une erreur c'est peut-être à cause de ça
        this.trips = trips;
        tripInfo = computeText(trips);
    }

    /**Second constructor for ticket class, creates a unique trip.
      @param from
      @param to
      @param points
     */
    public Ticket(Station from, Station to, int points){
        //this(List.of());
        this(List.of(new Trip(from, to, points)));
    }

    /**
    returns number of points for the specific ticket and connectivity
    @param connectivity
     @return int, max points if best connection was achieved, min points if no connection was achieved, or a specific amount of points if a in between connection was established.
     */
    public int points(StationConnectivity connectivity) {

        boolean max = false;
        int maxPts = trips.get(0).points();
        int minPts = trips.get(0).points();
        for (int h = 0; h < trips.size(); ++h) {
            if (connectivity.connected(trips.get(h).from(), trips.get(h).to()) == true) {
                if (maxPts < trips.get(h).points()) {
                    maxPts = trips.get(h).points();
                }
                max = true;
            }else {
                if (minPts > trips.get(h).points()) {
                    minPts = trips.get(h).points();
                }

            }
        }

        if (max == false){
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
     * @param trips
     * @return textual representation of the trip written on the ticket
     */
    private static String computeText(List<Trip> trips) {
        TreeSet<String> text = new TreeSet<String>();
        String info;

        for(int i = 0; i < trips.size(); ++i) {
            text.add(trips.get(i).to().name() + " (" + trips.get(i).points() + ")");
        }
        if(text.size() > 1) {
            info = trips.get(0).from().name() + " - " + "{" + String.join(", ", text) + "}";
        } else {
            info = trips.get(0).from().name() + " - " + text.last();
        }
        return info;
    }

    /**
     method that compares alphabetically the textual representations of two tickets
     @param that
     @return negative number if for ex: "a".compareTo("z");
             positive number if for ex: "z".compareTo("a");
             zero if for ex: "a".compareTo("a");
     */
    @Override
    public int compareTo(Ticket that) { return this.text().compareTo(that.text()); }
}
