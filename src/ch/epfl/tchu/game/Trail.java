package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

public final class Trail {

    private final Station station1;
    private final Station station2;
    private final List<Route> routeConnections;

    /**
     * @param station1, station of departure
     * @param station2, station of arrival
     * @param routes, the routes linking station1 and station2 together
     */
    private Trail(Station station1, Station station2, List<Route> routes) {
        this.station1 = station1;
        this.station2 = station2;

        routeConnections = copyList(routes);
    }

    /**
     * @param oldTrail, the trail to which we would like to add a route
     * @param routeToAdd, the route we would like to add to the trail
     */
    private Trail(Trail oldTrail, Route routeToAdd) {
        this.station1 = oldTrail.station1();
        //this.station2 = routeToAdd.station2();
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
            longest = initTrails.get(0);
        }

        for(Trail t : initTrails) {
            if(longest.length() < t.length()) {
                longest = t;
            }
        }

        while(!initTrails.isEmpty()) {
            List<Trail> trails = new ArrayList<>();
            for(Trail t : initTrails) {
                List<Route> routesToAdd = calculatePotentialRoutes(t, routes);
                for(Route r : routesToAdd) {
                    trails.add(new Trail(t, r));
                }
            }
            for(Trail t : trails) {
                if(longest.length() < t.length()) {
                    longest = t;
                }
            }
            initTrails = trails;
        }

        return longest;
    }


    /**
     * @param t, the trail to which we would like to add routes
     * @param routes the candidates that could potentially extend the trail
     * @return a list of the roads that can be added to the trail
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
     * @param routes, the routes we want to copy to a new list
     * @return the copy of the list
     */
    private static List<Route> copyList(List<Route> routes) {
        List<Route> copyList = new ArrayList<>();
        copyList.addAll(routes);
        return copyList;
    }

    //calculates the length of the trail
    public int length() {
        if(this == null) {
            return 0;
        }
        int l = 0;
        for(int i = 0; i < routeConnections.size(); ++i) {
            l += routeConnections.get(i).length();
        }
        return l;
    }

    //returns the station of departure
    public Station station1() {
        if (routeConnections == null) {
            return null;
        }
        return station1;
    }

    //returns the station of arrival
    public Station station2() {
        if (routeConnections == null) {
            return null;
        }
        return station2;
    }


    public List<Route> routeConnections() {
        return copyList(routeConnections);
    }


    @Override
    public String toString() {

        if(this == null) {
            return "Trail is null";
        }

        return station1.name() + " - " + station2.name();
    }

}


