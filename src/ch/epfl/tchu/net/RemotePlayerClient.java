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

        try {
            this.socket = new Socket(host, port);
            this.r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public void run() throws IOException {
        System.out.println("entered run");
        String string = r.readLine();
        while (string!= null){
            System.out.println("string is not null, entered while");
            String[] splitString = string.split(Pattern.quote(" "), -1);
            System.out.println(splitString[0]);
            System.out.println("deserialised message : " + Serdes.stringSerde.deserialize(splitString[0]));
            MessageId messageId = MessageId.valueOf(splitString[0]);
            switch (messageId){
                case INIT_PLAYERS:
                    PlayerId ownId = Serdes.playerIdSerde.deserialize(splitString[1]);
                    List<String> playerNames =  Serdes.stringListSerde.deserialize(splitString[2]);
                    Map<PlayerId, String> playerNameMap = new EnumMap<PlayerId, String>(PlayerId.class);
                    for(PlayerId playerId : PlayerId.ALL){
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
                    sendMessage(Serdes.ticketSortedBagSerde.serialize(player.chooseInitialTickets()));
                    break;
                case NEXT_TURN:
                    sendMessage(Serdes.turnKindSerde.serialize(player.nextTurn()));
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
            string = r.readLine();
        }
    }


    private void sendMessage(String message){
        try(

                BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII))){
            w.write(message);
            w.write('\n');
            w.flush();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
