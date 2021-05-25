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
    infoView.getStylesheets().add("colors.css");

    VBox vBox2 = new VBox();
    vBox2.setId("player-stats");

    //--------------------------this player's stats section--------------------------
    TextFlow ownTextFlowStats = new TextFlow();
    ownTextFlowStats.getStyleClass().add(playerId.toString());

    Circle ownCircle = new Circle(5);
    ownCircle.getStyleClass().add("filled");

    Text ownTextStats = new Text();
    StringExpression ownStringExpression = Bindings.format(StringsFr.PLAYER_STATS, playerNames.get(playerId),
            gameState.ownTicketsCountProperty(),  gameState.ownCardsCountProperty(),
            gameState.ownCarsCountProperty(), gameState.ownPointsProperty());
    ownTextStats.textProperty().bind(ownStringExpression);

    ownTextFlowStats.getChildren().add(ownCircle);
    ownTextFlowStats.getChildren().add(ownTextStats);

    //--------------------------rival player's stats section--------------------------
    TextFlow rivalTextFlowStats = new TextFlow();
    rivalTextFlowStats.getStyleClass().add(playerId.next().toString());

    Circle rivalCircle = new Circle(5);
    rivalCircle.getStyleClass().add("filled");

    Text rivalTextStats = new Text();
    StringExpression rivalStringExpression = Bindings.format(StringsFr.PLAYER_STATS, playerNames.get(playerId.next()),
            gameState.rivalTicketsCountProperty(),  gameState.rivalCardsCountProperty(),
            gameState.rivalCarsCountProperty(), gameState.rivalPointsProperty());
    rivalTextStats.textProperty().bind(rivalStringExpression);

    rivalTextFlowStats.getChildren().add(rivalCircle);
    rivalTextFlowStats.getChildren().add(rivalTextStats);

    //--------------------------Separator between player stats and game information--------------------------
    Separator separator = new Separator();
    separator.setOrientation(Orientation.HORIZONTAL);

    //--------------------------The messages giving information on the happenings in the game--------------------------
    TextFlow textFlowMessage = new TextFlow();
    textFlowMessage.setId("game-info");

    Text textMessage = new Text();
    textFlowMessage.getChildren().add(textMessage);
    Bindings.bindContent(textFlowMessage.getChildren(), observableGameTextList);

    //----------------------------------------------------
    vBox2.getChildren().add(ownTextFlowStats);
    vBox2.getChildren().add(rivalTextFlowStats);

    infoView.getChildren().add(vBox2);
    infoView.getChildren().add(separator);
    infoView.getChildren().add(textFlowMessage);


    return infoView;
}

}
