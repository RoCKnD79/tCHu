package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class RemotePlayerProxy implements Player {


    private final Socket socket;


    public RemotePlayerProxy(Socket socket){
        this.socket = socket;
    }

    @Override
    //TODO \n a la fin du message pas accepté wtf
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        List<String> listInfos = List.of(Serdes.playerIdSerde.serialize(ownId), Serdes.stringListSerde.serialize(List.of(playerNames.get(ownId), playerNames.get(ownId.next()))));
        sendMessage(MessageId.INIT_PLAYERS.name() + " " + Serdes.stringListSerde.serialize(listInfos) + " " +'\n');
    }

    @Override
    public void receiveInfo(String info) {
        String infoSerde = Serdes.stringSerde.serialize(info);
        String message = MessageId.RECEIVE_INFO.name() + " " + infoSerde + " " + '\n';
        sendMessage(message);
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        String newStateSerde = Serdes.publicGameStateSerde.serialize(newState);
        String ownStateSerde = Serdes.playerStateSerde.serialize(ownState);
        String message = MessageId.UPDATE_STATE.name() + " " + String.join(" ", newStateSerde, ownStateSerde) + " " + '\n';
        sendMessage(message);
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        String ticketsSerde = Serdes.ticketSortedBagSerde.serialize(tickets);
        String message = MessageId.SET_INITIAL_TICKETS.name() + " " + ticketsSerde + " " + '\n';
        sendMessage(message);
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS.name()+ " " + '\n');
        String string = receiveMessage();
        return Serdes.ticketSortedBagSerde.deserialize(string);
    }

    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN.name()+ " " + '\n');
        String string = receiveMessage();
        return Serdes.turnKindSerde.deserialize(string);
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS.name()+ " " +'\n');
        String string = receiveMessage();
        return Serdes.ticketSortedBagSerde.deserialize(string);
    }

    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT.name()+ " " + '\n');
        String string = receiveMessage();
        return Serdes.intSerde.deserialize(string);
    }

    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE.name()+ " " + '\n');
        String string = receiveMessage();
        return Serdes.routeSerde.deserialize(string);
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS.name()+ " " +'\n');
        String string = receiveMessage();
        return Serdes.cardSortedBagSerde.deserialize(string);
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS.name()+ " " + '\n');
        String string = receiveMessage();
        return Serdes.cardSortedBagSerde.deserialize(string);
    }

    private void sendMessage(String message){
        try{
           // BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII))){
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), US_ASCII));
            w.write(message);
            w.write('\n');
            w.flush();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private String receiveMessage(){
        try{
            BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
            return r.readLine();
        }catch(IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
