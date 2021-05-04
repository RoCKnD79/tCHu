package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.List;
import java.util.Map;

public class ObservableGameState {

    private final PlayerId playerId;
    private PlayerState playerState;
    private PublicGameState publicGameState;

    private final IntegerProperty ticketsPercent;
    private final IntegerProperty cardsPercent;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final Map<Route, ObjectProperty<PlayerId>> routesOwners;

    /*private final ObjectProperty<Integer> ticketsCount;
    private final ObjectProperty<Integer> cardsCount;
    private final ObjectProperty<Integer> carsCount;
    private final ObjectProperty<Integer> points;*/
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
        claimableRoutes = FXCollections.observableMap(claimableRoutesMap());
    }

    public void setState(PublicGameState pgs, PlayerState playerState) {
        publicGameState = pgs;
        this.playerState = playerState;
        //TODO doit update les propriétés concernées par des changements
    }

    public ReadOnlyIntegerProperty ticketsPercentProperty() { return ticketsPercent; }
    public ReadOnlyIntegerProperty cardsPercentProperty() { return cardsPercent; }
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) { return faceUpCards.get(slot); }
    public ReadOnlyObjectProperty<PlayerId> routesOwnersProperty(Route route) { return routesOwners.get(route); }

    public ReadOnlyIntegerProperty ticketsCountProperty() { return ticketsCount; }
    public ReadOnlyIntegerProperty cardsCountProperty() { return cardsCount; }
    public ReadOnlyIntegerProperty carsCountProperty() { return carsCount; }
    public ReadOnlyIntegerProperty pointsProperty() { return points; }

    public ReadOnlyListProperty<Ticket> ticketsListProperty() {
        return null;
    }
}
