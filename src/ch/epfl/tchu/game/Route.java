package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Route {

    private final Station station1;
    private final Station station2;
    private final String id;
    private final int length;
    private final Level level;
    private final Color color;
    private  Card Card;
    private static SortedBag possibleClaimCards;

    public Route(String id, Station station1, Station station2, int length, Level level, Color color) throws IllegalArgumentException{


        this.color = color; //can be null, this would mean its color is neutral (gray)
        this.level = Objects.requireNonNull(level);
        this.id = Objects.requireNonNull(id);
        this.length = length;
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);

        //TODO Jarrive pas a avoir les constantes de la classe constante non instanciable pour le if :
        if(station1.equals(station2) || length < Constants.MIN_ROUTE_LENGTH || length > Constants.MAX_ROUTE_LENGTH) {
            throw new IllegalArgumentException("Stations cannot be equal");
        }


    }

    public enum Level{
        OVERGROUND, UNDERGROUND;
    }
    /**
     * method that returns the id of the route
     * @return id
     */
    public String id(){
        return id;
    }
    /**
     * method that returns first station of the route
     * @return station1
     */
    public Station station1(){
        return station1;
    }
    /**
     * method that returns second station of the route
     * @return station2
     */
    public Station station2(){
        return station2;
    }
    /**
     * method that returns the length of the route
     * @return length
     */
    public int length(){
        return length;
    }
    /**
     * method that returns the level of the route
     * @return level
     */
    public Level level(){
        return level;
    }

    /**
     * method that returns the color of the route
     * @return color
     */
    public Color color(){
        return color;
    }

    /**
     * method that returns a list of the stations in order
     * @return Stations
     */
    public List<Station> stations(){
        return List.of(station1, station2);
        }

    /**
     * method that returns the opposite station than the one given (when called)
     * @param station
     * @return Station
     */
    public Station stationOpposite(Station station){
        if (station.equals(station1)){
            return station2;
        }
        return station1;
    }

    public List<SortedBag<Card>> possibleClaimCards() {

        List<SortedBag<Card>> list = new ArrayList<>();
        //SortedBag<Card> sb = SortedBag.of();

        if (level.equals(Level.OVERGROUND)) {

            if (color == null) {
                System.out.println("Overground, color null");
                for (int i = 0; i < Color.COUNT; ++i) {
                    list.add(SortedBag.of(length, Card.of(Color.ALL.get(i))));
                }
            } else {
                System.out.println("Overground, color not null");
                list.add(SortedBag.of(length, Card.of(color)));
            }

        } else { //case where route is a tunnel => Level.UNDERGROUND

            if (color == null) {
                System.out.println("Underground, color null");
                //list.add(SortedBag.of(length, Card.LOCOMOTIVE));
                for (int i = 0; i < Color.COUNT; ++i) {
                    for (int j = 0; j < length; ++j) {
                        //list.add(SortedBag.of(j, Card.of(Color.ALL.get(i)), length - j, Card.LOCOMOTIVE));
                        list.add(SortedBag.of(length-j, Card.of(Color.ALL.get(i)), j, Card.LOCOMOTIVE));
                    }
                }
                list.add(SortedBag.of(length, Card.LOCOMOTIVE));
            } else {
                System.out.println("Underground, color not null");
                for (int i = 0; i <= length; ++i) {
                    //list.add(SortedBag.of(i, Card.of(color), length - i, Card.LOCOMOTIVE));
                    list.add(SortedBag.of(length-i, Card.of(color), i, Card.LOCOMOTIVE));
                    //sb.union(SortedBag.of(i, Card.of(color), length-i, Card.LOCOMOTIVE));
                }
            }

        }

        return list;
        //list.add(sb);
    }

    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards)
                throws IllegalArgumentException{
        if(level.equals(Level.OVERGROUND) || drawnCards.size() != 3) {
            throw new IllegalArgumentException("Route is not a tunnel or number of drawn cards is not 3");
        }
        return drawnCards.size();
    }

    public int claimPoints() {
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }

}
