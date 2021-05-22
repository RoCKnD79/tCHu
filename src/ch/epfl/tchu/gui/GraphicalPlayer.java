package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphicalPlayer {

    //TODO j'ai dû le mettre en static pour pouvoir l'utiliser dans chooseCards
    static Stage primaryStage = new Stage();
    ObservableGameState observableGameState;
    ObservableList<Text> observableList = null;
    ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandler;
    ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandler;
    ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandler;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        this.observableGameState = new ObservableGameState(playerId);


        //setState(observableGameState);

        /*ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRoute =
                new SimpleObjectProperty<>(GraphicalPlayer::claimRoute);
        ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTickets =
                new SimpleObjectProperty<>(GraphicalPlayer::drawTickets);
        ObjectProperty<ActionHandlers.DrawCardHandler> drawCard =
                new SimpleObjectProperty<>(GraphicalPlayer::drawCard);*/

        claimRouteHandler = new SimpleObjectProperty<>(GraphicalPlayer::claimRoute);
        drawTicketsHandler = new SimpleObjectProperty<>(GraphicalPlayer::drawTickets);
        drawCardHandler = new SimpleObjectProperty<>(GraphicalPlayer::drawCard);


        //TODO ne devrait-il pas prendre plutôt GraphicalPlayer::chooseClaimCards en argument ?
        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRouteHandler, GraphicalPlayer::chooseCards);
        Node cardsView = DecksViewCreator
                .createCardsView(observableGameState, drawTicketsHandler, drawCardHandler);
        Node handView = DecksViewCreator
                .createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, observableList);

        //primaryStage = new Stage();
        BorderPane mainPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.setTitle("tCHu \u2014 " + playerNames.get(playerId));
        primaryStage.show();

    }


    public void setState(PublicGameState newGameState, PlayerState playerState){
        observableGameState.setState(newGameState, playerState);
    }

    public void receiveInfo(String message){
        //TODO vérifier que observableList n'est pas null
        Text text = new Text(message);
        if(observableList.size() >= 5){
            observableList.remove(0);
        }
        observableList.add(text);
    }

    public void startTurn(ActionHandlers.DrawTicketsHandler drawTicketsHandler,
                          ActionHandlers.DrawCardHandler drawCardHandler,
                          ActionHandlers.ClaimRouteHandler claimRouteHandler) {

        this.claimRouteHandler.set(claimRouteHandler);

        if(!observableGameState.canDrawTickets()) {this.drawTicketsHandler.set(null); }
        else { setHandlerProperties(drawTicketsHandler, null, null); }

        if(!observableGameState.canDrawCards()) { this.drawCardHandler.set(null); }
        else { setHandlerProperties(null, drawCardHandler, null); }


    }



    public void chooseTickets(SortedBag<Ticket> tickets){
        GridPane gridPane = new GridPane();
        if(tickets.size()  == 3){

        }
    }

    public void drawCard(ActionHandlers.DrawCardHandler handler) {
        this.drawCardHandler.set(handler);
        this.drawTicketsHandler.set(null);
        this.claimRouteHandler.set(null);
    }

    //TODO jsp pq ca doit etre static
    //cette méthode n'est destinée qu'à être passée en argument à createMapView en tant que valeur de type CardChooser
    public void chooseClaimCards(List<SortedBag<Card>> claimCards, ActionHandlers.ChooseCardsHandler handler) {
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(StringsFr.CARDS_CHOICE);

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add("chooser.css");

        TextFlow textFlow = new TextFlow();
        Text introText = new Text();
        introText.setText(StringsFr.CHOOSE_CARDS);

        //ListView<Card> choiceList = new ListView(observableGameState.possibleClaimCards());
        ObservableList<SortedBag<Card>> obsListClaimCards = FXCollections.observableArrayList(claimCards);
        ListView<SortedBag<Card>> choiceList = new ListView<SortedBag<Card>>(obsListClaimCards);
        choiceList.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));

        if(claimCards.size() > 1) {
            choiceList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }



        VBox vBox = new VBox();
    }


    public void chooseAdditionalCards(List<SortedBag<Card>> additionalCards, ActionHandlers.ChooseCardsHandler handler) {

    }

    private static void claimRoute(Route route, SortedBag<Card> cards) {
        System.out.printf("Prise de possession d'une route : %s - %s %s%n",
                route.station1(), route.station2(), cards);
    }

    //TODO jsp pq ca doit etre static
    private static void chooseCards(List<SortedBag<Card>> options, ActionHandlers.ChooseCardsHandler chooser) {
    }

    private static void drawTickets() {
        System.out.println("Tirage de billets !");
    }

    private static void drawCard(int slot) {
        System.out.printf("Tirage de cartes (emplacement %s)!\n", slot);
    }

    private void setHandlerProperties(ActionHandlers.DrawTicketsHandler drawTicketsHandler,
                                      ActionHandlers.DrawCardHandler drawCardHandler,
                                      ActionHandlers.ClaimRouteHandler claimRouteHandler) {
        this.drawTicketsHandler.set(drawTicketsHandler);
        this.drawCardHandler.set(drawCardHandler);
        this.claimRouteHandler.set(claimRouteHandler);
    }

    //TODO j'ai mis public pour pouvoir tester mais faudrait-il pas la rendre privée ?
    public static class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

        @Override
        public String toString(SortedBag<Card> object) {
            return Info.displayCards(object);
        }

        //never supposed to be used therefore throws an exception
        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException("never used");
        }
    }

}
