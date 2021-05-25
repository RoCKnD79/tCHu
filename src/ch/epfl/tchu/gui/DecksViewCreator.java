package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

class DecksViewCreator {

    private DecksViewCreator(){}

    /**
     *
     * @param ogs,
     * @return
     */
    static HBox createHandView(ObservableGameState ogs) {
        HBox handView = new HBox();
        handView.getStylesheets().add("decks.css");
        handView.getStylesheets().add("colors.css");

        ListView<Ticket> ticketListView = new ListView<>(ogs.ticketsListProperty());
        ticketListView.setId("tickets");
        handView.getChildren().add(ticketListView);

        HBox handPaneView = new HBox();
        handPaneView.setId("hand-pane");


        /* TODO
        La classe de style représentant la couleur d'une des cinq cartes face visible doit bien entendu changer
        lorsque la carte en question change. Cela peut se faire en attachant, au moyen de la méthode addListener,
        un auditeur à la propriété de l'état du jeu observable correspondant à l'emplacement. Lorsque cet auditeur
        est informé d'un changement, il modifie la classe de style du nœud afin de refléter le changement du type de carte.
         */

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

            //-------------Rectangle intérieur colorié-------------
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

            //-------------Ajout des nodes à StackPane-------------

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

    static VBox createCardsView(ObservableGameState observableGameState,
                                ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketHandler,
                                ObjectProperty<ActionHandlers.DrawCardHandler> cardHandler) {

        VBox cardsView = new VBox();
        cardsView.setId("card-pane");
        cardsView.getStylesheets().add("decks.css");
        cardsView.getStylesheets().add("colors.css");

        //-------------TicketButton and its gauge-------------
        Button ticketsButton = new Button();
        ticketsButton.setText("Billets");
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

        System.out.println("(DecksViewCreator) ticketHandler null ? " + ticketHandler.isNull().get());
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
                if(newValue == Card.LOCOMOTIVE)
                    cardNode.getStyleClass().add("NEUTRAL");
                else
                    cardNode.getStyleClass().add(newValue.toString());
            });

            //-------------Rectangle contour-------------
            Rectangle outsideRect = new Rectangle(60, 90);
            outsideRect.getStyleClass().add("outside");

            //-------------Rectangle intérieur colorié-------------
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
        deckButton.setText("Cartes");
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

}
