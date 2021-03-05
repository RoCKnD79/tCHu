package ch.epfl.tchu.game;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

public class RouteTest {

    @Test
    void possibleClaimCardsTest(){
        //System.out.println("hello");
        //System.out.println(ChMap.routes().get(1).color());
        for(int i = 1; i < ChMap.routes().size(); ++i){
            Route routeTest = new Route(ChMap.routes().get(i).id(), ChMap.routes().get(i).station1(), ChMap.routes().get(i).station2(), ChMap.routes().get(i).length(), ChMap.routes().get(i).level(), ChMap.routes().get(i).color());
            System.out.println(routeTest.possibleClaimCards() + "station1;" + routeTest.station1() + " station 2 : " + routeTest.station2());
        }
        //Route routeTest = new Route(ChMap.routes().get(3).id(), ChMap.routes().get(3).station1(), ChMap.routes().get(3).station2(), ChMap.routes().get(3).length(), ChMap.routes().get(3).level(), ChMap.routes().get(3).color());
        //System.out.println(routeTest.possibleClaimCards());
        System.out.println(ChMap.routes().get(3).level());
        System.out.println(ChMap.routes().get(1).color());

    }
    @Test
    void stationTest(){
        Route routeTest = new Route(ChMap.routes().get(1).id(), ChMap.routes().get(1).station1(), ChMap.routes().get(1).station2(), ChMap.routes().get(1).length(), ChMap.routes().get(1).level(), ChMap.routes().get(1).color());
        System.out.println(routeTest.stations());
        System.out.println(routeTest.station1() + " " + routeTest.station2());
    }

    @Test
    void stationOppositeTest(){
        Route routeTest = new Route(ChMap.routes().get(1).id(), ChMap.routes().get(1).station1(), ChMap.routes().get(1).station2(), ChMap.routes().get(1).length(), ChMap.routes().get(1).level(), ChMap.routes().get(1).color());
        System.out.println(routeTest.stationOpposite(routeTest.station1()));
        System.out.println(routeTest.stationOpposite(routeTest.station2()));
        System.out.println(routeTest.station1() + " " + routeTest.station2());
    }


    /*
    @Test
    void possibleClaimCardsAdditional(){
        SortedBag<Card> cardsClaimed;
        cardsClaimed = SortedBag.of(1, Card.CARS.get(2));
        SortedBag<Card> additionalCards;
        List<Card> additionalCardList = new ArrayList<>();
        additionalCardList.add(Card.CARS.get(1));
        additionalCardList.add(Card.CARS.get(1));
        additionalCardList.add(Card.CARS.get(1));
        additionalCards = SortedBag.of(additionalCardList);

       /* additionalCards = SortedBag.of(1, Card.CARS.get(3));
        additionalCards = SortedBag.of(1, Card.CARS.get(4));
        System.out.println("additional cards size : " + additionalCards.size());
        for(int i = 1; i < ChMap.routes().size(); ++i){
            Route routeTest = new Route(ChMap.routes().get(i).id(), ChMap.routes().get(i).station1(), ChMap.routes().get(i).station2(), ChMap.routes().get(i).length(), ChMap.routes().get(i).level(), ChMap.routes().get(i).color());
            System.out.println(ChMap.routes().get(i).level());
            System.out.println("Cards claimed : " + cardsClaimed.get(0));
            System.out.println("additional Cards : " + (additionalCards));
            System.out.println("number of Cards additional : " + routeTest.additionalClaimCardsCount(cardsClaimed, additionalCards));
        }
    }*/
/*
    @Test
    void claimPointsTest(){
        for(int i = 1; i < ChMap.routes().size(); ++i) {
            Route routeTest1 = new Route(ChMap.routes().get(i).id(), ChMap.routes().get(i).station1(), ChMap.routes().get(i).station2(), ChMap.routes().get(i).length(), ChMap.routes().get(i).level(), ChMap.routes().get(i).color());
            System.out.println("possible claim points : " + routeTest1.claimPoints() + "station1 : " + routeTest1.station1() + "station 2 : " + routeTest1.station2());
            //System.out.println("station1 : " + routeTest1.station1() + "station 2 : " + routeTest1.station2());
        }
    }*/
}
