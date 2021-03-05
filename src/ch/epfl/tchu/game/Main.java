package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        /*Station from1 = new Station(1, "Lausanne");
        Station from2 = new Station(2, "Lausanne");
        Station from3 = new Station(3, "Lausanne");

        Station to1 = new Station(4, "Winterthour");
        Station to2 = new Station(5, "Bern");
        Station to3 = new Station(6, "Geneva");

        List<Trip> trips = new ArrayList<Trip>();

        trips.add(new Trip(from1, to1, 1));
        trips.add(new Trip(from2, to2, 2));
        trips.add(new Trip(from3, to3, 3));

        Ticket ticket1 = new Ticket(trips);
        System.out.println(ticket1);

        Ticket ticket2 = new Ticket(to1, from2, 4);

        System.out.println("ticket1: " + ticket1 +
                            "\nticket2: " + ticket2 +
                            "\ncompare: " + ticket1.compareTo(ticket2));

        Route route = new Route("LAU_WHI_1", from1, to1, 2, Route.Level.OVERGROUND, Color.RED);
        */

        /*
        //Overground, 6, null
        Route route1 = ChMap.routes().get(48);
        System.out.println(route1.id());
        for(SortedBag<Card> c : route1.possibleClaimCards()) {
            System.out.println(c);
        }*/

        /*
        //Overground, color not null, ength: 4
        Route route1 = ChMap.routes().get(49);
        System.out.println(route1.id());
        for(SortedBag<Card> c : route1.possibleClaimCards()) {
            System.out.println(c);
        }*/

        /*
        //Underground, color null, length: 2
        Route route1 = ChMap.routes().get(41);
        System.out.println(route1.id());
        int i = 0;
        for(SortedBag<Card> c : route1.possibleClaimCards()) {
            System.out.println(c);
            i += 1;
        }
        System.out.println(i);
        */

        /*
        //Underground and color null, length: 3
        Route route1 = ChMap.routes().get(32);
        System.out.println(route1.id());
        for(SortedBag<Card> c : route1.possibleClaimCards()) {
            System.out.println(c);
        }*/

        /*
        //UnderGround and Color not null, length: 3
        Route route1 = ChMap.routes().get(33);
        System.out.println(route1.id());
        for(SortedBag<Card> c : route1.possibleClaimCards()) {
            System.out.println(c);
        }*/

        /*System.out.println(ChMap.routes().size());
        List<Route> routes = new ArrayList<>();

        for(int i = 2; i < 11; ++i) {
            routes.add(ChMap.routes().get(i));
        }
        System.out.println(Trail.longest(routes));*/

        /*List<Route> routes = new ArrayList<>();
        routes.add(ChMap.routes().get(2));
        routes.add(ChMap.routes().get(7));
        routes.add(ChMap.routes().get(67));*/
        /*for (int i = 0; i < routes.size(); i++) {
            System.out.println(routes.get(i).station1().name() + " - " + routes.get(i).station2().name());
        }*/
        //System.out.println(Trail.longest(routes));

        /*List<Route> routes = new ArrayList<>();
        routes.add(ChMap.routes().get(16));
        routes.add(ChMap.routes().get(60));
        routes.add(ChMap.routes().get(34));
        routes.add(ChMap.routes().get(52));
        routes.add(ChMap.routes().get(53));
        for (int i = 0; i < routes.size(); i++) {
            System.out.println(routes.get(i).station1().name() + " - " + routes.get(i).station2().name());
        }
        System.out.println("\n" + Trail.longest(routes));*/

        /*List<Route> routes = new ArrayList<>();
        routes.add(ChMap.routes().get(0));
        routes.add(ChMap.routes().get(25));
        routes.add(ChMap.routes().get(30));
        for (int i = 0; i < routes.size(); i++) {
            System.out.println(routes.get(i).station1().name() + " - " + routes.get(i).station2().name());
        }
        System.out.println("\n" + Trail.longest(routes));*/


        //Brigue - Wassen - Coire (9)
        //Allemagne - Kreuzlingen - Saint-Gall - Vaduz (4)
        List<Route> routes = new ArrayList<>();
        routes.add(ChMap.routes().get(24));
        routes.add(ChMap.routes().get(30));
        routes.add(ChMap.routes().get(35));
        routes.add(ChMap.routes().get(53));
        routes.add(ChMap.routes().get(81));
        for (int i = 0; i < routes.size(); i++) {
            System.out.println(routes.get(i).station1().name() + " - " + routes.get(i).station2().name());
        }
        System.out.println("\n" + Trail.longest(routes));

    }
}
