package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

public class GraphicalPlayerAdapter implements Player {
private GraphicalPlayer graphicalPlayer;
Queue<Integer> slotQueue = new ArrayDeque<>();
private final BlockingQueue<SortedBag<Ticket>> ticketsBlockingQueue = new ArrayBlockingQueue<>(1);
private final BlockingQueue<SortedBag<Card>> cardsBlockingQueue = new ArrayBlockingQueue<>(1);
private final BlockingQueue<Route> routeBlockingQueue = new ArrayBlockingQueue<>(1);
private final BlockingQueue<Integer> integerBlockingQueue = new ArrayBlockingQueue<>(1);


    public GraphicalPlayerAdapter() {}

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        //BlockingQueue<GraphicalPlayer> blockingQueueGraphicalPlayer = new ArrayBlockingQueue<>(1);

        runLater(() -> graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
        /*try{
            graphicalPlayer = blockingQueueGraphicalPlayer.take();
        }catch (InterruptedException e){
            throw new Error();
        }*/
    }

    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> graphicalPlayer.setState(newState, ownState));
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() -> graphicalPlayer.chooseTickets(tickets, ticketsBlockingQueue::add));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        try {
            return ticketsBlockingQueue.take();
        }catch (InterruptedException e){
            throw new Error();
        }
    }

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
    return turnKindBlockingQueue.take();}
        catch (InterruptedException e){
            throw new Error();
        }
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
       try{
           runLater(() -> graphicalPlayer.chooseTickets(options, ticketsBlockingQueue::add));
       return ticketsBlockingQueue.take();}
       catch (InterruptedException e){
           throw new Error();
       }
    }

    @Override
    public int drawSlot() {
    //TODO check slot queue
        runLater(() -> graphicalPlayer.drawCard(integerBlockingQueue::add));
        if (integerBlockingQueue.size() == 1){
            return integerBlockingQueue.remove();
        }else{
        try{
        return integerBlockingQueue.take();
        }
        catch (InterruptedException e){
            throw new Error();
        }
    }}

    @Override
    public Route claimedRoute() {
    try {
        return routeBlockingQueue.take();
    }catch (InterruptedException e){
        throw new Error();
    }
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
    try{
        return cardsBlockingQueue.take();}
    catch (InterruptedException e){
        throw new  Error();
    }
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
       try {
           BlockingQueue<SortedBag<Card>> sortedBagBlockingQueue = new ArrayBlockingQueue<>(1);
           runLater(() -> graphicalPlayer.chooseAdditionalCards(options, cardsBlockingQueue::add));
           return sortedBagBlockingQueue.take();
       }catch (InterruptedException e){
           throw new Error();
       }
    }
}
