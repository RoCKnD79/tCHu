package ch.epfl.tchu.game;

/*
implémente l'interface StationConnectivity, car ses instances ont pour but d'être passées à la méthode points de Ticket
 */
public final class StationPartition implements StationConnectivity {

    private final int[] links;

    /**
     * @param links, array containing the integers linking the stations to their representative in their subset
     */
    private StationPartition(int[] links) {
        this.links = links.clone();
    }

    /**
     * @param s1, the first station to test for connection
     * @param s2, the second station to test for connection
     * @return true if s1 and s2 are the same (=> have the same id) or if they both point to the same representative
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        //TODO Lorsqu'au moins une des gares qu'on lui passe est ainsi hors bornes, elle ne retourne vrai
        // que si les deux gares ont la même identité
        // => est-ce qu'on est obligé de vérifier si une des gares est hors-bornes ?
        if(s1.id() == s2.id()) {
            return true;
        }
        return links[s1.id()] == links[s2.id()];
    }

    public static final class Builder {

        private int stationCount;
        private int[] buildLinks;

        public Builder(int stationCount) {
            if(stationCount < 0) {
                throw new IllegalArgumentException("number of stations passed in argument must be positive (>0)");
            }
            this.stationCount = stationCount;

        }

        public Builder connect(Station s1, Station s2) {
            return null;
        }

        public StationPartition build() {
            return null;
        }
    }


}
