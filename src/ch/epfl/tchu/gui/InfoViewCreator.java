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
import java.util.HashMap;
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

        VBox vBoxStats = new VBox();
        vBoxStats.setId("player-stats");

        HBox tipBox = new HBox();

        Map<PlayerId, TextFlow> textFlowStatsOf = new HashMap<>();
        Map<PlayerId, Circle> circleOf = new HashMap<>();
        Map<PlayerId, Text> textStatsOf = new HashMap<>();
        Map<PlayerId, StringExpression> stringExpressionOf = new HashMap<>();

        //--------------------------players' stats section--------------------------
        for(PlayerId id : PlayerId.ALL) {
            textFlowStatsOf.put(id, new TextFlow());
            textFlowStatsOf.get(id).getStyleClass().add(id.toString());

            circleOf.put(id, new Circle(5));
            circleOf.get(id).getStyleClass().add("filled");

            textStatsOf.put(id, new Text());
            stringExpressionOf.put(id,
                            Bindings.format(StringsFr.PLAYER_STATS, playerNames.get(id),
                            gameState.ticketsCountProperty(id),  gameState.cardsCountProperty(id),
                            gameState.carsCountProperty(id), gameState.pointsProperty(id)) );
            textStatsOf.get(id).textProperty().bind(stringExpressionOf.get(id));

            textFlowStatsOf.get(id).getChildren().add(circleOf.get(id));
            textFlowStatsOf.get(id).getChildren().add(textStatsOf.get(id));
        }


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

        vBoxStats.getChildren().add(textFlowStatsOf.get(playerId));
        vBoxStats.getChildren().add(textFlowStatsOf.get(playerId.next()));

        tipBox.setPadding(new Insets(10, 10, 10, 5));
        tipBox.getChildren().add(tipsButton);

        infoView.getChildren().add(tipBox);
        infoView.getChildren().add(vBoxStats);
        infoView.getChildren().add(separator);
        infoView.getChildren().add(textFlowMessage);


        return infoView;
    }

}
