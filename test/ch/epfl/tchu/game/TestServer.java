package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.net.RemotePlayerProxy;
import ch.epfl.test.TestRandomizer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class TestServer {
    public static void main(String[] args) throws IOException{
        System.out.println("Starting Server !");
        try(ServerSocket serverSocket = new ServerSocket(5108);
            Socket socket = serverSocket.accept()) {
                Player playerProxy = new RemotePlayerProxy(socket);
                var playerNames = Map.of(PlayerId.PLAYER_1, "Ada", PlayerId.PLAYER_2, "Charles");
                //playerProxy.initPlayers(PlayerId.PLAYER_1, playerNames);
                //playerProxy.nextTurn();

                var faceUpCards = SortedBag.of(5, Card.LOCOMOTIVE).toList();
                var cardState = new PublicCardState(faceUpCards, 0, 0);
                var initialPlayerState = (PublicPlayerState) PlayerState.initial(SortedBag.of(4, Card.RED));
                var playerState = Map.of(
                        PLAYER_1, initialPlayerState,
                        PLAYER_2, initialPlayerState);

            var rng = TestRandomizer.newRandom();
            var chMap = new PlayerStateTest.ChMap();
            var routes = new ArrayList<>(chMap.ALL_ROUTES);
            var tickets = new ArrayList<>(chMap.ALL_TICKETS);
            var cards = new ArrayList<Card>(shuffledCards(rng));
            for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
                Collections.shuffle(routes, rng);
                Collections.shuffle(tickets, rng);
                Collections.shuffle(cards, rng);

                var routesCount = rng.nextInt(7);
                var ticketsCount = rng.nextInt(tickets.size());
                var cardsCount = rng.nextInt(cards.size());

                var playerRoutes = Collections.unmodifiableList(routes.subList(0, routesCount));
                var playerTickets = SortedBag.of(tickets.subList(0, ticketsCount));
                var playerCards = SortedBag.of(cards.subList(0, cardsCount));

                var newPlayerState = new PlayerState(playerTickets, playerCards, playerRoutes);
                PublicGameState publicGameState = new PublicGameState(12,cardState, PlayerId.PLAYER_1,  playerState, PLAYER_1);
                playerProxy.updateState(publicGameState, newPlayerState);
        }
        System.out.println("Server done ! thanks cuh");
}

}