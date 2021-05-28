package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/*
  @author Christopher Soriano (326354)
 */

/**
 *interface that contains five functional interfaces that represent different
 * handlers
 */
public interface ActionHandlers {


    interface DrawTicketsHandler{
        /**
         * is called when the player wants to draw tickets
         */
        void onDrawTickets();
    }

    interface DrawCardHandler{
        /**
         * called when the player wants to draw a card
         * @param slot, from 0 to 4 if slot is in face up cards and -1 if is deck
         */
        void onDrawCard(int slot);
    }
    interface ClaimRouteHandler{
        /**
         * called when a player claims a road
         * @param route route claimed
         * @param cardSet cards used to claim
         */
        void onClaimRoute(Route route, SortedBag<Card> cardSet);
    }
    interface ChooseTicketsHandler{
        /**
         * called when the palyer chooses to take tickets
         * @param ticketSet, tickets taken by player
         */
        void onChooseTickets(SortedBag<Ticket> ticketSet);
    }
    interface ChooseCardsHandler{
        /**
         * called when the player chooses to use the cards given as initial cards
         * @param cardSet, cards chosen
         */
        void onChooseCards(SortedBag<Card> cardSet);
    }


}
