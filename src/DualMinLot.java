public class DualMinLot extends Lot {
    private final int incrementLimit;
    private final int minInc1;
    private final int minInc2;

    // Type of lot (so it is not confused with other types of lots)
    public final static int lotType = 2;

    public DualMinLot(int lotNumber, int incLim, int min1, int min2) {
        super(lotNumber);
        this.incrementLimit = incLim;
        this.minInc1 = min1;
        this.minInc2 = min2;
    }

    @Override
    protected boolean checkBid(int bid, int bidderId) {

        // If the current winner is re-bidding then it's just to increase the current reserve bid
        if (bidderId == winningBidder) {
            if (bid > topBid) {
                topBid = bid;
                return true;
            }

        } else {
            // Decide which minBidIncrement to use
            int currIncLimit = minInc1;
            if (bid >= incrementLimit) {
                currIncLimit = minInc2;
            }

            // An acceptable bid must exceed the current bid by the minimum increment or more.
            if (bid >= topBid + currIncLimit) {
                winningBidder = bidderId;
                topBid = bid;
                return true;
            }
        }
        return false;
    }

    public int getIncrementLimit() {
        return incrementLimit;
    }

    public int getMinInc1() {
        return minInc1;
    }

    public int getMinInc2() {
        return minInc2;
    }
}