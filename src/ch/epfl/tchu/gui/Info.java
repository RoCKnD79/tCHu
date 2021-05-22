package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.*;

/**
 * @author Roman Danylovych (327830)
 */
//immutable class
public final class Info {

    private final String playerName;

    /**
     * basic constructor, initializing the player's name
     * @param playerName, name of player
     * @throws NullPointerException, if playerName is null
     */
    public Info(String playerName) throws NullPointerException {
        this.playerName = Objects.requireNonNull(playerName);
    }

    /**
     * @param card, card that is to be translated into text
     * @param count, quantity of 'card' there is (needed to know if we are putting the word in plural or no)
     * @return "[card]/s (plural if |count| <= 1)"
     */
    public static String cardName(Card card, int count) throws IllegalArgumentException {

        switch(card){
            case BLACK: return String.format("%s%s", StringsFr.BLACK_CARD, StringsFr.plural(count));
            case VIOLET: return String.format("%s%s", StringsFr.VIOLET_CARD, StringsFr.plural(count));
            case BLUE: return String.format("%s%s", StringsFr.BLUE_CARD, StringsFr.plural(count));
            case GREEN: return String.format("%s%s", StringsFr.GREEN_CARD, StringsFr.plural(count));
            case YELLOW: return String.format("%s%s", StringsFr.YELLOW_CARD, StringsFr.plural(count));
            case ORANGE: return String.format("%s%s", StringsFr.ORANGE_CARD, StringsFr.plural(count));
            case RED: return String.format("%s%s", StringsFr.RED_CARD, StringsFr.plural(count));
            case WHITE: return String.format("%s%s", StringsFr.WHITE_CARD, StringsFr.plural(count));
            case LOCOMOTIVE: return String.format("%s%s", StringsFr.LOCOMOTIVE_CARD, StringsFr.plural(count));

            default: throw new IllegalArgumentException("Enter an existing card");
        }
    }

    /**
     * @param playerNames, list of the names of the players taking part in the game
     * @param points, the number of points each player's won at the end of a round
     * @return "[player1] et [player2] sont ex æqo avec [points] points !
     */
    public static String draw(List<String> playerNames, int points) {

        String str = new StringBuilder()
                .append(playerNames.get(0))
                .append(StringsFr.AND_SEPARATOR)
                .append(playerNames.get(1))
                .toString();

        return String.format(StringsFr.DRAW, str, points);
    }

    /**
     * @return "[player] jouera en premier."
     */
    public String willPlayFirst() {
        return String.format(StringsFr.WILL_PLAY_FIRST, this.playerName);
    }

