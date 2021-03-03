package ch.epfl.tchu.game;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/*
public final class Trail {

    //private final int length;
    private final Station station1;
    private final Station station2;
    private static List<Route> routeConnections;
    private static Trail longestTrail;
    private static List<Trail> simpleTrails = new ArrayList<>();
    private static List<Route> routeSimple = new ArrayList<>();
    private static List<Route> routeSimpleToAdd = new ArrayList<>();

/*
    private Trail(int length, Station station1, Station station2, List<Route> routes){
        //this.length = length;
        this.station1 = station1;
        if(station1.id() == station2.id()) {
            throw new IllegalArgumentException(" 'from' station and 'to' station must be different");
        }
        this.station2 = station2;
        routeConnections = routes;
    }*/
/*
    private Trail(int length, Station station1, Station station2){
        this.length = length;
        this.station1 = station1;
        this.station2 = station2;
    }*/
/*
    private Trail(Route route) {
        this.station1 = route.station1();
        this.station2 = route.station2();
        routeConnections = List.of(route);
    }*/

    /*public static Trail longest(List<Route> routes) {

        //int j = 0;
        //boucle for qui met dans une list de Trail, tous les trails de taille 1 (avec une route)
        for (int i = 0; i < routes.size(); ++i) {
            simpleTrails.add(new Trail(1, routes.get(i).station1(), routes.get(i).station2()));
            //routeSimple.add(routes.get(i));
        }

        //TODO Will this while loop ever end ??
        while (simpleTrails.size() != 0) {
            List<Trail> simpleTrails2 = null;
            for (int i = 0; i < simpleTrails.size(); ++i) {
                if (!simpleTrails.contains(routes.get(i)) || (simpleTrails.get(i).station2()).equals(routes.get(i).station1())) {
                    routeSimpleToAdd.add(routes.get(i));
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
    /*
        return longestTrail;
    }*/

    /*
    public int length(){
        return length;
    }*/

/*
    private Trail(Trail oldTrail, Route routeToAdd) {
        this.station1 = oldTrail.station1();;
        this.station2 = oldTrail.station2();
        List<Route> routes = oldTrail.routeConnections();
        routes.add(routeToAdd);
        this.routeConnections = routes;

    }

    public static Trail longest(List<Route> routes) {

        List<Trail> initTrails = new ArrayList<>();
        for(int i = 0; i < routes.size(); ++i) {

            if(routes.get(i).length() == 1 ){
                initTrails.add(new Trail((routes.get(i).station1()), routes.get(i).station2(), List.of(routes.get(i))));
                System.out.println("station 1 : " + routes.get(i).station1());
                System.out.println("station 2 : " +routes.get(i).station2());
                System.out.println(initTrails);
                }
            }

            //initTrails.add(new Trail(routes.get(i)));

        System.out.println(initTrails.size());
        /*while(initTrails.size() != 0) {*/
/*
            List<Trail> trails = new ArrayList<>();
            for(Trail t : initTrails) {
                List<Route> potentialRoutes = new ArrayList<>();
                for(int j = 0; j < routes.size(); ++j) {
                    if(!t.routeConnections().contains(routes.get(j))
                            && routes.get(j).station1().id() == t.station2().id()
                            && routes.get(j).station2().id() != t.station1().id()) {
                        potentialRoutes.add(routes.get(j));
                        System.out.println("1: " + routes.get(j).station1().id() + " 2: " + t.station2().id());
                    }
                }
                for(Route r : potentialRoutes) {
                    trails.add(new Trail(t, r));
                }
            }
            /*initTrails = trails;
        }*/
