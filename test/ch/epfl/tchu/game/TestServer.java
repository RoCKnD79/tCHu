package ch.epfl.tchu.game;

import ch.epfl.tchu.net.RemotePlayerProxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

public final class TestServer {
    public static void main(String[] args) throws IOException{
        System.out.println("Starting Server !");
        try(ServerSocket serverSocket = new ServerSocket(5108);
            Socket socket = serverSocket.accept()) {
                Player playerProxy = new RemotePlayerProxy(socket);
                var playerNames = Map.of(PlayerId.PLAYER_1, "Ada", PlayerId.PLAYER_2, "Charles");
                playerProxy.initPlayers(PlayerId.PLAYER_1, playerNames);
        }
        System.out.println("Server done ! thanks cuh");
}

}