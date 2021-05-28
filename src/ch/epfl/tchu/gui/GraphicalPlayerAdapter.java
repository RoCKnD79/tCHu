package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;
/**
 * @author Christopher Soriano (326354)
 */

public class GraphicalPlayerAdapter implements Player {
private GraphicalPlayer graphicalPlayer;
private final BlockingQueue<SortedBag<Ticket>> ticketsBlockingQueue = new ArrayBlockingQueue<>(1);
private final BlockingQueue<SortedBag<Card>> cardsBlockingQueue = new ArrayBlockingQueue<>(1);
private final BlockingQueue<Route> routeBlockingQueue = new ArrayBlockingQueue<>(1);
private final BlockingQueue<Integer> integerBlockingQueue = new ArrayBlockingQueue<>(1);


    public GraphicalPlayerAdapter() {}

    /**
     * builds on the thread javafx a instance of a graphical player
     * @param ownId,       id of the player
     * @param playerNames, map linking ids to names
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    /**
     * calls on the javafx tread receiveInfo of graphical player
     * @param info, the information given
     */
    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    /**
     * calls on the ajva fx thread the setState method of graphicalplayer
     * @param newState, new state of the player
     * @param ownState, own state of the player
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> graphicalPlayer.setState(newState, ownState));
    }

    /**
     * calls on the java fx thread chooseTickets of the graphical player to ask
     * for the initial tickets by handing him a handler
     * @param tickets, sorted list of tickets disteibuted
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() -> graphicalPlayer.chooseTickets(tickets, ticketsBlockingQueue::add));
    }

    /**
     * uses a blocking queue to get the tickets taht the palyer will choose intially
     * @return a sorted bag of tickets
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        try {
            return ticketsBlockingQueue.take();
        }catch (InterruptedException e){
            throw new Error();
        }
    }

    /**
     * calls on the java fx thread startTurn form the graphicalPlayer by handing him a handler to figure out what nextturn
     * the player wants
     * @return the nextTurn
     */
    @Override
    public TurnKind nextTurn() {
        BlockingQueue<TurnKind> turnKindBlockingQueue = new ArrayBlockingQueue<>(1);
        runLater(() -> graphicalPlayer.startTurn(() -> turnKindBlockingQueue.add(TurnKind.DRAW_TICKETS),
                (s) -> {turnKindBlockingQueue.add(TurnKind.DRAW_CARDS);
                        integerBlockingQueue.add(s);}
                ,(r, t) -> {turnKindBlockingQueue.add(TurnKind.CLAIM_ROUTE);
                            routeBlockingQueue.add(r);
                            cardsBlockingQueue.add(t);}
        ));
        try{
            return turnKindBlockingQueue.take();
        } catch (InterruptedException e){
            throw new Error();
        }
    }

    /**
     *chains the actions done by setInitialTicketChoice
     * @param options, sorted bag of the new tickets
     * @return the sorted bag of tickets the player chose
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
       try{
           runLater(() -> graphicalPlayer.chooseTickets(options, ticketsBlockingQueue::add));
       return ticketsBlockingQueue.take();}
       catch (InterruptedException e){
           throw new Error();
       }
    }

    /**
     * test without blocking if the thread contains a slot,
     * if yes : calls remove()
     * if not : calls take() to block until there is a slot
     * @return the int of the slot the player chose to draw
     */
    @Override
    public int drawSlot() {
        if (!integerBlockingQueue.isEmpty()){
            return integerBlockingQueue.remove();
        }else{
        try{
            runLater(() -> graphicalPlayer.drawCard(integerBlockingQueue::add));
        return integerBlockingQueue.take();
        }
        catch (InterruptedException e){
            throw new Error();
        }
    }}

    /**
     * extracts and return the first element of the queue containing the routes
     * @return the route claimed
     */
    @Override
    public Route claimedRoute() {
        try {
            return routeBlockingQueue.take();
        }catch (InterruptedException e){
            throw new Error();
        }
    }

    /**
     * extracts and returns the first element of the que containing the cards intialy used to claim a road
     * @return a sorted bag of the cards used
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        try{
            return cardsBlockingQueue.take();}
        catch (InterruptedException e){
            throw new  Error();
        }
    }

    /**
     * calls on the java fx thread chooseAdditionalCards of the graphical player
     * @param options, list of the different options of cards he can use
     * @return the cards that the player chose
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
       try {
           runLater(() -> graphicalPlayer.chooseAdditionalCards(options, cardsBlockingQueue::add));
           return cardsBlockingQueue.take();
       }catch (InterruptedException e){
           throw new Error();
       }
    }
}