/*
        int longestTrailIndex = 0;
        for(int k = 1; k < trails.size(); ++k) {
            if(trails.get(k-1).length() > trails.get(k).length()) {
                longestTrailIndex = k-1;
            }
        }

        return trails.get(longestTrailIndex);
    }

    public int length() {
        int l = 0;
        for(int i = 0; i < routeConnections.size(); ++i) {
            l += routeConnections.get(i).length();
        }
        return l;
    }

    public Station station1(){
        return station1;
    }

    public Station station2(){
        return station2;
    }

    public List<Route> routeConnections() {
        List<Route> list = new ArrayList<>();
        for(int i = 0; i < routeConnections.size(); ++i) {
            list.add(routeConnections.get(i));
        }
        return list;
    }

    @Override
    public String toString() {
        TreeSet<String> text = new TreeSet<String>();
        String info = station1.name();
        /*
        for (int i = 0; i < routeConnections.size(); ++i) {
            text.add(routeConnections.get(i).station2().name());
        }
        info = station1.name() + " - " + String.join(" - ", text);
         */
/*
        for(int i = 0; i < routeConnections.size(); ++i) {
            info += " - " + routeConnections.get(i).station1().name();
        }
        info += " - " + station2.name();
        return info;
    }
}*/



public final class Trail {

    private final Station station1;
    private final Station station2;
    private final List<Route> routeConnections;

    private Trail(Station station1, Station station2, List<Route> routes) {
        this.station1 = station1;
        this.station2 = station2;
        routeConnections = List.copyOf(routes);
    }

    public static Trail longest(List<Route> routes) {

        if(routes == null) {
            return null;
        }

        List<Trail> initTrails = new ArrayList<>();
        for(int i = 0; i < routes.size(); ++i) {
            initTrails.add(new Trail(routes.get(i).station1(), routes.get(i).station2(), List.of(routes.get(i))));
            initTrails.add(new Trail(routes.get(i).station2(), routes.get(i).station1(), List.of(routes.get(i))));
            //System.out.println(initTrails.get(i));
        }
        //System.out.println((initTrails.size()));

        while(initTrails.size() != 0) {

            for(Trail t : initTrails) {
                List<Route> potentialRoutes = new ArrayList<>();
                for (int i = 0; i < routes.size(); ++i) {
                    if(!t.routeConnections.contains(routes.get(i))
                            && routes.get(i).station1().id() == t.routeConnections.get(t.routeConnections.size()-1).station2().id()
                            && routes.get(i).station2().id() != t.station1().id()) {
                        potentialRoutes.add(routes.get(i));
                    }
                }
                /*
                for (int i = 0; i < potentialRoutes.size(); i++) {
                    System.out.println("station1: " + potentialRoutes.get(i).station1().name() +
                            " station2: " + potentialRoutes.get(i).station2().name());
                }*/

                /*for (Route r : potentialRoutes) {

                }*/

            }



            initTrails.clear();
        }

        while(initTrails.size() != 0) {
            initTrails.clear();
        }

        return null;
    }

    public int length() {
        int l = 0;
        for(int i = 0; i < routeConnections.size(); ++i) {
            l += routeConnections.get(i).length();
        }
        return l;
    }

    public Station station1() {
        if (routeConnections == null) {
            return null;
        }
        return station1;
    }

    public Station station2() {
        if (routeConnections == null) {
            return null;
        }
        return station2;
    }

    @Override
    public String toString() {

        /*TreeSet<String> text = new TreeSet<String>();
        for(int i = 0; i < routeConnections.size(); ++i) {
            text.add(routeConnections.get(i).station1().name());
        }
        text.add(routeConnections.get(routeConnections.size()-1).station2().name());
        String info = String.join(" - ", text);*/

        List<String> text = new ArrayList<>();
        if(station1.id() == routeConnections.get(0).station1().id()) {
            for(int i = 0; i < routeConnections.size(); ++i) {
                text.add(routeConnections.get(i).station1().name());
            }
            text.add(station2.name());
        } else {
            text.add(station1.name());
            for(int i = routeConnections.size()-1; i >= 0; --i) {
                text.add(routeConnections.get(i).station1().name());
            }
        }
        //text.add(routeConnections.get(routeConnections.size()-1).station2().name());
        String info = String.join(" - ", text);

        return info;
    }

}


