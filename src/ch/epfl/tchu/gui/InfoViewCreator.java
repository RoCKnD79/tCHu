package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.Map;
/**
 * @author Christopher Soriano (326354)
 * @author Roman Danylovych (327830)
 */

class InfoViewCreator {
    /**
     *creates the vue for the information
     * called before the game starts : the list of information is then empty
     * there are three parts to the info :
     *  - the first two correspond to the stats of both players, separated by a line
     *  - the second part corresponds to the list of info for the unfolding of the game
     * @param playerId, id of player to which the interface belongs
     * @param playerNames, names of players
     * @param gameState, observable game state
     * @param observableGameTextList, contains the information on the unfolding of the game
     * @return the info view
     */
public static VBox createInfoView(PlayerId playerId, Map<PlayerId, String> playerNames, ObservableGameState gameState, ObservableList<Text> observableGameTextList){

    VBox infoView = new VBox();
    infoView.getStylesheets().add("info.css");
    infoView.getStylesheets().add("colors.css");
    infoView.getStylesheets().add("tips.css");

    VBox vBox2 = new VBox();
    vBox2.setId("player-stats");

    HBox tipBox = new HBox();

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

    Bindings.bindContent(textFlowMessage.getChildren(), observableGameTextList);


    //--------------------------Tips Button--------------------------
    Button tipsButton = new Button();
    tipsButton.getStyleClass().add("tips-button");
    tipsButton.setOnMouseClicked(e -> {
            try {
                TipsViewCreator.showTips();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

    //----------------------------------------------------
    vBox2.getChildren().add(ownTextFlowStats);
    vBox2.getChildren().add(rivalTextFlowStats);

    tipBox.setPadding(new Insets(10, 10, 10, 5));
    tipBox.getChildren().add(tipsButton);

    //tipBox.setAlignment(Pos.BOTTOM_LEFT);

    infoView.getChildren().add(tipBox);
    infoView.getChildren().add(vBox2);
    infoView.getChildren().add(separator);
    infoView.getChildren().add(textFlowMessage);



    return infoView;
}

}
