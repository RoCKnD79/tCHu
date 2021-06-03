package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import javax.sound.sampled.Port;
import java.io.*;
import java.net.Socket;
import java.time.chrono.IsoEra;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;
/**
@author Christopher Soriano (326354)
*/


public class RemotePlayerClient {

    final Socket socket;
    final BufferedReader r;
    Player player;

    /**
     * constructor for a RemotePlayerClient
     * @param player, the player that will be the client
     * @param host, the host of the server
     * @param port, the port used
     * @throws IOException if there is an issue whith the socket
     */
    public RemotePlayerClient(Player player, String host, int port) throws IOException {
        this.player = player;
            this.socket = new Socket(host, port);
            this.r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));

    }

    /**
     * this method does a loop while there is still a line that is read
     * in this loop :
     *  - waits for a message from the proxy
     *  - divides it into parts using a separator
     *  - determines the type of message from the first part of the divided message
     *  - depending on this type of message, deserialises the arguments, call the corresponding method of player.
     *  - if this method then returns a result (not void), then it serialises it and sends it to the proxy
     * @throws IOException if there is an issue with the socket
     */
    public void run() throws IOException {
        String string;
        while ((string = r.readLine()) != null){
            if(string.equals("")){
                continue;
            }
            String[] splitString = string.split(Pattern.quote(" "), -1);
            MessageId messageId = MessageId.valueOf(splitString[0]);
            switch (messageId){
                case RECEIVE_INFO:
                    String msg = Serdes.stringSerde.deserialize(splitString[1]);
                    this.player.receiveInfo(msg);
                    break;

                case INIT_PLAYERS:

                    List<String> playerNames = Serdes.stringListSerde.deserialize(splitString[2]);
                    PlayerId ownId = Serdes.playerIdSerde.deserialize(splitString[1]);
                    Map<PlayerId, String> playerNameMap = new EnumMap<>(PlayerId.class);
                    for(PlayerId playerId : PlayerId.ALL){
                        playerNameMap.put(playerId, playerNames.get(playerId.next().ordinal()));
                    }
                    player.initPlayers(ownId, playerNameMap);
                    break;
                case UPDATE_STATE:
                    PublicGameState newState = Serdes.publicGameStateSerde.deserialize(splitString[1]);
                    PlayerState ownState = Serdes.playerStateSerde.deserialize(splitString[2]);
                    player.updateState(newState, ownState);
                    break;

                case SET_INITIAL_TICKETS:
                    SortedBag<Ticket> tickets = Serdes.ticketSortedBagSerde.deserialize(splitString[1]);
                    player.setInitialTicketChoice(tickets);
                    break;
                case CHOOSE_INITIAL_TICKETS:
                    SortedBag<Ticket> sortedbagTickets = player.chooseInitialTickets();
                    sendMessage(Serdes.ticketSortedBagSerde.serialize(sortedbagTickets));
                    break;
                case NEXT_TURN:
                    Player.TurnKind turnkind = player.nextTurn();
                    sendMessage(Serdes.turnKindSerde.serialize(turnkind));
                    break;
                case CHOOSE_TICKETS:
                    SortedBag<Ticket> options = Serdes.ticketSortedBagSerde.deserialize(splitString[1]);
                    sendMessage(Serdes.ticketSortedBagSerde.serialize(player.chooseTickets(options)));
                    break;
                case DRAW_SLOT:
                    sendMessage(Serdes.intSerde.serialize(player.drawSlot()));
                    break;
                case ROUTE:
                    sendMessage(Serdes.routeSerde.serialize(player.claimedRoute()));
                    break;
                case CARDS:
                    sendMessage(Serdes.cardSortedBagSerde.serialize(player.initialClaimCards()));
                    break;
                case CHOOSE_ADDITIONAL_CARDS:
                    List<SortedBag<Card>> list = Serdes.cardSortedBagListSerde.deserialize(splitString[1]);
                    sendMessage(Serdes.cardSortedBagSerde.serialize(player.chooseAdditionalCards(list)));
                    break;
            }

        }

    }


    /**
     * used to send a message through buffered writer
     * @param message that will be sent
     */
    private void sendMessage(String message){
        try{
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
            w.write(message);
            w.write('\n');
            w.flush();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}