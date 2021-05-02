package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.*;

public final class TestClient {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting client!");
        RemotePlayerClient playerClient = new RemotePlayerClient(new TestPlayer(), "localhost", 5108);
        playerClient.run();
        System.out.println("Client done!");
    }

    private final static class TestPlayer implements Player{

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            System.out.printf("ownId: %s\n", ownId);
            System.out.printf("playerNames: %s\n", playerNames);
        }

        @Override
        public void receiveInfo(String info) {
            System.out.println(info);
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            System.out.printf("ownId: %s\n", newState);
            System.out.printf("playerNames: %s\n", ownState);

        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            return null;
        }

        @Override
        public TurnKind nextTurn() {
            System.out.println("ENTERED NEXT TURN");
            List<TurnKind> nextTurns = new ArrayList<>(TurnKind.ALL);
            Collections.shuffle(nextTurns);
            System.out.println("next turn : " + nextTurns.get(0));
            return nextTurns.get(0);
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            return null;
        }

        @Override
        public int drawSlot() {
            return 0;
        }

        @Override
        public Route claimedRoute() {
            return null;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return null;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            return null;
        }
    }
}
