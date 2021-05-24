package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientMain extends Application {

    public static void main(String[] args){
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<String> arguments = new ArrayList<>(getParameters().getRaw());
        String hostName = arguments.get(0);
        int portNumber = Integer.parseInt(arguments.get(1));

        GraphicalPlayerAdapter graphicalPlayerAdapter = new GraphicalPlayerAdapter();
        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(graphicalPlayerAdapter, hostName, portNumber);

        new Thread(() -> {
            try {
                remotePlayerClient.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


    }

}
