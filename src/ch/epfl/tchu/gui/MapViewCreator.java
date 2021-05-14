package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.List;

//Package private class
class MapViewCreator {

    public static Node createMapView(ObservableGameState observableGameState, ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser){
        Pane pane = new Pane();
        pane.getStylesheets().add("map.css");
        pane.getStylesheets().add("colors.css");

        ImageView imageView = new ImageView("map.png");
        pane.getChildren().add(imageView);



        for(Route r : ChMap.routes()){
            Group routeGroup = new Group();
            routeGroup.setId(r.id());
            routeGroup.getStyleClass().add("route");
            routeGroup.getStyleClass().add(r.level().name());

            if(r.color() == null)
                routeGroup.getStyleClass().add("NEUTRAL");
            else
                routeGroup.getStyleClass().add(r.color().name());

            pane.getChildren().add(routeGroup);

            ReadOnlyObjectProperty<PlayerId> property = observableGameState.routesOwnersProperty(r);
            property.addListener((prop, oldValue, newValue) -> routeGroup.getStyleClass().add(newValue.toString()));

            routeGroup.setOnMouseClicked(e -> {
            List<SortedBag<Card>> possibleClaimCards = observableGameState.possibleClaimCards(r);
            if(possibleClaimCards.size() == 1){
                claimRouteHandler.getValue().onClaimRoute(r, possibleClaimCards.get(0));
            }else{
                ActionHandlers.ChooseCardsHandler chooseCardsH =
                        chosenCards -> claimRouteHandler.getValue().onClaimRoute(r, chosenCards);
                cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
            }
            });
            routeGroup.disableProperty().bind(claimRouteHandler.isNull().or(observableGameState.booleanPropertyOfRoute(r).not()));

            for(int i = 1; i <= r.length(); ++i){
                Group caseGroup = new Group();
                caseGroup.setId(r.id() + "_" + i);
                javafx.scene.shape.Rectangle rectangle = new Rectangle(36, 12);
                rectangle.getStyleClass().add("track");
                rectangle.getStyleClass().add("filled");
                caseGroup.getChildren().add(rectangle);
                /*caseGroup.getChildren().add(rectangle);
                caseGroup.getStyleClass().add("track");
                caseGroup.getStyleClass().add("filled");*/

                Group wagonGroup = new Group();
                wagonGroup.getStyleClass().add("car");
                Rectangle rectangleWagon = new Rectangle(36,12);
                Circle circle1 = new Circle(12, 6, 3);
                Circle circle2 = new Circle(12, 6, 3);
                wagonGroup.getChildren().add(rectangleWagon);
                wagonGroup.getChildren().add(circle1);
                wagonGroup.getChildren().add(circle2);

                routeGroup.getChildren().add(wagonGroup);
                routeGroup.getChildren().add(caseGroup);
            }


        }
        return pane;
    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ActionHandlers.ChooseCardsHandler handler);
    }
}
