package ch.epfl.tchu.game;

import java.util.List;
import java.util.Objects;

public final class Route {

    private Station station1;
    private Station station2;
    private String id;
    private int length;
    private Level level;
    private Color color;
    private List<Station> stations;
    private List<Card> allCards;

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

        SortedBag possibleCards = SortedBag.of(Constants.TOTAL_CARDS_COUNT, Card); //Card est enum et on veut un objet mais je voit pas trop ce qu'on peut mettre d'autre);
        if (this.level().equals(Level.UNDERGROUND)){
            possibleCards = possibleCards.build(Card.of(color));
        }else{
            //only cards of the same of color as route
        }

    return possibleCards.toList();
    }

}
