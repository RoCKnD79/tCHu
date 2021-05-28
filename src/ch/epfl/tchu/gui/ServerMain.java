package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.util.*;

/**
 * @author Christopher Soriano (326354)
 */

public class ServerMain extends Application {
    String player1Name;
    String player2Name;
    Map<PlayerId, Player> players = new HashMap<>();
    Map<PlayerId, String> playerNames = new HashMap<>();

    /**
     * main method for server main, call launch with args in argument, which correspond to the name of the players
     * @param args, name of players in String table
     */
    public static void main(String[] args){
        launch(args);
    }

    /**
     * start method for server,
     * creates server socket,
     * analyses arguments,
     * creates a new thread that is launched
     * @param primaryStage, primary stage not used
     * @throws Exception, thrown by the serverSocket if there is an error when opening the socket
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5108);

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

        playerNames.put(PlayerId.PLAYER_1, player1Name);
        playerNames.put(PlayerId.PLAYER_2, player2Name);
        GraphicalPlayerAdapter player1 = new GraphicalPlayerAdapter();
        RemotePlayerProxy player2 = new RemotePlayerProxy(serverSocket.accept());

        players.put(PlayerId.PLAYER_1, player1);
        players.put(PlayerId.PLAYER_2, player2);
        new Thread(() -> Game.play(players, playerNames, SortedBag.of(ChMap.tickets()), new Random())).start();
    }
}
