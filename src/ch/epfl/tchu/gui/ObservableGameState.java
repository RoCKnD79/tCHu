package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.*;

/**
 * @author Roman Danylovych (327830)
 */
public class ObservableGameState {

    private final PlayerId playerId;
    private PlayerState playerState;
    private PublicGameState publicGameState;

    private final IntegerProperty ticketsPercent;
    private final IntegerProperty cardsPercent;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final Map<Route, ObjectProperty<PlayerId>> routesOwners;

    private final IntegerProperty ownTicketsCount = new SimpleIntegerProperty();
    private final IntegerProperty ownCardsCount = new SimpleIntegerProperty();
    private final IntegerProperty ownCarsCount = new SimpleIntegerProperty();
    private final IntegerProperty ownPoints = new SimpleIntegerProperty();

    private final IntegerProperty rivalTicketsCount = new SimpleIntegerProperty();
    private final IntegerProperty rivalCardsCount = new SimpleIntegerProperty();
    private final IntegerProperty rivalCarsCount = new SimpleIntegerProperty();
    private final IntegerProperty rivalPoints = new SimpleIntegerProperty();

    private final ObservableList<Ticket> ticketList;
    private final Map<Card, IntegerProperty> countPerCard;
    private final Map<Route, BooleanProperty> claimableRoutes;

    /**
     * ObservableGameState's constructor
     * @param playerId, playerId to which we'd like to attach an ObservableGameState
     */
    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;
        this.publicGameState = null;
        this.playerState = null;

        ticketsPercent = new SimpleIntegerProperty(0);
        cardsPercent = new SimpleIntegerProperty(0);
        faceUpCards = initFaceUpCards();
        routesOwners = initRoutesOwners();

