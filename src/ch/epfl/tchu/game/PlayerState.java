package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Christopher Soriano (326354)
 */
public final class PlayerState extends PublicPlayerState {
    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;
    private final List<Route> routes;


    /**
     * Constructor of player state
     * @param tickets, tickets of the player
     * @param cards, cards of the player
     * @param routes, routes of the player
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes){
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;
        this.routes = routes;
    }

    /**
     * Method that "initializes" the player's state with his initial card given at the beginning at the game
     * @param initialCards, sorted list of player's initial cards
     * @return a new player state with the initial cards
     * @throws IllegalArgumentException, if there are more or less than 4 initial cards
     */
    public static PlayerState initial(SortedBag<Card> initialCards) throws IllegalArgumentException{
        if (initialCards.size() != 4){
            throw new IllegalArgumentException("There are more or less than 4 initial cards");
        }
        SortedBag<Ticket> tickets = SortedBag.of();
        List<Route> routes = new ArrayList<>();
        return new PlayerState(tickets, initialCards, routes);
    }

    /**
     * gives the tickets of the player
     * @return tickets of player
     */
    private SortedBag<Ticket> ticket(){
        return tickets;
    }

    /**
     * Creates a new PlayerState with a new set of additional tickets added to the original
     * @param newTickets, tickets that are added to the original list of tickets
     * @return new PlayerState with additional tickets
     */
    private PlayerState withAddedTickets(SortedBag<Ticket> newTickets){

        List<Ticket> tickets = this.tickets.toList();

        for(Ticket c : newTickets){
            tickets.add(c);
        }

        SortedBag.Builder<Ticket> ticketSortedBuilder = new SortedBag.Builder<>();
        for(Ticket c : tickets){
            ticketSortedBuilder.add(c);
        }
        SortedBag<Ticket> withAddedTickets = ticketSortedBuilder.build();

        return new PlayerState(withAddedTickets, this.cards, this.routes);
    }

    /**
     * Method that gives the cards that the player has
     * @return cards of the palyer
     */
    private SortedBag<Card> cards(){
        return this.cards;
    }

    /**
     * Creates a new state for the player with an added card to his cards
     * @param card, the card to add to original list of cards
     * @return a new PlayerState with card added to original list
     */
    private PlayerState withAddedCard(Card card){
        List<Card> cards = this.cards.toList();
        cards.add(card);

        SortedBag.Builder<Card> cardSortedBuilder = new SortedBag.Builder<>();
        for(Card c : cards){
            cardSortedBuilder.add(c);
        }
        SortedBag<Card> withAddedCard = cardSortedBuilder.build();

        return new PlayerState(this.tickets, withAddedCard, this.routes);
    }

    /**
     * Creates a new state for the player with additional cards to his cards
     * @param additionalCards, the sorted list of cards to add to original list of cards
     * @return a new PlayerState with additional cards added to original list
     */
    private PlayerState withAddedCards(SortedBag<Card> additionalCards){
        List<Card> cards = this.cards.toList();
        for(Card c : additionalCards) {
            cards.add(c);
        }

        SortedBag.Builder<Card> cardSortedBuilder = new SortedBag.Builder<>();
        for(Card c : cards){
            cardSortedBuilder.add(c);
        }
        SortedBag<Card> withAddedCard = cardSortedBuilder.build();

        return new PlayerState(this.tickets, withAddedCard, this.routes);
    }

    //TODO
    private boolean canClaimRoute(Route route) {
        /*Ici, il faut mettre deux if séparés car possibleClaimCards ne doit être appelée que si le joueur a assez de wagons (piazza)*/

        if ((carCount >= route.length())){
            if (route.possibleClaimCards() //TODO)//
            return true;
        }
        return false;
    }

    private List<SortedBag<Card>> possibleClaimCards(Route route) throws IllegalArgumentException{
        List<SortedBag<Card>> list = new ArrayList<>();
        Set<Card> setOfCardsOwnedByPlayer = new TreeSet<>(cards.toSet());
        if((carCount < route.length())){
            throw new IllegalArgumentException("Player doesn't have enough cars to get the road");
        }
        if (route.level().equals(Route.Level.OVERGROUND)){
            if (route.color() == null){
               for(Card c : setOfCardsOwnedByPlayer){
                   if(cards.countOf(c) == route.length()) {
                       if(c.equals(Card.LOCOMOTIVE)) {
                           continue;
                       }
                       list.add(SortedBag.of(route.length(), c));
                   }
               }
            }else{
                if ((cards.contains(Card.of(route.color()))) && (cards.countOf(Card.of(route.color()))) == route.length()){
                    list.add(SortedBag.of(route.length(), Card.of(route.color())));
                }
            }
        }else{
            if(route.color() == null){
                for(Card c : setOfCardsOwnedByPlayer){
                    for (int i = 0; i <= route.length(); ++i) {
                        /*if(!(c.equals(Card.LOCOMOTIVE))) {
                            list.add(SortedBag.of(route.length() - i, c, i, Card.LOCOMOTIVE));
                        }*/
                        if(cards.countOf(c) < route.length()-i || cards.countOf(Card.LOCOMOTIVE) < i) {
                            continue;
                        }
                    }
                }
            }else{
                if ((cards.contains(Card.of(route.color()))) && (cards.countOf(Card.of(route.color()))) == route.length()){
                    for(int j = 0; j <= cards.countOf(Card.LOCOMOTIVE); ++j) {
                        for (int i = 0; i <= route.length(); ++i) {
                            list.add(SortedBag.of(length - i, Card.of(color), i, Card.LOCOMOTIVE));
                        }
                    }
                }
            }
            }
        }

       return route.possibleClaimCards();
    }

    private List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards) throws IllegalArgumentException {
        if ((additionalCardsCount < 1) || (additionalCardsCount > 3)) {
            throw new IllegalArgumentException("additionnal card count is not between 1 and 3 included");
        }
        if (initialCards.isEmpty()) ||() //TODO )
        {

        throw new IllegalArgumentException("inital cards list is empty or contains two different type of cards");
        }
        if(drawnCards.size() != 3){
            throw new IllegalArgumentException("the number of drawn cards is not equal to 3");
        }

        //removes intial cards from hand of player (in a new list !)
        List<Card> playersCardsWithoutInitial = SortedBag.of(cards).toList();
        for(Card c : initialCards){
            playersCardsWithoutInitial.remove(c);
        }

        //Ce n'est pas un multiensemble ? je sai spas ce que c'est un multiensemble
        List<Card> playableCards = new ArrayList<>();
        for(Card c : playersCardsWithoutInitial){
            if((c.color().equals(initialCards.get(0).color())) || c.equals(Card.LOCOMOTIVE)){
                playableCards.add(c);
            }
        }


    }
}
