package ch.epfl.tchu.game;

import java.util.List;

public final class Trail {

    private

    public static Trail longest(List<Route> routes) {
        Route r;
        for(int i = 1; i < routes.size(); ++i) {
            if(routes.get(i-1).length() > routes.get(i).length()) {
                r = routes.get(i-1);
            }
        }
        return r;
    }
}
