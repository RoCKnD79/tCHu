package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphicalPlayer {

    ObservableGameState observableGameState;
    ObservableList<Text> observableList = null;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        this.observableGameState = new ObservableGameState(playerId);
        Stage primaryStage = new Stage();

        //setState(observableGameState);

        ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRoute =
                new SimpleObjectProperty<>(GraphicalPlayer::claimRoute);
        ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTickets =
                new SimpleObjectProperty<>(GraphicalPlayer::drawTickets);
        ObjectProperty<ActionHandlers.DrawCardHandler> drawCard =
                new SimpleObjectProperty<>(GraphicalPlayer::drawCard);


        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRoute, GraphicalPlayer::chooseCards);
        Node cardsView = DecksViewCreator
                .createCardsView(observableGameState, drawTickets, drawCard);
        Node handView = DecksViewCreator
                .createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, observableList);


        BorderPane mainPane =
                new BorderPane(mapView, null, cardsView, handView, infoView);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();

    }

    private static void claimRoute(Route route, SortedBag<Card> cards) {
        System.out.printf("Prise de possession d'une route : %s - %s %s%n",
                route.station1(), route.station2(), cards);
    }

    private static void chooseCards(List<SortedBag<Card>> options,
                                    ActionHandlers.ChooseCardsHandler chooser) {
    }

    private static void drawTickets() {
        System.out.println("Tirage de billets !");
    }

    private static void drawCard(int slot) {
        System.out.printf("Tirage de cartes (emplacement %s)!\n", slot);
    }

    public void setState(PublicGameState newGameState, PlayerState playerState){
        observableGameState.setState(newGameState, playerState);
    }

    public void receiveInfo(String message){
        Text text = new Text(message);
        if(observableList.size() >= 5){
            observableList.remove(0);
        }
        observableList.add(text);
    }

    public void chooseTickets(SortedBag<Ticket> tickets){
        GridPane gridPane = new GridPane();
        if(tickets.size()  == 3){

        }

    }
}
