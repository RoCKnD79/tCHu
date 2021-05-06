package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

import java.util.Set;

public interface ActionHandlers {

    interface DrawTicketsHandler{
        void onDrawTickets();
    }
    interface DrawCardHandler{
        void onDrawCard(int slot);
    }
    interface ClaimRouteHandler{
        void onClaimRoute(Route route, SortedBag<Card> cardSet);
    }
    interface ChooseTicketsHandler{
        void onChooseTickets(SortedBag<Ticket> ticketSet);
    }
    interface ChooseCardsHandler{
        void onChooseCards(SortedBag<Card> cardSet);
    }


}
