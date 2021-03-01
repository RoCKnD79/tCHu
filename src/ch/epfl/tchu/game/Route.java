package ch.epfl.tchu.game;

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


        this.color = color;
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

    public List<SortedBag<Card>> possibleClaimCards(){

        //List<SortedBag<Card>> list =
        //SortedBag possibleCards = SortedBag.of(Constants.TOTAL_CARDS_COUNT, Card); //Card est enum et on veut un objet mais je voit pas trop ce qu'on peut mettre d'autre);
        if (this.level().equals(Level.UNDERGROUND)){
            //possibleCards = possibleCards.Builder.add(1, Card.color());
            SortedBag.Builder<Card> possibleClaimCards = new SortedBag.Builder<Card>();
            for(int i = 0; i < length; ++i) {
                possibleClaimCards.add(Card.of(color));
            }
        }else{
            //only cards of the same of color as route
        }

    return possibleClaimCards.toList();
    }

    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards)
                throws IllegalArgumentException{
        if(level.equals(Level.OVERGROUND) || drawnCards.size() != 3) {
            throw new IllegalArgumentException("Route is not a tunnel or number of drawn cards is not 3");
        }
        return drawnCards.size();
    }

    public int claimPoints() {
        //TODO jsp si c'est length-1 ou length qu'il faut mettre
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }

}
