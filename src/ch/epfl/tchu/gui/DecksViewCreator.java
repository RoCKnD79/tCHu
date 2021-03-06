package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * @author Roman Danylovych (327830)
 */
class DecksViewCreator {

    private static final String DEFAULT_CONTROL_INNER_BACKGROUND = "derive(-fx-base,80%)";
    private static final String HIGHLIGHTED_CONTROL_INNER_BACKGROUND = "derive(palegreen, 50%)";

    /**
     * private constructor, the class isn't supposed to be instantiated
     */
    private DecksViewCreator(){}

    /**
     * Creates the view of the player's hand => list of tickets and his cards
     * @param ogs, ObservableGameState
     * @return HBox (horizontal view) of the hand view
     */
    static HBox createHandView(ObservableGameState ogs) {
        HBox handView = new HBox();
        handView.getStylesheets().add("decks.css");
        handView.getStylesheets().add("colors.css");

        ListView<Ticket> ticketListView = new ListView<>(ogs.ticketsListProperty());

        ticketListView.setCellFactory(new Callback<ListView<Ticket>, ListCell<Ticket>>() {
                                          @Override
                                          public ListCell<Ticket> call(ListView<Ticket> param) {
                                              return new ListCell<Ticket>() {
                                                  @Override
                                                  protected void updateItem(Ticket item, boolean empty) {
                                                      super.updateItem(item, empty);

                                                      if (item == null || empty) {
                                                          setText(null);
                                                      } else {
                                                          setText(item.toString());
                                                          if (claimedTicket(ogs, item)) {
                                                              setStyle("-fx-control-inner-background: " + "derive(palegreen, 50%)");
                                                          }
                                                      }
                                                  }

                                              };
                                          }
                                      });




        ticketListView.setId("tickets");
        handView.getChildren().add(ticketListView);

        HBox handPaneView = new HBox();
        handPaneView.setId("hand-pane");

        for(Card c : Card.ALL) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().add("card");
            if(c.color() != null)
                stackPane.getStyleClass().add(c.color().toString());
            else
                stackPane.getStyleClass().add("NEUTRAL");

            //-------------Rectangle contour-------------
            Rectangle outsideRect = new Rectangle(60, 90);
            outsideRect.getStyleClass().add("outside");

            //-------------Rectangle int??rieur colori??-------------
            Rectangle filledInsideRect = new Rectangle(40, 70);
            filledInsideRect.getStyleClass().add("filled");
            filledInsideRect.getStyleClass().add("inside");

            //-------------Rectangle image train-------------
            Rectangle trainImageRect = new Rectangle(40, 70);
            trainImageRect.getStyleClass().add("train-image");

            //-------------compteur Cartes-------------
            Text countText = new Text();
            countText.getStyleClass().add("count");
            countText.textProperty().bind(Bindings.convert(ogs.countOfCard(c)));
            countText.visibleProperty().bind(Bindings.greaterThan(ogs.countOfCard(c), 1));

            //-------------Ajout des nodes ?? StackPane-------------

            stackPane.getChildren().add(outsideRect);
            stackPane.getChildren().add(filledInsideRect);
            stackPane.getChildren().add(trainImageRect);
            stackPane.getChildren().add(countText);

            stackPane.visibleProperty().bind(Bindings.greaterThan(ogs.countOfCard(c), 0));

            //-------------Ajout stackPane au HBox-------------
            handPaneView.getChildren().add(stackPane);
        }

        handView.getChildren().add(handPaneView);