        ticketList = FXCollections.observableArrayList();
        countPerCard = initCardCountMap();
        claimableRoutes = initClaimableRoutesMap();
    }

    /**
     * updates all attributes
     * @param newGameState, the new publicGameState after a player did an action
     * @param playerState, new PlayerState of th
     */
    public void setState(PublicGameState newGameState, PlayerState playerState) {
        publicGameState = newGameState;
        this.playerState = playerState;

        ticketsPercent.set((newGameState.ticketsCount() * 100) / ChMap.tickets().size() );
        cardsPercent.set( (newGameState.cardState().deckSize() * 100) / Constants.TOTAL_CARDS_COUNT);
        for (int slot : Constants.FACE_UP_CARD_SLOTS) {
            Card newCard = newGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
        updateRoutesOwners();

        ownTicketsCount.set(playerState.ticketCount());
        ownCardsCount.set(playerState.cardCount());
        ownCarsCount.set(playerState.carCount());
        ownPoints.set(playerState.claimPoints());

        PublicPlayerState rivalPlayerState = newGameState.playerState(playerId.next());
        rivalTicketsCount.set(rivalPlayerState.ticketCount());
        rivalCardsCount.set(rivalPlayerState.cardCount());
        rivalCarsCount.set(rivalPlayerState.carCount());
        rivalPoints.set(rivalPlayerState.claimPoints());

        ticketList.setAll(playerState.tickets().toList());
        SortedBag<Card> playerCards = playerState.cards();
        countPerCard.forEach( (c, i) -> i.set(playerCards.countOf(c)));
        updateClaimableRoutesMap();

    }

    /*
    Following getters return publicGameState properties visible to both players
     */
    /**
     * @return property containing the percentage of tickets left in tickets' deck
     */
    public ReadOnlyIntegerProperty ticketsPercentProperty() { return ticketsPercent; }

    /**
     * @return property containing the percentage of cards left in deck
     */
    public ReadOnlyIntegerProperty cardsPercentProperty() { return cardsPercent; }

    /**
     * @param slot, slot of the faceUpCard of which we'd like the property
     * @return property containing the faceUpCard at position slot
     */
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) { return faceUpCards.get(slot); }

    /**
     * @param route, route we'd like to know the ownership of
     * @return the BooleanProperty corresponding to the route => if playerId owns it, BooleanProperty contains true
     */
    public ReadOnlyObjectProperty<PlayerId> routesOwnersProperty(Route route) { return routesOwners.get(route); }



    /*
    Following getters return properties that are proper to this class' playerId playerState
    => their number of tickets, cards, cars and points
     */

    /**
     * @return property containing the tickets of playerId
     */
    public ReadOnlyIntegerProperty ownTicketsCountProperty() { return ownTicketsCount; }

    /**
     * @return property containing the number of cards playerId owns
     */
    public ReadOnlyIntegerProperty ownCardsCountProperty() { return ownCardsCount; }

    /**
     * @return property containing the number of cars playerId owns
     */
    public ReadOnlyIntegerProperty ownCarsCountProperty() { return ownCarsCount; }

    /**
     * @return property containing the number of points playerId owns
     */
    public ReadOnlyIntegerProperty ownPointsProperty() { return ownPoints; }

    /*
    Following getters return properties that are proper to playerId's adversary playerState
    => their number of tickets, cards, cars and points
     */
    /**
     * @return property containing the tickets of playerId's adversary
     */
    public ReadOnlyIntegerProperty rivalTicketsCountProperty() { return rivalTicketsCount; }

    /**
     * @return property containing the number of cards playerId's adversary owns
     */
    public ReadOnlyIntegerProperty rivalCardsCountProperty() { return rivalCardsCount; }

    /**
     * @return property containing the number of cars playerId's adversary owns
     */
    public ReadOnlyIntegerProperty rivalCarsCountProperty() { return rivalCarsCount; }

    /**
     * @return property containing the number of points playerId's adversary owns
     */
    public ReadOnlyIntegerProperty rivalPointsProperty() { return rivalPoints; }


    /**
     * @return ObservableList of playerId's tickets
     */
    public ObservableList<Ticket> ticketsListProperty() { return FXCollections.unmodifiableObservableList(ticketList); }

    /**
     * @param c, card of which we'd like to know the count
     * @return the number of cards of type c playerId has
     */
    public ReadOnlyIntegerProperty countOfCard(Card c) { return countPerCard.get(c); }

    /**
     * @param r, route we'd like to know if we can claim
     * @return a BooleanProperty containing true if route r can be claimed and false otherwise
     */
    public ReadOnlyBooleanProperty booleanPropertyOfRoute(Route r) { return claimableRoutes.get(r); }


    /**
     * @return true if there are is at least 1 ticket available in deck
     */
    public boolean canDrawTickets() { return publicGameState.canDrawTickets(); }

    /**
     * @return true if cards in deck + cards in discard >= 5
     */
    public boolean canDrawCards() { return publicGameState.canDrawCards(); }

    /**
     * @param routeToClaim, route we want to claim
     * @return the list of sorted bags of cards playerId can play to claim route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route routeToClaim) {
        return playerState.possibleClaimCards(routeToClaim);
    }




    //---------------------------updating methods---------------------------

    /**
     * updates the map associating a BooleanProperty to each route on map
     */
    private void updateClaimableRoutesMap() {
        //verify if it's this player's turn, if not then all routes' boolean properties are set to false
        if(publicGameState.currentPlayerId() != playerId) { ChMap.routes().forEach(r -> claimableRoutes.get(r).set(false)); }

        //removes all claimed routes from list of available routes
        List<Route> availableRoutes = new ArrayList<>(ChMap.routes());
        availableRoutes.removeAll(publicGameState.claimedRoutes());

        /*
        checks if the route attempted to be claimed is not a double-route, in which case if its neighbor route
        is claimed, it is removed from the list of available routes
         */
        for (Route r : new ArrayList<>(availableRoutes)) {
            if(r != null) {
                for (Route claimedRoute : publicGameState.claimedRoutes()) {
                    if(claimedRoute != null) {
                        if (r.stations().equals(claimedRoute.stations()))
                            availableRoutes.remove(r);
                    }
                }
            }
        }

        /*
        if player can claim route then the associated boolean property is set to true;
        else, it is set to false;
         */
        for(Route r : availableRoutes) {
            if(playerState.canClaimRoute(r))
                claimableRoutes.get(r).set(true);
            else
                claimableRoutes.get(r).set(false);
        }
        System.out.println("(OGS) n° CLAIMED ROUTES: " + (ChMap.routes().size() - availableRoutes.size()));
    }

    /**
     * updates the owners associated to each Route in the routesOwners map
     */
    private void updateRoutesOwners() {

        publicGameState.claimedRoutes().forEach(r -> {
            List<Route> playerRoutes1 = publicGameState.playerState(PlayerId.PLAYER_1).routes();
            List<Route> playerRoutes2 = publicGameState.playerState(PlayerId.PLAYER_2).routes();
            if(r != null) {
                if (playerRoutes1.contains(r)) {
                    routesOwners.get(r).set(PlayerId.PLAYER_1);
                } else if (playerRoutes2.contains(r))
                    routesOwners.get(r).set(PlayerId.PLAYER_2);
            }
        });
    }


    //---------------------------initializing methods---------------------------

    /**
     * @return an initial list of card properties containing a face-up card set at null each (used in constructor)
     */
    private List<ObjectProperty<Card>> initFaceUpCards() {
        List<ObjectProperty<Card>> list = new ArrayList<>();
        for(int i = 0; i < Constants.FACE_UP_CARDS_COUNT; ++i)
            list.add(new SimpleObjectProperty<>(null));

        return list;
    }

    /**
     * @return An initial map associating a playerId property, initially null, to each route on the map (used in constructor)
     */
    private Map<Route, ObjectProperty<PlayerId>> initRoutesOwners() {
        Map<Route, ObjectProperty<PlayerId>> map = new HashMap<>();
        ChMap.routes().forEach(r -> map.put(r, new SimpleObjectProperty<>(null)));
        //map.forEach((r, p) -> System.out.println(r));
        return map;
    }

    /**
     * @return An initial map containing an IntegerProperty set initially at 0 for each card in playerId's hand (used in constructor)
     */
    private Map<Card, IntegerProperty> initCardCountMap() {
        Map<Card, IntegerProperty> map = new HashMap<>();
        SortedBag<Card> cards = SortedBag.of(Card.ALL);
        cards.forEach(c -> map.put(c, new SimpleIntegerProperty(0)));
        return map;
    }

    /**
     * @return an initial map containing a BooleanProperty for each route of the game
     */
    private Map<Route, BooleanProperty> initClaimableRoutesMap() {
        Map<Route, BooleanProperty> map = new HashMap<>();
        ChMap.routes().forEach(r -> map.put(r, new SimpleBooleanProperty(false)));

        return map;
    }

}
