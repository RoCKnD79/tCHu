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
    private List<Station> Stations;

    public Route(String id, Station station1, Station station2, int length, Level level, Color color) throws IllegalArgumentException{


        this.color = color;
        this.level = Objects.requireNonNull(level);
        this.id = Objects.requireNonNull(id);
        this.length = length;
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);

        //TODO Jarrive pas a avoir les constantes de la classe constante non instanciable pour le if :
        if(station1.equals(station2)){
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
    public Station Station1(){
        return station1;
    }
    /**
     * method that returns second station of the route
     * @return station2
     */
    public Station Station2(){
        return station2;
    }
    /**
     * method that returns the length of the route
     * @return length
     */
    public int lenght(){
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
    public List<Station> Stations(){
        return Stations = List.of(station1, station2);
        }

//TODO ca marche c'est sur, mais est ce que c'est la plus "belle" manière de le faire ? A verifier
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

}