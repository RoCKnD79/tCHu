package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class PublicPlayerStateTest {

List<Route> routes = new ArrayList<>();

    @Test
    public void publicPlayerStateConstructor(){
        for(int i = 1; i < 5; ++i) {
            routes.add(ChMap.routes().get(i));
        }

       PublicPlayerState normalPublicPlayerState = new PublicPlayerState(1, 2, routes);
       assertThrows(IllegalArgumentException.class, () -> {
           PublicPlayerState withNegativeTicketCount = new PublicPlayerState(-1, 2, routes);

       });
        assertThrows(IllegalArgumentException.class, () -> {
            PublicPlayerState withNegativeCardCount = new PublicPlayerState(1, -2, routes);

        });
        System.out.println("ticket count : " + normalPublicPlayerState.ticketCount());
        System.out.println("card count : " + normalPublicPlayerState.cardCount());
    }

    @Test
    public void carCountTest(){
        for(int i = 1; i < 5; ++i) {
            routes.add(ChMap.routes().get(i));
            System.out.println( "route length" + routes.get(i-1).length());
        }
        PublicPlayerState normalPublicPlayerState = new PublicPlayerState(1, 2, routes);
        System.out.println(normalPublicPlayerState.carCount());
    }

    @Test
    public void carCountEmptyRouteTest(){
        PublicPlayerState normalPublicPlayerState = new PublicPlayerState(1, 2, routes);
        System.out.println(normalPublicPlayerState.carCount());
    }

    @Test
    public void claimPointsTest(){
        for(int i = 1; i < 2; ++i) {
            routes.add(ChMap.routes().get(i));
        }
        System.out.println(ChMap.routes().get(1).claimPoints());
        PublicPlayerState normalPublicPlayerState = new PublicPlayerState(1, 2, routes);
        System.out.println("number of points earned : " + normalPublicPlayerState.claimPoints() );
    }
}
