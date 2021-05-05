package ch.epfl.tchu.gui;

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
        void onClaimRoute(Route route, Set<Card> cardSet);
    }
    interface ChooseTicketsHandler{
        void onChooseTickets(Set<Ticket> ticketSet);
    }
    interface ChooseCardsHandler{
        void onChooseCards(Set<Card> cardSet);
    }


}
