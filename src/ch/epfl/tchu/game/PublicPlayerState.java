package ch.epfl.tchu.game;

import java.util.List;

/**
 * @author Christopher Soriano (326354)
 */

//IMMUTABLE class
    /*
    represents the public state of a player, which means :  :
    - the number of tickets, cards, and cars he has
    - the routes he owns
    - number of construction points
    */

public final class PublicPlayerState {

    public final int ticketCount;
    public final int cardCount;
    public final List<Route> routes;
    public final int carCount = carCount();
    public final int claimPoints = claimPoints();

    /**
     * Constructor of a player's card state
     * @param ticketCount, number of tickets the player has
     * @param cardCount, number of cards the player has
     * @param routes, list of the routes the player owns
     * @throws IllegalArgumentException, if ticketCount or cardCount is inferior to 0
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes) throws IllegalArgumentException{
        if ((ticketCount < 0) || (cardCount < 0)){
            throw new IllegalArgumentException("ticket count or card count is negative");
        }
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = routes;
    }

    /**
     * gives the number of ticket the player has.
     * @return, the number of tickets
     */
    public int ticketCount(){
        return ticketCount;
    }

    /**
     * gives the number of cards the player has.
     * @return the number of cards
     */
    public int cardCount(){
        return cardCount;
    }

    /**
     * gives a list of the routes the player owns
     * @return list of routes
     */
    public List<Route> routes(){
        return routes;
    }

    public int carCount(){
        int numberOfCars = 40;
        for(Route r : routes){
           numberOfCars -= r.length();
        }
        return numberOfCars;
    }

    public int claimPoints(){
        int numberOfPoints = 0;
        for(Route r : routes){
            numberOfPoints += r.claimPoints();
        }
        return numberOfPoints;
    }
}
