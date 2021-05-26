package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Christopher Soriano (326354)
 */

//IMMUTABLE class

/**
 * represents the public state of a player, which means :  :
 * - the number of tickets, cards, and cars he has
 * - the routes he owns
 * - number of construction points
 **/

public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;


    /**
     * Constructor of a player's card state
     *
     * @param ticketCount, number of tickets the player has
     * @param cardCount,   number of cards the player has
     * @param routes,      list of the routes the player owns
     * @throws IllegalArgumentException, if ticketCount or cardCount is inferior to 0
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes) throws IllegalArgumentException {
        if ((ticketCount < 0) || (cardCount < 0)) {
            throw new IllegalArgumentException("ticket count or card count is negative");
        }
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = routes;
    }

    /**
     * gives the number of ticket the player has.
     *
     * @return the number of tickets
     */
    public int ticketCount() {
        return ticketCount;
    }

    /**
     * gives the number of cards the player has.
     *
     * @return the number of cards
     */
    public int cardCount() {
        return cardCount;
    }

    /**
     * gives a list of the routes the player owns
     *
     * @return list of routes
     */
    public List<Route> routes() {
        return routes;
    }

    public int carCount() {
        int numberOfCars = 40;
        if (routes != null) {
            for (Route r : routes) {
                if(r != null)
                    numberOfCars -= r.length();
            }
        }
        return numberOfCars;
    }

    public int claimPoints() {
        int numberOfPoints = 0;

        if (routes != null) {
            for (Route r : this.routes) {
                if(r != null)
                    numberOfPoints += r.claimPoints();
            }
        }
        return numberOfPoints;
    }
}
