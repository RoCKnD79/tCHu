package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Christopher Soriano (326354)
 */
public final class PlayerState extends PublicPlayerState {
    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;
    private final List<Route> routes;


    /**
     * Constructor of player state
     *
     * @param tickets, tickets of the player
     * @param cards,   cards of the player
     * @param routes,  routes of the player
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;
        this.routes = routes;
    }

    /**
     * Method that "initializes" the player's state with his initial card given at the beginning at the game
     *
     * @param initialCards, sorted list of player's initial cards
     * @return a new player state with the initial cards
     * @throws IllegalArgumentException, if there are more or less than 4 initial cards
     */
    public static PlayerState initial(SortedBag<Card> initialCards) throws IllegalArgumentException {
        if (initialCards.size() != 4) {
            throw new IllegalArgumentException("There must exactly be 4 initial cards");
        }
        SortedBag<Ticket> tickets = SortedBag.of();
        List<Route> routes = new ArrayList<>();
        return new PlayerState(tickets, initialCards, routes);
    }

    /**
     * returns the tickets of the player
     *
     * @return tickets of player
     */
    public SortedBag<Ticket> tickets() {
        return SortedBag.of(tickets);
    }

    /**
     * Creates a new PlayerState with a new set of additional tickets added to the original
     *
     * @param newTickets, tickets that are added to the original list of tickets
     * @return new PlayerState with additional tickets
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets) {
        return new PlayerState(tickets.union(newTickets), this.cards, this.routes);
    }

    /**
     * Method that returns the cards that the player has
     *
     * @return cards of the player
     */
    public SortedBag<Card> cards() {
        return this.cards;
    }

    /**
     * Creates a new state for the player with an added card to his cards
     *
     * @param card, the card to add to original list of cards
     * @return a new PlayerState with card added to original list
     */
    public PlayerState withAddedCard(Card card) {
        SortedBag<Card> newCard = SortedBag.of(card);
        return new PlayerState(this.tickets, cards.union(newCard), this.routes);
    }

//dévore moi le poireau
    /**
     * tests if given route can be claimed with the cards the player has
     *
     * @param route the player wants to claim
     * @return true if claim is possible
     */
    public boolean canClaimRoute(Route route) {
        return (this.carCount() >= route.length() && possibleClaimCards(route).size() != 0);
    }

    /**
     * calculates a list of all possible card combinations the player can use to claim the given route
     *
     * @param route, route we want to claim
     * @return list of all possible claim card combinations
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {

        int length = route.length();

        if (this.carCount() < length) {
            throw new IllegalArgumentException("Number of cars must be at least " + length);
        }

        Set<Card> availableCards = cards.toSet();
        List<SortedBag<Card>> list = new ArrayList<>();

        if (route.level().equals(Route.Level.OVERGROUND) || !cards.contains(Card.LOCOMOTIVE)) {
            if (route.color() == null) {
                for (Card c : availableCards) {
                    if (c.equals(Card.LOCOMOTIVE)) continue;
                    if (cards.countOf(c) >= length) {
                        list.add(SortedBag.of(length, c));
                    }
                }
            } else {
                if (cards.countOf(Card.of(route.color())) >= length) {
                    list.add(SortedBag.of(length, Card.of(route.color())));
                }
            }

        } else {
            if (route.color() == null) {
                if (cards.countOf(Card.LOCOMOTIVE) >= length) {
                    list.add(SortedBag.of(length, Card.LOCOMOTIVE));
                }
            } else {
                for (int i = 0; i <= length; ++i) {
                    if (cards.countOf(Card.of(route.color())) >= length - i && cards.countOf(Card.LOCOMOTIVE) >= i) {
                        list.add(SortedBag.of(length - i, Card.of(route.color()), i, Card.LOCOMOTIVE));
                    }
                }
            }

        }
        return list;
    }


    /**
     * if initial cards are only locomotive cards, then the player will only be able to play additionalCardsCount locomotive cards
     * (if he has the sufficient amount of locomotive cards available)
     * if initial cards are composed of (only color cards) OR (color cards and locomotives), then the player will be able to play
     * additionalCardsCount cards of different combinations,
     * ex: if additionalCardsCount is 2 and the player initially used 1 blue and 1 locomotive,
     * then the player may play {2xB}, {1xB, 1xL} or {2xL}
     *
     * @param additionalCardsCount, number of additional cards the player will need to play
     * @param initialCards,         the cards the player played initially to claim the tunnel, before drawing the additional cards
     * @param drawnCards,           the 3 cards drawn by the player after he played his initialCards to begin claiming a tunnel
     * @return a List of possible additional cards the player is able to play in order to finish claiming the tunnel
     * @throws IllegalArgumentException if the number of additionalCards (=> additionalCardsCount) is < 1 or > 3
     *                                  if initialCards is empty or more than 2 types of cards (2 types because at most
     *                                  you can use both a certain color and locomotive cards to claim a tunnel)
     *                                  if the number of drawnCards isn't exactly 3
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards) throws IllegalArgumentException {

        if ((additionalCardsCount < 1) || (additionalCardsCount > 3)) {
            throw new IllegalArgumentException("additionnal card count is not between 1 and 3 included");
        }
        if (initialCards.isEmpty() || initialCards.toSet().size() > 2) {
            throw new IllegalArgumentException("inital cards list is empty or contains two different type of cards");
        }
        List<SortedBag<Card>> list = new ArrayList<>();
        SortedBag<Card> cardsLeft = cards.difference(initialCards);

        if (cardsLeft.size() < additionalCardsCount) {
            System.out.println("not enough cards");
            return List.of();
        }

        if (initialCards.countOf(Card.LOCOMOTIVE) == initialCards.size()) {
            if (cardsLeft.countOf(Card.LOCOMOTIVE) >= additionalCardsCount) {
                return List.of(SortedBag.of(additionalCardsCount, Card.LOCOMOTIVE));
            }
        } else {
            for (int i = 0; i <= additionalCardsCount; ++i) {
                for (Card c : initialCards.toSet()) {
                    if (c.equals(Card.LOCOMOTIVE)) continue;
                    if (cardsLeft.countOf(c) >= additionalCardsCount - i && cards.countOf(Card.LOCOMOTIVE) >= i) {
                        list.add(SortedBag.of(additionalCardsCount - i, c, i, Card.LOCOMOTIVE));
                    }
                }
            }
        }

        return list;
    }

    /**
     * @param route,      claimed route
     * @param claimCards, cards used to claim route
     * @return new PlayerState instance with player's routes and cards updated
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards) {
        List<Route> temp = new ArrayList<>(routes);
        temp.add(route);
        return new PlayerState(tickets, cards.difference(claimCards), temp);
    }

    /**
     * @return number of points the player can gain (or lose) with the tickets he has (depends on the routes he has)
     */
    public int ticketPoints() {
        int largestId = 0;
        for (Route r : routes) {
            int max = Math.max(r.station1().id(), r.station2().id());
            if (max > largestId) {
                largestId = max;
            }
        }

        StationPartition.Builder partitionBuild = new StationPartition.Builder(largestId + 1);

        for (Route r : routes) {
            partitionBuild.connect(r.station1(), r.stationOpposite(r.station1()));
        }

        StationPartition partition = partitionBuild.build();

        int ticketPoints = 0;
        for (Ticket t : tickets) {
            ticketPoints += t.points(partition);
        }

        return ticketPoints;
    }

    /**
     * calculates total points by adding claimPoints to the points earned thanks to tickets
     *
     * @return total points the player won/lost => claim points + ticket points
     */
    public int finalPoints() {
        return claimPoints() + ticketPoints();
    }

}
