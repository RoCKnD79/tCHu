package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class Ticket implements Comparable<Ticket> {

    //public final Trip trip;
    private final Station fromStation;
    //private final ArrayList<String> text = new ArrayList<>();
    private final String tripInfo;

    /**First constructor for ticket class, verifies that list of trips isn't null and that all trips have same origin.
     @param trips
    */
    public Ticket(List<Trip> trips) throws IllegalArgumentException {

        if (trips == null) {
            throw new IllegalArgumentException("List can't be null");
        }

        //TODO A verifier ;)
        fromStation = trips.get(0).from();
        for(int i = 0; i < trips.size(); ++i) {
            if (fromStation != trips.get(i).from()) {
                throw new IllegalArgumentException("Not all trips come from same Station");
            }
        }
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
    returns number of points for the specific ticket
    @param connectivity
     */
    public int points(StationConnectivity connectivity){
        return this.points(connectivity);
    }

    public String text() { return tripInfo; }

    @Override
    public String toString() { return tripInfo; }

    /**
     * @param trips
     * @return textual representation of the trip written on the ticket
     */
    private static String computeText(List<Trip> trips) {
        TreeSet<String> text = new TreeSet<String>();
        String info;

        for(int i = 0; i < trips.size(); ++i) {
            text.add(trips.get(i).to().toString());
        }
        info = trips.get(0).from().toString() + String.join(" - ", text);

        return info;
    }

    /**
     method that compares alphabetically the textual representations of two tickets
     @param that
     @return negative number if for ex: "a".compareTo("z");
             positive number if for ex: "z".compareTo("a");
             zero if for ex: "a".compareTo("a");
     */
    //TODO je sais pas si c'est juste en fait
    @Override
    public int compareTo(Ticket that) {

        /*if (this.text() < that.text()){
            return -1;
        }else if(this.text() > that.text()){
            return 1;
        }else if(this.text() = that.text()){
            return 0;
        }*/

        return this.text().compareTo(that.text());
    }
}
