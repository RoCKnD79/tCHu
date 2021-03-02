package ch.epfl.tchu.game;

import java.util.List;

public final class Trail {

    private final int length;
    private final Station station1;
    private final Station station2;
    private static List<Route> routes;
    private static Trail longestTrail;
    static List<Trail>  simpleTrails;
    static List<Route>  routeSimple;
    static List<Route>  routeSimpleToAdd;


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


        //int j = 0;

        //boucle for qui met dans une list de Trail, tous les trails de taille 1 (avec une route)
        for (int i = 0; i < ChMap.routes().size(); ++i) {
            simpleTrails.add(new Trail(1, ChMap.routes().get(i).station1(), ChMap.routes().get(i).station2()));
            routeSimple.add(ChMap.routes().get(i));
        }
        //TODO Will this while loop ever end ??

        while (simpleTrails.size() != 0) {
            List<Trail> simpleTrails2 = null;
            for (int i = 0; i < simpleTrails.size(); ++i) {
                if (!simpleTrails.contains(routeSimple.get(i)) || (simpleTrails.get(i).station2()).equals(routeSimple.get(i).station1())) {
                    routeSimpleToAdd.add(routeSimple.get(i));
                }
                for (int j = 0; j < routeSimpleToAdd.size(); ++j) {
                    simpleTrails2.add(new Trail(2, simpleTrails.get(i).station1(), routeSimpleToAdd.get(i).station2()));
                }
            }
            simpleTrails = simpleTrails2;
        }

        for(int i = 0; i < simpleTrails.size(); ++ i){
            if(simpleTrails.get(i).length() > simpleTrails.get(i+1).length()){
                longestTrail = simpleTrails.get(i+1);
            }
        }
           /* if (routes.equals(null)) {
                return new Trail(0, null, null);
            }*/
       /* for(int i = 1; i < routes.size(); ++i) {
            if(routes.get(i-1).length() > routes.get(i).length()) {
                longestRoute = routes.get(i-1);
                j = i;
            }
        }*/


       // return  new Trail(routes.get(j-1).length(),routes.get(j-1).station1(), routes.get(j-1).station2());
        return longestTrail;
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
