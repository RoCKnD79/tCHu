package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.*;

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

    public ReadOnlyIntegerProperty ticketsPercentProperty() { return ticketsPercent; }
    public ReadOnlyIntegerProperty cardsPercentProperty() { return cardsPercent; }
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) { return faceUpCards.get(slot); }
    public ReadOnlyObjectProperty<PlayerId> routesOwnersProperty(Route route) { return routesOwners.get(route); }

    public ReadOnlyIntegerProperty ownTicketsCountProperty() { return ownTicketsCount; }
    public ReadOnlyIntegerProperty ownCardsCountProperty() { return ownCardsCount; }
    public ReadOnlyIntegerProperty ownCarsCountProperty() { return ownCarsCount; }
    public ReadOnlyIntegerProperty ownPointsProperty() { return ownPoints; }

    public ReadOnlyIntegerProperty rivalTicketsCountProperty() { return rivalTicketsCount; }
    public ReadOnlyIntegerProperty rivalCardsCountProperty() { return rivalCardsCount; }
    public ReadOnlyIntegerProperty rivalCarsCountProperty() { return rivalCarsCount; }
    public ReadOnlyIntegerProperty rivalPointsProperty() { return rivalPoints; }

    public ObservableList<Ticket> ticketsListProperty() { return FXCollections.unmodifiableObservableList(ticketList); }
    public ReadOnlyIntegerProperty countOfCard(Card c) { return countPerCard.get(c); }
    public ReadOnlyBooleanProperty booleanPropertyOfRoute(Route r) { return claimableRoutes.get(r); }


    public boolean canDrawTickets() { return publicGameState.canDrawTickets(); }
    public boolean canDrawCards() { return publicGameState.canDrawCards(); }
    public List<SortedBag<Card>> possibleClaimCards(Route routeToClaim) {
        return playerState.possibleClaimCards(routeToClaim);
    }




    //---------------------------updating methods---------------------------
    private void updateClaimableRoutesMap() {
        //verify if it's this player's turn
        if(publicGameState.currentPlayerId() != playerId) { ChMap.routes().forEach(r -> claimableRoutes.get(r).set(false)); }

        //removes all claimed routes from list of available routes
        List<Route> availableRoutes = new ArrayList<>(ChMap.routes());
        availableRoutes.removeAll(publicGameState.claimedRoutes());

        /*
        checks if the route attempted to be claimed is not a double-route, in which case if its neighbor route
        is claimed, it is removed from the list of available routes
         */
        for(Route r : availableRoutes) {
            for(Route claimedRoute : publicGameState.claimedRoutes()) {
                if(r.stations().equals(claimedRoute.stations()))
                    availableRoutes.remove(r);
            }
        }

        for(Route r : availableRoutes) {
            if(playerState.canClaimRoute(r))
                claimableRoutes.get(r).set(true);
            else
                claimableRoutes.get(r).set(false);
        }
    }

    private void updateRoutesOwners() {
        publicGameState.claimedRoutes().forEach(r -> {
            List<Route> playerRoutes1 = publicGameState.playerState(PlayerId.PLAYER_1).routes();
            List<Route> playerRoutes2 = publicGameState.playerState(PlayerId.PLAYER_2).routes();
            if(playerRoutes1.contains(r))
                routesOwners.get(r).set(PlayerId.PLAYER_1);
            else if(playerRoutes2.contains(r))
                routesOwners.get(r).set(PlayerId.PLAYER_2);
        });
    }


    //---------------------------initializing methods---------------------------

    private List<ObjectProperty<Card>> initFaceUpCards() {
        List<ObjectProperty<Card>> list = new ArrayList<>();
        for(int i = 0; i < Constants.FACE_UP_CARDS_COUNT; ++i)
            list.add(new SimpleObjectProperty<>(null));

        return list;
    }

    private Map<Route, ObjectProperty<PlayerId>> initRoutesOwners() {
        Map<Route, ObjectProperty<PlayerId>> map = new HashMap<>();
        ChMap.routes().forEach(r -> map.put(r, new SimpleObjectProperty<>(null)));
        return map;
    }

    private Map<Card, IntegerProperty> initCardCountMap() {
        Map<Card, IntegerProperty> map = new HashMap<>();
        SortedBag<Card> cards = SortedBag.of(Card.ALL);
        cards.forEach(c -> map.put(c, new SimpleIntegerProperty(0)));
        return map;
    }

    /*
    - le joueur est le joueur courant,
    - la route n'appartient à personne et, dans le cas d'une route double, sa voisine non plus,
    - le joueur a les wagons et les cartes nécessaires pour s'emparer de la route - ou
      en tout cas tenter de le faire s'il s'agit d'un tunnel.
     */
    private Map<Route, BooleanProperty> initClaimableRoutesMap() {
        Map<Route, BooleanProperty> map = new HashMap<>();
        ChMap.routes().forEach(r -> map.put(r, new SimpleBooleanProperty(false)));

        return map;
    }

}
