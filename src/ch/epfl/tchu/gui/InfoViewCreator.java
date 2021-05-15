package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.GameState;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicPlayerState;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableStringValue;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

class InfoViewCreator {

public static VBox createInfoView(PlayerId playerId, Map<PlayerId, String> playerNames, ObservableGameState gameState, ObservableList<Text> observableGameTextList){
    VBox infoView = new VBox();
    infoView.getStylesheets().add("info.css");
    infoView.getStylesheets().add("color.css");

    VBox vBox2 = new VBox();
    vBox2.setId("player-stats");

    Separator separator = new Separator();
    separator.setOrientation(Orientation.HORIZONTAL);

    infoView.getChildren().add(separator);
    infoView.getChildren().add(vBox2);

    TextFlow textFlowStats = new TextFlow();
    //TODO je sais pas quoi doit remplacer le n
    textFlowStats.getStyleClass().add(playerId.toString());

    Circle circle = new Circle(5);
    StringExpression stringExpression = Bindings.format(playerNames.get(playerId), StringsFr.PLAYER_STATS, gameState.ticketsCountProperty(),  gameState.carsCountProperty(), gameState.carsCountProperty(), gameState.pointsProperty());
    Text textStats = new Text();
    textStats.textProperty().bind(stringExpression);
    textFlowStats.getChildren().add(circle);
    textFlowStats.getChildren().add(textStats);



    TextFlow textFlowMessage = new TextFlow();
    textFlowMessage.getStyleClass().add("game-info");

    Text textMessage = new Text();
    textFlowMessage.getChildren().add(textMessage);
    Bindings.bindContent(textFlowMessage.getChildren(), observableGameTextList);


    return infoView;
}

}
