package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientMain extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String hostname;
        int portNumber;
        List<String> arguments = getParameters().getRaw();
        if(arguments.size() == 0){
            hostname = "localhost";
            portNumber = 5108;
        }else {
            hostname = arguments.get(0);
            portNumber = Integer.parseInt(arguments.get(1));
        }

        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(new GraphicalPlayerAdapter(), hostname, portNumber);
        new Thread(() -> {
            try {
                remotePlayerClient.run();
                System.out.println("END OF RUN");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


    }

}
