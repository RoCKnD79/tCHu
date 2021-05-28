package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Roman Danylovych (327830)
 */
public class Serdes {

    private static final String COMMA = ",";
    private static final String SEMI_COLON = ";";
    private static final String COLON = ":";

    /**
     * constructor set to private since class isn't supposed to be instanciated
     */
    private Serdes(){}

    /*--------------------------------------------------------
    The following attributes in this class are all redefining:
            first: the serialize() method of Serde
            second: the deserialize() method of Serde
     --------------------------------------------------------*/

    //-----Serde to encode/decode integers-----
    public static final Serde<Integer> intSerde = Serde.of(Object::toString, Integer::parseInt);
    //-----Serde to encode/decode strings-----
    public static final Serde<String> stringSerde = Serde.of(s -> {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(s.getBytes(StandardCharsets.UTF_8));
    }, s -> {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(s);
        return new String(bytes, StandardCharsets.UTF_8);
    });
    //-----Serde to encode/decode PlayerId(s)-----
    public static final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);
    //-----Serde to encode/decode TurnKind(s)-----
    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);
    //-----Serde to encode/decode Card(s)-----
    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);
    //-----Serde to encode/decode Route(s)-----
    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());
    //-----Serde to encode/decode Ticket-----
    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());

    //-----Serde to encode/decode lists of strings-----
    public static final Serde<List<String>> stringListSerde = Serde.listOf(stringSerde, COMMA);
    //-----Serde to encode/decode lists of cards-----
    public static final Serde<List<Card>> cardListSerde = Serde.listOf(cardSerde, COMMA);
    //-----Serde to encode/decode lists of routes-----
    public static final Serde<List<Route>> routeListSerde = Serde.listOf(routeSerde, COMMA);
    //-----Serde to encode/decode sorted bags of cards
    public static final Serde<SortedBag<Card>> cardSortedBagSerde = Serde.bagOf(cardSerde, COMMA);
    //-----Serde to encode/decode sorted bags of tickets
    public static final Serde<SortedBag<Ticket>> ticketSortedBagSerde = Serde.bagOf(ticketSerde, COMMA);
    //-----Serde to encode/decode lists of sorted bags of cards-----
    public static final Serde<List<SortedBag<Card>>> cardSortedBagListSerde = Serde.listOf(cardSortedBagSerde, SEMI_COLON);

    //-----Serde to encode/decode publicCardState(s)-----
    public static final Serde<PublicCardState> publicCardStateSerde = Serde.of(
            publicCardState -> {
                String faceUpCards = cardListSerde.serialize(publicCardState.faceUpCards());
                String deckSize = intSerde.serialize(publicCardState.deckSize());
                String discardsSize = intSerde.serialize(publicCardState.discardsSize());

                List<String> list = new ArrayList<>();
                list.add(faceUpCards);
                list.add(deckSize);
                list.add(discardsSize);

                return String.join(SEMI_COLON, list);
            },
            s -> {
                String[] splitInitString = s.split(Pattern.quote(SEMI_COLON), -1);

                List<Card> faceUpCards = cardListSerde.deserialize(splitInitString[0]);
                int deckSize = intSerde.deserialize(splitInitString[1]);
                int discardsSize = intSerde.deserialize(splitInitString[2]);


                return new PublicCardState(faceUpCards, deckSize, discardsSize);
            }
    );
    //-----Serde to encode/decode PublicPlayerState(s)-----
    public static final Serde<PublicPlayerState> publicPlayerStateSerde = Serde.of(
      publicPlayerState -> {
          String ticketCount = intSerde.serialize(publicPlayerState.ticketCount());
          String cardCount = intSerde.serialize(publicPlayerState.cardCount());
          String routes = routeListSerde.serialize(publicPlayerState.routes());

          List<String> list = new ArrayList<>();
          list.add(ticketCount);
          list.add(cardCount);
          list.add(routes);

          return String.join(SEMI_COLON, list);
      },
      s -> {
          String[] splittedInitString = s.split(Pattern.quote(SEMI_COLON), -1);

          int ticketCount = intSerde.deserialize(splittedInitString[0]);
          int cardCount = intSerde.deserialize(splittedInitString[1]);
          List<Route> routes = routeListSerde.deserialize(splittedInitString[2]);

          return new PublicPlayerState(ticketCount, cardCount, routes);
      }
    );
    //-----Serde to encode/decode PlayerState(s)-----
    public static final Serde<PlayerState> playerStateSerde = Serde.of(
      playerState -> {
          String tickets = ticketSortedBagSerde.serialize(playerState.tickets());
          String cards = cardSortedBagSerde.serialize(playerState.cards());
          String routes = routeListSerde.serialize(playerState.routes());

          List<String> list = new ArrayList<>();
          list.add(tickets);
          list.add(cards);
          list.add(routes);

          return String.join(SEMI_COLON, list);
      },
      s -> {
          String[] splittedInitString = s.split(Pattern.quote(SEMI_COLON), -1);

          SortedBag<Ticket> ticketCount = ticketSortedBagSerde.deserialize(splittedInitString[0]);
          SortedBag<Card> cards = cardSortedBagSerde.deserialize(splittedInitString[1]);
          List<Route> routes = routeListSerde.deserialize(splittedInitString[2]);

          return new PlayerState(ticketCount, cards, routes);
      }
    );
    //-----Serde to encode/decode PublicGameState(s)-----
    public static final Serde<PublicGameState> publicGameStateSerde = Serde.of(
        publicGameState -> {
            String ticketsCount = intSerde.serialize(publicGameState.ticketsCount());
            String cardState = publicCardStateSerde.serialize(publicGameState.cardState());
            String currentPlayerId = playerIdSerde.serialize(publicGameState.currentPlayerId());
            String player1 = publicPlayerStateSerde.serialize(publicGameState.playerState(PlayerId.PLAYER_1));
            String player2 = publicPlayerStateSerde.serialize(publicGameState.playerState(PlayerId.PLAYER_2));
            String lastPlayer = playerIdSerde.serialize(publicGameState.lastPlayer());

            List<String> list = new ArrayList<>();
            list.add(ticketsCount);
            list.add(cardState);
            list.add(currentPlayerId);
            list.add(player1);
            list.add(player2);
            list.add(lastPlayer);

            return String.join(COLON, list);
        },
        s -> {
            String[] splitInitString = s.split(Pattern.quote(COLON), -1);

            int ticketsCount = intSerde.deserialize(splitInitString[0]);
            PublicCardState cardState = publicCardStateSerde.deserialize(splitInitString[1]);
            PlayerId currentPlayerId = playerIdSerde.deserialize(splitInitString[2]);
            PublicPlayerState player1 = publicPlayerStateSerde.deserialize(splitInitString[3]);
            PublicPlayerState player2 = publicPlayerStateSerde.deserialize(splitInitString[4]);
            PlayerId lastPlayer = playerIdSerde.deserialize(splitInitString[5]);

            Map<PlayerId, PublicPlayerState> map = new EnumMap<>(PlayerId.class);
            map.put(PlayerId.PLAYER_1, player1);
            map.put(PlayerId.PLAYER_2, player2);

            return new PublicGameState(ticketsCount, cardState, currentPlayerId, map, lastPlayer);
        }
    );
}