        return handView;
    }


    /**
     * creates the view of the cards in game => face-up cards, deck button and tickets button
     * @param observableGameState, ObservableGameState
     * @param ticketHandler, ticket handler, if null: tickets button can't be pressed
     *                                       else: preforms the action it is assigned
     * @param cardHandler, card handler, if null: deck button can't be pressed and face up cards can't be drawn
     *                                      else: preforms the action it is assigned
     * @return a VBox (vertical view) of the cards view
     */
    static VBox createCardsView(ObservableGameState observableGameState,
                                ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketHandler,
                                ObjectProperty<ActionHandlers.DrawCardHandler> cardHandler) {

        VBox cardsView = new VBox();
        cardsView.setId("card-pane");
        cardsView.getStylesheets().add("decks.css");
        cardsView.getStylesheets().add("colors.css");

        //-------------TicketButton and its gauge-------------
        Button ticketsButton = new Button();
        ticketsButton.setText(StringsFr.TICKETS);
        ticketsButton.getStyleClass().add("gauged");

        Group ticketGauge = new Group();
        Rectangle backgroundTicketRect = new Rectangle(50, 5);
        backgroundTicketRect.getStyleClass().add("background");

        Rectangle foregroundTicketGauge = new Rectangle(50, 5);
        foregroundTicketGauge.getStyleClass().add("foreground");
        ReadOnlyIntegerProperty ticketsPercentProperty = observableGameState.ticketsPercentProperty();
        foregroundTicketGauge.widthProperty().bind(ticketsPercentProperty.multiply(50).divide(100));

        ticketGauge.getChildren().add(backgroundTicketRect);
        ticketGauge.getChildren().add(foregroundTicketGauge);
        ticketsButton.setGraphic(ticketGauge);

        ticketsButton.disableProperty().bind(ticketHandler.isNull());
        ticketsButton.setOnMouseClicked(e -> ticketHandler.get().onDrawTickets());

        cardsView.getChildren().add(ticketsButton);



        //---------------------FACE_UP_CARDS---------------------

        for(int slot : Constants.FACE_UP_CARD_SLOTS) {
            ReadOnlyObjectProperty<Card> cardProperty = observableGameState.faceUpCard(slot);

            StackPane cardNode = new StackPane();

            cardNode.getStyleClass().add("card");

            //will update the faceUpCards everytime they change
            cardProperty.addListener((prop, oldValue, newValue) -> {
                int size = cardNode.getStyleClass().size();
                if(newValue == Card.LOCOMOTIVE) {
                    if (size == 2)
                        cardNode.getStyleClass().set(size - 1, "NEUTRAL");
                    else cardNode.getStyleClass().add("NEUTRAL");
                }
                else{
                    if(size == 2)
                        cardNode.getStyleClass().set(size-1, newValue.toString());
                    else cardNode.getStyleClass().add(newValue.toString());
                }

            });

            //-------------Rectangle contour-------------
            Rectangle outsideRect = new Rectangle(60, 90);
            outsideRect.getStyleClass().add("outside");

            //-------------Rectangle int??rieur colori??-------------
            Rectangle filledInsideRect = new Rectangle(40, 70);
            filledInsideRect.getStyleClass().add("filled");
            filledInsideRect.getStyleClass().add("inside");

            //-------------Rectangle image train-------------
            Rectangle trainImageRect = new Rectangle(40, 70);
            trainImageRect.getStyleClass().add("train-image");

            //----------------------------------------------------
            cardNode.getChildren().add(outsideRect);
            cardNode.getChildren().add(filledInsideRect);
            cardNode.getChildren().add(trainImageRect);

            cardNode.disableProperty().bind(cardHandler.isNull());
            cardNode.setOnMouseClicked(e -> cardHandler.get().onDrawCard(slot));

            cardsView.getChildren().add(cardNode);
        }

        //-------------DeckButton and its gauge-------------
        Button deckButton = new Button();
        deckButton.setText(StringsFr.CARDS);
        deckButton.getStyleClass().add("gauged");

        Group deckGauge = new Group();
        Rectangle backgroundDeckRect = new Rectangle(50, 5);
        backgroundDeckRect.getStyleClass().add("background");

        Rectangle foregroundDeckGauge = new Rectangle(50, 5);
        foregroundDeckGauge.getStyleClass().add("foreground");
        ReadOnlyIntegerProperty deckPercentProperty = observableGameState.cardsPercentProperty();
        //the following code is to adapt the percentage of cards to the size of the gauge
        foregroundDeckGauge.widthProperty().bind(deckPercentProperty.multiply(50).divide(100));


        deckGauge.getChildren().add(backgroundDeckRect);
        deckGauge.getChildren().add(foregroundDeckGauge);
        deckButton.setGraphic(deckGauge);

        deckButton.disableProperty().bind(cardHandler.isNull());
        deckButton.setOnMouseClicked(e -> cardHandler.get().onDrawCard(-1));

        cardsView.getChildren().add(deckButton);

        return cardsView;
    }

    private static boolean claimedTicket(ObservableGameState obs, Ticket ticket){
        int number = 0;
        if(obs.getPlayerState().routes() != null) {
            for (Route route : obs.getPlayerState().routes()) {
                if(route != null) {
                    number = Math.max(route.station1().id(), number);
                    number = Math.max(route.station2().id(), number);
                }
            }
        }


        StationPartition.Builder stationPartitionBuilder = new StationPartition.Builder(1 + number);
        for(Route route2 : obs.getPlayerState().routes()){
            if(route2 != null) {
                stationPartitionBuilder.connect(route2.station1(), route2.station2());
            }
        }

        StationPartition stationPartition = stationPartitionBuilder.build();
        return ticket.points(stationPartition) > 0;
    }

}
