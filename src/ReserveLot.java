public class ReserveLot extends Lot{
    private int reserveValue;

    // Type of lot (so it is not confused with other types of lots)
    public final static int lotType = 1;

    public ReserveLot(int lotNumber, int reserve) {
        super(lotNumber);
        this.reserveValue = reserve;
    }

    @Override
    protected int checkBid(int bid, int bidderId) {

        // If the current winner is re-bidding then it's just to increase the current reserve bid
        int outcome = BidAcceptableNotWinning;
        if (bidderId == winningBidder) {
            if (bid > topBid) {
                topBid = bid;
                outcome = BidWinning;
            }
        } else {

            // An acceptable bid must exceed the current bid and the reserve value
            if (bid >= reserveValue && bid > topBid) {

                outcome = BidWinning;
                winningBidder = bidderId;

                topBid = bid;
            }
        }
        return outcome;
    }

    public int getReserveValue() {
        return reserveValue;
    }
}