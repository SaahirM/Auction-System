public class ReserveLot extends Lot{
    private int reserveValue;

    // Type of lot (so it is not confused with other types of lots)
    public final static int lotType = 1;

    public ReserveLot(int lotNumber, int reserve) {
        super(lotNumber);
        this.reserveValue = reserve;
    }

    @Override
    protected boolean checkBid(int bid, int bidderId) {

        // If the current winner is re-bidding then it's just to increase the current reserve bid
        if (bidderId == winningBidder) {
            if (bid > topBid) {
                topBid = bid;
                return true;
            }

        } else if (bid >= reserveValue && bid > topBid) {
            // An acceptable bid must exceed the current bid and the reserve value
            winningBidder = bidderId;
            topBid = bid;
            return true;

        }
        return false;
    }

    public int getReserveValue() {
        return reserveValue;
    }
}
