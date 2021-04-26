package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Route.Level;
import ch.epfl.tchu.net.Serde;
import ch.epfl.tchu.net.Serdes;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SerdesTest {

    @Test
    void intSerdeTest() {
        for (int i = -9; i < 10; ++i) {
            String ser = Serdes.intSerde.serialize(i);
            System.out.print(ser + ", ");
            assertEquals(Integer.toString(i), ser);

            int de = Serdes.intSerde.deserialize(ser);
            System.out.println(de);
            assertEquals(i, de);
        }
        for (int i = -754; i < -673; ++i) {
            String ser = Serdes.intSerde.serialize(i);
            assertEquals(Integer.toString(i), ser);

            int de = Serdes.intSerde.deserialize(ser);
            assertEquals(i, de);
        }
        for (int i = 2021; i < 2600; ++i) {
            String ser = Serdes.intSerde.serialize(i);
            assertEquals(Integer.toString(i), ser);

            int de = Serdes.intSerde.deserialize(ser);
            assertEquals(i, de);
        }
    }

    @Test
    void stringSerdeTest1() {
        Base64.Encoder encoder = Base64.getEncoder();
        String ser = Serdes.stringSerde.serialize("mémoire");
        String enc = encoder.encodeToString("mémoire".getBytes(StandardCharsets.UTF_8));
        assertEquals(enc, ser);

        Base64.Decoder decoder = Base64.getDecoder();
        String de = Serdes.stringSerde.deserialize(ser);
        byte[] bytes = decoder.decode(ser);
        assertEquals(new String(bytes, StandardCharsets.UTF_8), de);
    }

    @Test
    void stringSerdeTest2() {
        byte[] b = new byte[1];
        for (int i = 0; i < 128; ++i) {
            b[0] = (byte) i;
            Base64.Encoder encoder = Base64.getEncoder();
            String ser = Serdes.stringSerde.serialize(Character.toString((char) b[0]));
            String enc = encoder.encodeToString(b);
            assertEquals(enc, ser);

            Base64.Decoder decoder = Base64.getDecoder();
            String de = Serdes.stringSerde.deserialize(ser);
            byte[] bytes = decoder.decode(ser);
            assertEquals(new String(bytes, StandardCharsets.UTF_8), de);
        }
    }

    //------------------ENUM------------------

    @Test
    void playerIdSerdeTest() {
        Serde<PlayerId> serde = Serdes.playerIdSerde;
        List<PlayerId> list = PlayerId.ALL;

        for (PlayerId id : list) {
            String ser = serde.serialize(id);
            assertEquals(Integer.toString(list.indexOf(id)), ser);

            PlayerId playerId = serde.deserialize(ser);
            assertEquals(id, playerId);
        }
    }

    @Test
    void turnKindSerdeTest() {
        Serde<Player.TurnKind> serde = Serdes.turnKindSerde;
        List<Player.TurnKind> list = Player.TurnKind.ALL;

        for (Player.TurnKind tk : list) {
            String ser = serde.serialize(tk);
            assertEquals(Integer.toString(list.indexOf(tk)), ser);

            Player.TurnKind turnKind = serde.deserialize(ser);
            assertEquals(tk, turnKind);
        }
    }

    @Test
    void cardSerdeTest() {
        for (Card c : Card.ALL) {
            String ser = Serdes.cardSerde.serialize(c);
            assertEquals(Integer.toString(Card.ALL.indexOf(c)), ser);

            Card card = Serdes.cardSerde.deserialize(ser);
            assertEquals(c, card);
        }
    }

    @Test
    void ticketSerdeTest() {
        Serde<Ticket> serde = Serdes.ticketSerde;
        List<Ticket> list = ChMap.tickets();

        for (Ticket t : list) {
            String ser = serde.serialize(t);
            assertEquals(Integer.toString(list.indexOf(t)), ser);

            Ticket ticket = serde.deserialize(ser);
            assertEquals(t, ticket);
        }
    }

    @Test
    void routeSerdeTest() {
        Serde<Route> serde = Serdes.routeSerde;
        List<Route> list = ChMap.routes();
        for (Route r : list) {
            String ser = serde.serialize(r);
            assertEquals(Integer.toString(list.indexOf(r)), ser);

            Route route = serde.deserialize(ser);
            assertEquals(r, route);
        }


    }

    //------------------LISTS------------------

    @Test
    void stringListSerde() {

    }

    @Test
    void cardListSerdeTest() {
        Serde<List<Card>> serde = Serdes.cardListSerde;
        List<Card> cards = Card.ALL;

        List<String> s = new ArrayList<>();
        for(int i = 0; i < cards.size(); ++i) { s.add(Integer.toString(i)); }

        String ser = serde.serialize(cards);
        assertEquals(String.join(Punctuation.COMMA, s), ser);

        List<Card> de = serde.deserialize(ser);
        for(int i = 0; i < cards.size(); ++i) {
            assertEquals(cards.get(i), de.get(i));
        }
    }



    @Test
    void publicGameStateSerdeTest() {



    }


    private static String displayPublicCardState(PublicCardState pcs) {

        StringJoiner sj = new StringJoiner(", ");
        for(Card c : pcs.faceUpCards()) { sj.add(c.toString()); }

        sj.add(Integer.toString(pcs.deckSize()));
        sj.add(Integer.toString(pcs.discardsSize()));

        return sj.toString();
    }

    private static String displayPublicPlayerState(PublicPlayerState pps) {
        StringJoiner sj = new StringJoiner(", ");

        sj.add(Integer.toString(pps.ticketCount()));
        sj.add(Integer.toString(pps.cardCount()));
        sj.add(displayRoutes(pps.routes()));

        return sj.toString();
    }

    private static String displayPlayerState(PlayerState ps) {

        StringJoiner stringJ = new StringJoiner(", ");
        ps.tickets().stream().forEach(ticket -> stringJ.add(ticket.toString()));
        ps.cards().stream().forEach(card -> stringJ.add(card.toString()));
        stringJ.add(displayRoutes(ps.routes()));

        return stringJ.toString();
    }

    private static String displayRoutes(List<Route> routes) {

        System.out.println(routes.isEmpty());
        System.out.println(routes == null);

        if(routes.isEmpty() || routes == null) return "";

        StringJoiner sj = new StringJoiner(", ");
        System.out.println(routes.get(0).station1().name());
        routes.forEach(r -> sj.add(r.station1().name() + " - " + r.station2().name()));

        return sj.toString();
    }

    private static String displayPublicGameState(PublicGameState pgs) {
        StringJoiner sj = new StringJoiner(", ");

        sj.add(Integer.toString(pgs.ticketsCount()));
        sj.add(displayPublicCardState(pgs.cardState()));
        sj.add(pgs.currentPlayerId().toString());
        sj.add(displayPublicPlayerState(pgs.playerState(PlayerId.PLAYER_1)));
        sj.add(displayPublicPlayerState(pgs.playerState(PlayerId.PLAYER_2)));

        if(pgs.lastPlayer() != null)
            sj.add(pgs.lastPlayer().toString());

        return sj.toString();
    }


    private static class Punctuation {
        public static final String COMMA = ",";
        public static final String SEMI_COLON = ";";
        public static final String COLON = ":";
    }

}
