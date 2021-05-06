package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.List;

//Package private class
class MapViewCreator {

    public void createMapView(ObservableGameState observableGameState, ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandler, CardChooser cardChooser){
        Pane pane = new Pane();
        pane.getStylesheets().add("map.css");
        pane.getStylesheets().add("color.css");

        ImageView imageView = new ImageView("map.png");
        pane.getChildren().add(imageView);



        for(Route r : ChMap.routes()){
            Group routeGroup = new Group();
            routeGroup.setId(r.id());
            routeGroup.getStyleClass().add("route");
            routeGroup.getStyleClass().add(r.level().name());
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
                caseGroup.getChildren().add(rectangle);
                caseGroup.getStylesheets().add("track");
                caseGroup.getStylesheets().add("filled");

                Group wagonGroup = new Group();
                wagonGroup.getStylesheets().add("car");
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

    }

    @FunctionalInterface
    interface CardChooser {
        void chooseCards(List<SortedBag<Card>> options,
                         ActionHandlers.ChooseCardsHandler handler);
    }
}
