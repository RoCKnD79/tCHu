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

        /*Route route1 = ChMap.routes().get(48);
        System.out.println(route1.id());
        for(SortedBag<Card> c : route1.possibleClaimCards()) {
            System.out.println(c);
        }*/

        Route route1 = ChMap.routes().get(41);
        System.out.println(route1.id());
        for(SortedBag<Card> c : route1.possibleClaimCards()) {
            System.out.println(c);
        }

    }
}
