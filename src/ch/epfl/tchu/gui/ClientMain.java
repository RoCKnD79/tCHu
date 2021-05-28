package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christopher Soriano (326354)
 */

public class ClientMain extends Application {
    /**
     * main for client main
     * lauches with arguments in parameters which correspond to the host and port (in order)
     * @param args
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * contains principal programm for the tCHu server.
     * analyses the arguments to get the names of the players
     * waits for a connexion from the client on the port passed in argument
     * creates two players :
     *  -1rst player : graphicalPlayerAdapter
     *  -2nd player  : remotePlayerClient
     * launches a thread that runs run from the client
     * @param primaryStage, is ignored
     * @throws Exception if the socket is not accepted
     */
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
