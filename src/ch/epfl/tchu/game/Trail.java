package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christopher Soriano (326354)
 * @author Roman Danylovych (327830)
 */
public final class Trail {

    private final Station station1;
    private final Station station2;
    private final List<Route> routeConnections;

    /**
     * main constructor of the class
     * @param station1, station of departure
     * @param station2, station of arrival
     * @param routes, the routes linking station1 and station2 together
     */
    private Trail(Station station1, Station station2, List<Route> routes) {
        this.station1 = station1;
        this.station2 = station2;
        routeConnections = List.copyOf(routes);
    }

    /**
     * second constructor, mainly used in longest() method in order to create new trails
     * @param oldTrail, the trail to which we would like to add a route
     * @param routeToAdd, the route we would like to add to the trail
     */
    private Trail(Trail oldTrail, Route routeToAdd) {
        this.station1 = oldTrail.station1();
        this.station2 = routeToAdd.stationOpposite(oldTrail.station2);

        List<Route> routes = oldTrail.routeConnections();
        routes.add(routeToAdd);
        this.routeConnections = List.copyOf(routes);
    }


    /**
     * @param routes, the routes in your network
     * @return the longest Trail you can form with the routes given in argument
     */
    public static Trail longest(List<Route> routes) {

        if(routes == null || routes.isEmpty()) {
            return new Trail(null, null, routes);
        }

        //initial list of trails constituted of only one route each
        List<Trail> initTrails = new ArrayList<>();
        for (Route route : routes) {
            initTrails.add(new Trail(route.station1(), route.station2(), List.of(route)));
            initTrails.add(new Trail(route.station2(), route.station1(), List.of(route)));
        }

        Trail longest = null;
        if(!initTrails.isEmpty()) {
            //finds initially the longest trail among all the initial trials
            longest = longestTrail(initTrails.get(0), initTrails);
        }

        //the main algorithm that will build new trails with available routes and find the longest possible trail
        while(!initTrails.isEmpty()) {
            List<Trail> trails = new ArrayList<>();
            //builds a list of new trails built with added routes
            for(Trail t : initTrails) {
                List<Route> routesToAdd = calculatePotentialRoutes(t, routes);
                routesToAdd.forEach(route -> trails.add(new Trail(t, route)));
            }
            longest = longestTrail(longest, trails);
            initTrails = trails;
        }

        return longest;
    }

    /**
     * method used to calculate the longest trail in a list of trails
     * (specifically written for method longest(List<Route> routes))
     * @param prevLongest, current longest trail
     * @param trails, list of trails from which we'd like to find the longest one
     * @return longest trail out of the list or prevLongest if it is still the longest
     */
    private static Trail longestTrail(Trail prevLongest, List<Trail> trails) {
        Trail longest = prevLongest;
        for(Trail t : trails) {
            if(longest.length() < t.length()) {
                longest = t;
            }
        }
        return longest;
    }

    /**
     * @param t, the trail to which we would like to add routes
     * @param routes the candidates that could potentially extend the trail
     * @return a list of the routes that can be added to the trail
     */
    private static List<Route> calculatePotentialRoutes(Trail t, List<Route> routes) {

        List<Route> potentialRoutes = new ArrayList<>();

        for (Route r : routes) {
            if (!t.routeConnections.contains(r)
                && (t.station2.equals(r.station1()) || t.station2.equals(r.station2()) )) {
                potentialRoutes.add(r);
            }
        }
        return List.copyOf(potentialRoutes);
    }

    /**
     * @return length of trail
     */
    public int length() {
        return routeConnections.stream().mapToInt(Route::length).sum();
    }

    /**
     * @return station of departure
     */
    public Station station1() {
        return routeConnections == null ? null : station1;
    }

    /**
     * @return station of arrival
     */
    public Station station2() {
        return routeConnections == null ? null : station2;
    }

    /**
     * @return list of routeConnections
     */
    public List<Route> routeConnections() {
        return new ArrayList<>(routeConnections);
    }

    @Override
    public String toString() {
        if(station1 == null || station2 == null)
            return "Trail is null";

        return station1.name() + " - " + station2.name();
    }

}


