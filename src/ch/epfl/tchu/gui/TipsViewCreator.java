package ch.epfl.tchu.gui;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_BLUEPeer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TipsViewCreator {

    private TipsViewCreator() {}

    static void showTips() throws IOException {
        Stage stage = new Stage(StageStyle.UTILITY);

        List<Text> tutorialTexts = new ArrayList<>();
        List<Pane> panes = new ArrayList<>();

        //-------------Text 1: choisir billets-------------
        Text text1 = new Text("Dès le début de la partie il est demandé de choisir des billets qu'il " +
                "va falloir utiliser pour gagner des points. Le but étant de relier les stations d'un " +
                "billet entre elles.\nLors d'un tour il est possible de tirer des billets pour pouvoir " +
                "essayer de continuer de gagner des points. ATTENTION, si un billet n'est pas accompli " +
                "vous perdez des points !");
        tutorialTexts.add(text1);

        BorderPane pane1 = new BorderPane();
        pane1.getStylesheets().add("choix-billets-image.css");
        ImageView imageView1 = new ImageView();
        pane1.setCenter(imageView1);

        panes.add(pane1);

        //-------------Text 2: choisir des cartes-------------
        Text text2 = new Text("Pour pouvoir accomplir des billets il va falloir s'emparer de routes.\n" +
                "Pour se faire, il faut des cartes. Lors de votre tour, vous pouvez choisir soit entre " +
                "les 5 cartes visibles\n (votre adversaire saura quelle carte vous avez tirer), soit tirer " +
                " une carte secrete dans la pile.\nATTENTION, lorsque vous tirez une carte, vous êtes obligés " +
                "d'en tirer une deuxième ce qui terminera votre tour.");
        tutorialTexts.add(text2);

        BorderPane pane2 = new BorderPane();
        pane2.getStylesheets().add("choix-cartes-image.css");
        ImageView imageView2 = new ImageView();
        pane2.setCenter(imageView2);

        panes.add(pane2);

        //-------------Text 3: claim routes-------------
        Text text3 = new Text("Finalement, ayant les cartes nécessaires il faut s'emparer de routes ou " +
                "essayer de s'emparer d'un tunnel.\n S'il y a plusieurs combinaisons de cartes jouables " +
                "elles seront proposées afin que vous puissiez choisir celle qui vous convient le mieux.\n" +
                "- Pour s'emparer d'une route ayant une couleur, il faut avoir autant de cartes de cette couleur " +
                "que de cases constituants cette route sur la carte.\n" +
                "- Si la route est grise, il est possible d'utiliser une combinaison de type: " +
                "cartes d'une certaine couleur + cartes locomotive");
        tutorialTexts.add(text3);

        BorderPane pane3 = new BorderPane();
        pane3.getStylesheets().add("claim-routes-image.css");
        ImageView imageView3 = new ImageView();
        pane3.setCenter(imageView3);

        panes.add(pane3);

        //-------------Text 4: claim tunnel-------------
        Text text4 = new Text("Les tunnels sont les routes entourées de pointillés.\nPour commencer à s'en " +
                "emparer les règles sont les mêmes que pour une route normale.\nMAIS, lorsqu'on a essayé " +
                "de s'emparer d'un tunnel, trois cartes additionnelles sont tirées de la pile.\nSi certaines " +
                "des cartes additionnelles correspondent aux cartes posées initialement,\nil va falloir jouer " +
                "ces cartes (ou locomotives) en plus pour pouvoir complètement s'emparer du tunnel.");
        tutorialTexts.add(text4);

        BorderPane pane4 = new BorderPane();
        pane4.getStylesheets().add("claim-tunnels-image.css");
        ImageView imageView4 = new ImageView();
        pane4.setCenter(imageView4);

        panes.add(pane4);

        //-------------Text 5: game infos-------------
        Text text5 = new Text("Toutes les informations sur le déroulement du jeu sont affichées à gauche de l'écran. " +
                "Sur ce, bonne chance pour accumuler le max de points et gagner !");
        tutorialTexts.add(text5);

        BorderPane pane5 = new BorderPane();
        pane5.getStylesheets().add("game-infos-image.css");
        ImageView imageView5 = new ImageView();
        pane5.setCenter(imageView5);

        panes.add(pane5);


        createTipBox(tutorialTexts, panes);
    }

    /**
     *
     * @param texts, explanations added to each box
     * @param panes, panes containing the images to add to window
     * size of text list and pane list must be the same
     */
    private static void createTipBox(List<Text> texts, List<Pane> panes) {
        int numBoxes = texts.size();
        List<Stage> stages = new ArrayList<>();

        for(int i = numBoxes - 1; i >= 0; --i) {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Tutoriel: " + (i+1) + "/" + numBoxes);

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);

            int slideNum = i;

            if(i == numBoxes - 1) {
                Button buttonClose = new Button();
                buttonClose.setText("Fermer");
                buttonClose.setOnMouseClicked(e -> stages.get(0).hide());

                vBox.getChildren().add(texts.get(i));
                vBox.getChildren().add(panes.get(i));
                vBox.getChildren().add(buttonClose);

                Scene scene = new Scene(vBox, 1000, 600);
                stage.setScene(scene);

                stages.add(stage);
                continue;
            }

            //-------------"Next" button-------------
            Button buttonNext = new Button();
            buttonNext.setText("Suivant");
            buttonNext.setOnMouseClicked(e -> {
                stages.get(numBoxes - 1 - slideNum).hide();
                stages.get(numBoxes - 2 - slideNum).show();
            });

            //-------------Constructing our VBox, Scene and Stage-------------
            vBox.getChildren().add(texts.get(i));
            vBox.getChildren().add(panes.get(i));
            vBox.getChildren().add(buttonNext);

            Scene scene = new Scene(vBox, 1000, 600);
            stage.setScene(scene);

            stages.add(stage);
        }

        stages.get(stages.size()-1).show();

    }

}
