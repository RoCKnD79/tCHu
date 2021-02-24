package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

public final class Ticket implements Comparable<Ticket>{

    public Trip trip;
    private Station fromStation;
    private ArrayList<String> text = new ArrayList<>();

    /*First constructor for ticket class, verifies that list of trips isn't null and that all trips have same origin.
     @param List<Trip> trips
    */
    public Ticket(List<Trip> trips) throws IllegalArgumentException{

        if (trips == null){
            throw new IllegalArgumentException("List can't be null");
        }

        //TODO A verifier ;)
        fromStation = trips.get(0).from();
        for(int i= 0; i < trips.size(); ++i) {
            if (fromStation != trips.get(i).from()) {
                throw new IllegalArgumentException("Not all trips come from same Station");
            }
        }
        }

    /*Second constructor for ticket class, creates a unique trip.
      @param Station from
      @param Station to
      @int points
     */
    public Ticket(Station from, Station to, int points){
        this(List.of());
        //this(List.of(trip = new Trip(from, to, points)))

    }

    /*
    returns number of points for the specific ticket
    @param StationConnectivity connectivity
     */
    public int points(StationConnectivity connectivity){
        return this.points(connectivity);
    }

    public String text(){

    }

    private String  computeText(List<Trip> trips){



    }

    /*
    method that compares the length of the textual representations for each ticket
    @param Ticket that
     */
    //TODO je sais pas si c'est juste en fait
    @Override
    public int compareTo(Ticket that) {
        if (this.text() < that.text()){
            return -1;
        }else if(this.text() > that.text()){
            return 1;
        }else if(this.text() = that.text()){
            return 0;
        }
    }
}
