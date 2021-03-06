package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * @author Christopher Soriano (326354)
 * @author Roman Danylovych (327830)
 */

//IMMUTABLE class

public final class Route {

    private final Station station1;
    private final Station station2;
    private final String id;
    private final int length;
    private final Level level;
    private final Color color;

    /**
     * @author Christopher Soriano (326354)
     */
    public enum Level {
        OVERGROUND, UNDERGROUND
    }

    /**
     * Constructor of a route
     *
     * @param id, id of the route
     * @param station1, first station of the route
     * @param station2, second station of the route
     * @param length, length of the route
     * @param level, level (tunnel or not) of the route
     * @param color, color of the route
     * @throws IllegalArgumentException, if first station is equal to end station, or if the length of the route is higher or lower than a constant.
     * @throws NullPointerException,     if either the station1, station 2, the level or the id is null.
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) throws IllegalArgumentException, NullPointerException {

        if (station1.equals(station2) || length < Constants.MIN_ROUTE_LENGTH || length > Constants.MAX_ROUTE_LENGTH) {
            throw new IllegalArgumentException("Stations cannot be equal");
        }

        this.color = color; //can be null, this would mean its color is neutral (gray)
        this.level = Objects.requireNonNull(level);
        this.id = Objects.requireNonNull(id);
        this.length = length;
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
    }

    /**
     * method that returns the id of the route
     *
     * @return id
     */
    public String id() {
        return id;
    }

    /**
     * method that returns first station of the route
     *
     * @return station1
     */
    public Station station1() {
        return station1;
    }

    /**
     * method that returns second station of the route
     *
     * @return station2
     */
    public Station station2() {
        return station2;
    }

    /**
     * method that returns the length of the route
     *
     * @return length
     */
    public int length() {
        return length;
    }

    /**
     * method that returns the level of the route
     *
     * @return level
     */
    public Level level() {
        return level;
    }

    /**
     * method that returns the color of the route
     *
     * @return color
     */
    public Color color() {
        return color;
    }

    /**
     * method that returns a list of the stations in order
     *
     * @return Stations
     */
    public List<Station> stations() {
        return List.of(station1, station2);
    }

    /**
     * method that returns the opposite station than the one given (when called)
     *
     * @param station, the station of which we want the opposite
     * @return the opposite of the station
     */
    public Station stationOpposite(Station station) throws IllegalArgumentException {
        if ((station != station1) && (station != station2)) {
            throw new IllegalArgumentException("station not equal to any of the two station of the route");
        }
        if (station.equals(station1)) {
            return station2;
        }
        return station1;
    }

    /**
     * method that fins all the cards that could be played to get control of the route
     *
     * @return a list of cards that could be played to claim the route
     */
    public List<SortedBag<Card>> possibleClaimCards() {
        List<SortedBag<Card>> list = new ArrayList<>();
        if (level.equals(Level.OVERGROUND)) { //case where route is not a tunnel => Level.OVERGROUND

            if (color == null) {
                for (int i = 0; i < Color.COUNT; ++i) {
                    list.add(SortedBag.of(length, Card.of(Color.ALL.get(i))));
                }
            } else {
                list.add(SortedBag.of(length, Card.of(color)));
            }

        } else { //case where route is a tunnel => Level.UNDERGROUND

            if (color == null) {
                for (int i = 0; i < length; ++i) {
                    for (int j = 0; j < Color.COUNT; ++j) {
                        list.add(SortedBag.of(length - i, Card.of(Color.ALL.get(j)), i, Card.LOCOMOTIVE));
                    }
                }
                list.add(SortedBag.of(length, Card.LOCOMOTIVE));
            } else {
                for (int i = 0; i <= length; ++i) {
                    list.add(SortedBag.of(length - i, Card.of(color), i, Card.LOCOMOTIVE));
                }
            }

        }
        return list;
    }

    /**
     * counts how many additional cards have to be played by the player to take control of a tunnel, depending on which cards where drawn from deck.
     *
     * @param claimCards, initial claim cards used by player
     * @param drawnCards, cards that were drawn
     * @return the number of cards that need to be put down by the player to claim the tunnel.
     * @throws IllegalArgumentException, if not 3 cards were drawn, or if the route trying to be claimed is not a tunnel
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards)
            throws IllegalArgumentException {
        if ((drawnCards.size() != 3) || (this.level().equals(Level.OVERGROUND))) {
            throw new IllegalArgumentException("size of drawn Cards is not equal to 3 or the route is not a tunnel");
        }
        int additionalClaimCardsNumber = 0;
        for (int i = 0; i < drawnCards.size(); ++i) {

            if (drawnCards.get(i).equals(claimCards.get(0)) || drawnCards.get(i).equals(Card.LOCOMOTIVE)) {

                additionalClaimCardsNumber += 1;

            }
        }
        return additionalClaimCardsNumber;
    }

    /**
     * method that finds how many points will be given out to the player if he manages to take control of the route
     *
     * @return number of points for the route
     */
    public int claimPoints() {
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }

}
