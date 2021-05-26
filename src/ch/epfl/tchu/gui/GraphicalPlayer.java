package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphicalPlayer {

    Stage primaryStage = new Stage();
    ObservableGameState observableGameState;
    //ObservableList<Text> observableList = null;
    ObservableList<Text> observableList = FXCollections.observableArrayList();
    ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandler;
    ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandler;
    ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandler;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        this.observableGameState = new ObservableGameState(playerId);

        claimRouteHandler = new SimpleObjectProperty<>();
        drawTicketsHandler = new SimpleObjectProperty<>();
        drawCardHandler = new SimpleObjectProperty<>();

        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRouteHandler, this::chooseClaimCards);
        Node cardsView = DecksViewCreator
                .createCardsView(observableGameState, drawTicketsHandler, drawCardHandler);
        Node handView = DecksViewCreator
                .createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, observableList);

        BorderPane mainPane = new BorderPane(mapView, null, cardsView, handView, infoView);

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        System.out.println(width);
        double height = screenSize.getHeight();
        System.out.println(height);

        primaryStage.setScene(new Scene(mainPane, 1280, 600));
        primaryStage.setTitle("tCHu \u2014 " + playerNames.get(playerId));

        primaryStage.setMaxHeight(1080);
        primaryStage.setMaxWidth(1920);
        primaryStage.show();

    }

    /**
     * calls setState of observableGameState at each update
     * @param newGameState, the new refreshed publicGameState
     * @param playerState, the new refreshed playerState
     */
    public void setState(PublicGameState newGameState, PlayerState playerState){
        observableGameState.setState(newGameState, playerState);
    }

    /**
     * List of information about the on-going game that will be displayed in the left-hand side of the screen
     * @param message, message to add to the list of information
     */
    public void receiveInfo(String message){
        assert Platform.isFxApplicationThread();

        Text text = new Text(message);
        if(observableList.size() >= 5){
            observableList.remove(0);
        }
        observableList.add(text);
    }

    /**
     * begins each turn by setting up each handler for the player to be able to do one of three possible actions
     *      - draw at least 1 ticket
     *      - draw 2 cards
     *      - attempt to claim a route
     * The player will be able to do only the actions whose handler is not null after running this method.
     * After a player does an allowed action, all handlers (including this action's handler) are set to null so that
     * they can't do another action.
     * @param drawTicketsHandler, handler in charge of dealing with drawing tickets
     * @param drawCardHandler, handler in charge of dealing with drawing cards
     * @param claimRouteHandler, handler in charge of dealing with claiming a route
     */
    public void startTurn(ActionHandlers.DrawTicketsHandler drawTicketsHandler,
                          ActionHandlers.DrawCardHandler drawCardHandler,
                          ActionHandlers.ClaimRouteHandler claimRouteHandler) {
        assert Platform.isFxApplicationThread();

        //--------------DrawTicketsHandlerProperty--------------
        if(!observableGameState.canDrawTickets()) { this.drawTicketsHandler.set(null); }
        else {
            this.drawTicketsHandler.set(() -> {
                drawTicketsHandler.onDrawTickets();
                setHandlerPropertiesToNull();
                });
        }

        //--------------DrawCardHandlerProperty--------------
        if(!observableGameState.canDrawCards()) { this.drawCardHandler.set(null); }
        else {
            this.drawCardHandler.set((slot1) -> {
                drawCardHandler.onDrawCard(slot1);
                setHandlerPropertiesToNull();
                drawCard(drawCardHandler);
            });
        }

        //--------------ClaimRouteProperty--------------
        this.claimRouteHandler.set((r, cs) -> {
            claimRouteHandler.onClaimRoute(r, cs);
            setHandlerPropertiesToNull();
                });

    }


    /**
     * displays a modal dialog box that allows the player to choose:
     *      - at least 3 tickets out of 5 if it's the beginning of the game
     *      - at least 1 ticket out of 3 if we are already in game
     * @param tickets, ticket choices
     * @param handler, handler in charge of dealing with drawing tickets
     */
    public void chooseTickets(SortedBag<Ticket> tickets, ActionHandlers.ChooseTicketsHandler handler){
        assert Platform.isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(StringsFr.TICKETS_CHOICE);
        stage.setOnCloseRequest(Event::consume);

        VBox vBox = new VBox();

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");

        //----------------------Display on opening window----------------------
        Text introText = new Text();

        ObservableList<Ticket> obsListTickets = FXCollections.observableArrayList(tickets.toList());
        ListView<Ticket> choiceList = new ListView<>(obsListTickets);

        choiceList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //----------------------Choice Confirmation Button----------------------
        Button confirmButton = new Button();
        confirmButton.setText("Choisir");

        //start of game, at least 3 tickets must be chosen out of the 5 initial ones in order to press the button
        if(tickets.size() == Constants.INITIAL_TICKETS_COUNT) {
            introText.setText(String.format(StringsFr.CHOOSE_TICKETS, 3, StringsFr.plural(3)));
            confirmButton.disableProperty().bind(Bindings.greaterThan(3,
                    Bindings.size(choiceList.getSelectionModel().getSelectedItems())));
        }
        //must choose at least one ticket after having drawn 3 from tickets deck in order to press the button
        else {
            introText.setText(String.format(StringsFr.CHOOSE_TICKETS, 1, StringsFr.plural(1)));
            confirmButton.disableProperty().bind(Bindings.greaterThan(1,
                    Bindings.size(choiceList.getSelectionModel().getSelectedItems())));
        }

        //after confirming, window is closed and chosen tickets are passed as an argument to the handler
        confirmButton.setOnAction(e -> {
            stage.hide();
            SortedBag.Builder<Ticket> sbb = new SortedBag.Builder<>();
            choiceList.getSelectionModel().getSelectedItems().forEach(sbb::add);
            handler.onChooseTickets(sbb.build());
        });

        //-------------------------------------------------------
        TextFlow textFlow = new TextFlow(introText);

        vBox.getChildren().add(textFlow);
        vBox.getChildren().add(choiceList);
        vBox.getChildren().add(confirmButton);

        stage.setScene(scene);

        stage.show();

    }

    /**
     * Method supposed to be called after the player already drew one card
     * @param handler, handler in charge of dealing with drawing cards
     */
    public void drawCard(ActionHandlers.DrawCardHandler handler) {
        assert Platform.isFxApplicationThread();

        drawCardHandler.set(slot -> {
            handler.onDrawCard(slot);
            setHandlerPropertiesToNull();
        });
    }

    /**
     * Method destined to be passed as argument of type CardChooser to the createMapView method (of MapViewCreator)
     * Opens a modal dialog box that closes only if player chooses an option
     * @param claimCards, initial cards used to claim a route
     * @param handler, handler in charge of dealing with choosing cards
     */
    public void chooseClaimCards(List<SortedBag<Card>> claimCards,
                                        ActionHandlers.ChooseCardsHandler handler) {
        assert Platform.isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(StringsFr.CARDS_CHOICE);
        stage.setOnCloseRequest(Event::consume);

        VBox vBox = new VBox();

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");


        ObservableList<SortedBag<Card>> obsListClaimCards = FXCollections.observableArrayList(claimCards);
        ListView<SortedBag<Card>> choiceList = new ListView<>(obsListClaimCards);
        choiceList.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));

        //----------------------Choice Confirmation Button----------------------
        Button confirmButton = new Button();
        confirmButton.setText("Choisir");
        confirmButton.disableProperty().bind(Bindings.greaterThan(1,
                Bindings.size(choiceList.getSelectionModel().getSelectedItems())));

        //after confirming, window is closed and chosen tickets are passed as an argument to the handler
        confirmButton.setOnAction(e -> {
            stage.hide();
            SortedBag<Card> cardSet = choiceList.getSelectionModel().getSelectedItem();
            handler.onChooseCards(cardSet);
        });

        //introText: "Choisissez les cartes à utiliser pour vous emparer de cette route :"
        Text introText = new Text();
        introText.setText(StringsFr.CHOOSE_CARDS);
        TextFlow textFlow = new TextFlow(introText);

        vBox.getChildren().add(textFlow);
        vBox.getChildren().add(choiceList);
        vBox.getChildren().add(confirmButton);

        stage.setScene(scene);

        stage.show();

    }

    /**
     * Opens a modal dialog box containing a list of the additional cards the player can play to attempt
     * claiming a tunnel.
     * The box closes if the player either:
     *      - choose nothing
     *      - choose one option
     * @param additionalCards, list of the additional cards the player can play to attempt to claim the tunnel
     * @param handler, handler in charge of dealing with choosing the cards
     */
    public void chooseAdditionalCards(List<SortedBag<Card>> additionalCards,
                                      ActionHandlers.ChooseCardsHandler handler) {
        assert Platform.isFxApplicationThread();

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(StringsFr.CARDS_CHOICE);
        stage.setOnCloseRequest(Event::consume);

        VBox vBox = new VBox();

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");


        ObservableList<SortedBag<Card>> obsListClaimCards = FXCollections.observableArrayList(additionalCards);
        ListView<SortedBag<Card>> choiceList = new ListView<>(obsListClaimCards);
        choiceList.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));

        //----------------------Choice Confirmation Button----------------------
        Button confirmButton = new Button();
        confirmButton.setText("Choisir");

        //after confirming, window is closed and chosen tickets are passed as an argument to the handler
        confirmButton.setOnAction(e -> {
            stage.hide();
            SortedBag<Card> cardSet = choiceList.getSelectionModel().getSelectedItem();
            handler.onChooseCards(cardSet);
        });

        //introText: "Choisissez les cartes à utiliser pour vous emparer de cette route :"
        Text introText = new Text();
        introText.setText(StringsFr.CHOOSE_ADDITIONAL_CARDS);
        TextFlow textFlow = new TextFlow(introText);

        vBox.getChildren().add(textFlow);
        vBox.getChildren().add(choiceList);
        vBox.getChildren().add(confirmButton);

        stage.setScene(scene);

        stage.show();

    }

    /**
     *
     * @param drawTicketsHandler, handler in charge of dealing with drawing tickets, can be null
     * @param drawCardHandler, handler in charge of dealing with drawing cards, can be null
     * @param claimRouteHandler, handler in charge of dealing with claiming routes
     */
    private void setHandlerProperties(ActionHandlers.DrawTicketsHandler drawTicketsHandler,
                                      ActionHandlers.DrawCardHandler drawCardHandler,
                                      ActionHandlers.ClaimRouteHandler claimRouteHandler) {
        this.drawTicketsHandler.set(drawTicketsHandler);
        this.drawCardHandler.set(drawCardHandler);
        this.claimRouteHandler.set(claimRouteHandler);
    }

    /**
     * sets the handlers contained inside the handler properties to null
     */
    private void setHandlerPropertiesToNull() {
        setHandlerProperties(null, null, null);
    }

    /**
     * Nested class whose role is to give a way to represent sorted bags of cards in an appealing textual form
     * Was created in order to modify the cell factory of a ListView of SortedBag<Card>
     *     (c.f. chooseClaimCards and chooseAdditionalCards)
     */
    private class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

        /**
         * displays "1 violette et 3 rouges" instead of "{VIOLET, 3×RED}"
         * @param object SortedBag to display
         * @return a textual representation of the object in question
         */
        @Override
        public String toString(SortedBag<Card> object) {
            return Info.displayCards(object);
        }

        /**
         * NEVER SUPPOSED TO BE USED THEREFORE THROWS AN EXCEPTION
         * @throws UnsupportedOperationException, use of method is forbidden
         */
        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException("never used");
        }
    }

}
