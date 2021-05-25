package ch.epfl.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.ActionHandlers;
import ch.epfl.tchu.gui.GraphicalPlayer;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
import static org.junit.Assert.assertEquals;

public final class GraphicalPlayerTest extends Application {
    private void setState(GraphicalPlayer player) {
        PlayerState p1State =
                new PlayerState(SortedBag.of(ChMap.tickets().subList(0, 3)),
                        SortedBag.of(1, Card.BLUE, 3, Card.RED),
                        ChMap.routes().subList(0, 3));

        PublicPlayerState p2State =
                new PublicPlayerState(0, 0, ChMap.routes().subList(3, 6));

        Map<PlayerId, PublicPlayerState> pubPlayerStates =
                Map.of(PlayerId.PLAYER_1, p1State, PlayerId.PLAYER_2, p2State);
        PublicCardState cardState =
                //new PublicCardState(Card.ALL.subList(0, 5), 110 - 2 * 4 - 5, 0);
                new PublicCardState(Card.ALL.subList(4, 9), 110 - 2 * 4 - 5, 0);
        PublicGameState publicGameState =
                new PublicGameState(36, cardState, PlayerId.PLAYER_1, pubPlayerStates, null);
        // … construit exactement les mêmes états que la méthode setState
        // du test de l'étape 9
        player.setState(publicGameState, p1State);



        /*SortedBag<Ticket> sb1 = SortedBag.of(1, ChMap.tickets().get(0));
        SortedBag<Ticket> sb2 = SortedBag.of(1, ChMap.tickets().get(2));
        SortedBag<Ticket> sb3 = SortedBag.of(1, ChMap.tickets().get(3));
        SortedBag<Ticket> sb4 = SortedBag.of(1, ChMap.tickets().get(4));
        SortedBag<Ticket> sb5 = SortedBag.of(1, ChMap.tickets().get(5));
        SortedBag.Builder<Ticket> sbb = new SortedBag.Builder<>();
        sbb.add(sb1); sbb.add(sb2); sbb.add(sb3); sbb.add(sb4); sbb.add(sb5);

        player.chooseTickets(sbb.build(), e -> System.out.println("chooseTickets lancé dans GraphicalPlayerTest"));*/

        SortedBag<Card> cs1 = SortedBag.of(2, Card.BLUE);
        SortedBag<Card> cs2 = SortedBag.of(2, Card.GREEN);
        SortedBag<Card> cs3 = SortedBag.of(2, Card.RED);
        SortedBag<Card> cs4 = SortedBag.of(2, Card.YELLOW);
        SortedBag<Card> cs5 = SortedBag.of(2, Card.WHITE);
        List<SortedBag<Card>> cardList = new ArrayList<>();
        cardList.add(cs1); cardList.add(cs2); cardList.add(cs3); cardList.add(cs4); cardList.add(cs5);

        //player.chooseClaimCards(cardList, e -> System.out.println("(GraphicalPlayerTest) chooseClaimCards"));

        player.chooseAdditionalCards(cardList, e -> System.out.println("(GraphicalPlayerTest) chooseAdditionalCards"));

    }

    @Override
    public void start(Stage primaryStage) {
        Map<PlayerId, String> playerNames =
                Map.of(PLAYER_1, "Ada", PLAYER_2, "Charles");
        GraphicalPlayer p = new GraphicalPlayer(PLAYER_1, playerNames);
        setState(p);

        ActionHandlers.DrawTicketsHandler drawTicketsH =
                () -> p.receiveInfo("Je tire des billets !\n");
        ActionHandlers.DrawCardHandler drawCardH =
                s -> p.receiveInfo(String.format("Je tire une carte de %s !\n", s));
        ActionHandlers.ClaimRouteHandler claimRouteH =
                (r, cs) -> {
                    String rn = r.station1() + " - " + r.station2();
                    p.receiveInfo(String.format("Je m'empare de %s avec %s\n", rn, cs));
                };

        p.startTurn(drawTicketsH, drawCardH, claimRouteH);
    }

    @Test
    void testToStringDeCardConverter() {
        /*GraphicalPlayer.CardBagStringConverter c = new GraphicalPlayer.CardBagStringConverter();
        SortedBag<Card> b = SortedBag.of(1, Card.VIOLET, 3, Card.RED);
        assertEquals("1 violette et 3 rouges", c.toString(b));*/
    }
}