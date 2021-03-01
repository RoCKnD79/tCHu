package ch.epfl.tchu.game;

import java.util.List;

public final class Trail {

    private final int length;
    private final Station station1;
    private final Station station2;
    private static Station station1FromLongestRoute;
    private static Station station2FromLongestRoute;
    private static List<Route> routes;
    private static Route longestRoute;


    private Trail(int length, Station station1, Station station2, List<Route> routes){
        this.length = length;
        this.station1 = station1;
        this.station2 = station2;
        this.routes = routes;
    }

    private Trail(int length, Station station1, Station station2){
        this.length = length;
        this.station1 = station1;
        this.station2 = station2;
    }

    public static Trail longest(List<Route> routes) {
    int j = 0;

    if(routes.equals(null)){
        return new Trail(0,null, null);
    }
        for(int i = 1; i < routes.size(); ++i) {
            if(routes.get(i-1).length() > routes.get(i).length()) {
                longestRoute = routes.get(i-1);
                j = i;
            }
        }

        return  new Trail(routes.get(j-1).length(),routes.get(j-1).station1(), routes.get(j-1).station2());
    }

    public int length(){
        return length;
    }

    public Station station1(){
        return station1;
    }

    public Station station2(){
        return station2;
    }
}