    /**
     * @param count, number of tickets the player keeps
     * @return "[player] a gardé [count] billet/s"
     */
    public String keptTickets(int count) {
        return String.format(StringsFr.KEPT_N_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    /**
     * @return "C'est à [player] de jouer"
     */
    public String canPlay() {
        return String.format(StringsFr.CAN_PLAY, playerName);
    }

    /**
     * @param count, number of tickets drawn by player
     * @return "[player] a tiré [count] billet/s"
     */
    public String drewTickets(int count) {
        return String.format(StringsFr.DREW_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    /**
     * @return "[player] a tiré une carte de la pioche
     */
    public String drewBlindCard() {
        return String.format(StringsFr.DREW_BLIND_CARD, this.playerName);
    }

    /**
     * @param card, visible card drawn by the player
     * @return "[player] a tiré une carte [card] visible."
     */
    public String drewVisibleCard(Card card) {
        return String.format(StringsFr.DREW_VISIBLE_CARD, this.playerName, cardName(card, 1));
    }

    /**
     * @param route, the route that has been claimed
     * @param cards, the card that have been used to claim the route
     * @return "[player] a pris possession de la route [route] au moyen de [cards]
     */
    public String claimedRoute(Route route, SortedBag<Card> cards) {
        return String.format(StringsFr.CLAIMED_ROUTE, this.playerName, displayRoute(route), displayCards(cards));
    }

    /**
     * @param route, the tunnel that the player is trying to claim
     * @param initialCards, the cards used to try and claim the tunnel
     * @return "[player] tente de s'emparer du tunnel [tunnel] au moyen de [initialCards]"
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, this.playerName,
                displayRoute(route), displayCards(initialCards));
    }

    /**
     * @param drawnCards, the 3 cards the player decided to draw from the deck
     * @param additionalCost, possible additional cost (number of additional cards the player may be asked to play)
     * @return "Les cartes supplémentaires sont [drawnCards]."
     *          if there is NO additional cost: "Elles n'impliquent aucun coût additionnel."
     *          if there IS an additional cost: "Elles impliquent un coût additionnel de [additionalCost] cartes."
     * @throws IllegalArgumentException if drawn cards is null or additional cost is negative
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) throws IllegalArgumentException {

        if(drawnCards == null || additionalCost < 0) {
            throw new IllegalArgumentException("drawnCards null or additional cost is negative");
        }

        StringBuilder stringBuilder = new StringBuilder(String.format(StringsFr.ADDITIONAL_CARDS_ARE, displayCards(drawnCards)));
        if(additionalCost == 0) {
            stringBuilder.append(StringsFr.NO_ADDITIONAL_COST);
        } else {
            stringBuilder.append(String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost)));
        }
        return stringBuilder.toString();
    }


    /**
     * @param route, the route whose claim attempt has been abandonned
     * @return "[player] n'a pas pu (ou voulu) s'emparer de la route [route]."
     */
    public String didNotClaimRoute(Route route) {
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, this.playerName, displayRoute(route));
    }


    /**
     * declares the start of the last turn because the player has 2 or less cars left
     * @param carCount, number of cars that the player has left
     * @return "[player] n'a plus que [carCount] wagon/s, le dernier tour commence !"
     */
    //pas demandé de tester si carCount est bien <= 2
    public String lastTurnBegins(int carCount) {
        return String.format(StringsFr.LAST_TURN_BEGINS, this.playerName, carCount, StringsFr.plural(carCount));
    }

    /**
     * @param longestTrail, longest trail in the network
     * @return [playerName] reçoit un bonus de 10 points pour le plus long trajet(departure station - arrival station)
     */
    public String getsLongestTrailBonus(Trail longestTrail) {
        String str = new StringBuilder()
                .append(longestTrail.station1())
                .append(StringsFr.EN_DASH_SEPARATOR)
                .append(longestTrail.station2())
                .toString();

        return String.format(StringsFr.GETS_BONUS, this.playerName, str);
    }

    /**
     * states that the player has won the game
     * @param points, total points collected the winner
     * @param loserPoints, total points collected by the loser
     * @return "[player] remporte la victoire avec [points] point/s, contre [loserPoints] point/s !
     */
    public String won(int points, int loserPoints) {
        return String.format(StringsFr.WINS, this.playerName, points, StringsFr.plural(points),
                loserPoints, StringsFr.plural(loserPoints));
    }


    /**
     * @param cards, cards to be displayed
     * @return (1) ex: 2 bleues (if only one type of card)
     *         (2) ex: 1 rouge et 2 vertes (if 2 types of cards => we have a "et" between the cards)
     *         (3) ex: 1 orange, 4 bleues, 2 vertes et 1 jaune (if > 2 types of cards => ","
     *         between the cards and a "et" between the 2 last cards)
     */
    //is public in order to be used in GraphicalPlayer
    //private static String displayCards(SortedBag<Card> cards) {
    public static String displayCards(SortedBag<Card> cards) {

        List<String> text = new ArrayList<>();
        for (Card c: cards.toSet()) {
            int numOccurrences = cards.countOf(c);
            text.add(numOccurrences + " " + cardName(c, numOccurrences));
        }

        if(text.size() == 1) { //ex: 2 bleues
            return text.get(0);
        } else if(text.size() == 2) { //ex: 1 rouge   et   2 vertes
            return new StringBuilder()
                    .append(text.get(0))
                    .append(StringsFr.AND_SEPARATOR)
                    .append(text.get(1))
                    .toString();
        } else { //ex: 1 orange   ,   4 bleues   ,   2 vertes    et   1 jaune
            return new StringBuilder()
                    .append(String.join(", ", text.subList(0, text.size()-1)))
                    .append(StringsFr.AND_SEPARATOR )
                    .append(text.get(text.size()-1))
                    .toString();
        }
    }

    /**
     * @param route, route to display
     * @return "station1 - station2"
     */
    private static String displayRoute(Route route) {
        return new StringBuilder()
                .append(route.station1())
                .append(StringsFr.EN_DASH_SEPARATOR)
                .append(route.station2())
                .toString();
    }

}
