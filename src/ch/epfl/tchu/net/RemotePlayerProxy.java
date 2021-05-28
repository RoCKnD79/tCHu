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

/**
 * instanciable class
 * @author Christopher Soriano (326354)
 */
public class RemotePlayerProxy implements Player {

    BufferedReader r;
    private final Socket socket;

    /**
     * constructor of remotePlayerProxy
     * @param socket, socket used to communicate
     * @throws IOException if there is an issue with the socket
     */
    public RemotePlayerProxy(Socket socket) throws IOException {
        this.socket = socket;
        try {
            this.r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     * used when initialising players
     * serialises arguments and sends them to the client
     * @param ownId,       id of the player
     * @param playerNames, map linking ids to names
     */
    @Override

    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        String namesSerialized = Serdes.stringListSerde.serialize(List.of(playerNames.get(ownId), playerNames.get(ownId.next())));
        sendMessage(MessageId.INIT_PLAYERS.name() + " " + Serdes.playerIdSerde.serialize(ownId) + " " + namesSerialized + " " );

    }

    /**
     * serialises the info that need to be sent and then sends in to the client
     * @param info, the information given
     */
    @Override
    public void receiveInfo(String info) {
        String infoSerde = Serdes.stringSerde.serialize(info);
        String message = (MessageId.RECEIVE_INFO.name() + " " + infoSerde + " " + '\n');
        sendMessage(message);
    }

    /**
     * used to update the state of the game
     * @param newState, new state of the player
     * @param ownState, own state of the palyer
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        String newStateSerde = Serdes.publicGameStateSerde.serialize(newState);
        String ownStateSerde = Serdes.playerStateSerde.serialize(ownState);
        String message = (MessageId.UPDATE_STATE.name() + " " + String.join(" ", newStateSerde, ownStateSerde) + " " + '\n');
        sendMessage(message);
    }

    /**
     * used to set the initial thickets choice for the player and sends the tickets to the client
     * @param tickets, sorted list of tickets distributed
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        String ticketsSerde = Serdes.ticketSortedBagSerde.serialize(tickets);
        String message = (MessageId.SET_INITIAL_TICKETS.name() + " " + ticketsSerde + " " + '\n');
        sendMessage(message);
    }

    /**
     * used to know which tickets the player wants to keep
     * @return a sorted bag tickets that the player chose
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS.name()+ " " + '\n');
        String string = receiveMessage();
        return Serdes.ticketSortedBagSerde.deserialize(string);
    }

    /**
     * is used to know which turn the player wants to play next
     * @return the turn kind
     */
    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN.name()+ " " + '\n');
        String string = receiveMessage();
        return Serdes.turnKindSerde.deserialize(string);
    }

    /**
     * used when a player wants to choose new tickets
     * @param options, sorted bag of the new tickets
     * @return the sorted bag of tickets the player chose
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS.name()+ " " + Serdes.ticketSortedBagSerde.serialize(options) + " " + '\n');
        String string = receiveMessage();
        return Serdes.ticketSortedBagSerde.deserialize(string);
    }

    /**
     * used when a player draw a card to know which slot he wants to draw the card from
     * @return the slot
     */
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT.name()+ " " + '\n');
        String string = receiveMessage();
        return Serdes.intSerde.deserialize(string);
    }

    /**
     * used when a player claimed a route to figure out what route was claimed
     * @return the route claimed
     */
    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE.name()+ " " + '\n');
        String string = receiveMessage();
        return Serdes.routeSerde.deserialize(string);
    }

    /**
     * used to figure out which cards the player initially used to claim a road
     * @return the cards initially used
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS.name()+ " " +'\n');
        String string = receiveMessage();
        return Serdes.cardSortedBagSerde.deserialize(string);
    }

    /**
     * used when the player tries to claim a tunnel to figure out which cards he has to add
     * @param options, list of the different options of cards he can use
     * @return the cards
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS.name()+ " " + Serdes.cardSortedBagListSerde.serialize(options) + " " + '\n');
        String string = receiveMessage();
        return Serdes.cardSortedBagSerde.deserialize(string);
    }

    /**
     * sendMessage is used to write the message through the buffered writer and then flush it
     * @param message that needs to be sent
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

    /**
     * receiveMessage is used to receive a message from the client through the buffered reader
     * @return the string that was received
     */
    private String receiveMessage(){
        try{
            BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream(), US_ASCII));
            return r.readLine();
        }catch(IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
