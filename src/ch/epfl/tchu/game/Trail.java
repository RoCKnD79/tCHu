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
        this.station2 = routeToAdd.station2();
        List<Route> routes = oldTrail.routeConnections();
        routes.add(routeToAdd);
        this.routeConnections = routes;
    }

    /**
     * @param routes, the routes in your network
     * @return the longest Trail you can form with the routes given in argument
     */
    public static Trail longest(List<Route> routes) {

        if(routes == null) {
            return null;
        }

        /*
        I had a lot of difficulties working only with one-way routes and inverting each one of them to create new trails
        in order to have: Bâle-Baden and Baden-Bâle
        Therefore I created a list that just contains both ways
         */
        List<Route> routeBothWays = new ArrayList<>();

        for (int i = 0; i < routes.size(); i++) {
            Route temp = routes.get(i);
            routeBothWays.add(routes.get(i));
            routeBothWays.add(new Route(temp.id(), temp.station2(), temp.station1(), temp.length(), temp.level(), temp.color()));
        }

        //initial list of trails constituted of only one route each
        List<Trail> initTrails = new ArrayList<>();
        for(int i = 0; i < routeBothWays.size(); ++i) {
            initTrails.add(new Trail(routeBothWays.get(i).station1(), routeBothWays.get(i).station2(), List.of(routeBothWays.get(i))));
        }

        //see it as a temporary list used to stock new trails and assign them to iterTrails
        List<Trail> trails = new ArrayList<>();

        /*iterTrails is the list on which we'll iterate to create new trails, each time the while loop loops,
        iterTrails will be cleared and given the new trails*/
        List<Trail> iterTrails = new ArrayList<>();
        for (int i = 0; i < initTrails.size(); i++) {
            iterTrails.add(initTrails.get(i));
        }

        List<Trail> longestTrails = new ArrayList<>();
        for(Trail t : initTrails) {
            List<Trail> candidateTrails = possibleTrails(t, routeBothWays);
            longestTrails.add(findLongest(candidateTrails));
        }

        return findLongest(longestTrails);
    }

    /**
     * @param t, trail to which we'd like to add routes
     * @param routes, the potentialroutes we could add
     * @return the list of all the trails we could build by extending t with routes
     */
    private static List<Trail> possibleTrails(Trail t, List<Route> routes) {
        List<Trail> trails = new ArrayList<>();
        List<Trail> tempTrails = new ArrayList<>();
        List<Trail> addTrails = new ArrayList<>();
        tempTrails.add(t);
        boolean canExtend = false;
        do {
            canExtend = false;
            for (Trail temp: addTrails) {
                tempTrails.add(temp);
            }
            tempTrails.removeAll(trails);
            for (int i = 0; i < tempTrails.size(); i++) {
                trails.add(tempTrails.get(i));
            }

            addTrails.clear();
            for(Trail iter : tempTrails) {
                List<Route> potentialRoutes = calculatePotentialRoutes(iter, routes);
                for (Route r : potentialRoutes) {
                    addTrails.add(new Trail(iter, r));
                }

                if(!potentialRoutes.isEmpty()) {
                    canExtend = true;
                }
            }
        } while (canExtend);

        return trails;
    }


    /**
     * @param t, the trail to which we would like to add routes
     * @param routes the candidates that could potentially extend the trail
     * @return a list of the roads that can be added to the trail
     */
    private static List<Route> calculatePotentialRoutes(Trail t, List<Route> routes) {

        List<Route> potentialRoutes = new ArrayList<>();

        for (int i = 0; i < routes.size(); ++i) {

            boolean partOfTrail = false;
            for (int j = 0; j < t.routeConnections.size(); j++) {
                if(routes.get(i).station2().id() == t.routeConnections.get(j).station1().id()) {
                    partOfTrail = true;
                }
            }

            if (!t.routeConnections.contains(routes.get(i))
                    && (routes.get(i).station1().id() == t.station2().id())
                    && !partOfTrail) {
                potentialRoutes.add(routes.get(i));
            }
        }
        return potentialRoutes;
    }


    /**
     * @param trails, the list of the trails of which we'll be comparing the lengths
     * @return the longest trail ("longest" being the sum of the lengths of the routes constituting the list)
     */
    private static Trail findLongest(List<Trail> trails) {

        if(trails == null) {
            return null;
        }

        int index = 0;
        for (int i = 1; i < trails.size(); i++) {
            if (trails.get(index).length() < trails.get(i).length()) {
                index = i;
            }
        }
        return trails.get(index);
    }


    /**
     * @param trails, the trails of which we'll choose the longest
     * @return the length of the longest trail in the list
     */
    private static int biggestLength(List<Trail> trails) {
        if(trails == null) {
            return 0;
        }
        return findLongest(trails).length();

    }

    /**
     * @param routes, the routes we want to copy to a new list
     * @return the copy of the list
     */
    private static List<Route> copyList(List<Route> routes) {
        List<Route> copyList = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            copyList.add(routes.get(i));
        }
        return copyList;
    }

    //calculates the length of the trail
    public int length() {
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

        List<String> text = new ArrayList<>();
        text.add(station1.name());
        for (int i = 0; i < routeConnections.size(); i++) {
            text.add(routeConnections.get(i).station2().name());
        }

        String info = String.join(" - ", text);
        return info;
    }

}


