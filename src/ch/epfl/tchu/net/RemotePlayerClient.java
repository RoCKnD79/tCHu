package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import javax.sound.sampled.Port;
import java.io.*;
import java.net.Socket;
import java.time.chrono.IsoEra;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class RemotePlayerClient {

    final Socket socket;
    final BufferedReader r;
    Player player;

    public RemotePlayerClient(Player player, String host, int port) throws IOException {
        this.player = player;
            this.socket = new Socket(host, port);
            this.r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));

    }

    public void run() throws IOException {
        String string;
        //.out.println("line read is the following : " + string);
        while ((string = r.readLine()) != null){
            if(string.equals("")){
                continue;
            }
            System.out.println("message received by client is :  " + string);
            String[] splitString = string.split(Pattern.quote(" "), -1);
            System.out.println(splitString[0] + " enum looked for");
            MessageId messageId = MessageId.valueOf(splitString[0]);
            switch (messageId){
                case RECEIVE_INFO:
                    String msg = Serdes.stringSerde.deserialize(splitString[1]);
                    this.player.receiveInfo(msg);
                    break;

                case INIT_PLAYERS:

                    List<String> playerNames = Serdes.stringListSerde.deserialize(splitString[2]);
                    PlayerId ownId = Serdes.playerIdSerde.deserialize(splitString[1]);
                    Map<PlayerId, String> playerNameMap = new EnumMap<PlayerId, String>(PlayerId.class);
                    for(PlayerId playerId : PlayerId.ALL){
                        System.out.println("Player names size " + playerNames.size());
                        playerNameMap.put(playerId, playerNames.get(playerId.ordinal()));
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
                    System.out.println("choose initial tickets client message was sent");
                    //System.out.println(r.readLine());
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
                    System.out.println("route is claimed");
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


    private void sendMessage(String message){
        try{
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
            System.out.println("message sent by client is : " + message);
            w.write(message);
            w.write('\n');
            w.flush();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
//hsins