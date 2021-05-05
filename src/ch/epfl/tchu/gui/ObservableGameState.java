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

    private final IntegerProperty ticketsCount = new SimpleIntegerProperty();
    private final IntegerProperty cardsCount = new SimpleIntegerProperty();
    private final IntegerProperty carsCount = new SimpleIntegerProperty();
    private final IntegerProperty points = new SimpleIntegerProperty();

    private final ObservableList<Ticket> ticketList;
    private final Map<Card, IntegerProperty> countPerCard;
    private final Map<Route, BooleanProperty> claimableRoutes;

    public ObservableGameState(PublicGameState publicGameState, PlayerId playerId, PlayerState playerState) {
        this.publicGameState = publicGameState;
        this.playerId = playerId;
        this.playerState = playerState;

        ticketsPercent = new SimpleIntegerProperty(100);
        cardsPercent = new SimpleIntegerProperty(100);
        faceUpCards = createFaceUpCards();
        routesOwners = FXCollections.observableMap(initRoutesOwners());

        ticketList = FXCollections.observableArrayList(playerState.tickets().toList());
        countPerCard = FXCollections.observableMap(cardCountMap());
        claimableRoutes = FXCollections.observableMap(initClaimableRoutesMap());
    }

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

        ticketsCount.set(playerState.ticketCount());
        cardsCount.set(playerState.cardCount());
        carsCount.set(playerState.carCount());
        points.set(playerState.ticketPoints());

        ticketList.setAll(playerState.tickets().toList());
        SortedBag<Card> playerCards = playerState.cards();
        countPerCard.forEach( (c, i) -> i.set(playerCards.countOf(c)));
        updateClaimableRoutesMap(publicGameState, playerState, claimableRoutes);

    }

    public ReadOnlyIntegerProperty ticketsPercentProperty() { return ticketsPercent; }
    public ReadOnlyIntegerProperty cardsPercentProperty() { return cardsPercent; }
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) { return faceUpCards.get(slot); }
    public ReadOnlyObjectProperty<PlayerId> routesOwnersProperty(Route route) { return routesOwners.get(route); }

    public ReadOnlyIntegerProperty ticketsCountProperty() { return ticketsCount; }
    public ReadOnlyIntegerProperty cardsCountProperty() { return cardsCount; }
    public ReadOnlyIntegerProperty carsCountProperty() { return carsCount; }
    public ReadOnlyIntegerProperty pointsProperty() { return points; }

    public ObservableList<Ticket> ticketsListProperty() { return FXCollections.unmodifiableObservableList(ticketList); }
    public ReadOnlyIntegerProperty countOfCard(Card c) { return countPerCard.get(c); }
    public ReadOnlyBooleanProperty booleanPropertyOfRoute(Route r) { return claimableRoutes.get(r); }


    public boolean canDrawTickets() { return publicGameState.canDrawTickets(); }
    public boolean canDrawCards() { return publicGameState.canDrawCards(); }

    public List<SortedBag<Card>> possibleClaimCards(Route routeToClaim) {
        return playerState.possibleClaimCards(routeToClaim);
    }


    private List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> list = new ArrayList<>();
        List<Card> faceUpCards = publicGameState.cardState().faceUpCards();
        for(Card c : faceUpCards)
            list.add(new SimpleObjectProperty<>(c));
        return list;
    }

    private Map<Route, ObjectProperty<PlayerId>> initRoutesOwners() {
        Map<Route, ObjectProperty<PlayerId>> map = new HashMap<>();
        ChMap.routes().forEach(r -> map.put(r, null));
        return map;
    }

    private Map<Card, IntegerProperty> cardCountMap() {
        Map<Card, IntegerProperty> map = new HashMap<>();
        SortedBag<Card> cards = SortedBag.of(Card.ALL);
        SortedBag<Card> playerCards = playerState.cards();
        cards.forEach(c -> map.put(c, new SimpleIntegerProperty(playerCards.countOf(c))));
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
        updateClaimableRoutesMap(publicGameState, playerState, map);

        return map;
    }

    private void updateClaimableRoutesMap(PublicGameState publicGameState, PlayerState playerState, Map<Route, BooleanProperty> map) {
        if(publicGameState.currentPlayerId() != playerId) { ChMap.routes().forEach(r -> map.get(r).set(false)); }

        List<Route> availableRoutes = ChMap.routes();
        availableRoutes.removeAll(publicGameState.claimedRoutes());

        for(Route r : availableRoutes) {
            for(Route claimedRoute : publicGameState.claimedRoutes()) {
                if(r.stations().equals(claimedRoute.stations()))
                    availableRoutes.remove(r);
            }
        }

        for(Route r : availableRoutes) {
            if(playerState.canClaimRoute(r))
                map.get(r).set(true);
            else
                map.get(r).set(false);
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

}

//MANJ MOA LE POIRO