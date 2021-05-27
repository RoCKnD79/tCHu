package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServerMain extends Application {
    String player1Name;
    String player2Name;
    Map<PlayerId, Player> players = new HashMap<>();
    Map<PlayerId, String> playerNames = new HashMap<>();


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5108);
        //Socket socket = serverSocket.accept();
        //System.out.println(socket + "socket");

        List<String> arguments = new ArrayList<>(getParameters().getRaw());
        if(arguments.size() ==  2) {
            player1Name = arguments.get(0);
            player2Name = arguments.get(1);
        }else if (arguments.size() == 1){
            player1Name = arguments.get(0);
            player2Name = "Charles";
        }else{
            player1Name = "Ada";
            player2Name = "Charles";
        }
        System.out.println(player1Name + " player 1 name");
        System.out.println(player2Name + " player 2 name");

        playerNames.put(PlayerId.PLAYER_1, player1Name);
        playerNames.put(PlayerId.PLAYER_2, player2Name);
        System.out.println(playerNames.size() + "player name size in server");
        GraphicalPlayerAdapter player1 = new GraphicalPlayerAdapter();
        RemotePlayerProxy player2 = new RemotePlayerProxy(serverSocket.accept());

        players.put(PlayerId.PLAYER_1, player1);
        players.put(PlayerId.PLAYER_2, player2);
        new Thread(() -> Game.play(players, playerNames, SortedBag.of(ChMap.tickets()), new Random())).start();
    }
}
