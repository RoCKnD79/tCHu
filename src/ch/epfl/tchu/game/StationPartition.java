package ch.epfl.tchu.game;

/**
 * @author Roman Danylovych (327830)
 */

/*
implements StationConnectivity interface because its instances
are aimed to be passed in the points(...) method of the Ticket class
 */
public final class StationPartition implements StationConnectivity {

    private final int[] repArray;

    /**
     * @param links, array containing the integers linking the stations to their representative in their subset
     */
    private StationPartition(int[] links) { this.repArray = links.clone(); }

    /**
     * will test to see if s1 and s2 have the same representative => if they can be linked
     * @param s1, the first station to test for connection
     * @param s2, the second station to test for connection
     * @return true if s1 and s2 are the same (=> have the same id) or if they both point to the same representative
     */
    @Override
    public boolean connected(Station s1, Station s2) {
        if(s1.id() >= repArray.length || s2.id() >= repArray.length) {
            return s1.id() == s2.id();
        }
        return repArray[s1.id()] == repArray[s2.id()];
    }

    /**
     * The only way to initialize an instance of type StationPartition is through its Builder
     */
    public static final class Builder {

        private int stationCount;
        private int[] r;

        /**
         * constructor the StationPartition builder
         * @param stationCount = max(id1, id2,..., idN) + 1 where id1, id2,..., idN are the ids
         *                     of the stations that are going to be added to the representatives array (r)
         */
        public Builder(int stationCount) {
            if(stationCount < 0) {
                throw new IllegalArgumentException("number of stations passed in argument must be positive (>= 0)");
            }
            this.stationCount = stationCount;

            r = new int[stationCount];
            for(int i = 0; i < r.length; ++i) {
                r[i] = i;
            }
        }

        /**
         * @param s1, station 1
         * @param s2, station 2
         * @return the current instance of the builder, which will have its representatives array (r) modified (in most of the cases)
         * @throws IndexOutOfBoundsException, if the id of the station given in argument is >= stationCount
         */
        public Builder connect(Station s1, Station s2) throws IndexOutOfBoundsException {
            if (s1.id() >= stationCount || s2.id() >= stationCount) {
                throw new IndexOutOfBoundsException("Station ID cannot be >= than " + stationCount +
                        " (number passed in argument in the constructor of Builder)");
            }

            //case where s1 points to itself. Starts pointing at s2 if s2's final representative isn't s1
            if(r[s1.id()] == s1.id()) {
                if(representative(s2.id()) != s1.id()){
                    r[s1.id()] = s2.id();
                }
            } else { //case where s1 is already pointing to another station
                int temp = representative(s1.id());
                r[s1.id()] = r[representative(s2.id())];
                r[temp] = s1.id();
            }

            return this;
        }

        /**
         * This is basically the method which will allow us to initialize an instance of type StationPartition
         * @return an instance of StationPartition
         */
        public StationPartition build() {
            for(int i = 0; i < r.length; ++i) {
                r[i] = representative(i);
            }

            return new StationPartition(r);
        }

        /**
         * @param , the id of the station whose representative we would like to know
         * @return the representative of the station in question
         */
        private int representative(int id) {
            boolean valueChange;
            int repId = r[id];
            do {
                valueChange = false;

                //case where s1 points to s2 and s2 points to s1
                if (r[r[repId]] == repId)
                    r[repId] = repId;

                if(repId != r[repId]) {
                    repId = r[repId];
                    valueChange = true;
                }
            } while(valueChange);

            return repId;
        }

        /**
         * used for debug
         * @param array, array to display
         */
        private void displayArray(int[] array) {
            for(int i : array) {
                System.out.print(i + ", ");
            }
            System.out.println();
        }
    }


}
